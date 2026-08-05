# Civic Drishti — Crowdsourced Civic Issue Reporting and Resolution Platform

Civic Drishti is a Kotlin Multiplatform (KMP) based civic engagement platform designed to bridge the communication gap between citizens and municipal authorities through digital issue reporting, intelligent prioritization, and real-time status tracking.

The project enables citizens to report civic issues such as potholes, damaged streetlights, overflowing garbage bins, water leakages, road obstructions, and other public infrastructure problems using a mobile application. Each report is enriched with geographical location, photographic evidence, descriptive information, and cloud synchronization to facilitate efficient issue management.

On the administrative side, the platform includes a desktop dashboard that provides municipal authorities with a centralized interface for monitoring incoming reports, assigning priorities, tracking resolution progress, and analyzing civic issue trends.

The project is developed using Kotlin Multiplatform to maximize code reuse across Android, iOS (planned), and Desktop platforms while maintaining platform-specific user interfaces where required. Firebase provides cloud infrastructure for real-time synchronization, cloud storage, and scalable document-based data management.

Rather than functioning as a simple issue reporting application, Civic Drishti aims to establish a scalable digital civic participation ecosystem where citizens actively contribute toward improving public infrastructure while authorities receive structured, location-aware, and evidence-backed reports for faster decision making.

---

# Table of Contents

1. Project Overview
2. Problem Statement
3. Project Objectives
4. Technology Stack
5. System Architecture
6. High-Level Application Architecture
7. Cross Platform Architecture
8. Folder Structure
9. Module Overview
10. MVVM Architecture
11. Repository Pattern
12. Firebase Cloud Infrastructure
13. Firestore Database Design
14. Application Workflow
15. Screenshots
16. Setup Instructions
17. Build Targets
18. Future Enhancements
19. License
20. Author

---

# Problem Statement

Urban civic infrastructure requires continuous maintenance to ensure public safety and quality of life. Although municipal corporations provide mechanisms for reporting civic issues, existing systems often suffer from fragmented communication, delayed responses, lack of transparency, and poor citizen engagement.

Several practical challenges were identified during the conceptualization of this project:

- Citizens often do not know the correct department responsible for reporting civic issues.
- Traditional complaint mechanisms rarely provide real-time status tracking.
- Duplicate reports frequently overwhelm authorities without proper prioritization.
- Municipal officers often receive incomplete reports lacking precise location or photographic evidence.
- Existing systems provide limited transparency regarding issue resolution progress.
- There is little incentive for citizens to actively participate in civic improvement initiatives.

These limitations reduce reporting efficiency and increase the average issue resolution time.

Civic Drishti addresses these challenges by combining mobile technologies, cloud computing, geolocation services, and real-time synchronization into a unified digital platform capable of improving communication between citizens and municipal authorities.

---

# Project Objectives

The primary objective of Civic Drishti is to create a scalable, cloud-connected civic issue reporting platform capable of improving both citizen participation and municipal response efficiency.

The project has been designed around the following objectives:

- Develop a modern Android application using Jetpack Compose and Kotlin Multiplatform.
- Enable citizens to report civic issues using photographs captured directly from the device camera.
- Automatically capture geographical coordinates using GPS services.
- Allow manual adjustment of detected locations through Google Maps integration.
- Store issue photographs securely using Firebase Storage.
- Store structured report metadata inside Cloud Firestore.
- Provide real-time synchronization between citizen applications and administrative dashboards.
- Track issue lifecycle from reporting to resolution.
- Maintain authenticated user profiles and reporting history.
- Provide contribution statistics including Civic Coins, Impact Score, and achievement badges.
- Build a reusable MVVM architecture suitable for future platform expansion.
- Prepare the application architecture for future AI-assisted issue classification and intelligent priority scoring.

---

# Technology Stack

