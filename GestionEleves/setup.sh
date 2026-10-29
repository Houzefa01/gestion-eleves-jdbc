#!/bin/bash
# setup.sh - Installation complete GestionEleves (Linux)
# Cree la base, les tables, les utilisateurs et les eleves.
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SQL_FILE="$PROJECT_DIR/script.sql"
PROPS_FILE="$PROJECT_DIR/resources/database.properties"

RED='\033[0;31m'; GRN='\033[0;32m'; YLW='\033[1;33m'; BLU='\033[0;34m'; NC='\033[0m'

echo "=============================================="
echo -e "  ${BLU}GestionEleves - Installation${NC}"
echo "=============================================="
echo ""
echo "Type de base de donnees :"
echo "  1) MariaDB"
echo "  2) MySQL"
echo "  3) PostgreSQL"
echo "  4) MS Access (fichier local)"
echo ""
read -p "Choix [1-4] : " DB_CHOICE

case "$DB_CHOICE" in
1) DB_TYPE="mariadb";  CLI="mariadb"; PORT_DEF="3306" ;;
2) DB_TYPE="mysql";    CLI="mysql";   PORT_DEF="3306" ;;
3) DB_TYPE="postgresql";CLI="psql";    PORT_DEF="5432" ;;
4) DB_TYPE="access" ;;
*) echo -e "${RED}Choix invalide.${NC}"; exit 1 ;;
esac

if [ "$DB_TYPE" = "access" ]; then
    echo ""
    echo -e "${YLW}MS Access ne necessite pas de script SQL.${NC}"
    echo "L'application cree automatiquement le fichier .accdb au premier lancement."
    echo ""
    echo "Configuration :"
    echo "  1. cp resources/database_access.properties.back resources/database.properties"
    echo "  2. bash build.sh"
    echo "  3. java -jar dist/GestionEleves.jar"
    echo ""
    exit 0
fi

echo ""
read -p "Hote       [localhost] : " DB_HOST;   DB_HOST=${DB_HOST:-localhost}
read -p "Port       [$PORT_DEF]  : " DB_PORT;   DB_PORT=${DB_PORT:-$PORT_DEF}
read -p "Base       [gestion_eleves] : " DB_NAME; DB_NAME=${DB_NAME:-gestion_eleves}
read -p "Utilisateur[root]     : " DB_USER;   DB_USER=${DB_USER:-root}
read -sp "Mot de passe          : " DB_PASS;   echo ""

echo ""
echo -e "${BLU}Execution du script SQL...${NC}"

if [ "$DB_TYPE" = "postgresql" ]; then
    PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE $DB_NAME;" 2>/dev/null || true
    PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SQL_FILE" 2>&1
else
    mariadb -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" < "$SQL_FILE" 2>&1
fi

echo -e "${GRN}Base creee avec succes.${NC}"

echo ""
echo -e "${BLU}Ecriture de database.properties...${NC}"

cat > "$PROPS_FILE" << EOF
db.type=$DB_TYPE
db.host=$DB_HOST
db.port=$DB_PORT
db.name=$DB_NAME
db.username=$DB_USER
db.password=$DB_PASS
EOF

echo -e "${GRN}database.properties ecrit.${NC}"

echo ""
echo -e "${BLU}Build du JAR...${NC}"
bash "$PROJECT_DIR/build.sh"

echo ""
echo "=============================================="
echo -e "  ${GRN}Installation terminee !${NC}"
echo ""
echo "  Lancement : java -jar dist/GestionEleves.jar"
echo "  Login     : admin / admin"
echo "=============================================="
