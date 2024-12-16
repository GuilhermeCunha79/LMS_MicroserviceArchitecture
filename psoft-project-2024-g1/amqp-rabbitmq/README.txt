
> cd /path/to/lmsbooks

Dockerfile
> mvn package
> docker build -t lmsbooks .

DockerfileWithPackaging (inclui mvn package)
> docker build -f DockerfileWithPackaging -t lmsbooks .

Running:
> docker compose -f docker-compose-rabbitmq+postgres.yml up -d

> docker compose up