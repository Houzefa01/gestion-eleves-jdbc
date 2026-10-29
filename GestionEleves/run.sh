#!/bin/bash
# run.sh - Lancement GestionEleves
# Usage : ./run.sh             -> build + lance
#         ./run.sh --build     -> build seulement
#         ./run.sh --run       -> lance le JAR existant

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAR_FILE="$PROJECT_DIR/dist/GestionEleves.jar"

echo "=============================================="
echo "  GestionEleves"
echo "=============================================="

if ! command -v java &>/dev/null; then
    echo "ERREUR : Java 17+ requis."
    exit 1
fi
echo "[OK] Java"

if [ "$1" = "--build" ] || [ "$1" = "" ]; then
    echo "[BUILD] Compilation et creation du JAR..."
    bash "$PROJECT_DIR/build.sh" || exit 1
    [ "$1" = "--build" ] && exit 0
fi

if [ ! -f "$JAR_FILE" ]; then
    echo "JAR introuvable, build automatique..."
    bash "$PROJECT_DIR/build.sh" || exit 1
fi

echo "[RUN] Lancement..."
java -jar "$JAR_FILE"
