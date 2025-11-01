set -e

echo "--- Iniciando restauração dos dumps para múltiplos BDs ---"

# Variáveis padrão (ajuste conforme o seu docker-compose)
DB_USER="$POSTGRES_USER"

# Função para restaurar um dump
restore_dump() {
    DB_NAME=$1
    DUMP_FILE=$2

    echo "Restaurando $DUMP_FILE no banco de dados $DB_NAME..."

    # Restaura o dump (assumindo formato SQL simples, por isso o psql)
    # Se o dump for formatado (--Fc), use 'pg_restore -U $DB_USER -d $DB_NAME $DUMP_FILE'
    psql -v ON_ERROR_STOP=1 --username "$DB_USER" --dbname "$DB_NAME" < "$DUMP_FILE"

    echo "Restauração de $DB_NAME concluída."
}

# 🚨 1. Restauração do MS-AUTHUSER
restore_dump authuser_capcred_db /docker-entrypoint-initdb.d/3_authuser_dump.sql

# 🚨 2. Restauração do MS-LOAN
restore_dump loan_capcred_db /docker-entrypoint-initdb.d/4_loan_dump.sql

# 🚨 3. Restauração do MS-PAYMENT
restore_dump payment_capcred_db /docker-entrypoint-initdb.d/5_payment_dump.sql

echo "--- Todas as restaurações de dumps concluídas ---"