| Layer | Technology |
|--------|------------|
| Programming Language | Kotlin |
| Cross Platform Framework | Kotlin Multiplatform (KMP) |
| Android UI Toolkit | Jetpack Compose |
| Desktop UI | Compose Multiplatform |
| Architecture Pattern | MVVM (Model-View-ViewModel) |
| Data Layer | Repository Pattern |
| Cloud Database | Firebase Cloud Firestore |
| Cloud Storage | Firebase Storage |
| Authentication | Firebase Authentication (Project Integration) |
| Maps & Geolocation | Google Maps SDK + Google Play Services Location |
| Camera Integration | CameraX |
| Asynchronous Programming | Kotlin Coroutines |
| State Management | StateFlow / MutableStateFlow |
| Navigation | Navigation Compose |
| IDE | Android Studio |
| Build System | Gradle Kotlin DSL |
| Version Control | Git & GitHub |

---

# System Architecture

The Civic Drishti ecosystem consists of three primary components that work together to facilitate issue reporting and resolution.

```
                  +---------------------------+
                  |       Citizen Mobile      |
                  |      Android Application  |
                  +-------------+-------------+
                                |
                                |
                                |
                                ▼
                     Firebase Cloud Platform
        +-----------------------------------------------+
        |                                               |
        |  Cloud Firestore                              |
        |  Firebase Storage                             |
        |  Authentication                               |
        |  Real-time Synchronization                    |
        +-------------------+---------------------------+
                            |
                            |
                            ▼
               Municipal Authority Dashboard
              (Compose Multiplatform Desktop)
```

The Android application acts as the citizen-facing client responsible for collecting issue reports and uploading structured information to Firebase.

Firebase functions as the cloud infrastructure layer by storing issue metadata, user profiles, media files, and synchronizing updates across connected devices in real time.

The Desktop Dashboard serves as the municipal authority interface where administrators can monitor reports, update issue status, assign priorities, and track overall civic analytics.

---

# High-Level Application Architecture

The project follows a layered architecture that separates user interface, business logic, and data management into independent components.

```
                Presentation Layer
        --------------------------------
        Jetpack Compose Screens
                │
                ▼
            ViewModels
                │
                ▼
        Repository Layer
                │
                ▼
         Firebase Service Layer
                │
                ▼
        Firebase Cloud Platform
     (Firestore + Storage + Auth)
```

Each architectural layer has a clearly defined responsibility.

The presentation layer is responsible only for rendering user interfaces and collecting user interactions.

The ViewModel layer manages UI state, validation logic, and business rules while exposing observable state to Compose screens.

Repositories abstract all database operations, ensuring that the UI never communicates directly with Firebase services.

The Firebase service layer performs low-level cloud operations including document storage, image uploads, authentication, and real-time data synchronization.

This separation improves maintainability, scalability, readability, and testability while reducing coupling between application components.

---

# Cross Platform Architecture

Civic Drishti is built using Kotlin Multiplatform (KMP), enabling code sharing across multiple platforms while preserving native user experiences.

```
                    Kotlin Multiplatform

                   Shared Business Logic
                (Future commonMain Module)
                          │
      ┌───────────────────┼───────────────────┐
      │                   │                   │
      ▼                   ▼                   ▼

 Android App         Desktop Dashboard      iOS App
 Jetpack Compose     Compose Desktop       (Planned)
```

The Android application currently serves as the primary citizen interface.

The Desktop application is intended for municipal authorities to manage and monitor reported civic issues.

The project structure has also been prepared for future iOS support through Kotlin Multiplatform, allowing business logic to be shared while platform-specific implementations remain isolated.

This architecture minimizes code duplication and simplifies long-term maintenance while enabling future expansion to additional platforms.

# Folder Structure

The project follows a modular architecture based on the MVVM (Model–View–ViewModel) design pattern combined with the Repository Pattern. Each layer has a clearly defined responsibility, allowing the application to remain maintainable, scalable, and easy to extend.

