# Ricci Surface Lab — Android

A native Android app for exploring one discrete educational surface visualization inspired by the **Tangible Ricci Flow Surface** concept. A 10 × 10 grid displays 100 height samples that gradually smooth over 60 steps.

## Features

- Conceptual 3D view of a 10 × 10 height-field grid.
- Step slider, **Start simulation / Pause**, and **Reset** controls.
- English interface, launcher icon, and local operation with no network permission.
- Unit tests for the discrete surface model.

## Model scope

This app is **not a complete Ricci-flow solver**. It does not calculate curvature tensors or solve a geometric PDE. Instead, each grid point moves toward the local average of its available neighbors. This simple height-field rule only illustrates how a sampled surface can smooth over time.

The app does **not** control a physical surface, 100 motors, shift registers, or other hardware. Its screen visualization is not a physical simulation or formal mathematical analysis.

## Build the APK

Requirements: JDK 17+, Android SDK Platform 35, Build Tools 35.0.0, and an internet connection for the initial Gradle dependency download.

```bash
./gradlew testDebugUnitTest assembleDebug
```

The installable debug APK is `release/Ricci-Surface-Lab-debug.apk`; its SHA-256 is listed in `release/SHA256SUMS.txt`.

## Install on Android

Transfer the APK to an Android device and open it to start the system installer. If Android asks, allow installation from the file source you used. This is a **debug build** signed with the local debug key, suitable for private testing. Public distribution requires a release build signed with an owner-managed release key.

Minimum Android version: 6.0 (API 23). The current sandbox had no connected Android device or emulator, so on-device UI testing was not available.

## Source layout

- `app/src/main/java/.../MainActivity.java` — native UI and animation controls.
- `app/src/main/java/.../SurfaceModel.java` — 10 × 10 grid and discrete averaging.
- `app/src/main/java/.../SurfaceMeshView.java` — conceptual 3D mesh renderer.
- `app/src/test/.../SurfaceModelTest.java` — local unit tests.
- `docs/` — user guide, model limitations, and build/test notes.

See [user guide](docs/panduan-pengguna.md), [model and limitations](docs/model-dan-batasan.md), and [build and testing](docs/build-dan-pengujian.md). The companion [Windows x64 edition](../tangible-ricci-flow-windows/README.md) is documented separately.

## License

Source is provided under the MIT License. See `LICENSE`.
