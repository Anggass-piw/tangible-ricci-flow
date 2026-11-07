# Build and Testing

## Requirements

- .NET 10 SDK.
- Internet access for the first restore of Windows desktop targeting packs and runtime assets.
- A Windows 10/11 x64 computer to run or install the final application.

## Build and model smoke test

Run from the Windows project directory:

```bash
dotnet run --project tests/ModelSmokeTest/ModelSmokeTest.csproj -c Release
dotnet publish src/RicciSurfaceLab.csproj -c Release -r win-x64 --self-contained true -o publish
```

The portable model smoke test is a normal `net10.0` console program and can run on Linux or Windows. The desktop app targets `net10.0-windows`, `win-x64`, self-contained single-file publishing.

## Verification for this build

- The model smoke test passed: all 100 grid points were finite, the surface changed after averaging, and index clamping worked.
- Windows x64 publish completed successfully on Ubuntu 24.04 using .NET SDK 10.0.403.
- The output was identified as a Windows PE32+ GUI executable for x86-64. The self-contained app is approximately 111 MB.
- No Windows computer or Wine runtime was available. The `.exe`, PowerShell installation/uninstallation scripts, Start Menu shortcut, and UI have **not** been installed or launched on Windows. A Windows 10/11 x64 runtime check remains necessary before public distribution.

## Expected artifact

The publish output is `publish/RicciSurfaceLab.exe`. The deliverable copy is `release/RicciSurfaceLab-win-x64.exe`.

The optional PowerShell scripts install/uninstall the app for the current user only. They create/remove the app folder under `%LOCALAPPDATA%\Programs\RicciSurfaceLab` and a Start Menu shortcut; no administrator rights are intended. SHA-256 checksum is in `release/SHA256SUMS.txt`.

The executable is not digitally signed. Windows SmartScreen may warn when opening an unfamiliar download.