```
Civic-Drishti/
│
├── composeApp/
│   ├── src/
│   │
│   ├── commonMain/                    # Shared business logic (Future KMP expansion)
│   ├── commonTest/                    # Shared test cases
│   │
│   ├── androidMain/
│   │
│   │   ├── data/
│   │   │   ├── firebase/
│   │   │   ├── model/
│   │   │   └── repository/
│   │   │
│   │   ├── ui/
│   │   │   ├── navigation/
│   │   │   ├── screens/
│   │   │   ├── theme/
│   │   │   └── viewmodels/
│   │   │
│   │   ├── MainActivity.kt
│   │   └── Platform.android.kt
│   │
│   ├── iosMain/
│   └── jvmMain/
│
├── iosApp/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

The project has been organized to ensure that user interface components, business logic, cloud communication, and data models remain completely independent of one another.

---

# Module Overview

The application consists of multiple logical modules, each responsible for a specific aspect of the system.

| Module | Responsibility |
|----------|---------------|
| data/model | Defines all application data models used throughout the project |
| data/repository | Handles communication between ViewModels and Firebase |
| data/firebase | Performs direct Firebase Firestore and Storage operations |
| ui/screens | Contains all Jetpack Compose user interfaces |
| ui/viewmodels | Manages screen state and business logic |
| ui/navigation | Navigation graph and routing between screens |
| ui/theme | Application colors, typography, and Material theme |
| MainActivity | Android application entry point |
| commonMain | Shared KMP business logic (future expansion) |
| iosMain | iOS platform implementation |
| jvmMain | Desktop Dashboard implementation |

This modular organization ensures that changes within one layer do not unnecessarily affect the rest of the application.

---

# MVVM Architecture

Civic Drishti follows the **Model–View–ViewModel (MVVM)** architectural pattern to achieve complete separation between presentation logic, business logic, and data access.

```
               View
       (Compose Screens)

              │

              ▼

          ViewModel

              │

              ▼

         Repository

              │

              ▼

      Firebase Services

              │

              ▼

      Firestore / Storage
```

Each layer performs exactly one responsibility.

## View

The View consists entirely of Jetpack Compose screens responsible for displaying information and collecting user interactions.

Examples include:

- Welcome Screen
- Aadhaar Authentication
- Home Screen
- Report Issue Screen
- Report Tracking
- Leaderboard
- User Profile

The View layer never communicates directly with Firebase or performs business logic.

Instead, it observes UI state exposed by ViewModels.

---

## ViewModel

ViewModels act as the bridge between the user interface and the Repository layer.

Responsibilities include:

- Input validation
- State management
- Loading indicators
- Error handling
- Business logic
- Invoking Repository functions
- Exposing observable UI state

Each major screen has its own dedicated ViewModel.

```
AuthViewModel

↓

ReportViewModel

↓

LeaderboardViewModel

↓

UserProfileViewModel
```

This separation keeps Compose screens lightweight and focused entirely on rendering UI.

---

## Model

The Model layer represents application entities.

Current models include:

```
UserData.kt

ReportData.kt

LeaderboardData.kt
```

Each model is implemented as a Kotlin Data Class.

Example:

```
data class ReportData(...)
```

The models simply represent structured application data and contain no business logic.

---

# Why MVVM?

The MVVM architecture was selected because it offers several advantages for medium and large-scale Android applications.

### Separation of Concerns

Every architectural layer performs only one responsibility.

UI handles presentation.

ViewModels manage business logic.

Repositories manage cloud communication.

Firebase handles persistent storage.

---

### Maintainability

Since business logic is isolated from UI components, modifying one layer rarely affects the others.

This significantly reduces maintenance complexity as the application grows.

---

### Scalability

Additional features such as authentication providers, AI classification, push notifications, or REST APIs can be introduced without redesigning the entire architecture.

---

### Testability

Business logic inside ViewModels can be tested independently without rendering user interfaces or communicating with Firebase.

---

# Repository Pattern

The Repository Pattern acts as an abstraction layer between business logic and cloud infrastructure.

Instead of allowing ViewModels to directly communicate with Firebase, all cloud operations are delegated to Repository classes.

```
Compose Screen

      │

      ▼

ViewModel

      │

      ▼

Repository

      │

      ▼

Firebase
```

Current repositories include:

```
AuthRepository.kt

ReportRepository.kt

UserRepository.kt

