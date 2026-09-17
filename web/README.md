# Aura AI — web preview

An interactive, phone-framed **port of the Aura AI Android app**: same five screens, same colours
(`ui/theme/Color.kt`), same seed data (`data/EverydayAssistantRepository.kt`) and the same
rule-based orchestrator, implemented in React + TypeScript so the app can be run and reviewed in a
browser without an Android build.

## Commands

| Command | Description |
| --- | --- |
| `npm install` | install dependencies |
| `npm run dev` | Vite dev server on `0.0.0.0:5173` (binds all hosts, CORS open, for the Arena preview proxy) |
| `npm run test` | Vitest + Testing Library smoke tests that drive the real UI |
| `npm run build` | `tsc --noEmit` + production bundle in `dist/` |
| `npm run preview` | serve the production bundle |
| `npm run build:standalone` | one self-contained HTML file (JS + CSS + fonts inlined) → `web/standalone/aura-ai-app.html` and `docs/aura-ai-app.html` |
| `npm run emoji:subset` | rebuild `src/assets/noto-color-emoji-subset.woff2` (`pip install fonttools brotli` first) |

## Layout

```
src/
├── lib/
│   ├── models.ts        # mirrors model/AiOrchestratorModels.kt + model/ContextModels.kt
│   ├── seed.ts          # mirrors the repository seed data (assets, locations, alerts, catalog…)
│   ├── orchestrator.ts  # port of EverydayAssistantRepository.orchestratePrompt()
│   └── store.ts         # Observable store (useSyncExternalStore) = the shared ViewModel
├── components/          # AuraBadge / AuraCard / ContextTag / dock, Material-symbol icons
├── screens/             # Chat, OperationalMap, Orders, Context, PartnerPortal
└── test/app.test.tsx    # orchestration, proactive alert and context/partner flows
```

## Behaviour parity notes

* `sendPrompt` shows the same 500 ms "Orchestrating intents…" delay before the response arrives.
* The orchestrator is keyword based, exactly like the Kotlin version: hard-drive / flower / AC /
  taxi intents, the BMW maintenance, parents'-phone and generic fallbacks.
* Approving a plan sets it to `IN_FULFILLMENT`, appends the Persian confirmation message and updates
  the plan everywhere it is rendered (chat + Orders tab).
* `switchOffer` swaps a selected offer with its alternative and recomputes the plan total.
* Privacy toggles, asset register/delete, product/service publishing all mutate the same store.

## Fonts

Everything is bundled locally (no CDN calls, so the preview works offline and behind the sandbox
proxy): Vazirmatn for Latin + Persian copy, Material Symbols Outlined for icons, and a 27 KB
subset of Noto Color Emoji for the ✅ 📦 🔧 🚚 ⚙️ ⚡ 💡 ⭐ 📍 glyphs used in the interface.

## Standalone single-file build

`npm run build:standalone` produces **`aura-ai-app.html`** (~1.4 MB): the whole app — React bundle,
styles, Vazirmatn, icons and the colour-emoji subset — inlined into one file with **no external
requests at all**. Open it by double-clicking, send it over messenger, or drop it on a phone; it
works offline. The committed copy lives at [`../docs/aura-ai-app.html`](../docs/aura-ai-app.html)
(verified with headless Chromium loaded over `file://` with all network access blocked: 0 blocked
requests, 0 console errors, the 4-intent `$508` plan and the operational map all working).
