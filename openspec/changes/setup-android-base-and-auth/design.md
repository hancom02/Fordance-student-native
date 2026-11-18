# Design: Android Base Architecture and Authentication

## Context

See `proposal.md` for motivation and system scope. The target implementation will be created at `D:\Job\Project\FORDANCE-NATIVE` as a standalone Android Native application connecting to the existing Fordance Supabase backend (`https://dkasnzwgahkgoczktpfe.supabase.co`).

## Goals / Non-Goals

**Goals:**
- Establish modern Android project tooling with Gradle Version Catalog (`libs.versions.toml`), Kotlin 2.x, and Jetpack Compose BOM.
- Setup Hilt Dependency Injection with modular modules (`NetworkModule`, `DataStoreModule`, `RepositoryModule`).
- Integrate official Supabase Kotlin Multiplatform SDK (`auth-kt`, `postgrest-kt`, `realtime-kt`) using Ktor OkHttp engine.
- Implement session persistence with Jetpack DataStore Preferences to securely maintain authentication tokens and auto-login state.
- Implement Clean Architecture layering (Core, Data, Domain, Presentation) with Unidirectional Data Flow (UDF) for Auth.
- Build reactive Compose UI for Login and Registration screens with dark/purple Fordance theme.

**Non-Goals:**
- Social/OAuth sign-in (Google/Apple) or Biometrics (maintaining 1:1 parity with current RN email/password flow).
- Implementing downstream screens (Discover, Home, Video Player, Profile) in this change.

## Decisions

### 1. Build System: Gradle Version Catalog (`libs.versions.toml`)
- **Choice**: Use Gradle Version Catalog (`gradle/libs.versions.toml`) with Kotlin DSL (`build.gradle.kts`).
- **Rationale**: Standard recommended practice by Google Android team; provides type-safe dependency access, centralized dependency versioning, and clean build files.
- **Alternatives Considered**: Groovy Gradle buildscripts (legacy, lack IDE autocompletion) or `buildSrc` (slower configuration caching).

### 2. Dependency Injection: Hilt
- **Choice**: Google Dagger Hilt (`com.google.dagger:hilt-android`).
- **Rationale**: Compile-time dependency graph verification, seamless integration with `@HiltViewModel` and Jetpack Compose Navigation (`hiltViewModel()`).
- **Alternatives Considered**: Koin (runtime resolution, potential runtime crashes if bindings are missing).

### 3. Backend Integration: Official Supabase Kotlin Multiplatform SDK
- **Choice**: `io.github.jan-tennert.supabase:bom:3.x` with `auth-kt` and `postgrest-kt`, using Ktor client OkHttp engine.
- **Rationale**: Provides idiomatic Kotlin Coroutines & Flow APIs, automatic session refresh handling, and type-safe Postgrest queries.
- **Alternatives Considered**: Custom Retrofit/Ktor REST client (requires manually writing endpoints, auth refresh interceptors, and WebSocket realtime protocols).

### 4. Local Session Storage: Jetpack DataStore Preferences
- **Choice**: `androidx.datastore:datastore-preferences`.
- **Rationale**: Modern, asynchronous, safe API running on Kotlin Coroutines & Dispatchers.IO, eliminating UI blocking inherent in traditional `SharedPreferences`.
- **Alternatives Considered**: `SharedPreferences` (blocking I/O) or `EncryptedSharedPreferences` (known key-store corruption edge cases across OEM Android forks).

### 5. Presentation Pattern: MVVM + Unidirectional Data Flow (UDF)
- **Choice**: `ViewModel` exposing `StateFlow<AuthUiState>`, accepting `AuthUiEvent` (SignInClicked, SignUpClicked, InputChanged), and emitting one-off navigation/toast effects via `Channel<AuthUiEffect>`.
- **Rationale**: Complete separation of UI presentation from business logic; easily unit-testable and lifecycle-aware.
- **Alternatives Considered**: Simple stateless callbacks (hard to manage configuration changes and async loading states).

## Architecture & Folder Structure (Feature-First)

The project adheres strictly to **Feature-First Clean Architecture** (referencing `geckospace-main-app` modules pattern), where each feature directory self-contains its own `presentation`, `domain`, and `data` layers:

```
com.fordance.student/
+-- core/
|   +-- common/           (ResultWrapper, Dispatchers)
|   +-- components/       (Shared Compose UI components)
|   +-- datastore/        (UserPreferencesDataStore)
|   +-- network/          (SupabaseConfig)
|   +-- theme/            (Color, Type, Theme)
+-- di/                   (Global Hilt modules: NetworkModule, DataStoreModule)
+-- navigation/           (Screen routes, ForDanceNavGraph)
+-- features/
|   +-- auth/
|   |   +-- data/
|   |   |   +-- remote/dto/ (UserDto)
|   |   |   +-- repository/ (AuthRepositoryImpl)
|   |   +-- domain/
|   |   |   +-- model/      (StudentUser, UserSession)
|   |   |   +-- repository/ (AuthRepository interface)
|   |   |   +-- usecase/    (SignInUseCase, SignUpUseCase, GetSessionUseCase, SignOutUseCase)
|   |   +-- presentation/   (AuthViewModel, LoginScreen, RegisterScreen)
|   |   +-- di/             (AuthModule binding AuthRepository)
|   +-- home/
|       +-- presentation/   (HomePlaceholderScreen)
+-- ForDanceApp.kt        (@HiltAndroidApp)
+-- MainActivity.kt       (@AndroidEntryPoint Single Activity)
```

## Risks / Trade-offs

- **[Risk] User role mismatch during authentication**: User credentials might be valid in Supabase Auth, but the user is not registered as a `student` (e.g. instructor or admin).
  - *Mitigation*: In `SignInUseCase`, after successful `supabase.auth.signInWith(Email)`, immediately query `users` table filtering by `email` and `role = 'student'`. If not matched or empty, perform `supabase.auth.signOut()`, clear session, and throw a `RoleRestrictionException`.
- **[Risk] Network failure on app cold launch auto-login**: Token validation might fail due to lack of internet connectivity.
  - *Mitigation*: Cache the last authenticated user profile in DataStore so the app can launch in offline-tolerant state or gracefully prompt retry rather than immediately logging the student out.

## Migration & Verification Plan

1. Initialize git and Gradle scaffolding at `D:\Job\Project\FORDANCE-NATIVE`.
2. Sync Gradle dependencies in Android Studio.
3. Run the application on an Android Emulator (API 34/35).
4. Perform end-to-end authentication test against live Supabase backend:
   - Login with valid student credentials -> verify navigation to placeholder Home screen.
   - Login with invalid credentials -> verify error alert.
   - Kill app and restart -> verify auto-login restores session directly.
