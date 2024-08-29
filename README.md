# API de Gestión de Alquileres de Propiedades

Este proyecto es un API REST desarrollado con Java y Spring Boot para gestionar el alquiler de propiedades temporales. El API permite registrar, listar, editar, eliminar y arrendar propiedades, asegurando que todas las operaciones estén protegidas mediante JWT.

## Requisitos

- Java 17 o superior
- Maven 3.8.1 o superior
- MySQL 8.0 o superior
- Git

## Configuración Inicial

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/usuario/repositorio.git
   cd repositorio
2. **Configurar la base de datos:**

Asegúrate de tener MySQL instalado y configurado. Luego, crea una base de datos llamada rental_management:

```CREATE DATABASE rental_management;```

Configura el usuario y contraseña en el archivo application.properties:

```
spring.datasource.url=jdbc:mysql://localhost:3306/rental_management
spring.datasource.username=prueba
spring.datasource.password=pruebapass
```

3. **Instalar dependencias:**

Ejecuta el siguiente comando para instalar las dependencias:

```
mvn clean install
```

4. **Ejecutar la aplicación:**

Inicia la aplicación con:

```
mvn spring-boot:run
```

La aplicación estará disponible en http://localhost:8181.

## Uso del API
**Autenticación**

Antes de utilizar los endpoints protegidos, es necesario registrar un usuario y autenticarse para obtener un JWT.

1. Registro de usuario:

Ejecuta el siguiente script:

```/bin/bash -c '
curl -X POST http://localhost:8181/auth/register \
-H "Content-Type: application/json" \
-d '{
"username": "othernewuser2",
"password": "password123",
"firstName": "John",
"lastname": "Doe",
"country": "USA"
}'
'
```

2. Iniciar sesión:

Ejecuta el siguiente script para obtener el token JWT:

```
/bin/bash -c '
curl -X POST http://localhost:8181/auth/login \
-H "Content-Type: application/json" \
-d '{
"username": "othernewuser2",
"password": "password123"
}'
'
```

3. Copia el token JWT que recibas para así poder correr los demás endpoints

## Endpoints
1. Crear Propiedad
   ```
   /bin/bash -c '
   curl -X POST http://localhost:8181/api/property/create \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
   -d "{\"name\": \"Propiedad12\", \"ubication\": \"123 Calle Falsa\", \"price\": 650000.0, \"availability\": true}\"
   '
   ```
2. Listar Propiedades Disponibles
   ```
   /bin/bash -c '
   curl -X GET http://localhost:8181/api/property/available \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
   '
   ```
3. Listar Propiedades por Rango de Precio
   ```
   /bin/bash -c '
   curl -X GET "http://localhost:8181/api/property/available/price-range?minPrice=100000&maxPrice=200000" \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
   '
   ```
4. Editar Propiedad
   ```
   /bin/bash -c '
   curl -X PUT http://localhost:8181/api/property \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
   -d "{\"name\": \"Propiedad1\", \"ubication\": \"Medellín\", \"price\": 160000.0, \"availability\": false}\"
   '
   ```
5. Eliminar Propiedad por Nombre
   ```
   /bin/bash -c '
   curl -X DELETE http://localhost:8181/api/property/Propiedad1 \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
   '
   ```
6. Arrendar Propiedad
   ```
   /bin/bash -c '
   curl -X PUT http://localhost:8181/api/property/lease \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
   -d "{\"idClient\": 3, \"propertyName\": \"Propiedad5\"}"
   '
   ```
7. Obtener Propiedad por Nombre
   ```
   /bin/bash -c '
   curl -X GET http://localhost:8181/api/property/Propiedad1 \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
   '
   ```
## Pruebas
   Este proyecto incluye pruebas unitarias y de integración. Puedes ejecutar las pruebas utilizando el siguiente comando:

```
mvn test
```
Se generará un reporte de cobertura de código con Jacoco, que se puede consultar en la carpeta target/site/jacoco/index.html.

## Trazabilidad
Cada vez que se registre una propiedad, se generarán logs que permitirán realizar un seguimiento de la operación.