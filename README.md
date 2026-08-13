# Cadence

Cadence is a lightweight, privacy-first digital wellbeing application for Windows. It runs quietly in the background to track your application usage, keystrokes, and focus sessions, helping you build healthier screen-time habits without sacrificing your data to the cloud.
<br><br>

## Dashboard Screen
![Cadence Dashboard](desktopApp\src\main\resources\images\ss1.png)
<br><br>

## Activity Screen
![Cadence Dashboard](desktopApp\src\main\resources\images\ss2.png)
<br><br>

## Apps Screen
![Cadence Dashboard](desktopApp\src\main\resources\images\ss3.png)
<br><br>

## Dashboard with line chart and donut chart (coming soon...)
![Cadence Dashboard](desktopApp\src\main\resources\images\ss4.png)
<br><br>

## 🌟 Core Principles

- **Native Windows Feel**: Seamlessly integrates into your workflow.
- **Minimal UI**: Clean, modern, and distraction-free interface powered by Material 3.
- **Local-First & Privacy-Focused**: Your data never leaves your machine. No cloud sync, no forced accounts, no tracking telemetry.
- **Resource Efficient**: Designed to track your activity continuously with virtually zero impact on your CPU or RAM.

## ✨ Features

- **Activity Tracking**: Automatically detects your active window and logs continuous focus sessions.
- **Typing Metrics**: Counts your keys typed live throughout the day to measure productivity intensity.
- **Smart Break Reminders**: Alerts you when you've been working too long (default 45 mins), with 15-minute snooze support.
- **Visual Dashboards**: View your daily timeline, focus score, top apps, and session breakdowns through beautiful charts.
- **Background Mode**: Cadence lives in your system tray and continues tracking even when the main dashboard is closed.
- **Daily Partitioning**: All tracking data is efficiently saved into daily local files for long-term scalability.

## 🛠 Tech Stack

- **Language**: Kotlin
- **Framework**: Compose Multiplatform (Desktop)
- **Design System**: Material 3 (with custom sleek components)
<!-- - **System Integration**: JNA (Win32 APIs) for global keystroke tracking and window state detection -->
<!-- - **Storage**: JSON-based local storage (Room + SQLite planned for future releases) -->

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher
- Windows OS (Required for Win32 API hooks)

### Building the Project

Cadence uses the Compose Multiplatform Gradle plugin. 

To run the application locally during development:
```powershell
.\gradlew.bat :desktopApp:run
```

To build a standalone, minified `.msi` Windows Installer (No Java required for the end user):
```powershell
.\gradlew.bat :desktopApp:packageReleaseMsi
```

To build a standalone `.exe`:
```powershell
.\gradlew.bat :desktopApp:packageReleaseExe
```
*Note: Release builds use ProGuard for minification to ensure the smallest possible application footprint.*

<!-- ## 📂 Architecture Overview

- `ui/` - Compose UI screens (Dashboard, Activity, Apps, Settings) and theme definitions.
- `tracking/` - Core background services that run independently of the UI.
  - `AppTracker.kt` - Monitors active foreground windows.
  - `KeyboardTracker.kt` - JNA-powered global keyboard hook.
  - `SystemTracker.kt` - Detects OS sleep, lock, and idle states.
  - `SessionManager.kt` - Aggregates data into continuous focus blocks.
  - `StorageManager.kt` - Handles daily data persistence.
- `viewmodel/` & `data/` - For UI state management and future database integrations. -->

<!-- ## 🤝 Contributing

This is a local-first wellbeing project. Feel free to fork, customize, and build your own modules. Ensure any new libraries introduced adhere to the core principles of remaining lightweight and privacy-respecting. -->

---
<center><em>Take control of your time, locally.</em></center>