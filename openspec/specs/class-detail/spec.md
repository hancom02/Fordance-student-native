# class-detail Specification

## Purpose

Enables students to explore comprehensive dance class curriculums, review instructor qualifications, inspect the sequenced list of sessions, and navigate into individual session lessons.

## Requirements

### Requirement: Class Curriculum Display
The system SHALL display dance class metadata including cover image, class name, level, genre, instructor info, overview description, and the complete ordered list of sessions.

#### Scenario: Successful class curriculum loading
- **WHEN** student navigates to class detail with a valid class ID
- **THEN** system displays hero cover image, instructor credentials, class summary, and list of sessions with sequence index and title

#### Scenario: Empty or failed class load
- **WHEN** class data fails to load due to network error
- **THEN** system displays an error view with a retry button

### Requirement: Session Item Navigation
The system SHALL allow students to tap any session card in the class curriculum list to launch its respective session detail view.

#### Scenario: Open session from curriculum
- **WHEN** student clicks on a session item within the class curriculum list
- **THEN** system navigates to the session detail screen passing the selected session's ID