LeaderboardRepository.kt
```

Each repository is responsible for a single domain.

For example,

ReportRepository performs operations such as:

- Upload report
- Retrieve reports
- Update report status
- Fetch report history

while UserRepository manages

- User profile
- Civic coins
- Impact score
- User badges
- Statistics

---

# Why Repository Pattern?

Using a Repository provides multiple architectural benefits.

### Data Source Independence

The UI never knows where the data originates.

Today the repository communicates with Firebase.

Tomorrow it could communicate with

- Django REST APIs
- Spring Boot
- Node.js
- Laravel
- Local Room Database

without requiring changes to the UI.

---

### Cleaner Code

Firebase-specific code remains isolated inside repositories rather than scattered throughout multiple Compose screens.

---

### Easier Backend Migration

If Firebase is replaced with another backend technology, only Repository implementations require modification.

The rest of the application remains unchanged.

---

# Firebase Cloud Infrastructure

Firebase serves as the backend infrastructure for Civic Drishti.

It provides cloud storage, real-time synchronization, user authentication, and media hosting without requiring custom backend server deployment.

The project currently integrates:

| Firebase Service | Purpose |
|-----------------|----------|
| Cloud Firestore | Stores reports, users, leaderboard, and application data |
| Firebase Storage | Stores uploaded issue photographs |
| Firebase Authentication | User identity management |
| Firebase Analytics | Application usage analytics (future integration) |

Firebase was selected because it offers:

- Real-time synchronization
- Automatic scalability
- Cloud hosting
- Minimal backend maintenance
- Native Android SDK support
- Tight integration with Kotlin

---

# Firebase Service Layer

The Firebase service layer acts as the lowest abstraction level within the application.

```
FirebaseService.kt
```

Responsibilities include:

- Firestore initialization
- Storage initialization
- Uploading images
- Reading documents
- Writing documents
- Updating documents
- Querying collections

Repositories never communicate directly with Firebase SDKs.

Instead, they invoke methods exposed by FirebaseService.

This approach isolates cloud-specific implementation details from the rest of the application.

---

# Firestore Database Design

The application stores structured data using Firebase Cloud Firestore.

The database currently consists of the following primary collections.

```
users

reports

leaderboard
```

---

## Users Collection

Stores citizen profile information and contribution statistics.

| Field | Type |
|--------|------|
| userId | String |
| name | String |
| aadhaarHash | String |
| city | String |
| civicCoin | Number |
| impactScore | Number |
| trustScore | Double |
| totalReports | Number |
| badges | Array |
| location | Map |

Example:

```
users

↓

user123

↓

{
  name
  city
  civicCoin
  impactScore
  badges
}
```

---

## Reports Collection

Stores every civic issue submitted through the mobile application.

| Field | Type |
|--------|------|
| reportId | String |
| userId | String |
| reportedBy | String |
| issueType | String |
| detectedIssue | String |
| description | String |
| imageUrl | String |
| latitude | Double |
| longitude | Double |
| location | String |
| priority | Integer |
| status | String |
| createdAt | Timestamp |
| acknowledgedAt | Timestamp |
| actionAssignedAt | Timestamp |
| resolvedAt | Timestamp |
| civicCoinReward | Integer |

Each report progresses through the following lifecycle.

```
REPORTED

↓

ACKNOWLEDGED

↓

ACTION_ASSIGNED

↓

RESOLVED

↓

COIN_REWARDED
```

---

## Leaderboard Collection

Stores contribution rankings for active citizens.

Typical fields include:

- User Name
- Civic Coins
- Impact Score
- Total Reports
- City Rank
- Badge

The Leaderboard module retrieves this information to display the highest contributing citizens and encourage civic participation through gamification.

---

# Why Firestore?

Firestore was selected over a traditional SQL database because the project requires:

- Real-time synchronization
- Cloud-hosted infrastructure
- Flexible document storage
- Automatic scalability
- Offline caching support
- Tight integration with Firebase Storage
- Seamless Android SDK support

The document-oriented model also aligns naturally with the application's data structure, where reports, user profiles, and leaderboard entries can be represented as independent collections while remaining easy to query and synchronize.


# Application Workflow

The Civic Drishti application follows a citizen-centric workflow designed to simplify civic issue reporting while ensuring authenticated participation.

The application currently supports Aadhaar-based user registration, location-aware issue reporting, Firebase cloud synchronization, report tracking, and a citizen leaderboard.

The following diagram illustrates the overall application flow.

```
Application Launch
        │
        ▼
 Splash Screen
        │
        ▼
