# Aura AI — Android app (Jetpack Compose) + interactive web preview

**Aura AI** is an AI-powered everyday-life assistant: you describe what you need in natural
language (Persian or English) and Aura splits the request into *intents*, picks a provider offer for
each one, and assembles a single **Fulfillment Plan** that can be approved, re-priced and tracked.

| Android (source of truth) | Web preview (`web/`) |
| --- | --- |
| `app/src/main/java/com/example/**` — Jetpack Compose, Material 3, single-Activity MVVM | React + TypeScript port of the same screens, colours, copy, seed data and orchestration rules |

## The five screens

| Tab | Kotlin | Web |
| --- | --- | --- |
| Chat / orchestrator | `ui/chat/ChatScreen.kt` | `web/src/screens/ChatScreen.tsx` |
| Operational map | `ui/map/OperationalMapScreen.kt` | `web/src/screens/OperationalMapScreen.tsx` |
| Orders & fulfillment plans | `ui/orders/OrdersScreen.kt` | `web/src/screens/OrdersScreen.tsx` |
| Context & personalization | `ui/context/ContextScreen.kt` | `web/src/screens/ContextScreen.tsx` |
| Partner console (seller / service) | `ui/partner/PartnerPortalScreen.kt` | `web/src/screens/PartnerPortalScreen.tsx` |

Business logic lives in `data/EverydayAssistantRepository.kt` (orchestrator, plan confirmation,
offer switching, seller/service CRUD) and is mirrored 1:1 in `web/src/lib/orchestrator.ts` +
`web/src/lib/store.ts`.

## Run the interactive preview

```bash
cd web
npm install
npm run dev          # http://localhost:5173
```

Try the hero request (also available as a quick chip and via the microphone button):

> برای شرکت ۴ تا هارد میخوام، برای خونه هم گل سفارش بده، چهارشنبه کولر رو سرویس کن و برای ساعت ۶ هم تاکسی بگیر

Aura answers with four intents — SSD purchase, flower delivery, Daikin AC service booking and a
scheduled taxi — totalling **$508**, with a budget alternative (WD Blue, $272) that re-prices the
plan to **$400**, then **Approve & Pay All** moves it to *En Route / Active* and the map / orders
tabs.

Other preview commands:

```bash
npm run test         # 3 vitest + Testing Library specs driving the real UI
npm run build        # type-check + production bundle into web/dist
npm run emoji:subset # regenerate the subsetted colour-emoji font (needs: pip install fonttools brotli)
```

### Outputs you can look at right now

| Output | Where |
| --- | --- |
| **Live preview** (runs here, open it on your phone too) | the LIVE PREVIEW panel of this session — dev server on `0.0.0.0:5173` |
| **Single-file app** — the whole app in one offline HTML file, no server needed | [`docs/aura-ai-app.html`](docs/aura-ai-app.html) (1.4 MB, `npm run build:standalone`, verified offline with 0 network requests) |
| **Screenshots** of all five tabs | [`web/screenshots`](web/screenshots) |
| **Installable APK** | see *Get an installable APK* below — not built here because this sandbox cannot reach Google's Maven repository |

## Build the Android app

The repository intentionally ships only the Gradle **wrapper properties**
(`gradle/wrapper/gradle-wrapper.properties`); generate the wrapper once, then build:

```bash
gradle wrapper                 # creates ./gradlew (needs a local Gradle 8.x+)
./gradlew assembleDebug        # -> app/build/outputs/apk/debug/app-debug.apk
# or open the project in Android Studio and press Run
```

Requirements: JDK 17+, Android SDK with **compileSdk 36 / build-tools** matching `AGP 9.1.1`
(see `gradle/libs.versions.toml`). A release build additionally needs `KEYSTORE_PATH`,
`STORE_PASSWORD` and `KEY_PASSWORD` (see `app/build.gradle.kts`).

### Get an installable APK without a local Android setup

The quickest path is GitHub Actions, which has full network access to Google's Maven repository:

1. Open **<https://github.com/samv-95/mion/new/arena/01a0b02b-mion?filename=.github/workflows/build-apk.yml>**
   (or *Add file ▸ Create new file* while on the `arena/01a0b02b-mion` branch) and name it
   `.github/workflows/build-apk.yml`.
2. Paste the contents of [`docs/ci/build-apk.yml`](docs/ci/build-apk.yml) and commit.
3. Go to **Actions ▸ Build debug APK ▸ Run workflow** (<https://github.com/samv-95/mion/actions>).
   When the run finishes, the APK is under **Artifacts ▸ `aura-ai-apk`** on the run page
   (artifact downloads require being signed in to GitHub).

The template is not active in this repository because the automation token that created this branch
has no `workflows` permission, so it could not be pushed to `.github/workflows/`. Its first run is
the real verification — it was written in an environment without access to Google's Maven repository.

Builds need network access to `dl.google.com`, `maven.google.com`, `repo.maven.apache.org` and
`services.gradle.org`; the sandbox this preview was prepared in blocks those hosts, which is why the
APK could not be compiled here — the web preview above is the runnable output of the same UI/logic.
