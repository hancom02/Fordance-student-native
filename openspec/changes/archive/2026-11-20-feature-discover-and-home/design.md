# Design: Discover Feed and Course Catalog

## Context

Change 1 established the foundation: Kotlin 2.x, Jetpack Compose BOM, Material 3, Hilt DI, DataStore session storage, and Supabase Auth with student role verification. The app currently displays a temporary `HomePlaceholderScreen` upon successful sign-in.

The React Native application (`src/navigation/navigation.tsx`, `src/screens/discover/`, and `src/screens/home/`) structures student exploration into two primary tabs:
1. **Discover Screen**: A rich content feed with horizontal carousels for featured sessions, student-joined sessions (`users_sessions_joined`), class programs (`classes`), and saved favorite sessions (`users_sessions_favourite`).
2. **Home / Catalog Screen**: A search and browsing hub featuring 3 category tabs (`SESSIONS`, `CLASSES`, `INSTRUCTORS`), a text search input bar, a multi-criteria filter bottom sheet (Levels, Genres, Instructors), and card list views.

This design outlines the native Android migration of these features, leveraging Feature-First Clean Architecture, Kotlin Coroutines Flow, Hilt DI, and Coil for asynchronous image loading.

## Goals / Non-Goals

**Goals:**
- Implement a persistent Material 3 `NavigationBar` scaffold containing Discover, Catalog/Home, and Profile/Settings tabs.
- Implement the Discover Feed screen with featured sessions carousel, joined sessions carousel, class programs carousel, and favorite sessions carousel.
- Implement the Course Catalog screen with tab switching (`SESSIONS`, `CLASSES`, `INSTRUCTORS`), debounced search input, and multi-criteria filter bottom sheet.
- Implement Clean Architecture data layers for querying Supabase tables: `sessions`, `classes`, `users` (instructors), `users_sessions_joined`, and `users_sessions_favourite`.
- Integrate Coil 2.x for smooth image loading with crossfade and error fallback.
- Support pull-to-refresh and reactive loading/empty/error states across both screens.

**Non-Goals:**
- ExoPlayer video playback and full session player controls (deferred to Change 3).
- Session join, payment processing, and checkout flow (deferred to Change 3/4).
- Submitting reviews, ratings, and comments (deferred to Change 3).
- Full user profile and instructor profile detail editors (deferred to Change 4).

## Decisions

### 1. Main Bottom Navigation Scaffold
- **Decision**: Create a `MainScaffold` Composable that hosts a Material 3 `NavigationBar` with 3 items:
  - `Discover`: icon `Icons.Default.Explore` / `Home`
  - `Catalog`: icon `Icons.Default.Search` / `Book`
  - `Profile`: icon `Icons.Default.Person` (includes current user info & sign-out button)
- **Rationale**: Replaces the interim `HomePlaceholderScreen` with the permanent app shell. Detail screens (SessionDetail, ClassDetail, InstructorDetail) navigate as top-level destinations over the main graph.

### 2. Feature-First Clean Architecture Organization
- **Decision**: Organize new code under two feature packages:
  - `com.fordance.student.features.discover`:
    - `data/`: `dto/`, `datasource/`, `repository/DiscoverRepositoryImpl.kt`
    - `domain/`: `model/`, `repository/DiscoverRepository.kt`, `usecase/`
    - `presentation/`: `DiscoverViewModel.kt`, `DiscoverUiState.kt`, `DiscoverScreen.kt`
  - `com.fordance.student.features.catalog`:
    - `data/`: `dto/`, `datasource/`, `repository/CatalogRepositoryImpl.kt`
    - `domain/`: `model/`, `repository/CatalogRepository.kt`, `usecase/`
    - `presentation/`: `CatalogViewModel.kt`, `CatalogUiState.kt`, `CatalogScreen.kt`, `FilterBottomSheet.kt`
  - Shared UI components in `com.fordance.student.core.components`:
    - `SessionCard`, `SessionHorizontalCard`, `ClassCard`, `ClassWiderCard`, `InstructorCard`, `TagChip`, `SearchInputBar`
- **Rationale**: Preserves modularity and separation of concerns matching the structure introduced in Change 1 (`features/auth`).

### 3. Data Fetching Strategy for User-Linked Sessions
- **Decision**: To fetch "Joined Sessions" and "Favourite Sessions", use a two-step query:
  1. Query `users_sessions_joined` / `users_sessions_favourite` filtered by `user_id.eq(studentUserId)` to retrieve session IDs.
  2. If session IDs exist, query `sessions` filtered by `id.isIn(sessionIds)` and active status (`neq("status", "rejected")`, etc.).
- **Rationale**: Mirrors the existing React Native implementation in `discover_slice.tsx`. It is robust against RLS constraints and does not require complex Postgrest embedded joins across pivot tables.

### 4. Debounced Search & Multi-Criteria Filtering
- **Decision**: In `CatalogViewModel`, search text updates trigger a debounce of 300ms using Coroutines `flow.debounce` before dispatching queries to Postgrest. Search queries apply case-insensitive matching (`FilterOperator.ILIKE` or `textSearch`).
- **Rationale**: Prevents sending HTTP requests on every single keystroke while maintaining snappy, responsive UI feedback.

### 5. UI Cards & Image Loading
- **Decision**: Use `AsyncImage` from `coil-compose` with `crossfade(true)`, rounded clipping, and dark gradient overlays to guarantee readable typography over variable image colors.
- **Rationale**: Matches the visual design of the React Native prototype where text (levels, titles, categories) overlays dark gradient backgrounds.

## Risks / Trade-offs

- **[Risk] Supabase textSearch vs ILIKE**: Supabase `textSearch` requires full-text search indexes on PostgreSQL columns; if missing, queries can throw errors.
  - *Mitigation*: Use `ilike` (`%query%`) with `FilterOperator.ILIKE` for robust substring matching on session names, class names, and instructor names.
- **[Risk] Missing or invalid image URLs**: Supabase thumbnail or cover URLs may be null or point to non-existent assets.
  - *Mitigation*: Provide graceful placeholder backgrounds in `AsyncImage` and fallback color surfaces so cards always render neatly without visual glitching.
- **[Risk] Performance on large lists**: Rendering dozens of image cards simultaneously could cause stutters.
  - *Mitigation*: Use `LazyRow` and `LazyColumn` with stable item keys (`id`), fixed aspect ratios, and Coil memory/disk caching.
