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
