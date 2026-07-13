# SmartCatalog

SmartCatalog is a **Kotlin Multiplatform (KMP)** application that shares business logic and UI across Android and iOS using **Compose Multiplatform**. The project demonstrates a scalable, modern architecture with shared code, platform-specific integrations, and a clean separation of concerns.

---

# Business Requirements

The application provides a simple catalog browsing experience with a focus on code sharing between Android and iOS.

## Features

- Browse product catalog
- Search products
- View product details
- Shared business logic across platforms
- Modern UI using Compose Multiplatform
- Platform-specific integrations when required
- Scalable architecture for future enhancements

---

# Project Architecture

The project follows **Kotlin Multiplatform (KMP)** with **Compose Multiplatform**.

```
SmartCatalog/
│
├── androidApp/
│   └── Android application entry point
│
├── iosApp/
│   └── iOS application entry point (SwiftUI/Xcode)
│
├── shared/
│   ├── src/
│   │
│   ├── commonMain/
│   │   ├── Shared UI
│   │   ├── Business Logic
│   │   ├── Domain Models
│   │   ├── Repository
│   │   └── Use Cases
│   │
│   ├── androidMain/
│   │   └── Android-specific implementations
│   │
│   ├── iosMain/
│   │   └── iOS-specific implementations
│   │
│   └── commonTest/
│
└── build.gradle.kts
```

## Module Overview

### `androidApp`
Contains the Android application entry point and Android-specific configuration.

### `iosApp`
Contains the iOS application entry point. Even when using Compose Multiplatform, this module is required for launching the application on iOS and for integrating SwiftUI or native iOS components.

### `shared`
Contains the shared codebase used by both Android and iOS.

#### `commonMain`
Shared business logic, Compose UI, domain models, repositories, and use cases.

#### `androidMain`
Android-specific implementations such as platform APIs and integrations.

#### `iosMain`
iOS-specific implementations for Apple frameworks and native APIs.

---

# Technology Stack

- Kotlin Multiplatform
- Compose Multiplatform
- Kotlin
- Kotlin Coroutines
- Flow
- Material 3
- Gradle Kotlin DSL

---

# Build & Run

## Prerequisites

- JDK 17+
- Android Studio (latest stable with KMP support)
- Xcode 15 or later (for iOS)
- CocoaPods (if required)
- Kotlin Multiplatform plugin

---

## Clone Repository

```bash
git clone https://github.com/karttiktungenwar/SmartCatalog.git

cd SmartCatalog
```

---

## Build the Project

```bash
./gradlew build
```

---

## Run Android App

Build the debug APK:

```bash
./gradlew :androidApp:assembleDebug
```

Or run directly from Android Studio using the provided run configuration.

---

## Run iOS App

Open the iOS project in Xcode:

```bash
open iosApp
```

Then:

- Select a simulator or connected iPhone.
- Click **Run (▶)** in Xcode.

---

# Trade-offs & Assumptions

## Trade-offs

- Compose Multiplatform is used to maximize code sharing while allowing native integrations where necessary.
- Platform-specific functionality is isolated in `androidMain` and `iosMain` to keep shared code clean.
- The project prioritizes maintainability and scalability over minimizing boilerplate.

## Assumptions

- Android and iOS are the primary target platforms.
- Shared business logic resides in the `shared` module.
- Platform-specific APIs are implemented only when required.
- Development uses the latest stable versions of Kotlin and Compose Multiplatform.

---

# Future Improvements

- Offline caching
- Pagination
- Product filtering
- Dependency Injection (Koin/Hilt)
- Unit and UI testing
- CI/CD pipeline
- Dark mode support
- Localization
- Performance optimization

---

# Author

**Karttik Tungenwar**

GitHub: https://github.com/karttiktungenwar
**Karttik Tungenwar**

GitHub: https://github.com/karttiktungenwar
