#!/bin/bash
# build.sh - Genere le JAR executable GestionEleves
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
LIB_DIR="$PROJECT_DIR/lib"
BUILD_DIR="$PROJECT_DIR/build"
DIST_DIR="$PROJECT_DIR/dist"
JAR_NAME="GestionEleves.jar"

echo "=============================================="
echo "  Build - GestionEleves"
echo "=============================================="

rm -rf "$BUILD_DIR" "$DIST_DIR"
mkdir -p "$BUILD_DIR" "$DIST_DIR"

CLASSPATH=""
for jar in "$LIB_DIR"/*.jar; do
    CLASSPATH="$CLASSPATH:$jar"
done

echo "[1/3] Compilation..."
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
echo "       OK."

echo "[2/3] Copie des ressources..."
cp "$PROJECT_DIR/resources/database_mariadb.properties" "$BUILD_DIR/"
cp "$PROJECT_DIR/resources/database_access.properties" "$BUILD_DIR/"
cp "$PROJECT_DIR/resources/gestion_eleves_example.accdb" "$BUILD_DIR/"
cp -r "$PROJECT_DIR/resources/icons" "$BUILD_DIR/"
echo "       OK."

echo "[3/3] Creation du JAR..."

TEMP_DIR="$PROJECT_DIR/temp_jar"
rm -rf "$TEMP_DIR"
mkdir -p "$TEMP_DIR"

for jar in "$LIB_DIR"/*.jar; do
    (cd "$TEMP_DIR" && jar xf "$jar")
done

cp -r "$BUILD_DIR"/* "$TEMP_DIR/" 2>/dev/null || true

mkdir -p "$TEMP_DIR/META-INF"
cat > "$TEMP_DIR/META-INF/MANIFEST.MF" << 'MANIFEST'
Manifest-Version: 1.0
Main-Class: Main
MANIFEST

(cd "$TEMP_DIR" && jar cfm "$DIST_DIR/$JAR_NAME" META-INF/MANIFEST.MF .)

rm -rf "$TEMP_DIR"

echo "       JAR : $DIST_DIR/$JAR_NAME"
echo "=============================================="
echo "  Termine ($(du -h "$DIST_DIR/$JAR_NAME" | cut -f1))"
echo "  Lancement : java -jar dist/$JAR_NAME"
echo "=============================================="
