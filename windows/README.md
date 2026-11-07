# Ricci Surface Lab — Windows x64

A native Windows desktop companion to the Android app. It presents the same single discrete educational surface visualization inspired by the **Tangible Ricci Flow Surface** concept. Both interfaces and their documentation are in English.

## Features

- Native C# Windows Forms application; no browser, web view, or network requests.
- 10 × 10 height-field mesh with 100 sample points.
- Step slider from 0 to 60, **Start simulation / Pause**, and **Reset** controls.
- Self-contained Windows x64 executable; no separate .NET runtime installation is required.
- Portable run option and optional per-user Start Menu installation scripts.

## Model scope

This is **not a complete Ricci-flow solver**. The simulation applies a simple discrete neighbor-averaging rule to a sampled height field. It does not calculate curvature tensors, solve a geometric PDE, represent manifold topology, simulate physical hardware, or control 100 motors. See [model and limitations](docs/model-and-limitations.md).

## Run or install

For a portable run, double-click `release/RicciSurfaceLab-win-x64.exe`.

For an optional per-user Start Menu entry, keep `Install-RicciSurfaceLab.ps1` beside the executable and run the script from PowerShell. It copies the app into the current user's LocalAppData Programs folder and creates a shortcut; administrator rights are not intended. `Uninstall-RicciSurfaceLab.ps1` removes that app folder and shortcut.

The executable is unsigned. Windows SmartScreen may show a warning for an unfamiliar download. The Windows build and installer scripts were cross-built/reviewed on Linux; actual installation and runtime testing on Windows were not available in this environment.

## Build and test

Requirements: .NET 10 SDK and an internet connection for the initial Windows-targeted restore.

```bash
dotnet run --project tests/ModelSmokeTest/ModelSmokeTest.csproj -c Release
dotnet publish src/RicciSurfaceLab.csproj -c Release -r win-x64 --self-contained true -o publish
```

The cross-build succeeded, and the portable model smoke test passed. The output is `release/RicciSurfaceLab-win-x64.exe`. See [build and testing](docs/build-and-testing.md) for exact verification limits.

## License

Source is provided under the MIT License. See `LICENSE`.
