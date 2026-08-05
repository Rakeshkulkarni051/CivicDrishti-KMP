# Civic Drishti — Crowdsourced Civic Issue Reporting and Resolution Platform

Civic Drishti is a Kotlin Multiplatform (KMP) based civic engagement platform designed to bridge the communication gap between citizens and municipal authorities through digital issue reporting, intelligent prioritization, and real-time status tracking.

The project enables citizens to report civic issues such as potholes, damaged streetlights, overflowing garbage bins, water leakages, road obstructions, and other public infrastructure problems using a mobile application. Each report is enriched with geographical location, photographic evidence, descriptive information, and cloud synchronization to facilitate efficient issue management.

On the administrative side, the platform includes a desktop dashboard that provides municipal authorities with a centralized interface for monitoring incoming reports, assigning priorities, tracking resolution progress, and analyzing civic issue trends.

The project is developed using Kotlin Multiplatform to maximize code reuse across Android, iOS (planned), and Desktop platforms while maintaining platform-specific user interfaces where required. Firebase provides cloud infrastructure for real-time synchronization, cloud storage, and scalable document-based data management.

Rather than functioning as a simple issue reporting application, Civic Drishti aims to establish a scalable digital civic participation ecosystem where citizens actively contribute toward improving public infrastructure while authorities receive structured, location-aware, and evidence-backed reports for faster decision making.

---

# Table of Contents

1. [Project Overview](#project-overview)
2. [Problem Statement](#problem-statement)
3. [Project Objectives](#project-objectives)
4. [Technology Stack](#technology-stack)
5. [System Architecture](#system-architecture)
6. [High-Level Application Architecture](#high-level-application-architecture)
7. [Cross Platform Architecture](#cross-platform-architecture)
8. [Folder Structure](#folder-structure)
9. [Module Overview](#module-overview)
10. [MVVM Architecture](#mvvm-architecture)
11. [Repository Pattern](#repository-pattern)
12. [Firebase Cloud Infrastructure](#firebase-cloud-infrastructure)
13. [Firestore Database Design](#firestore-database-design)
14. [Application Workflow](#application-workflow)
15. [Screenshots](#screenshots)
16. [Build Instructions](#build-instructions)
17. [Setup Guide](#setup-guide)
18. [Future Enhancements](#future-enhancements)
19. [Known Limitations](#known-limitations)
20. [License](#license)
21. [Author](#author)
22. [Acknowledgements](#acknowledgements)
23. [Repository](#repository)

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

                  Project Structure

          ┌────────────┼────────────┐
          │            │            │

          ▼            ▼            ▼

   Android App   Desktop Dashboard   iOS
    (Citizen)      (Authorities)   (Scaffold)
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

| Welcome Screen | Aadhaar Verification |
|----------------|----------------------|
| ![Welcome Screen](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/Wellcome%20Screen.png) | ![Aadhaar Verification](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/Verify%20user.png) |

| Home Dashboard | Report Issue |
|----------------|--------------|
| ![Home Dashboard](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/Home%20Screen.png) | ![Report Issue](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/report%20page.png) |

| Report Tracking | Leaderboard |
|-----------------|-------------|
| ![Reported Issues](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/Reported%20Issues.png) | ![Leaderboard](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/Leader%20%20Board.png) |

| Authority Dashboard [Desktop app]|
|----------------|
| ![Dashboard](https://raw.githubusercontent.com/Rakeshkulkarni051/CivicDrishti-KMP/main/Dashboard.png) |

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


# Future Enhancements

The current implementation establishes a scalable foundation for a citizen-centric civic issue reporting platform. The following enhancements are planned as the project evolves toward a production-ready system.

## Artificial Intelligence Integration

- Integrate an on-device TensorFlow Lite model for automatic issue classification from captured images.
- Classify issues such as potholes, garbage accumulation, broken streetlights, water leakage, and damaged roads.
- Automatically estimate issue severity and assign reporting priority.

---

## Push Notifications

Integrate Firebase Cloud Messaging (FCM) to notify users when:

- A report is acknowledged.
- Action has been assigned.
- An issue has been resolved.
- Civic coin rewards have been credited.

---

## Real-Time Report Tracking

Enhance the report tracking module with live status synchronization, allowing users to monitor progress without manually refreshing the application.

---

## Offline Support

Introduce local caching using Room Database to allow users to:

- View previously submitted reports offline.
- Create reports without internet connectivity.
- Automatically synchronize pending reports once connectivity is restored.

---

## Advanced Analytics

Provide visual dashboards displaying:

- Reports submitted per month.
- Civic contribution trends.
- City-wise issue distribution.
- Personal impact statistics.

---

## Gamification

Expand the Civic Coin ecosystem by introducing:

- Additional citizen badges.
- Achievement milestones.
- City and state rankings.
- Reward redemption system.
- Community participation challenges.

---

## Kotlin Multiplatform Expansion

Although the current implementation primarily targets Android, the project structure has been designed using Kotlin Multiplatform to support future deployment on:

- Android
- iOS
- Desktop (JVM)

The existing architecture minimizes platform-specific dependencies, allowing business logic to be progressively migrated into shared modules.

---

## Security Improvements

Future releases will include:

- End-to-end encryption for sensitive user information.
- Stronger session management.
- Role-based access control.
- Secure backend validation.
- Rate limiting and abuse prevention.

---

## Continuous Integration

Planned DevOps improvements include:

- GitHub Actions CI/CD
- Automated testing
- Static code analysis
- APK generation
- Automated releases

---

# Known Limitations

The current implementation represents an academic prototype and therefore includes a few intentional limitations.

- Aadhaar verification is simulated and does not connect to official UIDAI services.
- Government authority portal is planned but not yet implemented.
- AI-based issue classification is currently represented by placeholder logic.
- Push notifications are reserved for future releases.
- Reward redemption system is not yet available.
- iOS and Desktop modules are scaffolded but not feature complete.

These limitations have been intentionally documented as future scope for continued development.

---

# License

This project is licensed under the **MIT License**.

Permission is granted to use, modify, distribute, and extend the source code while preserving the original copyright notice.

See the accompanying `LICENSE` file for complete license information.

---

# Author

**Rakesh Kulkarni**


Android & Kotlin Multiplatform Developer

GitHub:  
https://github.com/Rakeshkulkarni051


---

# Acknowledgements

The development of Civic Drishti was inspired by the growing need for accessible and technology-driven civic participation platforms.

Special thanks to:

- Google for providing Android, Jetpack Compose, Kotlin Multiplatform, Google Maps Platform, and Firebase services.
- Firebase for cloud database, storage, and authentication infrastructure.
- JetBrains for Kotlin and Compose Multiplatform.
- The Android developer community for open-source libraries, documentation, and best practices.
- RV Institute of Technology and Management for supporting this academic major project.

---

# Repository

GitHub Repository:

https://github.com/Rakeshkulkarni051/CivicDrishti-KMP

---

Thank you for taking the time to review this project.

Feedback, suggestions, and contributions are always welcome.
