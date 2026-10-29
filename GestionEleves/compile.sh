#!/bin/bash
# compile.sh - Compilation GestionEleves
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
LIB_DIR="$PROJECT_DIR/lib"
BUILD_DIR="$PROJECT_DIR/build"

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

CLASSPATH=""
for jar in "$LIB_DIR"/*.jar; do
    CLASSPATH="$CLASSPATH:$jar"
done

echo "Compilation..."
javac -encoding UTF-8 -d "$BUILD_DIR" -cp "$CLASSPATH" \
    "$SRC_DIR"/model/Eleve.java \
    "$SRC_DIR"/model/Utilisateur.java \
    "$SRC_DIR"/util/ThemeUtil.java \
    "$SRC_DIR"/util/IconProvider.java \
    "$SRC_DIR"/util/DatabaseConfig.java \
    "$SRC_DIR"/util/DBConnection.java \
    "$SRC_DIR"/dao/UtilisateurDAO.java \
    "$SRC_DIR"/dao/EleveDAO.java \
    "$SRC_DIR"/service/AuthService.java \
    "$SRC_DIR"/service/EleveService.java \
    "$SRC_DIR"/controller/AuthController.java \
    "$SRC_DIR"/controller/EleveController.java \
    "$SRC_DIR"/view/LoginView.java \
    "$SRC_DIR"/view/EleveFormView.java \
    "$SRC_DIR"/view/MainView.java \
    "$SRC_DIR"/view/UserManagementView.java \
    "$SRC_DIR"/Main.java

cp "$PROJECT_DIR/resources/database_mariadb.properties" "$BUILD_DIR/"
cp "$PROJECT_DIR/resources/database_access.properties" "$BUILD_DIR/"
cp "$PROJECT_DIR/resources/gestion_eleves_example.accdb" "$BUILD_DIR/"
cp -r "$PROJECT_DIR/resources/icons" "$BUILD_DIR/"

echo "Termine. Classes dans $BUILD_DIR/"
