# Tasks

## 1. Backend REST API Service (`backend/`)

- [x] 1.1 Scaffold `backend/` directory with `package.json`, `tsconfig.json`, and dependencies (`express`, `cors`, `dotenv`, `@supabase/supabase-js`, `ts-node-dev`) and verify clean build.
- [x] 1.2 Implement Supabase client configuration and JWT `authMiddleware` for token verification.
- [x] 1.3 Implement Auth endpoints (`POST /api/v1/auth/login`, `POST /api/v1/auth/register`, `GET /api/v1/auth/me`).
- [x] 1.4 Implement Discover & Catalog endpoints (`GET /api/v1/discover/feeds`, `GET /api/v1/catalog/sessions`, `GET /api/v1/catalog/classes`, `GET /api/v1/catalog/instructors`).
- [x] 1.5 Implement Session & Class detail and engagement endpoints (`GET /api/v1/sessions/:id`, `POST/DELETE /api/v1/sessions/:id/join`, `POST/DELETE /api/v1/sessions/:id/favorite`, `GET /api/v1/classes/:id`).
- [x] 1.6 Verify backend server starts and returns valid JSON envelope for all endpoints via local test script.

## 2. Android Dependencies & Retrofit Network Layer

- [x] 2.1 Add Retrofit 2, OkHttp 3, Logging Interceptor, and Media3 ExoPlayer dependencies in `gradle/libs.versions.toml` and `app/build.gradle.kts`.
- [x] 2.2 Configure `network_security_config.xml` and update `AndroidManifest.xml` to allow development traffic to `10.0.2.2`.
- [x] 2.3 Implement `AuthInterceptor` retrieving Bearer token from `UserPreferencesDataStore` and attach to HTTP requests.
- [x] 2.4 Define `ForDanceApiService` with standard Retrofit annotations (`@GET`, `@POST`, `@DELETE`, `@Path`, `@Query`, `@Body`).
- [x] 2.5 Update `NetworkModule` providing `OkHttpClient`, `Retrofit`, and `ForDanceApiService`, and refactor `AuthRepositoryImpl`, `DiscoverRepositoryImpl`, `CatalogRepositoryImpl` to use Retrofit with passing unit tests.

## 3. Detail Repositories and Domain Layer

- [x] 3.1 Create DTOs (`SessionDetailDto`, `ClassDetailDto`, `ActionResponseDto`) and Domain Models (`SessionDetail`, `ClassDetail`) with bidirectional mappers.
- [x] 3.2 Implement `DetailRepository` supporting session detail, class detail, join toggle, and favorite toggle with unit tests.
- [x] 3.3 Create Use Cases (`GetSessionDetailUseCase`, `ToggleJoinSessionUseCase`, `ToggleFavoriteSessionUseCase`, `GetClassDetailUseCase`) with Hilt DI bindings.

## 4. AndroidX Media3 ExoPlayer & Reusable Components

- [x] 4.1 Implement `VideoPlayer` Jetpack Compose composable wrapping AndroidX Media3 `PlayerView` with lifecycle-aware play/pause and dispose handling.
- [x] 4.2 Implement detail UI components: `InstructorOverviewCard`, `CurriculumSessionItem`, and `DetailActionBottomBar`.

## 5. Session Detail Screen Implementation

- [x] 5.1 Implement `SessionDetailViewModel` and `SessionDetailUiState` managing playback, metadata, and optimistic join/favorite updates with unit tests.
- [x] 5.2 Implement `SessionDetailScreen` composable integrating `VideoPlayer`, metadata chips, instructor card, pedagogical sections, and action buttons.

## 6. Class Detail Screen & Navigation Integration

- [x] 6.1 Implement `ClassDetailViewModel` and `ClassDetailUiState` managing class details and curriculum list with unit tests.
- [x] 6.2 Implement `ClassDetailScreen` composable with hero cover image, instructor row, and `LazyColumn` curriculum sessions.
- [x] 6.3 Update `Screen.kt` and `ForDanceNavGraph.kt` with `SessionDetail` and `ClassDetail` routes, and connect card click handlers in `DiscoverScreen` and `CatalogScreen`.
- [x] 6.4 Run `./gradlew compileDebugKotlin` and `./gradlew test` in `D:\Job\Project\FORDANCE-NATIVE` to verify all components compile cleanly and all unit tests pass.
