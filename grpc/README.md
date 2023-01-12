## 1. Definición
Este proyecto consta de una interfaz gRPC y dos microservicios, que se ejecutan uno como servidor gRPC y el otro como cliente gRPC.
### 1.1 gRPC Schools Interface
- Contiene el archivo .proto y el plugin de generación de las clases Java a partir de tales archivos. Los dos microservicios definidos a continuación dependen de este proyecto.
- Actúa como **interfaz gRPC**
- No necesita ejecución, pero sí requiere que se haga la generación de las clases Java que consumirán los microservicios, ejecutando el siguiente comando en su carpeta raíz:        
``mvn clean install``

### 1.2. Students
- Contiene información sobre los estudiantes y una referencia a sus respectivas escuelas.
- Actúa como **cliente gRPC**.
- Para ejecutarlo, hay que escribir el siguiente comando en su carpeta raíz:      
``mvn clean install spring-boot:run``
- Una vez levantado, se puede acceder a su Swagger en       
http://localhost:8080/swagger-ui/index.html

### 1.3. Schools
- Contiene información sobre las escuelas.
- Actúa como **servidor gRPC**.
- Para ejecutarlo, hay que escribir el siguiente comando en su carpeta raíz:        
``mvn clean install spring-boot:run``
- Una vez levantado, se puede acceder a su Swagger en http://localhost:8081/swagger-ui/index.html
## 2. Contenido de utilidad
- Dependencia Starter de gRPC para usar con SpringBoot. Muestra cómo distribuir los proyectos y qué dependencias requiere cada uno.     
https://yidongnan.github.io/grpc-spring-boot-starter/en/server/getting-started.html
