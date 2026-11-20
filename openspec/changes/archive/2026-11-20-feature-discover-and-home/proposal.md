# Proposal: Discover Feed and Course Catalog

## Why

Following the completion of the base architecture and authentication in Change 1, students need core content exploration interfaces to browse, search, and discover dance content. Migrating the Discover feed and Course Catalog (Home) screens from React Native to native Android Jetpack Compose delivers high-performance list and carousel rendering, fluid tab switching, reactive search and multi-criteria filtering, and seamless navigation into detail views.

## What Changes

- **Top-Level Bottom Navigation**: Implement a bottom navigation bar (`Discover`, `Catalog/Home`, `Profile/Settings`) replacing the temporary single-screen `HomePlaceholderScreen` with structured bottom tab navigation and scaffold.
- **Discover Feed Screen**:
  - Featured / Highlighted Sessions carousel with thumbnail, level, and genre badges.
  - "Joined Sessions" horizontal carousel fetching sessions joined by the logged-in student (`users_sessions_joined`) with "See All" navigation.
  - "Classes" horizontal carousel displaying dance class programs (`classes`) with "See All" navigation.
  - "Favourite Sessions" horizontal carousel displaying sessions bookmarked by the student (`users_sessions_favourite`) with "See All" navigation.
  - Pull-to-refresh and reactive loading/error/empty states.
- **Course Catalog & Search Screen (Home)**:
  - Three-tab switcher: `SESSIONS`, `CLASSES`, `INSTRUCTORS`.
  - Search bar supporting debounced text query matching session names, class names, or instructor names.
  - Filter bottom sheet supporting multi-selection by Dance Level (`beginner`, `intermediate`, `advanced`), Dance Genre (e.g., hiphop, ballet, contemporary, etc.), and Instructor.
  - Responsive result lists: Session cards (thumbnail, name, level, genre, instructor, price), Class cards (cover image, name, level, genre, instructor, session count), and Instructor items (avatar, name).
  - Empty search results state and error retry handling.
- **Clean Architecture & Supabase Integration**:
  - DTOs and mappers for Supabase tables: `sessions`, `classes`, `users`, `users_sessions_favourite`, and `users_sessions_joined`.
  - Repositories and Use Cases for Discover feed aggregation and Catalog search/filter.
  - ViewModels (`DiscoverViewModel`, `CatalogViewModel`) exposing immutable state flows (`DiscoverUiState`, `CatalogUiState`).
- **Image Loading**: Integrate Coil 3 Compose for cached, asynchronous image loading across all cards and list items.

## Capabilities

### New Capabilities
- `discover`: Student discovery home feed displaying featured sessions, student's joined sessions, class programs, and saved favorite sessions with quick navigation.
- `course-catalog`: Interactive catalog with tabbed browsing (Sessions, Classes, Instructors), text search, and multi-criteria filtering by level, genre, and instructor.

### Modified Capabilities
- *(None - existing auth capability requirements remain unchanged)*

## Impact
- **Navigation**: Replaces `HomePlaceholderScreen` in `ForDanceNavGraph.kt` with a `MainScaffold` containing the bottom bar and tab destinations (`Discover`, `Catalog`, `Profile`). Adds placeholder routes for detail screens (`SessionDetail`, `ClassDetail`, `InstructorProfile`).
- **Dependencies**: Adds Coil Compose dependency (`io.coil-kt.coil3:coil-compose` or `io.coil-kt:coil-compose`) in `libs.versions.toml` and `app/build.gradle.kts`.
- **Database**: Queries existing Supabase tables (`sessions`, `classes`, `users`, `users_sessions_favourite`, `users_sessions_joined`) without modifying table structures.
