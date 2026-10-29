@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM ============================================================================
REM setup.bat - Installation complete de GestionEleves (Windows)
REM ============================================================================

set PROJECT_DIR=%~dp0
set SQL_FILE=%PROJECT_DIR%script.sql
set PROPS_FILE=%PROJECT_DIR%resources\database.properties

echo ==============================================
echo   GestionEleves - Installation
echo ==============================================
echo.
echo Type de base de donnees :
echo   1) MariaDB
echo   2) MySQL
echo   3) PostgreSQL
echo   4) MS Access (fichier local)
echo.
set /p DB_CHOICE="Choix [1-4] : "

if "%DB_CHOICE%"=="1" (set DB_TYPE=mariadb& set CLI=mariadb& set PORT_DEF=3306)
if "%DB_CHOICE%"=="2" (set DB_TYPE=mysql&   set CLI=mysql&   set PORT_DEF=3306)
if "%DB_CHOICE%"=="3" (set DB_TYPE=postgresql& set CLI=psql&  set PORT_DEF=5432)
if "%DB_CHOICE%"=="4" (set DB_TYPE=access)

if "%DB_TYPE%"=="" (
    echo Choix invalide.
    pause
    exit /b 1
)

if "%DB_TYPE%"=="access" (
    echo.
    echo MS Access ne necessite pas de script SQL.
    echo L'application cree automatiquement le fichier .accdb au premier lancement.
    echo.
    echo Configuration :
    echo   1. copy resources\database_access.properties.back resources\database.properties
    echo   2. build.bat
    echo   3. java -jar dist\GestionEleves.jar
    echo.
    pause
    exit /b 0
)

echo.
set /p DB_HOST="Hote       [localhost] : " || set DB_HOST=localhost
set /p DB_PORT="Port       [%PORT_DEF%]  : " || set DB_PORT=%PORT_DEF%
set /p DB_NAME="Base       [gestion_eleves] : " || set DB_NAME=gestion_eleves
set /p DB_USER="Utilisateur[root]     : " || set DB_USER=root
set /p DB_PASS="Mot de passe          : "

if "%DB_HOST%"=="" set DB_HOST=localhost
if "%DB_PORT%"=="" set DB_PORT=%PORT_DEF%
if "%DB_NAME%"=="" set DB_NAME=gestion_eleves
if "%DB_USER%"=="" set DB_USER=root

echo.
echo Execution du script SQL...
if "%DB_TYPE%"=="postgresql" (
    set PGPASSWORD=%DB_PASS%
    psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d postgres -c "CREATE DATABASE %DB_NAME%;" 2>nul
    psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -f "%SQL_FILE%"
) else (
    %CLI% -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% < "%SQL_FILE%"
)

if %ERRORLEVEL% NEQ 0 (
    echo ERREUR : Echec de l'execution SQL.
    pause
    exit /b 1
)
echo Base creee avec succes.

echo.
echo Ecriture de database.properties...
(
echo db.type=%DB_TYPE%
echo db.host=%DB_HOST%
echo db.port=%DB_PORT%
echo db.name=%DB_NAME%
echo db.username=%DB_USER%
echo db.password=%DB_PASS%
) > "%PROPS_FILE%"

echo database.properties ecrit.

echo.
echo Build du JAR...
call "%PROJECT_DIR%build.bat"

echo.
echo ==============================================
echo   Installation terminee !
echo.
echo   Lancement : java -jar dist\GestionEleves.jar
echo   Login     : admin / admin
echo ==============================================
pause
