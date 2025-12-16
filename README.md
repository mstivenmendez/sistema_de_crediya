# Sistema de Cobros de Cartera "CrediYa"

## 📋 Descripción General

CrediYa es un sistema de gestión de préstamos y cobros de cartera desarrollado en Java para la empresa CrediYa S.A.S. El sistema permite digitalizar el control de préstamos personales, reemplazando las hojas de cálculo tradicionales con una solución robusta que gestiona empleados, clientes, préstamos y pagos.

### Características Principales

- **Gestión de Empleados**: Registro, consulta y administración de empleados con roles específicos
- **Gestión de Clientes**: Control completo de clientes y sus préstamos asociados
- **Módulo de Préstamos**: Creación, seguimiento y actualización de préstamos con cálculo automático de intereses
- **Sistema de Pagos**: Registro de abonos y actualización automática de saldos pendientes
- **Reportes Avanzados**: Generación de reportes con filtros usando Stream API y expresiones Lambda
- **Persistencia Dual**: Almacenamiento en archivos de texto y base de datos MySQL
- **Sistema de Notificaciones**: Generación de notificaciones y planes de cuota en formato TXT

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 11+
- **Gestión de Dependencias**: Maven
- **Base de Datos**: MySQL 8.0+
- **JDBC**: Conectividad con base de datos
- **Paradigma**: Programación Orientada a Objetos (POO)
- **Patrones de Diseño**: SOLID, CRUD genérico

## 📁 Estructura del Proyecto

```
sistema_de_crediya/
├── src/main/java/proyecto/
│   ├── Main.java                    # Punto de entrada de la aplicación
│   ├── conector/
│   │   └── ConexionDB.java         # Gestión de conexión a MySQL
│   ├── crud/
│   │   ├── CrudEntity.java         # Interfaz genérica CRUD
│   │   ├── ClienteCrud.java        # Operaciones CRUD de clientes
│   │   ├── EmpleadoCrud.java       # Operaciones CRUD de empleados
│   │   └── UsuarioCrud.java        # Gestión de usuarios
│   ├── personal/
│   │   ├── Persona.java            # Clase base abstracta
│   │   ├── Cliente.java            # Modelo de cliente
│   │   └── Empleado.java           # Modelo de empleado
│   ├── prestamo/
│   │   ├── Prestamo.java           # Modelo de préstamo
│   │   ├── CrudPrestamo.java       # Operaciones CRUD de préstamos
│   │   └── Estado.java             # Enum de estados de préstamo
│   ├── pagos/
│   │   ├── Pago.java               # Modelo de pago
│   │   ├── CrudPago.java           # Operaciones CRUD de pagos
│   │   └── EstadoPago.java         # Enum de estados de pago
│   ├── notificacion/
│   │   └── Notificacion.java       # Sistema de notificaciones
│   ├── reportes/
│   │   └── Reportes.java           # Generación de reportes
│   ├── ui/
│   │   ├── Inicio.java             # Pantalla de inicio
│   │   ├── Menu.java               # Sistema de menús
│   │   └── Opcion.java             # Enum de opciones de menú
│   ├── util/
│   │   ├── IngresoDatos.java       # Utilidades de entrada de datos
│   │   ├── SesionUsuario.java      # Gestión de sesión
│   │   └── Simular.java            # Simulaciones
│   └── validaciones/
│       ├── Validacion.java         # Validaciones genéricas
│       ├── ValidacionUsuario.java  # Validación de usuarios
│       └── ValidarNumero.java      # Validación numérica
├── database_schema.sql              # Esquema de base de datos
├── sql_vista_usuarios_datos.sql    # Vistas SQL
└── pom.xml                          # Configuración de Maven
```

## 🚀 Requisitos Previos

### Software Necesario

1. **Java Development Kit (JDK) 11 o superior**
   - Verificar instalación: `java -version`
   
2. **Apache Maven 3.6+**
   - Verificar instalación: `mvn -version`

3. **MySQL Server 8.0+**
   - Verificar instalación: `mysql --version`

4. **IDE recomendado** (opcional)
   - IntelliJ IDEA
   - Eclipse
   - Visual Studio Code con extensiones Java

## ⚙️ Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/mstivenmendez/sistema_de_crediya.git
cd sistema_de_crediya
```

### 2. Configurar la Base de Datos

```

Ejecutar el script de esquema

```sql
source database_schema.sql;
```

O ejecutar manualmente:

```sql
CREATE DATABASE crediya_db;
USE crediya_db;

CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80),
    documento VARCHAR(30),
    rol VARCHAR(30),
    correo VARCHAR(80),
    salario DECIMAL(10,2)
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80),
    documento VARCHAR(30),
    correo VARCHAR(80),
    telefono VARCHAR(20)
);

CREATE TABLE prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    empleado_id INT,
    monto DECIMAL(12,2),
    interes DECIMAL(5,2),
    cuotas INT,
    fecha_inicio DATE,
    estado VARCHAR(20),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id INT,
    fecha_pago DATE,
    monto DECIMAL(10,2),
    FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)
);
```

#### Paso 3: Configurar credenciales de conexión

Editar el archivo `ConexionDB.java` con tus credenciales:

```java
private static final String URL = "jdbc:mysql://localhost:3306/crediya_db";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

### 3. Compilar el Proyecto

```bash
mvn clean compile
```

### 4. Ejecutar el Proyecto

#### Opción 1: Con Maven

```bash
mvn exec:java -Dexec.mainClass="proyecto.Main"
```

#### Opción 2: Desde el JAR compilado

```bash
mvn clean package
java -jar target/sistema_de_crediya-1.0.jar
```

#### Opción 3: Desde el IDE

Ejecutar la clase `Main.java` directamente desde tu IDE.

## 📖 Uso del Sistema

### Flujo de Trabajo Básico

1. **Inicio de Sesión**
   - Al iniciar el sistema, se presenta la pantalla de inicio
   - Ingresar credenciales de empleado o cliente

2. **Menú Principal**
   - El sistema presenta diferentes opciones según el rol del usuario

### Ejemplos de Uso

#### Ejemplo 1: Registrar un Nuevo Cliente

```
=== MENÚ EMPLEADO ===
1. Gestión de Clientes
   > 1. Registrar Nuevo Cliente
   
Ingrese nombre: Juan Pérez
Ingrese documento: 123456789
Ingrese correo: juan.perez@email.com
Ingrese teléfono: 3001234567

✓ Cliente registrado exitosamente con ID: 12
```

#### Ejemplo 2: Crear un Préstamo

```
=== GESTIÓN DE PRÉSTAMOS ===
1. Crear Nuevo Préstamo

Ingrese ID del cliente: 12
Ingrese monto del préstamo: 5000000
Ingrese tasa de interés (%): 2.5
Ingrese número de cuotas: 12

Resumen del Préstamo:
- Cliente: Juan Pérez (ID: 12)
- Monto: $5,000,000
- Interés: 2.5% mensual
- Cuotas: 12
- Valor cuota mensual: $456,789
- Total a pagar: $5,481,468

✓ Préstamo creado exitosamente con ID: 285214
📄 Plan de cuotas generado: Plan_Cuotas_285214_20251216_003605.txt
```

#### Ejemplo 3: Registrar un Pago

```
=== REGISTRO DE PAGO ===

Ingrese ID del préstamo: 285214
Ingrese monto del pago: 456789

Préstamo ID: 285214
Saldo anterior: $5,481,468
Monto pagado: $456,789
Saldo restante: $5,024,679

✓ Pago registrado exitosamente
```

#### Ejemplo 4: Generar Reportes

```
=== REPORTES ===
1. Préstamos Activos
2. Préstamos Vencidos
3. Clientes Morosos
4. Histórico de Pagos
5. Préstamos por Empleado

> 1

=== PRÉSTAMOS ACTIVOS ===
Total: 15 préstamos

ID: 285214 | Cliente: Juan Pérez | Monto: $5,000,000 | Estado: ACTIVO
ID: 285215 | Cliente: María García | Monto: $3,500,000 | Estado: ACTIVO
...

📄 Reporte exportado: PrestamosActivos_20251216_120000.txt
```

## 📊 Funcionalidades Principales

### Módulo de Empleados
- ✅ Registro de nuevos empleados
- ✅ Consulta y listado de empleados
- ✅ Actualización de información
- ✅ Asignación de roles
- ✅ Persistencia en BD y archivos

### Módulo de Clientes
- ✅ Registro de clientes
- ✅ Consulta de información del cliente
- ✅ Visualización de préstamos asociados
- ✅ Historial de pagos
- ✅ Generación de notificaciones

### Módulo de Préstamos
- ✅ Creación de préstamos
- ✅ Cálculo automático de intereses
- ✅ Generación de plan de cuotas
- ✅ Cambio de estados (PENDIENTE, ACTIVO, PAGADO, VENCIDO)
- ✅ Asociación con cliente y empleado
- ✅ Exportación de planes de pago

### Módulo de Pagos
- ✅ Registro de abonos
- ✅ Actualización automática de saldos
- ✅ Histórico de pagos por préstamo
- ✅ Validación de montos
- ✅ Estados de pago (PENDIENTE, PAGADO, PARCIAL)

### Módulo de Reportes
- ✅ Préstamos activos
- ✅ Préstamos vencidos
- ✅ Clientes morosos
- ✅ Préstamos por empleado
- ✅ Filtros con Stream API y Lambda
- ✅ Exportación a archivos TXT

## 🔒 Seguridad y Validaciones

- Validación de datos de entrada
- Validación de usuarios y contraseñas
- Validación de números y montos
- Manejo de excepciones SQL
- Prevención de inyección SQL mediante PreparedStatements
- Validación de estados de préstamos y pagos

## 📝 Archivos Generados

El sistema genera automáticamente archivos de texto con información relevante:

- `Plan_Cuotas_[ID]_[FECHA].txt`: Plan de cuotas de cada préstamo
- `Notificaciones_Cliente[ID]_[FECHA].txt`: Notificaciones de clientes
- `PrestamosAprobados_Empleado[ID]_[FECHA].txt`: Reportes de empleados


### Archivos TXT no se generan

**Solución:**
- Verificar permisos de escritura en el directorio
- Ejecutar la aplicación con permisos adecuados

## 🤝 Contribuciones

Para contribuir al proyecto:

1. Fork el repositorio
2. Crear una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit los cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## 📄 Licencia

Este proyecto fue desarrollado como parte de un ejercicio académico para CampusLands.

## 👥 Autores

- Maicol Estiven Mendez Cuadros, JUan David Quiñonez Rojas.

## 📞 Soporte

Para reportar problemas o sugerencias, crear un issue en el repositorio.

---

**CrediYa S.A.S.** - Sistema de Gestión de Préstamos y Cobros de Cartera