───────────────────────────────
Check User Session (30 Days)
───────────────────────────────
        │
 ┌──────┴────────┐
 │               │
 ▼               ▼
Session Exists   No Session
 │               │
 ▼               ▼
Home Screen   Aadhaar Verification
 │               │
 │        Validate User
 │               │
 │      Existing User?
 │
 ┌──────┴────────┐
 │               │
 ▼               ▼
Fetch User     Create User
 │               │
 └──────┬────────┘
        ▼
     Home Screen
        │
        ▼
Report Civic Issue
        │
        ▼
Capture Image
        │
        ▼
Detect GPS Location
        │
        ▼
Adjust Location on Map
        │
        ▼
Describe Issue
        │
        ▼
Upload Image
        │
        ▼
Save Report to Firestore
        │
        ▼
Report Success
        │
        ▼
Track Report Progress
        │
        ▼
Leaderboard & User Dashboard
```

The workflow has been intentionally designed to minimize user interaction while collecting sufficient information for civic authorities to process reported issues efficiently.

---

# Feature Workflow

## Splash Screen

The application launches with a branded splash screen.

During this stage the application:

- Initializes Firebase
- Checks locally stored login session
- Determines navigation path

If a valid session exists (30-day validity), the user is taken directly to the Home Screen.

Otherwise, the application redirects the user to Aadhaar Verification.

---

## Aadhaar Authentication

The authentication screen collects:

- Citizen Name
- Aadhaar Number

The Aadhaar number is never stored directly.

Instead, it is converted into a SHA-256 hash before being saved to Firestore.

The authentication flow performs the following checks:

1. Validate input.
2. Generate Aadhaar hash.
3. Search Firestore for an existing user.
4. If user exists, fetch profile.
5. Otherwise create a new user document.
6. Store user session locally.
7. Navigate to Home Screen.

---

## Home Dashboard

The Home Screen acts as the central hub of the application.

From here users can:

- Report a civic issue
- Track submitted reports
- View leaderboard rankings
- Access their profile
- Monitor civic contribution statistics

---

## Report Issue Workflow

The reporting process consists of multiple stages to ensure accurate issue reporting.

### Step 1 — Capture Image

The application requests Camera permission.

Users capture an image directly using the device camera.

Gallery uploads are intentionally disabled to encourage real-time issue reporting.

Captured images are temporarily stored locally before upload.

---

### Step 2 — Detect Location

The application requests GPS permission.

Using Google's Fused Location Provider, the application automatically retrieves:

- Latitude
- Longitude
- Human-readable address

---

### Step 3 — Map Adjustment

After automatic location detection, an interactive Google Map is displayed.

Users can manually adjust the marker to improve location accuracy before submitting the report.

---

### Step 4 — Describe Issue

The user provides a textual description of the reported issue.

This description is intended to support future AI-based issue classification and prioritization.

---

### Step 5 — Upload Report

When the Report button is pressed, the application performs the following sequence.

```
Capture Image

↓

Upload Image to Firebase Storage

↓

Receive Image URL

↓

Generate Report Object

↓

Store Document in Firestore

↓

Navigate to Success Screen
```

---

## Track Reports

The Reports module retrieves every report submitted by the currently authenticated user.

Each report displays:

- Image
- Issue Type
- Description
- Location
- Submission Date
- Current Status

The current report lifecycle is:

```
REPORTED

↓

ACKNOWLEDGED

↓

ACTION_ASSIGNED

↓

RESOLVED

↓

