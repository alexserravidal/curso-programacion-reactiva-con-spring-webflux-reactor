
## 1. Introducción

Este proyecto consta de dos microservicios:
### 1.1. Students
- Contiene información sobre los estudiantes y una referencia a sus respectivas escuelas.
- Actúa como **cliente gRPC**.
- Para ejecutarlo, hay que escribir el siguiente comando en su carpeta raíz:
``mvn clean install spring-boot:run``
- Una vez levantado, se puede acceder a su Swagger en http://localhost:8080/swagger-ui/index.html
### 1.2. Schools
- Contiene información sobre las escuelas.
- Actúa como **servidor gRPC**.
- Para ejecutarlo, hay que escribir el siguiente comando en su carpeta raíz:
``mvn clean install spring-boot:run``
- Una vez levantado, se puede acceder a su Swagger en http://localhost:8081/swagger-ui/index.html