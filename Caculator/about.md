# Project Structure
Caculator/
├── APK/
│   └── calculator.apk  (Compiled APK for Android devices)
└── Calculator7/
    ├── .idea/                  (IDE configuration files)
    ├── app/
    │   ├── build.gradle.kts     (App-level Gradle configuration)
    │   ├── src/
    │   │   ├── androidTest/
    │   │   │   └── ExampleInstrumentedTest.java
    │   │   ├── main/
    │   │   │   ├── java/com/example/calc/
    │   │   │   │   ├── FirstScreen.java
    │   │   │   │   └── MainActivity.java
    │   │   │   └── res/         (UI and resource files)
    │   │   └── test/
    │   │       └── ExampleUnitTest.java
    ├── build.gradle.kts
    └── settings.gradle.kts

# Calculator Application

This project contains an Android calculator application.

## Structure
- `APK/` - Contains the built APK files for the application.
- `Calculator7/` - Contains the Android Studio project files with source code.

## How to Run
1. Open `Calculator7/` in Android Studio.
2. Build and run the project using an emulator or physical device.
3. The APK can be installed directly from the `APK/` folder.

## Features
- Basic calculator operations (addition, subtraction, multiplication, division).
- User-friendly UI for easy navigation.
