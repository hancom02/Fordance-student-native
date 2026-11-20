# Design: RESTful API Backend Service and Session & Class Detail Screens

## Context

See `proposal.md` for background and motivation. The FOR DANCE Android application currently uses `io.github.jan.supabase:postgrest-kt` querying PostgreSQL directly. To demonstrate industry-standard Android architecture (Retrofit 2, OkHttp 3, Clean Architecture) for enterprise recruitment, we introduce a dedicated REST API service and implement the core detail views.

## Goals / Non-Goals

**Goals:**
- Provide a lightweight, reliable Node.js Express REST API in `backend/` exposing standard HTTP endpoints.
- Transition all Android network calls to **Retrofit 2** + **OkHttp 3** with custom `AuthInterceptor` and serialization.
- Deliver production-ready **SessionDetailScreen** with AndroidX Media3 ExoPlayer video playback and interactive actions (join, bookmark).
- Deliver production-ready **ClassDetailScreen** with structured session curriculum list.
- Maintain 100% build pass rate on Android with clean Unit Tests.

**Non-Goals:**
- Re-architecting database schemas or discarding existing Supabase tables.
- Building complex web admin dashboards.
- Implementing in-app payment transactions (marked as out of scope for Student Change 3).

## Decisions

### Decision 1: Monorepo Express + TypeScript REST Service connecting to Supabase PostgreSQL
- **Rationale**: Keeps database schema, seeded tables, and media assets intact while exposing clean REST endpoints (`/api/v1/*`). Express runs with minimal footprint locally (`localhost:3000` / `10.0.2.2:3000` for Android emulator) and requires zero migration overhead.
- **Alternatives Considered**:
  - *Spring Boot / NestJS*: Too heavy, requires extensive boilerplate that risks breaching the 24-hour deadline.
  - *Direct PostgREST HTTP queries in Retrofit*: Quicker, but produces database-like query params (`/rest/v1/sessions?select=*`) rather than idiomatic enterprise REST API paths.

### Decision 2: Retrofit 2 + OkHttp 3 + Kotlinx Serialization on Android
- **Rationale**: Retrofit 2 + OkHttp 3 is the de facto industry standard for Android. Combining it with `kotlinx.serialization` matches our existing domain DTOs without introducing duplicate JSON parsers like Gson/Moshi.
- **Interceptors**:
  - `AuthInterceptor`: Reads stored JWT access token from `UserPreferencesDataStore` and attaches `Authorization: Bearer <token>` to protected endpoints.
  - `HttpLoggingInterceptor`: Enabled in debug builds for network transparency.

### Decision 3: AndroidX Media3 ExoPlayer in Jetpack Compose
- **Rationale**: Media3 is Google's modern media playback library replacing legacy ExoPlayer. Wrapping `PlayerView` inside `AndroidView` with a `DisposableEffect` and `LifecycleEventObserver` guarantees proper pause/release handling when navigating or backgrounding.
- **Alternatives Considered**:
  - *VideoView / Android MediaPlayer*: Lacks adaptive streaming, caching, and robust codec support.

### Decision 4: Detail Screens Architecture (MVVM & Clean)
- `SessionDetailViewModel` exposes `SessionDetailUiState` (Loading, Success, Error, IsJoined, IsFavorited).
- `ClassDetailViewModel` exposes `ClassDetailUiState` (Loading, Success, Error, ClassInfo, SessionList).
- Uses `ResultWrapper<T>` for consistent error propagation across repository and usecase layers.

## Technical Architecture & Endpoints

### 1. REST Endpoints Specification
```
POST   /api/v1/auth/login                  -> { email, password } => { token, user }
POST   /api/v1/auth/register               -> { email, password, full_name } => { token, user }
GET    /api/v1/auth/me                     -> Bearer Auth => { user }
GET    /api/v1/discover/feeds              -> Bearer Auth => { featured, joined, classes, favourites }
GET    /api/v1/catalog/sessions            -> Query: { q, level, genre, instructor_id } => Session[]
GET    /api/v1/catalog/classes             -> Query: { q, level, genre, instructor_id } => Class[]
GET    /api/v1/catalog/instructors         -> Instructor[]
GET    /api/v1/sessions/:id                -> Bearer Auth => SessionDetailDto (with instructor & class)
POST   /api/v1/sessions/:id/join           -> Bearer Auth => { success: true }
DELETE /api/v1/sessions/:id/join           -> Bearer Auth => { success: true }
POST   /api/v1/sessions/:id/favorite       -> Bearer Auth => { success: true }
DELETE /api/v1/sessions/:id/favorite       -> Bearer Auth => { success: true }
GET    /api/v1/classes/:id                 -> Bearer Auth => ClassDetailDto (with instructor & sessions)
```

### 2. Android Network Security Configuration
- To allow communication with local development backend (`http://10.0.2.2:3000` or local IP), configure `network_security_config.xml` to permit cleartext traffic for local loopback / development IPs while keeping production HTTPS secure.

## Risks / Trade-offs

- **[Risk] Android cleartext traffic blocked on Android 9+**
  → *Mitigation*: Add `android:networkSecurityConfig="@xml/network_security_config"` with cleartext allowed for `10.0.2.2` and `localhost`.
- **[Risk] ExoPlayer resource leaks when fast-switching screens**
  → *Mitigation*: Release ExoPlayer instance cleanly inside `DisposableEffect(Unit) { onDispose { player.release() } }`.
- **[Risk] Network latency or local server offline during testing**
  → *Mitigation*: Support base URL configuration in `local.properties` / `BuildConfig` with graceful retry and mock fallback in tests.
