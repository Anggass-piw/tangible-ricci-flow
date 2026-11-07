# Build and Testing

## Requirements

- JDK 17 or newer.
- Android SDK Platform 35 and Build Tools 35.0.0.
- The project Gradle Wrapper; it downloads Gradle on first use.
- Internet access on the first build to fetch Gradle and JUnit dependencies.

Set `ANDROID_HOME` to the Android SDK location if needed. The app has no AndroidX runtime, web view, or network permission.

## Commands

From the project directory:

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

The first command runs local unit tests for the discrete height model. The second creates the debug APK.

## Verification for this build

- `testDebugUnitTest` and `assembleDebug` completed successfully.
- Both unit tests passed with no failures or errors. They check finite model values, surface evolution, and safe index handling.
- The APK package ID is `id.anggass.ricciflow`, version `1.0.0`, minimum SDK 23, target SDK 35. APK signature verification succeeded.
- No Android device or emulator was connected to the build environment, so direct phone UI testing was not available.

## Artifacts

Gradle output: `app/build/outputs/apk/debug/app-debug.apk`.
Installable copy: `release/Ricci-Surface-Lab-debug.apk`.
SHA-256 checksum: `release/SHA256SUMS.txt`.

This is a **debug build** signed with the local debug key, suitable for private installation/testing. Public distribution requires a release APK/AAB signed with an owner-managed release key.

## Device check

If an Android device is connected and USB debugging is enabled:

```bash
adb install -r release/Ricci-Surface-Lab-debug.apk
adb shell am start -n id.anggass.ricciflow/.MainActivity
```
