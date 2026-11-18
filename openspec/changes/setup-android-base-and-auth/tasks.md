# Tasks

## 1. Project Initialization & Gradle Scaffolding

- [ ] 1.1 Initialize git repository at `D:\Job\Project\FORDANCE-NATIVE` and create `.gitignore` for Android/Gradle. Verify `git status` shows clean initial state.
- [ ] 1.2 Setup Gradle Version Catalog in `gradle/libs.versions.toml` with dependencies for AGP, Kotlin 2.x, Compose BOM, Hilt, Supabase BOM, Ktor, and DataStore. Verify file syntax is valid.
- [ ] 1.3 Create root `settings.gradle.kts`, `build.gradle.kts`, and `app/build.gradle.kts` with application plugin, Hilt plugin, Compose compiler plugin, and Java 17 toolchain. Verify Gradle configuration evaluates without errors.
- [ ] 1.4 Setup standard Android project source tree under `app/src/main/` including `AndroidManifest.xml` with Internet permission and application class `ForDanceApp` annotated with `@HiltAndroidApp`. Verify file structure is complete.

## 2. Core Architecture & Infrastructure

- [ ] 2.1 Implement `core/common` utilities including `ResultWrapper` (`Success`, `Error`, `Loading`) and Coroutine dispatchers module. Verify unit test for `ResultWrapper` mapping succeeds.
- [ ] 2.2 Configure Supabase Client in `core/network/SupabaseClientProvider` with Supabase URL, anon key, Auth plugin, and Postgrest plugin using Ktor engine. Verify client instance can be injected via Hilt `NetworkModule`.
- [ ] 2.3 Implement `core/datastore/UserPreferencesDataStore` with methods to save, read, and clear session tokens and student profile. Verify unit test for reading and writing preferences.

## 3. Data & Domain Layer (Auth)

- [ ] 3.1 Create domain models `StudentUser` and `UserSession` under `domain/model/`. Verify model properties match backend expectations.
- [ ] 3.2 Define `AuthRepository` interface in `domain/repository/` with sign-in, sign-up, sign-out, and session-flow methods. Verify interface methods align with `specs/auth/spec.md`.
- [ ] 3.3 Implement `AuthRepositoryImpl` in `data/repository/` integrating Supabase Auth and DataStore caching, plus student role verification against the `users` table. Verify repository unit tests pass.
- [ ] 3.4 Implement domain UseCases: `SignInUseCase`, `SignUpUseCase`, `GetSessionUseCase`, and `SignOutUseCase`. Verify UseCase unit tests pass for student role and invalid credentials scenarios.

## 4. Presentation Layer & UI (Auth)

- [ ] 4.1 Define Fordance theme in `presentation/theme/` (Color palette matching dark purple branding, Typography, Shapes). Verify Compose Preview renders theme correctly.
- [ ] 4.2 Create reusable Compose components in `presentation/components/` (PrimaryButton, OutlinedTextField, LoadingIndicator, ErrorAlert). Verify components render in Compose Previews.
- [ ] 4.3 Implement `AuthViewModel` with `AuthUiState`, `AuthUiEvent`, and `AuthUiEffect` channels. Verify ViewModel test covers sign-in success, sign-in failure, and loading states.
- [ ] 4.4 Build `LoginScreen` and `RegisterScreen` in Jetpack Compose connected to `AuthViewModel`. Verify screen layouts in Compose Preview.
- [ ] 4.5 Set up root `NavGraph` in `presentation/navigation/` with splash/session-check, login, register, and home placeholder destinations. Verify navigation transitions work correctly.

## 5. End-to-End Verification

- [ ] 5.1 Build the complete Android project using `./gradlew assembleDebug` and verify build succeeds without compiler errors.
- [ ] 5.2 Verify sign-in flow against live Supabase backend on emulator/device, verifying successful navigation and session persistence across app process kill and restart.
