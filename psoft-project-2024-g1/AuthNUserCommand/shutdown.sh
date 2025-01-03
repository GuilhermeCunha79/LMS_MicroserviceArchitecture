#!/bin/bash

# Listar e escalar todos os serviços que correspondem ao padrão users_users_X
docker service ls --format "{{.Name}}" | grep -E "^users_users_[0-9]+$" | while read service; do
  docker service scale ${service}=0
  docker service rm ${service}
  echo "Stopped and removed ${service}"
done


# Configurações do banco de dados
db_base_name="test_users_"
db_base_port=55000

# Obter o índice mais alto dos containers de banco de dados
latest_i=$(docker ps --filter "name=^${db_base_name}[0-9]+$" --format "{{.Names}}" | sort -V | tail -n 1 | grep -oE '[0-9]+$')

# Parar e remover todos os containers do banco de dados
if [[ -n "$latest_i" ]]; then
  for ((i = 1; i <= latest_i; i++)); do
    db_name="${db_base_name}${i}"
    db_port=$((db_base_port + i))

    docker stop ${db_name} > /dev/null 2>&1
    docker rm ${db_name} > /dev/null 2>&1

    echo "Stopped and removed ${db_name} on port ${db_port}"
  done
else
  echo "No database containers found to remove."
fi
