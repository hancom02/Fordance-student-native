# Spec Delta

## Purpose

Enables dance students to search, filter, and browse sessions, classes, and instructors with multi-criteria filtering across levels, genres, and teachers.

## ADDED Requirements

### Requirement: Tabbed Catalog Navigation
The system SHALL provide three distinct catalog tabs: SESSIONS, CLASSES, and INSTRUCTORS to categorize browsable content.

#### Scenario: Switching tabs
- **WHEN** the student selects a catalog tab (e.g., CLASSES)
- **THEN** the system switches the active view to display items corresponding to that category

### Requirement: Search Query Input
The system SHALL support keyword search filtering by session name on SESSIONS, class name on CLASSES, and instructor name on INSTRUCTORS.

#### Scenario: Search query entered
- **WHEN** the student enters text into the catalog search bar
- **THEN** the system filters items in the active tab matching the entered text query

### Requirement: Multi-Criteria Filter Sheet
The system SHALL provide a modal filter bottom sheet allowing multi-selection of dance levels, genres, and instructors to filter sessions and classes.

#### Scenario: Applying level and genre filters
- **WHEN** the student selects specific levels and genres in the filter sheet and applies them
- **THEN** the catalog list updates to only show sessions or classes that match the selected filter criteria

#### Scenario: Resetting filters
- **WHEN** the student clears all applied filter criteria
- **THEN** the catalog list displays all available items without filter constraints

### Requirement: Empty and Error Catalog States
The system SHALL display an informative empty state when no items match search or filter criteria, and an error state with retry on network failure.

#### Scenario: No matching results
- **WHEN** a search or filter yields zero results
- **THEN** the system displays a clear message informing the student that no matching items were found

#### Scenario: Network error during catalog fetch
- **WHEN** a network error occurs while fetching catalog items
- **THEN** the system displays an error banner with a retry button
