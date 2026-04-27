# 📦 Sistema de Gestión de Inventario

> Aplicación web empresarial para la gestión integral de proveedores, productos, clientes, pedidos e historial de entregas.  
> Desarrollada como proyecto académico para la **Fundación Universitaria Compensar**.

---

## 🗂 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Configuración de Base de Datos](#configuración-de-base-de-datos)
- [Instalación y Despliegue](#instalación-y-despliegue)
- [Credenciales de Acceso](#credenciales-de-acceso)
- [Endpoints de la API REST](#endpoints-de-la-api-rest)
- [Funcionalidades](#funcionalidades)
- [Autores](#autores)

---

## Descripción General

Sistema de inventario full-stack que permite administrar el ciclo completo de una operación comercial: desde el registro de proveedores y productos hasta la creación de pedidos por clientes y el seguimiento de entregas. El frontend consume la API REST mediante `fetch` nativo y presenta la información en un panel de administración moderno.

---

## Arquitectura del Proyecto

```
┌─────────────────────────────────────────────────┐
│                   FRONTEND                       │
│         HTML + CSS + JavaScript (Fetch API)      │
│         index.html  ·  dashboard.html            │
│         login.css   ·  dashboard.css             │
│         auth.js     ·  app.js                    │
└──────────────────────┬──────────────────────────┘
                       │ HTTP / JSON
┌──────────────────────▼──────────────────────────┐
│               BACKEND — Jakarta EE               │
│            Apache Tomcat 11 (Servlet)            │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────┐ │
│  │ Controllers │→ │   Services   │→ │   DAOs  │ │
│  │  (REST API) │  │ (Lógica de   │  │(Queries │ │
│  │             │  │  negocio)    │  │ MySQL)  │ │
│  └─────────────┘  └──────────────┘  └────┬────┘ │
└───────────────────────────────────────────┼──────┘
                                            │ JDBC
┌───────────────────────────────────────────▼──────┐
│                  MySQL Database                   │
│   proveedores · productos · clientes · pedidos   │
│          detalle_pedidos · historial_entregas    │
└──────────────────────────────────────────────────┘
```

**Capas:**

| Capa | Paquete | Responsabilidad |
|------|---------|----------------|
| **Entidades** | `com.compensar.inventario.model.entity` | Mapeo de tablas MySQL a clases Java |
| **DTO** | `com.compensar.inventario.DTO` | Objetos de transferencia de datos entre capas |
| **DAO** | `com.compensar.inventario.dao` | Acceso directo a base de datos (JDBC) |
| **Service** | `com.compensar.inventario.service` | Lógica de negocio, validaciones, CRUD |
| **Controller** | `com.compensar.inventario.controller` | Exposición de endpoints REST (JSON) |
| **Frontend** | `Web Pages/` | Interfaz de usuario, consumo de API con `fetch` |

---

## Tecnologías Utilizadas

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| Lenguaje | Java | 21+ |
| Servidor | Apache Tomcat | 11 |
| Framework | Jakarta EE | 10+ |
| Base de datos | MySQL | 8.x |
| Build | Apache Maven | 3.9+ |
| IDE | NetBeans | 21+ |
| Frontend | HTML5 + CSS3 + JS | ES2020 |

---

## Estructura del Proyecto

```
inventario-1.0-SNAPSHOT/
│
├── Web Pages/
│   ├── META-INF/
│   ├── WEB-INF/
│   ├── css/
│   │   ├── dashboard.css          # Estilos del panel de administración
│   │   └── login.css              # Estilos de la pantalla de inicio de sesión
│   ├── js/
│   │   ├── app.js                 # Lógica del dashboard (CRUD, fetch, tablas)
│   │   └── auth.js                # Lógica de autenticación y sesión
│   ├── dashboard.html             # Panel principal de administración
│   └── index.html                 # Pantalla de login
│
└── Source Packages/
    ├── com.compensar.inventario/
    │   └── JakartaRestConfiguration.java      # Configuración JAX-RS
    │
    ├── com.compensar.inventario.DTO/
    │   ├── ClienteDTO.java
    │   ├── DetallePedidoDTO.java
    │   ├── PedidoDTO.java
    │   ├── ProductoDTO.java
    │   └── ProveedorDTO.java
    │
    ├── com.compensar.inventario.controller/
    │   ├── ClienteController.java
    │   ├── HistorialEntregaController.java
    │   ├── PedidoController.java
    │   ├── ProductoController.java
    │   └── ProveedorController.java
    │
    ├── com.compensar.inventario.dao/
    │   ├── GenericDAO.java
    │   ├── ClienteDAO.java
    │   ├── DetallePedidoDAO.java
    │   ├── HistorialEntregaDAO.java
    │   ├── PedidoDAO.java
    │   ├── ProductoDAO.java
    │   └── ProveedorDAO.java
    │
    ├── com.compensar.inventario.model.entity/
    │   ├── Cliente.java
    │   ├── DetallePedido.java
    │   ├── HistorialEntrega.java
    │   ├── Pedido.java
    │   ├── Producto.java
    │   └── Proveedor.java
    │
    ├── com.compensar.inventario.resources/
    │   └── JakartaEE11Resource.java
    │
    ├── com.compensar.inventario.service/
    │   ├── ClienteService.java
    │   ├── HistorialEntregaService.java
    │   ├── PedidoService.java
    │   ├── ProductoService.java
    │   └── ProveedorService.java
    │
    └── com.compensar.inventario.util/
        └── JPAUtil.java                       # Utilidad de conexión a base de datos
```

---

## Requisitos Previos

Antes de instalar el proyecto asegúrate de tener instalado:

- ✅ **Java JDK 21** o superior → [Descargar](https://www.oracle.com/java/technologies/downloads/)
- ✅ **Apache Tomcat 11** → [Descargar](https://tomcat.apache.org/download-11.cgi)
- ✅ **MySQL Server 8.x** → [Descargar](https://dev.mysql.com/downloads/mysql/)
- ✅ **NetBeans IDE 21+** → [Descargar](https://netbeans.apache.org/front/main/download/)
- ✅ **Apache Maven 3.9+** (incluido en NetBeans)
- ✅ **MySQL Connector/J** (driver JDBC, declarado en `pom.xml`)

---

## Configuración de Base de Datos

### 1. Crear la base de datos

Ejecuta el siguiente script SQL en MySQL Workbench o tu cliente favorito:

```sql
CREATE DATABASE IF NOT EXISTS inventario
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE inventario;

-- Tabla: proveedores
CREATE TABLE proveedores (
    idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    contacto    VARCHAR(100),
    telefono    VARCHAR(20),
    email       VARCHAR(100)
);

-- Tabla: productos
CREATE TABLE productos (
    idProducto  INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio      DECIMAL(10,2) NOT NULL,
    stock       INT NOT NULL DEFAULT 0,
    idProveedor INT,
    CONSTRAINT fk_producto_proveedor
        FOREIGN KEY (idProveedor) REFERENCES proveedores(idProveedor)
        ON DELETE SET NULL
);

-- Tabla: clientes
CREATE TABLE clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    telefono  VARCHAR(20),
    email     VARCHAR(100)
);

-- Tabla: pedidos
CREATE TABLE pedidos (
    idPedido    INT AUTO_INCREMENT PRIMARY KEY,
    idCliente   INT NOT NULL,
    fechaPedido DATE NOT NULL,
    estado      ENUM('pendiente','en proceso','entregado') DEFAULT 'pendiente',
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (idCliente) REFERENCES clientes(idCliente)
);

-- Tabla: detalle_pedidos
CREATE TABLE detalle_pedidos (
    idDetalle  INT AUTO_INCREMENT PRIMARY KEY,
    idPedido   INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad   INT NOT NULL,
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (idPedido) REFERENCES pedidos(idPedido),
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);

-- Tabla: historial_entregas
CREATE TABLE historial_entregas (
    idEntrega      INT AUTO_INCREMENT PRIMARY KEY,
    idPedido       INT NOT NULL,
    fechaEntrega   DATE,
    estadoEntrega  ENUM('pendiente','en proceso','entregado') DEFAULT 'pendiente',
    CONSTRAINT fk_historial_pedido
        FOREIGN KEY (idPedido) REFERENCES pedidos(idPedido)
);
```

### 2. Configurar credenciales de conexión

Ubica el archivo `JPAUtil.java` en el paquete `com.compensar.inventario.util` y verifica o ajusta los siguientes valores:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/inventario?useSSL=false&serverTimezone=UTC";
private static final String USER     = "root";        // ← tu usuario MySQL
private static final String PASSWORD = "tu_password"; // ← tu contraseña MySQL
```

> ⚠️ **Importante:** Si tu MySQL usa un puerto distinto al `3306`, actualiza la URL de conexión.

### 3. Verificar el driver MySQL en `pom.xml`

El `pom.xml` debe contener la dependencia del conector. Si no está, agrégala:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

---

## Instalación y Despliegue

### Opción A — Desde NetBeans (recomendado)

1. **Clonar o copiar** el proyecto en tu equipo.

2. **Abrir NetBeans** → `File` → `Open Project` → selecciona la carpeta `inventario-1.0-SNAPSHOT`.

3. **Configurar Tomcat 11** en NetBeans:
   - `Tools` → `Servers` → `Add Server`
   - Seleccionar **Apache Tomcat or TomEE**
   - Indicar la ruta de instalación de Tomcat 11
   - Finalizar el asistente

4. **Asociar el servidor al proyecto:**
   - Clic derecho en el proyecto → `Properties`
   - `Run` → seleccionar **Tomcat 11** como servidor

5. **Configurar la base de datos** siguiendo la sección anterior.

6. **Limpiar y compilar:**
   - Clic derecho en el proyecto → `Clean and Build`

7. **Ejecutar:**
   - Clic derecho en el proyecto → `Run`
   - NetBeans desplegará el `.war` en Tomcat automáticamente

8. **Abrir en el navegador:**
   ```
   http://localhost:8080/inventario/index.html
   ```

### Opción B — Desde línea de comandos (Maven)

```bash
# 1. Clonar el repositorio
git clone <url-del-repositorio>
cd inventario-1.0-SNAPSHOT

# 2. Compilar y empaquetar
mvn clean package

# 3. Copiar el .war generado a Tomcat
cp target/inventario-1.0-SNAPSHOT.war /ruta/tomcat11/webapps/inventario.war

# 4. Iniciar Tomcat
/ruta/tomcat11/bin/startup.sh        # Linux/Mac
/ruta/tomcat11/bin/startup.bat       # Windows

# 5. Abrir en el navegador
# http://localhost:8080/inventario/index.html
```

---

## Credenciales de Acceso

### Aplicación Web

| Campo | Valor |
|-------|-------|
| Usuario | `admin` |
| Contraseña | `admin` |

> La autenticación es local (validada en `auth.js`). La sesión se mantiene con `sessionStorage`.

### Base de Datos MySQL (por defecto)

| Campo | Valor sugerido |
|-------|---------------|
| Host | `localhost` |
| Puerto | `3306` |
| Base de datos | `inventario` |
| Usuario | `root` |
| Contraseña | *(la que configuraste en MySQL)* |

---

## Endpoints de la API REST

La base de todos los endpoints es:

```
http://localhost:8080/inventario/api
```

### Proveedores

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/proveedores` | Listar todos los proveedores |
| `GET` | `/proveedores/{id}` | Obtener proveedor por ID |
| `POST` | `/proveedores` | Crear nuevo proveedor |
| `PUT` | `/proveedores/{id}` | Actualizar proveedor |
| `DELETE` | `/proveedores/{id}` | Eliminar proveedor |

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/productos` | Listar todos los productos |
| `GET` | `/productos/{id}` | Obtener producto por ID |
| `POST` | `/productos` | Crear nuevo producto |
| `PUT` | `/productos/{id}` | Actualizar producto |
| `DELETE` | `/productos/{id}` | Eliminar producto |

### Clientes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/clientes` | Listar todos los clientes |
| `GET` | `/clientes/{id}` | Obtener cliente por ID |
| `POST` | `/clientes` | Crear nuevo cliente |
| `PUT` | `/clientes/{id}` | Actualizar cliente |
| `DELETE` | `/clientes/{id}` | Eliminar cliente |

### Pedidos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/pedidos` | Listar todos los pedidos |
| `POST` | `/pedidos` | Crear pedido (descuenta stock automáticamente) |
| `PUT` | `/pedidos/{id}` | Cambiar estado del pedido |

### Historial de Entregas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/historial` | Ver todas las entregas |
| `GET` | `/historial/cliente/{id}` | Filtrar entregas por cliente |

---

## Funcionalidades

- 🏭 **Proveedores** — CRUD completo: registrar, editar y eliminar proveedores.
- 📦 **Productos** — CRUD completo con control de stock y asociación a proveedor.
- 👤 **Clientes** — CRUD completo de clientes con datos de contacto.
- 🛒 **Pedidos** — Creación de pedidos con múltiples productos; descuento automático de stock al confirmar; cambio de estado (`pendiente` → `en proceso` → `entregado`).
- 📋 **Historial de Entregas** — Consulta global y filtro por cliente.
- 🔐 **Autenticación** — Login con sesión en `sessionStorage`; redirección automática si no hay sesión activa.
- 📱 **Diseño Responsive** — Adaptado para escritorio y dispositivos móviles.

---

## Autores

Desarrollado para la asignatura de **Programación Web / Desarrollo de Aplicaciones Empresariales**  
**Fundación Universitaria Compensar** — 2025

---

> _"Del inventario al cliente, trazabilidad total."_
