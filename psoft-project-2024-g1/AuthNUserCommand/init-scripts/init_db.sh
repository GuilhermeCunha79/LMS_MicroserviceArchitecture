#!/bin/bash
DB_NAME="${POSTGRES_DB:-test_users_$(taskslot)}"  # Expansão correta de hostname

# Exibir o nome do banco de dados que será criado
echo "Tentando criar o banco de dados: $DB_NAME"

# Verificar se o banco de dados já existe
DB_EXISTS=$(psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'")

if [ "$DB_EXISTS" != "1" ]; then
  echo "Criando o banco de dados: $DB_NAME"
  psql -U postgres -c "CREATE DATABASE \"$DB_NAME\";"
else
  echo "O banco de dados $DB_NAME já existe."
fi
