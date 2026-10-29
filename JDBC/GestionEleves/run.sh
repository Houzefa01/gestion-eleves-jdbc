#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

SRC_DIR="$SCRIPT_DIR/src"
LIB_DIR="$SCRIPT_DIR/lib"
BUILD_DIR="$SCRIPT_DIR/build"
RES_DIR="$SCRIPT_DIR/resources"

# --- Java check ---
if ! command -v java &>/dev/null; then
    echo "ERREUR : Java 17+ requis."
    exit 1
fi

JAVA_VER=$(java -version 2>&1 | head -1 | grep -oP '\d+' | head -1)
if [ -z "$JAVA_VER" ] || [ "$JAVA_VER" -lt 17 ]; then
    echo "ERREUR : Java 17+ requis (detecte: $JAVA_VER)."
    exit 1
fi

echo "[OK] Java $(java -version 2>&1 | head -1)"

# --- Check database.properties ---
PROPS_FILE=""
if [ -f "$BUILD_DIR/database.properties" ]; then
    PROPS_FILE="$BUILD_DIR/database.properties"
elif [ -f "$RES_DIR/database.properties" ]; then
    PROPS_FILE="$RES_DIR/database.properties"
fi

if [ -n "$PROPS_FILE" ]; then
    DB_TYPE=$(grep -oP '^db\.type\s*=\s*\K.*' "$PROPS_FILE" 2>/dev/null | tr -d ' ')
    echo "[OK] Base configurée : ${DB_TYPE:-manuel}"
else
    echo "[WARN] database.properties introuvable"
fi

# --- Compile ---
echo "[BUILD] Compilation..."
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

CLASSPATH=$(echo "$LIB_DIR"/*.jar | tr ' ' ':')

javac -encoding UTF-8 -d "$BUILD_DIR" -cp "$CLASSPATH" $(find "$SRC_DIR" -name "*.java" | sort)
echo "        OK"

# --- Copy resources ---
cp "$RES_DIR/database.properties" "$BUILD_DIR/" 2>/dev/null || true
if [ -d "$RES_DIR/icons" ]; then
    cp -r "$RES_DIR/icons" "$BUILD_DIR/"
fi
echo "        Ressources copiées"

# --- Run ---
echo "[RUN] Lancement de GestionEleves..."
echo "       Login : admin / admin"
echo "       Login : user  / user"
echo "----------------------------------------------"
java -cp "$BUILD_DIR:$CLASSPATH" Main
