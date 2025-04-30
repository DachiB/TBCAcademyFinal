# AR Home Designer

AR Home Designer is an Android application built with Jetpack Compose and SceneView that enables users to place 3D furniture models into real-world spaces using Augmented Reality (AR). The app follows Clean Architecture principles and integrates Firebase for authentication, storage, and persistence.

## Features

- 🔍 Search and filter furniture by name, price range, category, or AR model availability
- 🪑 Preview AR models of furniture in real space using SceneView
- ⭐ Save products to a personal collection stored locally
- 📸 Take AR scene screenshots and upload to Firebase Storage
- 🌓 Switch between light/dark theme
- 🌐 Support for multiple languages (English & Georgian)

## Tech Stack

- **UI:** Jetpack Compose, Material 3
- **AR:** SceneView + Google ARCore
- **Architecture:** MVI + Clean Architecture
- **Persistence:** Room, DataStore
- **Backend:** Firebase Auth, Firestore, Firebase Storage
- **Testing:** JUnit, Mockito, Coroutine Test
- 
## Getting Started

1. Clone the repository
2. Connect your Firebase project and add the `google-services.json` file
3. Enable Firebase Authentication, Firestore, and Storage
4. Build and run the project using Android Studio (API 24+)
