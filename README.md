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

Rendered screenshots of every screen are in [`web/screenshots`](web/screenshots) (captured from the
web preview with headless Chromium).

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

A ready-made GitHub Actions recipe that produces an installable `app-debug.apk` artifact lives at
[`docs/ci/build-apk.yml`](docs/ci/build-apk.yml) — copy it to `.github/workflows/build-apk.yml` and
run it from the Actions tab (it is not active here because the token used for this branch has no
`workflows` permission). It was written in an environment without access to Google's Maven
repository, so treat its first run as the verification.

Builds need network access to `dl.google.com`, `maven.google.com`, `repo.maven.apache.org` and
`services.gradle.org`; the sandbox this preview was prepared in blocks those hosts, which is why the
APK could not be compiled here — the web preview above is the runnable output of the same UI/logic.
