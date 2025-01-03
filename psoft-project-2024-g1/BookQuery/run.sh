#!/bin/bash

# Verificar se o argumento é um número inteiro válido
if [[ $1 =~ ^-?[0-9]+$ ]]; then
  if (( $1 < 1 )); then
    ./shutdown.sh
    exit
  fi
else
  echo "Error: Argument is not a valid number"
  exit
fi

# Base de dados e configurações
db_container_name="bookquery_postgres_in_lms_network"
db_base_name="booksquery_"
db_base_port=5422

# Verifica se o contêiner já existe
if ! docker ps --filter "name=^${db_container_name}$" --format "{{.Names}}" | grep -q "^${db_container_name}$"; then
  echo "Starting PostgreSQL container: ${db_container_name}"
  docker run -d \
    --name "${db_container_name}" \
    --network lms_network2 \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=password \
    -p "${db_base_port}:5432" \
    postgres:latest
else
  echo "PostgreSQL container ${db_container_name} already running."
fi

# Configurar os bancos de dados
existing_dbs=$(docker exec -it ${db_container_name} psql -U postgres -tAc "SELECT datname FROM pg_database WHERE datname LIKE '${db_base_name}%'")
latest_i=$(echo "$existing_dbs" | grep -oE '[0-9]+$' | sort -n | tail -n 1)
latest_i=${latest_i:-0} # Define 0 se nenhum banco de dados existir

# Remover bancos de dados extras
if (( latest_i > $1 )); then
  for ((i = $1+1; i <= latest_i; i++)); do
    db_name="${db_base_name}${i}"
    docker exec -it "${db_container_name}" psql -U postgres -c "DROP DATABASE IF EXISTS ${db_name};"
    echo "Dropped database: ${db_name}"
  done
fi

# Criar bancos de dados adicionais
if (( latest_i < $1 )); then
  for ((i = latest_i+1; i <= $1; i++)); do
    db_name="${db_base_name}${i}"
    docker exec -it "${db_container_name}" psql -U postgres -c "CREATE DATABASE ${db_name};"
    echo "Created database: ${db_name}"
  done
fi

echo "Configured $1 instances of databases (books_1 to books_$1) in container ${db_container_name}."


if docker service ls --filter "name=bookquery" --format "{{.Name}}" | grep -q "^bookquery"; then
  docker service scale bookquery_bookquery=$1
else
  docker service create -d \
    --name lmsbooks \
    --env SPRING_PROFILES_ACTIVE=bootstrap,relational,IDService1 \
    --env spring.datasource.url=jdbc:postgresql://booksquery_db_{{.Task.Slot}}:5432/postgres \
    --env spring.datasource.username=postgres \
    --env spring.datasource.password=password \
    --env file.upload-dir=/tmp/uploads-psoft-g1-instance{{.Task.Slot}} \
    --env spring.rabbitmq.host=rabbitmq \
    --mount type=volume,source=uploaded_files_volume_{{.Task.Slot}},target=/tmp \
    --publish 8085:8080 \
    --network lms_network2 \
    lmsbooks:latest

  docker service scale bookquery_bookquery=$1
fi