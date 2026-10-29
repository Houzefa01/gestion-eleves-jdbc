#!/bin/bash
# Script de switching rapide entre les bases de données
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROPS="$SCRIPT_DIR/resources/database.properties"

case "${1:-}" in
    access)
        echo "db.type=access"         >  "$PROPS"
        echo "db.path=./gestion_eleves.accdb" >> "$PROPS"
        echo "db.username="           >> "$PROPS"
        echo "db.password="           >> "$PROPS"
        echo "[OK] Base : MS Access"
        ;;
    mariadb)
        echo "db.type=mariadb"        >  "$PROPS"
        echo "db.host=${2:-localhost}" >> "$PROPS"
        echo "db.port=${3:-3306}"     >> "$PROPS"
        echo "db.name=${4:-gestion_eleves}" >> "$PROPS"
        echo "db.username=${5:-root}" >> "$PROPS"
        echo "db.password=${6:-}"     >> "$PROPS"
        echo "[OK] Base : MariaDB sur ${2:-localhost}:${3:-3306}/${4:-gestion_eleves}"
        ;;
    mysql)
        echo "db.type=mysql"          >  "$PROPS"
        echo "db.host=${2:-localhost}" >> "$PROPS"
        echo "db.port=${3:-3306}"     >> "$PROPS"
        echo "db.name=${4:-gestion_eleves}" >> "$PROPS"
        echo "db.username=${5:-root}" >> "$PROPS"
        echo "db.password=${6:-}"     >> "$PROPS"
        echo "[OK] Base : MySQL sur ${2:-localhost}:${3:-3306}/${4:-gestion_eleves}"
        ;;
    postgresql|postgres)
        echo "db.type=postgresql"     >  "$PROPS"
        echo "db.host=${2:-localhost}" >> "$PROPS"
        echo "db.port=${3:-5432}"     >> "$PROPS"
        echo "db.name=${4:-gestion_eleves}" >> "$PROPS"
        echo "db.username=${5:-postgres}" >> "$PROPS"
        echo "db.password=${6:-}"     >> "$PROPS"
        echo "[OK] Base : PostgreSQL sur ${2:-localhost}:${3:-5432}/${4:-gestion_eleves}"
        ;;
    "")
        echo "Usage: $0 <type> [host] [port] [dbname] [user] [pass]"
        echo "  types : access | mariadb | mysql | postgresql"
        echo ""
        echo "Exemples:"
        echo "  $0 access"
        echo "  $0 mariadb localhost 3306 gestion_eleves root secret"
        exit 1
        ;;
    *)
        echo "Type inconnu : $1"
        echo "Types supportés : access, mariadb, mysql, postgresql"
        exit 1
        ;;
esac
