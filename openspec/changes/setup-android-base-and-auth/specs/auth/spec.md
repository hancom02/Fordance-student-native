# Spec Delta: auth

## Purpose

Provides student user authentication, registration, session persistence across application restarts, and token lifecycle management for the Fordance Android client.

## ADDED Requirements

### Requirement: Student Sign In with Email and Password
The system SHALL authenticate a student user using email and password against the backend and verify that the user's role is student before granting access.

#### Scenario: Successful sign in as student
- **WHEN** user submits valid credentials for an existing student account
- **THEN** system authenticates the user, stores the session tokens, and navigates to the main application interface

#### Scenario: Sign in fails with invalid credentials
- **WHEN** user submits an invalid email or password
- **THEN** system displays an authentication error message and keeps the user on the login screen

#### Scenario: Non-student role account sign in attempt
- **WHEN** user signs in with credentials belonging to an account with a role other than student
- **THEN** system denies access, displays a role restriction error message, and does not establish a student session

### Requirement: Student Sign Up with Role Validation
The system SHALL allow new users to register a student account by providing an email and password, automatically assigning the student role in metadata.

#### Scenario: Successful student registration
- **WHEN** user submits a valid email and non-empty password on the registration screen
- **THEN** system creates a new student user in the backend and signs them in or prompts for confirmation

#### Scenario: Registration with duplicate email
- **WHEN** user submits an email that is already registered
- **THEN** system displays an error indicating the email is already in use

### Requirement: Session Persistence and Auto-Login
The system SHALL persist authentication session tokens in local secure storage and automatically restore the authenticated session upon app launch.

#### Scenario: Valid saved session on app launch
- **WHEN** app is opened and a valid persisted session exists
- **THEN** system restores the session state and navigates directly to the home screen without requiring re-login

#### Scenario: Expired or absent session on app launch
- **WHEN** app is opened and no valid session exists or token refresh fails
- **THEN** system directs the user to the login screen

### Requirement: User Sign Out
The system SHALL allow an authenticated user to sign out, clearing local session tokens and terminating the remote session.

#### Scenario: User initiates sign out
- **WHEN** authenticated user triggers the sign out action
- **THEN** system clears stored session tokens and navigates back to the login screen
