# bKash Clone - Jetpack Compose

A high-fidelity clone of the bKash Android app, built entirely using **Jetpack Compose**. This project demonstrates modern Android development practices, including custom UI components, state management, and seamless navigation with local data persistence.

![Banner](https://img.shields.io/badge/Platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue)

## 📱 Features

- **Full Registration Flow:** Multi-step registration including Phone, Name, Profile Picture (with Gallery picker), and PIN setup.
- **Smart Navigation:** Detects if a user is already registered using `SharedPreferences` and skips registration on next launch.
- **Multi-Language Support:** Seamless toggle between **English** and **Bangla** across all screens.
- **Realistic Home Screen:**
  - Interactive "Tap for Balance" animation.
  - Expandable/Collapsible Services Grid (2 rows default).
  - Auto-sliding Promotional Banners with touch support.
  - Categorized services (Quick Features & Other Services).
- **Custom Loading Overlay:** Transparent dimming effect with a custom bKash Bird GIF during screen transitions.
- **Custom UI Components:** Reusable bKash-styled Number Pad, Top Bars, and Bottom Navigation.

## 🛠️ Tech Stack

- **UI:** Jetpack Compose (Material 3)
- **Navigation:** Compose Navigation Component
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) (including GIF support)
- **Persistence:** SharedPreferences (via `PrefManager`)
- **Animation:** Compose Animation API (InfiniteTransitions, animateFloat)
- **Concurrency:** Kotlin Coroutines

## 📸 Screenshots

| Splash Screen | Login | Registration | Home Screen |
|:---:|:---:|:---:|:---:|
| ![Splash](https://github.com/yourusername/bkash-clone-compose/raw/main/screenshots/splash.png) | ![Login](https://github.com/yourusername/bkash-clone-compose/raw/main/screenshots/login.png) | ![Registration](https://github.com/yourusername/bkash-clone-compose/raw/main/screenshots/registration.png) | ![Home](https://github.com/yourusername/bkash-clone-compose/raw/main/screenshots/home.png) |

> 📁 Place your screenshots in the `/screenshots` folder at the root of the repository and replace `yourusername` with your actual GitHub username.

## 🚀 Getting Started

1. **Clone the repository:**
```bash
   git clone https://github.com/kazol196295/BkashClone.git
```

2. **Open in Android Studio:** Ensure you have the latest version of Android Studio (Ladybug or newer).
3. **Sync Project:** Wait for Gradle to download the necessary dependencies (especially Coil for GIFs).
4. **Run the App:** Connect an emulator or a physical device.

## 📂 Project Structure

| File | Description |
|---|---|
| `MainActivity.kt` | Contains the `NavHost`, global state management, and the `LoadingOverlay`. |
| `HomeScreen.kt` | All components related to the dashboard (Header, Pager, Service Grids). |
| `Components.kt` | Reusable UI elements like `BkashNumberPad` and `BkashTopBar`. |
| `PrefManager.kt` | Logic for storing user registration and PIN locally. |

## 📝 Prerequisites

- Min SDK: **24**
- Target SDK: **34**
- Android Studio **Ladybug** or later

## 📄 License

This project is for educational purposes only. bKash is a registered trademark of bKash Limited.
