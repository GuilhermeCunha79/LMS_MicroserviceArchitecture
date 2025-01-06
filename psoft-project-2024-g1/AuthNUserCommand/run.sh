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
db_container_name="postgres_in_lms_network_user"
db_base_name="test_users_"
db_base_port=5446

# Verifica se o contêiner já existe
latest_i=$(docker ps --filter "name=^${db_base_name}[1-9][0-9]*$" --format "{{.Names}}" | sort -V | tail -n 1 | grep -oE '[0-9]+$')
if((latest_i > $1)); then
  for ((i = $1+1; i <= latest_i; i++)); do
    db_name="$db_base_name${i}"
    db_port=$(($db_base_port + i))

    docker stop ${db_name}
    docker rm ${db_name}

    echo "Stopped ${db_name} on port ${db_port}"
  done

else
  if ((latest_i < $1)); then
    for ((i = 1; i <= $1; i++)); do
      db_name="$db_base_name${i}"
      db_port=$((db_base_port + i))

      docker run -d \
        --name "${db_name}" \
        --network lms_network2 \
        -e POSTGRES_USER=postgres \
        -e POSTGRES_PASSWORD=password \
        -p "${db_port}:5432" \
        postgres

      echo "Started ${db_name} on port ${db_port}"
    done
  fi
fi

echo "Configured $1 instances of databases (users_1 to users_$1) in container ${db_container_name}."

# Configurar serviços para cada instância do banco de dados
for ((i = 1; i <= $1; i++)); do
  service_name="users_users_${i}"
  db_name="${db_base_name}${i}"
  db_port=$((db_base_port + i))

  if docker service ls --filter "name=${service_name}" --format "{{.Name}}" | grep -q "^${service_name}$"; then
    docker service scale ${service_name}=$1
  else
    docker service create \
      --name ${service_name} \
      --env SPRING_PROFILES_ACTIVE=test,relational,firebase \
      --env spring.datasource.url=jdbc:postgresql://${db_name}:5432/postgres \
      --env spring.datasource.username=postgres \
      --env spring.datasource.password=password \
      --env file.upload-dir=/tmp/uploads-psoft-g1-instance{{.Task.Slot}} \
      --env spring.rabbitmq.host=rabbitmq \
      --mount type=volume,source=uploaded_files_volume_${i},target=/tmp \
      --publish $((8093 + i)):8080 \
      --network lms_network2 \
      lmsusers:latest
  fi
done
