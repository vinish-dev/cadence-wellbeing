# Cadence

Cadence is a lightweight Windows digital wellbeing application built using:

- Kotlin
- Compose Desktop
- Material 3
- JNA for Win32 APIs
- SQLite + Room (later)

Principles:

- Native Windows feel
- Minimal UI
- Local-first
- Privacy focused
- No cloud
- No accounts

Architecture:

ui/
viewmodel/
tracking/
data/
repository/
service/

Do not introduce unnecessary libraries.

Prefer Kotlin Coroutines and StateFlow.

Follow Material 3.