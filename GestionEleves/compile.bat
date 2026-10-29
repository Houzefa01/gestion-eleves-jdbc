@echo off
setlocal enabledelayedexpansion

REM ============================================================================
REM compile.bat - Compilation du projet GestionEleves (Windows)
REM ============================================================================

set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src
set LIB_DIR=%PROJECT_DIR%lib
set BUILD_DIR=%PROJECT_DIR%build

if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
mkdir "%BUILD_DIR%"

echo Compilation...

set CLASSPATH=
for %%f in ("%LIB_DIR%\*.jar") do (
    if "!CLASSPATH!"=="" (
        set CLASSPATH=%%f
    ) else (
        set CLASSPATH=!CLASSPATH!;%%f
    )
)

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

copy "%PROJECT_DIR%resources\database_mariadb.properties" "%BUILD_DIR%\" >nul
copy "%PROJECT_DIR%resources\database_access.properties" "%BUILD_DIR%\" >nul
copy "%PROJECT_DIR%resources\gestion_eleves_example.accdb" "%BUILD_DIR%\" >nul
xcopy "%PROJECT_DIR%resources\icons\*" "%BUILD_DIR%\icons\" /E /Q /Y >nul

echo Termine. Classes dans %BUILD_DIR%\
pause
