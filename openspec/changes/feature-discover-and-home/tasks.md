# Tasks

## 1. Domain Models and DTOs

- [x] 1.1 Create DTOs (`SessionDto`, `ClassDto`, `InstructorDto`, `UserSessionJoinDto`, `UserSessionFavDto`) with `@Serializable` annotations for Supabase Postgrest serialization and verify compilation.
- [x] 1.2 Create Domain Models (`Session`, `DanceClass`, `Instructor`, `CatalogFilter`, `DanceLevel`, `DanceGenre`) and bidirectional mappers in `features/discover` and `features/catalog` with unit tests verifying mapping accuracy.

## 2. Data Sources & Repositories

- [x] 2.1 Implement `DiscoverRepository` and remote queries for featured sessions, student-joined sessions (`users_sessions_joined`), class programs (`classes`), and favorite sessions (`users_sessions_favourite`) with unit tests.
- [x] 2.2 Implement `CatalogRepository` and remote queries supporting tabbed searches (sessions, classes, instructors), instructor list, and multi-criteria filters with unit tests.
- [x] 2.3 Configure Hilt DI modules (`DiscoverModule`, `CatalogModule`) providing repository and use case bindings, and verify DI graph builds successfully.

## 3. UI Components & Design System

- [x] 3.1 Implement reusable `SessionCard` and `SessionHorizontalCard` composables with Coil `AsyncImage`, level badge, genre label, and click handler.
- [x] 3.2 Implement `ClassCard` and `ClassWiderCard` composables with cover image, class title, level badge, instructor name, and session count.
- [x] 3.3 Implement `InstructorCard` with circular avatar and name, and `TagChip` / `FilterChip` composables for levels and genres.
- [x] 3.4 Implement `SearchInputBar` and `FilterBottomSheet` supporting multi-selection by level, genre, and instructor with apply and clear actions.

## 4. Discover Screen Implementation

- [x] 4.1 Implement `DiscoverViewModel` and `DiscoverUiState` managing feeds, loading, error, and pull-to-refresh with unit tests.
- [x] 4.2 Implement `DiscoverScreen` composable with header, featured carousel, joined sessions carousel, class programs carousel, and favorite sessions carousel.

## 5. Course Catalog Screen Implementation

- [x] 5.1 Implement `CatalogViewModel` and `CatalogUiState` managing active tab (`SESSIONS`, `CLASSES`, `INSTRUCTORS`), debounced search, and applied filters with unit tests.
- [x] 5.2 Implement `CatalogScreen` composable with category tabs, search input, filter sheet trigger, and dynamic card list results.

## 6. Bottom Navigation & Main Scaffold Integration

- [x] 6.1 Implement `MainScaffold` with Material 3 `NavigationBar` hosting Discover, Catalog, and Profile tabs.
- [x] 6.2 Update `Screen.kt` and `ForDanceNavGraph.kt` to route authenticated users to `MainScaffold` and verify destination navigation.
- [x] 6.3 Run `./gradlew assembleDebug` and `./gradlew test` in `D:\Job\Project\FORDANCE-NATIVE` to verify all components compile and tests pass.
