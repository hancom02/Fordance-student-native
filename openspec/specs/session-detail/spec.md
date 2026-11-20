# session-detail Specification

## Purpose

Enables students to view session learning content, stream instructional dance videos via an integrated player, review teacher credentials, and manage enrollment and favorites.

## Requirements

### Requirement: Session Detail Loading and Display
The system SHALL retrieve and display complete session details including title, thumbnail, duration, level, genre, instructor profile, and pedagogical guidance (what you will learn, what to prepare).

#### Scenario: Successful detail fetch
- **WHEN** student navigates to a session detail screen with a valid session ID
- **THEN** system displays session header, level and genre tags, instructor avatar and name, learning objectives, and enrollment status

#### Scenario: Session not found or network failure
- **WHEN** session ID is invalid or network request fails
- **THEN** system displays an error message with a retry action button

### Requirement: Video Playback with ExoPlayer
The system SHALL stream session demonstration video using AndroidX Media3 ExoPlayer with player controls, pause on backgrounding, and fallback thumbnail.

#### Scenario: Media playback startup
- **WHEN** session detail screen loads with a valid video URL
- **THEN** player initializes, buffers media, and provides playback controls (play, pause, seek)

#### Scenario: Application goes to background
- **WHEN** user moves app to background or navigates away while video is playing
- **THEN** player automatically pauses playback and releases resources appropriately on disposal

### Requirement: Join and Unjoin Session
The system SHALL allow authenticated students to enroll in or withdraw from a session with optimistic UI updates.

#### Scenario: Join session successfully
- **WHEN** student clicks "Join Session"
- **THEN** system sends enrollment request to REST API and updates button state to "Joined"

#### Scenario: Unjoin session
- **WHEN** student clicks "Joined" to withdraw
- **THEN** system sends cancellation request to REST API and reverts button state to "Join Session"

### Requirement: Favorite Session Bookmark Toggle
The system SHALL allow students to bookmark or un-bookmark a session as favorite with instant UI feedback.

#### Scenario: Toggle favorite bookmark
- **WHEN** student taps the bookmark icon
- **THEN** system updates bookmark icon highlight state and synchronizes preference with backend REST API