COIN_REWARDED
```

Future versions will include live status updates and push notifications.

---

## User Dashboard

The Profile screen displays the user's civic contribution.

Information includes:

- Aadhaar Verification Status
- Civic Coins
- Impact Score
- Total Reports
- Earned Badge
- Rank within the City

All values are retrieved directly from Firestore and updated whenever the user profile changes.

---

## Leaderboard

The Leaderboard promotes community participation through gamification.

Users are ranked based on:

- Civic Impact Score
- Civic Coins
- Total Reports
- Issue Resolution Contributions

Future versions will support city-wise and state-wise rankings.

---

# Screenshots

The following screenshots demonstrate the current user interface of Civic Drishti.

| Screen | Description |
|----------|-------------|
| Welcome Screen | Citizen entry point and application introduction |
| Aadhaar Verification | Identity verification using Aadhaar details |
| Home Dashboard | Central dashboard displaying reporting options and civic statistics |
| Report Issue | Capture image, detect location, describe issue, and submit report |
| Reported Issues | Displays previously submitted reports with tracking status |
| Leaderboard | Gamified citizen ranking based on civic contribution |
| User Dashboard | Displays profile information, badges, impact score, and civic coins |

## Application Screens

| Welcome | Aadhaar Verification |
|-----------|----------------------|
| ![](screenshots/Wellcome%20Screen.png) | ![](screenshots/Verify%20user.png) |

| Home Dashboard | Report Issue |
|----------------|--------------|
| ![](screenshots/Home%20Screen.png) | ![](screenshots/report%20page.png) |

| Report Tracking | Leaderboard |
|-----------------|-------------|
| ![](screenshots/Reported%20Issues.png) | ![](screenshots/Leader%20Board.png) |

| User Dashboard |
|----------------|
| ![](screenshots/Dashboard.png) |

> Create a folder named `screenshots` in the repository root and place all screenshots inside it using the same filenames shown above.

---

# Build Instructions

## Prerequisites

Before building the application, ensure the following software is installed.

- Android Studio Hedgehog or newer
- JDK 17 (or compatible version configured for the project)
- Android SDK
- Git
- Kotlin Multiplatform Plugin
- Google Play Services (for Maps)

---

## Clone Repository

```bash
git clone https://github.com/Rakeshkulkarni051/CivicDrishti-KMP.git

cd CivicDrishti-KMP
```

---

## Open Project

Open the project using Android Studio.

Allow Gradle Sync to complete before running the application.

---

## Firebase Configuration

Create a Firebase Project and enable:

- Cloud Firestore
- Firebase Storage
- Firebase Authentication

Download the Firebase configuration file.

Place:

```
google-services.json
```

inside

```
composeApp/src/androidMain/
```

---

## Google Maps Configuration

Create a Google Cloud Platform project.

Enable:

- Maps SDK for Android
- Geocoding API

Generate an Android API Key.

Add the key inside:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY"/>
```

located in

```
composeApp/src/androidMain/AndroidManifest.xml
```

---

## Sync Gradle

Once Firebase and Maps are configured, synchronize Gradle.

Android Studio automatically downloads all required dependencies.

---

## Run Application

Select an Android device or emulator.

Click **Run**.

or execute:

```bash
./gradlew installDebug
```

---

## Build Release APK

```bash
./gradlew assembleRelease
```

The generated APK will be located inside:

```
composeApp/build/outputs/apk/release/
```

---

# Setup Guide

To run the project successfully, complete the following steps.

### 1. Clone Repository

```bash
git clone https://github.com/Rakeshkulkarni051/CivicDrishti-KMP.git
```

---

### 2. Open Android Studio

Open the cloned project.

Allow Gradle to synchronize completely.

---

### 3. Configure Firebase

- Create Firebase Project
- Enable Firestore
- Enable Storage
- Enable Authentication
- Download `google-services.json`
- Copy it into:

```
composeApp/src/androidMain/
```

---

### 4. Configure Google Maps

- Create Google Cloud API Key
- Enable Maps SDK for Android
- Enable Geocoding API
- Add API key to AndroidManifest

---

### 5. Build the Project

```bash
./gradlew build
```

---

### 6. Run on Device

Grant the following permissions when prompted:

- Camera
- Fine Location
- Network Access

The application is now ready for use.

---

The project has been structured to separate presentation, business logic, and cloud communication using MVVM and Repository Pattern principles, making it suitable for future expansion to additional Kotlin Multiplatform targets including Desktop and iOS.
