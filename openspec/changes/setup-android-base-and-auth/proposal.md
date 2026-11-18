# Proposal: Setup Android Base Architecture and Authentication

## Why

Migrating the FORDANCE Student mobile app from React Native to Android Native (Kotlin + Jetpack Compose + Clean Architecture) provides superior UI smoothness, native lifecycle management for media playback, and robust type safety. Establishing a solid base architecture, dependency injection, and authentication foundation in a dedicated Android project is the essential first milestone before migrating feature screens.

## What Changes

- **Android Project Initialization**: Initialize a standalone Git repository at `D:\Job\Project\FORDANCE-NATIVE` with Gradle Version Catalog (`libs.versions.toml`), Kotlin 2.x, Jetpack Compose BOM, Material 3, and Hilt DI.
- **Clean Architecture Foundation**: Establish standard modular layers:
  - `core/`: Dispatchers, Network, DataStore session storage, and Result wrappers.
  - `di/`: Hilt modules for Network (Supabase), Local Storage (DataStore), and Repositories.
  - `data/`: Remote Supabase data source, DTOs, and Repository implementations.
  - `domain/`: Business models, Repository interfaces, and Use Cases for authentication.
  - `presentation/`: Base theme, common UI components, and Navigation graph.
- **Supabase Kotlin SDK Integration**: Configure official Supabase Kotlin Multiplatform SDK (`bom:3.x`, `auth-kt`, `postgrest-kt`, `realtime-kt`) powered by Ktor.
- **Session Persistence**: Implement reactive session caching via Jetpack DataStore Preferences to maintain user login state across app restarts.
- **Authentication Feature**: Implement Sign In (email/password with student role verification), Sign Up (student role), Session check on startup, and Sign Out with MVI/MVVM ViewModel and Compose UI.

## Out of Scope

- Course catalog, Discover screen, and Home screen (deferred to Change 2).
- Video player, Media3 ExoPlayer integration, comments, and ratings (deferred to Change 3).
- User profile editing, favorites, and joined sessions management (deferred to Change 4).

## Capabilities

### New Capabilities
- `auth`: Student authentication and session lifecycle management, including email/password sign-in, sign-up with role validation, secure session token persistence via DataStore, and sign-out.

### Modified Capabilities
- *(None - this is a new native Android implementation)*

## Impact

- **New Repository**: Creates the clean Android project structure in `D:\Job\Project\FORDANCE-NATIVE`.
- **Backend**: Connects to the existing Supabase instance (`https://dkasnzwgahkgoczktpfe.supabase.co`) without requiring database schema changes.
- **Existing App**: Leaves the React Native repository intact and functioning independently during migration.
