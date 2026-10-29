@echo off
setlocal enabledelayedexpansion

REM run.bat - Lancement de GestionEleves (Windows)
REM Usage : run.bat             -> build + lance
REM         run.bat --build     -> build seulement
REM         run.bat --run       -> lance le JAR existant

set PROJECT_DIR=%~dp0
set JAR_FILE=%PROJECT_DIR%dist\GestionEleves.jar

echo ==============================================
echo   GestionEleves
echo ==============================================

where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR : Java 17+ requis.
    pause
    exit /b 1
)
echo [OK] Java

if "%1"=="--build" goto do_build
if "%1"=="--run"   goto do_run

:do_build
echo [BUILD] Compilation et creation du JAR...
call "%PROJECT_DIR%build.bat"
if %ERRORLEVEL% NEQ 0 exit /b 1
if "%1"=="--build" goto end

:do_run
if not exist "%JAR_FILE%" (
    echo JAR introuvable, build automatique...
    call "%PROJECT_DIR%build.bat"
    if %ERRORLEVEL% NEQ 0 exit /b 1
)
echo [RUN] Lancement...
java -jar "%JAR_FILE%"

:end
pause
