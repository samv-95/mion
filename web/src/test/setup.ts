// jsdom lacks a canvas implementation; the operational map degrades gracefully
// (the component already guards on a missing 2D context), so we only silence the
// "not implemented" notice jsdom would otherwise print for every draw call.
const originalError = console.error
console.error = (...args: unknown[]) => {
  const first = args[0]
  if (typeof first === 'string' && first.includes('Not implemented: HTMLCanvasElement')) return
  originalError(...args)
}
