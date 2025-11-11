
[![CI](https://github.com/WALAAHAJJ/catalogue-service/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/WALAAHAJJ/catalogue-service/actions/workflows/ci.yml)
# GoodFood – Catalogue Service
Spring Boot + Postgres. Endpoints: `/restaurants`, `/restaurants/{id}/menu`, CRUD catégories & items.

## Run (Docker)
docker build -t goodfood/catalogue:0.1 .
docker run --rm -p 8082:8081 --network goodfood-db_default \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-catalogue:5432/catalogue_db \
-e SPRING_DATASOURCE_USERNAME=postgres \
-e SPRING_DATASOURCE_PASSWORD=postgres \
goodfood/catalogue:0.1
