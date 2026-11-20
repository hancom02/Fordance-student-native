# Proposal: RESTful API Backend Service and Session & Class Detail Screens

## Why

To align the FOR DANCE Android application with real-world enterprise Android production standards and satisfy the technical requirements of the Android Developer position (Kotlin, Jetpack Compose, RESTful API integration), the application must transition from direct BaaS Supabase client-side querying to a structured RESTful API architecture using **Retrofit 2** and **OkHttp 3**. Furthermore, students require dedicated detail views—**Session Detail** (with ExoPlayer Media3 video playback and join/favorite actions) and **Class Detail** (curriculum syllabus and enrollment)—to complete the end-to-end learning journey.

## What Changes

- **Backend REST API Service (`backend/`)**:
  - Deploy a lightweight Node.js/Express + TypeScript REST API service inside the repository monorepo connecting directly to the Supabase PostgreSQL database.
  - Expose standardized RESTful HTTP endpoints for Authentication (`/api/v1/auth/*`), Discover feed (`/api/v1/discover/feeds`), Course Catalog (`/api/v1/catalog/*`), Session Detail & Actions (`/api/v1/sessions/*`), and Class Detail (`/api/v1/classes/*`).
- **Android Network Layer Migration**:
  - Integrate **Retrofit 2** and **OkHttp 3** with Kotlinx Serialization converter into `app/build.gradle.kts`.
  - Implement `ForDanceApiService` with standard HTTP annotations (`@GET`, `@POST`, `@DELETE`, `@Path`, `@Query`, `@Body`).
  - Implement `AuthInterceptor` to dynamically attach `Authorization: Bearer <token>` from `UserPreferencesDataStore`.
  - Refactor existing Repositories (`AuthRepositoryImpl`, `DiscoverRepositoryImpl`, `CatalogRepositoryImpl`) to consume `ForDanceApiService`.
- **AndroidX Media3 (ExoPlayer) Integration**:
  - Add `androidx.media3:media3-exoplayer` and `androidx.media3:media3-ui`.
  - Build lifecycle-aware Jetpack Compose video player composable for streaming session demonstration videos.
- **Session Detail Screen (`features/session_detail`)**:
  - Hero ExoPlayer video player with aspect ratio handling, playback controls, and poster thumbnail fallback.
  - Detailed metadata display: title, level chip, genre chip, duration, instructor profile card.
  - Pedagogical insights: "What you will learn" and "What to prepare" sections.
  - Interactive actions: Join session button (with optimistic toggle state) and Favorite bookmark toggle.
- **Class Detail Screen (`features/class_detail`)**:
  - Hero header with class cover image, instructor information, level, and genre badges.
  - Comprehensive curriculum list (`LazyColumn`) of all sessions belonging to the class with order number, duration, and click-to-play navigation into Session Detail.
  - Primary CTA button for class enrollment / start learning.
- **Navigation Integration**:
  - Register `session_detail/{sessionId}` and `class_detail/{classId}` routes in `Screen.kt` and `ForDanceNavGraph.kt`.
  - Connect card click handlers in `DiscoverScreen` and `CatalogScreen` to navigate to respective detail screens.

## Capabilities

### New Capabilities
- `session-detail`: Rich interactive screen featuring ExoPlayer (Media3) video streaming, session learning outcomes, instructor insights, and real-time student actions (join session, bookmark favorite).
- `class-detail`: Comprehensive dance class curriculum screen presenting course syllabus, session sequence, instructor credentials, and seamless navigation into session playback.

### Modified Capabilities
- *(None - existing functional requirements for `auth`, `discover`, and `course-catalog` remain unchanged; only their data layer implementation transitions to Retrofit REST API).*

## Impact

- **Architecture**: Shifts Android data source from `io.github.jan.supabase` to `com.squareup.retrofit2:retrofit` and `okhttp3:okhttp`.
- **Monorepo**: Introduces `backend/` directory housing Express + TypeScript REST service.
- **Dependencies**: Adds Retrofit 2, OkHttp 3, and AndroidX Media3 (ExoPlayer) in `libs.versions.toml` and `app/build.gradle.kts`.
- **Navigation**: Extends `ForDanceNavGraph` with deep-linkable detail routes.
