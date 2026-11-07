$ErrorActionPreference = 'Stop'

$source = Join-Path $PSScriptRoot 'RicciSurfaceLab-win-x64.exe'
if (-not (Test-Path -LiteralPath $source -PathType Leaf)) {
    throw "Executable not found beside this script: $source"
}

$installDir = Join-Path $env:LOCALAPPDATA 'Programs\RicciSurfaceLab'
New-Item -ItemType Directory -Path $installDir -Force | Out-Null
$installedExe = Join-Path $installDir 'RicciSurfaceLab.exe'
Copy-Item -LiteralPath $source -Destination $installedExe -Force

$startMenuPrograms = Join-Path ([Environment]::GetFolderPath('StartMenu')) 'Programs'
New-Item -ItemType Directory -Path $startMenuPrograms -Force | Out-Null
$shortcutPath = Join-Path $startMenuPrograms 'Ricci Surface Lab.lnk'
$wsh = New-Object -ComObject WScript.Shell
$shortcut = $wsh.CreateShortcut($shortcutPath)
$shortcut.TargetPath = $installedExe
$shortcut.WorkingDirectory = $installDir
$shortcut.Description = 'Discrete educational surface visualization'
$shortcut.Save()

Write-Host "Installed Ricci Surface Lab for the current user: $installedExe"
Write-Host "Start Menu shortcut: $shortcutPath"
