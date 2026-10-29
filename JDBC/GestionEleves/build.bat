@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM ============================================================================
REM build.bat - Génération du JAR exécutable (Windows)
REM ============================================================================

set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src
set LIB_DIR=%PROJECT_DIR%lib
set BUILD_DIR=%PROJECT_DIR%build
set DIST_DIR=%PROJECT_DIR%dist
set TEMP_DIR=%PROJECT_DIR%temp_jar

echo ==============================================
echo   Build - GestionEleves
echo ==============================================

if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
if exist "%TEMP_DIR%" rmdir /s /q "%TEMP_DIR%"
mkdir "%BUILD_DIR%"
mkdir "%DIST_DIR%"
mkdir "%TEMP_DIR%"

set CLASSPATH=
for %%f in ("%LIB_DIR%\*.jar") do (
    if "!CLASSPATH!"=="" (
        set CLASSPATH=%%f
    ) else (
        set CLASSPATH=!CLASSPATH!;%%f
    )
)

echo [1/3] Compilation...
javac -encoding UTF-8 -d "%BUILD_DIR%" -cp "!CLASSPATH!" ^
    "%SRC_DIR%\model\Eleve.java" ^
    "%SRC_DIR%\model\Utilisateur.java" ^
    "%SRC_DIR%\util\ThemeUtil.java" ^
    "%SRC_DIR%\util\IconProvider.java" ^
    "%SRC_DIR%\util\DatabaseConfig.java" ^
    "%SRC_DIR%\util\DBConnection.java" ^
    "%SRC_DIR%\dao\UtilisateurDAO.java" ^
    "%SRC_DIR%\dao\EleveDAO.java" ^
    "%SRC_DIR%\service\AuthService.java" ^
    "%SRC_DIR%\service\EleveService.java" ^
    "%SRC_DIR%\controller\AuthController.java" ^
    "%SRC_DIR%\controller\EleveController.java" ^
    "%SRC_DIR%\view\LoginView.java" ^
    "%SRC_DIR%\view\EleveFormView.java" ^
    "%SRC_DIR%\view\MainView.java" ^
    "%SRC_DIR%\view\UserManagementView.java" ^
    "%SRC_DIR%\Main.java"

if %ERRORLEVEL% NEQ 0 (
    echo ERREUR : Compilation echouee !
    pause
    exit /b 1
)
echo        OK.

echo [2/3] Copie des ressources...
copy "%PROJECT_DIR%resources\database.properties" "%BUILD_DIR%\" >nul
xcopy "%PROJECT_DIR%resources\icons\*" "%BUILD_DIR%\icons\" /E /Q /Y >nul
echo        OK.

echo [3/3] Creation du JAR...

for %%f in ("%LIB_DIR%\*.jar") do (
    cd /d "%TEMP_DIR%"
    jar xf "%%f"
)
cd /d "%PROJECT_DIR%"

xcopy "%BUILD_DIR%\*" "%TEMP_DIR%\" /E /Q /Y >nul

(
echo Manifest-Version: 1.0
echo Main-Class: Main
) > "%TEMP_DIR%\META-INF\MANIFEST.MF"

cd /d "%TEMP_DIR%"
jar cfm "%DIST_DIR%\GestionEleves.jar" META-INF\MANIFEST.MF .
cd /d "%PROJECT_DIR%"

rmdir /s /q "%TEMP_DIR%"

echo        JAR : %DIST_DIR%\GestionEleves.jar
echo ==============================================
echo   Termine
echo   Lancement : java -jar dist\GestionEleves.jar
echo ==============================================
pause
