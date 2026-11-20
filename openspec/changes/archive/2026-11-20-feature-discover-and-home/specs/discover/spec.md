# Spec Delta

## Purpose

Provides the core personalized discovery feed for dance students to explore featured sessions, review joined classes and sessions, browse class programs, and access bookmarked favorite sessions.

## ADDED Requirements

### Requirement: Featured Sessions Carousel
The system SHALL display a horizontal carousel of featured dance sessions with valid active status, showing thumbnail, session name, level, and genre.

#### Scenario: Render featured sessions
- **GIVEN** active sessions exist in the system
- **WHEN** the student opens the Discover feed
- **THEN** the system displays the top active sessions in a horizontal carousel

### Requirement: Joined Sessions Section
The system SHALL display sessions that the authenticated student has previously joined, ordered by recent activity, with a "See All" button.

#### Scenario: Student with joined sessions
- **GIVEN** the student is logged in and has joined dance sessions
- **WHEN** the Discover feed is loaded
- **THEN** the system displays a horizontal carousel of joined sessions with thumbnail, session title, and level badge

#### Scenario: Student with no joined sessions
- **GIVEN** the student has not joined any sessions
- **WHEN** the Discover feed is loaded
- **THEN** the system displays a placeholder message indicating no sessions have been joined yet

### Requirement: Classes Program Section
The system SHALL display available dance class programs with cover image, class name, level, genre, and instructor name, along with a "See All" button.

#### Scenario: Render class programs
- **WHEN** the student views the Classes section in the Discover feed
- **THEN** the system displays a horizontal list of dance classes with cover image, level tag, class name, and instructor username

### Requirement: Favourite Sessions Section
The system SHALL display sessions that the authenticated student has marked as favorites, allowing quick navigation to them.

#### Scenario: View favorite sessions
- **GIVEN** the student is logged in and has favorited sessions
- **WHEN** the Discover feed is loaded
- **THEN** the system displays a horizontal list of favorite session cards

### Requirement: Pull to Refresh
The system SHALL allow students to pull down to refresh all Discover feed content from the remote database.

#### Scenario: Student triggers refresh
- **WHEN** the student performs a pull-to-refresh gesture on the Discover feed
- **THEN** the system re-fetches featured sessions, joined sessions, class programs, and favorite sessions, updating the UI accordingly
