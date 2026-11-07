# Ricci Surface Lab — Android and Windows

Two native app editions of one discrete educational simulation inspired by the Tangible Ricci Flow Surface concept. Both app interfaces and the documentation are in English.

## Downloads

- **Windows:** [RicciSurfaceLab-win-x64.exe](https://github.com/Anggass-piw/tangible-ricci-flow/releases/download/v1.0.0/RicciSurfaceLab-win-x64.exe) — self-contained x64 GUI executable. Optional per-user Start Menu [install/uninstall scripts](windows/scripts/) are included in the source; place the downloaded executable beside the scripts before use.
- **Android:** build the debug APK from the [Android source](android/README.md), minimum Android 6.0 (API 23). No Android binary is published in this release.

## Shared simulation

Both apps render a 10 × 10 height-field grid with 100 sampled points. The user can inspect 0–60 precomputed frames, play/pause the smoothing animation, and reset to the initial ring-like relief. The discrete model blends each point's current height with the average of its available four-direction neighbors:

\[
h_{t+1}(i,j)=0.65h_t(i,j)+0.35\,\operatorname{avg}(N_4(i,j)).
\]

This is a **discrete educational visualization**, not a complete Ricci-flow solver, curvature computation, manifold-topology simulation, or physical 100-motor controller.

## Verification and limits

- Android Gradle build completed; both model unit tests passed; APK signature and package metadata were checked.
- Windows x64 self-contained publish completed on Ubuntu Linux; the model smoke test passed; the output was identified as a PE32+ x86-64 Windows GUI executable.
- No Android device/emulator or Windows computer was available. Neither app was installed and launched on its target operating system in this environment. The Windows PowerShell installation scripts and UI still need a Windows 10/11 x64 runtime check.
- The Windows executable is unsigned, so SmartScreen may warn. The Android APK is a debug-signed build, suitable for private testing rather than public release.

## Source and review

The [Android](android/) and [Windows](windows/) source directories include English user/build documentation and an MIT License. Build caches and release binaries are excluded from the source tree; the Windows download is distributed through GitHub Releases.
