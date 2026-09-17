import { render, screen, within, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { App } from '../App'
import { ICONIC_PROMPT } from '../lib/seed'
import { resetStore } from '../lib/store'

describe('Aura AI — orchestration smoke test', () => {
  beforeEach(() => {
    resetStore()
  })

  afterEach(() => {
    resetStore()
  })

  it('orchestrates the iconic Persian prompt into a multi-intent fulfillment plan', async () => {
    const user = userEvent.setup()
    render(<App />)

    // Seeded welcome message + context tags
    expect(screen.getByText(/دستیار هوشمند شما هستم/)).toBeTruthy()

    const input = screen.getByTestId('chat_input_field')
    await user.click(input)
    await user.paste(ICONIC_PROMPT)
    await user.click(screen.getByTestId('send_prompt_button'))

    // The user bubble is echoed immediately, then Aura answers after the thinking delay
    const plan = await screen.findByTestId('plan-card', {}, { timeout: 3000 })
    expect(within(plan).getByText(/Fulfillment Plan AUR-/)).toBeTruthy()

    // Four intents: hard drives, flowers, AC service, taxi
    expect(within(plan).getByText(/4x Samsung 870 EVO 1TB SSD/)).toBeTruthy()
    expect(within(plan).getByText(/White Lily & Peony Bouquet/)).toBeTruthy()
    expect(within(plan).getByText(/Daikin AC Full Tune-up/)).toBeTruthy()
    expect(within(plan).getByText(/Executive Ride/)).toBeTruthy()

    // 380 + 45 + 65 + 18 = 508
    expect(within(plan).getByText('$508')).toBeTruthy()
    expect(within(plan).getByText('Ready for Review')).toBeTruthy()

    // Switching to the budget alternative re-prices the plan (272 instead of 380 => 400)
    await user.click(within(plan).getByText(/Compare with alternative/))
    await user.click(within(plan).getByTestId('select-alt-offer-wd-blue'))
    await waitFor(() => expect(within(plan).getByText('$400')).toBeTruthy())

    // Approve & pay -> the plan flips to active fulfillment
    await user.click(within(plan).getByText(/Approve & Pay All/))
    expect(await screen.findByText(/برنامه با موفقیت تأیید شد/)).toBeTruthy()
    expect(within(plan).getByText('En Route / Active')).toBeTruthy()

    // Orders tab is now populated with the plan
    await user.click(screen.getByTestId('nav_orders_tab'))
    const orders = screen.getByText(/Multi-Destination & Multi-Provider Plans/)
    expect(orders).toBeTruthy()
    expect(await screen.findByText(/Plan AUR-/)).toBeTruthy()
    expect(screen.getByText('IN FULFILLMENT')).toBeTruthy()
  })

  it('proactive alert banner can dismiss and trigger a coordinated request', async () => {
    const user = userEvent.setup()
    render(<App />)

    expect(screen.getByText('Vehicle Maintenance Nearing')).toBeTruthy()
    await user.click(screen.getByText(/Ask Aura to Coordinate/))

    // Banner for the first alert is dismissed and a BMW service intent is created
    await waitFor(() => expect(screen.queryByText('Vehicle Maintenance Nearing')).toBeNull())
    const plan = await screen.findByTestId('plan-card', {}, { timeout: 3000 })
    expect(within(plan).getByText(/BMW 320i 15,000 km Service Check/)).toBeTruthy()
  })

  it('exposes the context screen, partner console and operational map', async () => {
    const user = userEvent.setup()
    render(<App />)

    await user.click(screen.getByTestId('nav_context_tab'))
    expect(screen.getByText(/Registered Assets \(4\)/)).toBeTruthy()
    await user.click(screen.getByText(/Add Asset/))
    await user.type(screen.getByLabelText(/Asset Name/), 'Bosch Dishwasher')
    await user.click(screen.getByText('Register Asset'))
    expect(await screen.findByText(/Registered Assets \(5\)/)).toBeTruthy()

    await user.click(screen.getByTestId('nav_partner_tab'))
    expect(screen.getByText(/Live Catalog \(6 Items\)/)).toBeTruthy()
    await user.click(screen.getByText('Add Product'))
    await user.type(screen.getByLabelText('Product Name'), 'USB-C Docking Station')
    await user.click(screen.getByText('Publish to AI Engine'))
    expect(await screen.findByText(/Live Catalog \(7 Items\)/)).toBeTruthy()

    await user.click(screen.getByText('Service Specialists'))
    expect(screen.getByText(/Active Services \(4\)/)).toBeTruthy()
    await user.click(screen.getByText('Add Service'))
    await user.click(screen.getByText(/Cancel/))

    await user.click(screen.getByTestId('nav_map_tab'))
    expect(screen.getByText('OPERATIONAL MAP')).toBeTruthy()
    expect(screen.getByText('TechCorp Tower')).toBeTruthy()
  })
})
