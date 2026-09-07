# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A cross-platform JavaFX desktop GUI for managing SAP Commerce Cloud (CCV2) builds and deployments via the [SAP Commerce Cloud Management API](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/452dcbb0e00f47e88a69cdaeb87a925d/66abfe678b55457fab235ce8039dda71.html). It lets users trigger builds, monitor progress, and deploy to environments — replacing the CLI with a visual interface.

## Commands

```bash
./gradlew run                      # Run the app
./gradlew build                    # Compile and build
./gradlew packageAppForMac         # Package for macOS (tarball)
./gradlew packageAppForLinux       # Package for Linux (tarball)
./gradlew packageAppForWindows     # Package for Windows (zipball)
./gradlew packageApp               # Package for all platforms
```

There are no tests in this project.

## Architecture

**Two source packages:**

- `com.nbeghin.ccv2.api.gui.sapcommercecloudapigui` — the application code
- `com.sap.cx.commercecloud.management.openapi` — Swagger/OpenAPI auto-generated client code (Retrofit2). Do not hand-edit; regenerate with swagger-codegen-cli if the API spec changes.

**Application layer structure:**

- `App` — entry point and static preferences store using `java.util.prefs.Preferences`. All preferences are namespaced under the subscription code (except the subscription code itself). `Constants.SUBSCRIPTION_CODE` and `Constants.ACCESS_TOKEN` are set at runtime from stored preferences.
- `JavaFXApplication` — JavaFX lifecycle (extends `Application`), loads `main.fxml`
- `AbstractController` — base controller providing `dialogError()`, `dialogInfo()`, `notificationInfo()`, cross-platform notification dispatch (osascript on macOS, ControlsFX on others), and FontAwesome glyph setup
- `MainController` — primary UI controller (builds tab + deployments tab). Binds FXML fields, persists user selections via `App.savePreference()`, spawns background `Task` threads for all API calls
- `BuildController` — progress dialog controller for the build+deploy flow (`progress.fxml`)
- `SettingsController` — settings dialog (`settings.fxml`) for subscription code and API token

**Task pattern:** All API calls are `javafx.concurrent.Task<T>` subclasses extending `AbstractTask<T>`. `AbstractTask` constructs the `ApiClient` with the OAuth2 access token from `Constants.ACCESS_TOKEN` on every instantiation, then creates Retrofit service instances (`BuildApi`, `DeploymentApi`, `EnvironmentApi`, `EndpointApi`). Tasks are run on new threads spawned directly by the controllers. `WaitForCompletion` tasks poll the API until a build/deployment reaches a terminal state.

**Key constants** (`utils/Constants.java`): `BASE_PATH` is the hardcoded API URL (`https://portalapi.commerce.ondemand.com/v2/`). `MAX_NUM_BUILDS = 8` limits the build list fetch. `DEBUG_ENABLED` toggles OkHttp request/response logging via `OkHttpLoggingInterceptor`.

**UI resources** (`src/main/resources/com/nbeghin/ccv2/api/gui/sapcommercecloudapigui/`):
- `main.fxml` — main window with Builds and Deployments tabs
- `progress.fxml` — modal progress dialog for build/deploy operations
- `settings.fxml` — settings dialog
