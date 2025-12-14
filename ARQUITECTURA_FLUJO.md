# Arquitectura y Flujo de Datos del Sistema

## Estructura de Clases CRUD

El proyecto sigue un patrón consistente con clases CRUD que heredan de `CrudEntity`:

```
CRUD Entity (Interface)
├── ClienteCrud      → Maneja operaciones de clientes
├── EmpleadoCrud     → Maneja operaciones de empleados  
├── UsuarioCrud      → Maneja operaciones del usuario logueado (NUEVO)
├── CrudPrestamo     → Maneja operaciones de préstamos
└── CrudPago         → Maneja operaciones de pagos
```

## Flujo de Autenticación

1. **Login** (`ValidacionUsuario.ValidacionUsuarioExistente()`)
   - Solicita usuario y contraseña
   - Valida credenciales contra BD
   - Automáticamente guarda el `usuario_id` en `SesionUsuario`

2. **Sesión Activa** (`SesionUsuario`)
   - Almacena `usuario_id`, `nombre_usuario`, `rol`
   - Accesible globalmente en la aplicación
   - Se puede cerrar con `SesionUsuario.cerrarSesion()`

## UsuarioCrud - Nueva Clase

### Responsabilidades:
- Obtener datos del usuario logueado desde la vista `vista_usuarios_datos`
- Actualizar información del usuario (correo, nombre_usuario, contraseña, teléfono)
- Formatear datos para mostrar en la UI

### Métodos principales:

```java
// Obtener datos
UsuarioCrud usuarioCrud = new UsuarioCrud();
Map<String, Object> datos = usuarioCrud.obtenerDatosUsuario();
String datosFormato = usuarioCrud.obtenerDatosFormateados();

// Actualizar información
usuarioCrud.actualizarCorreo("nuevo@email.com");
usuarioCrud.actualizarNombreUsuario("nuevo_usuario");
usuarioCrud.actualizarContraseña("nueva_clave");
usuarioCrud.actualizarTelefono("3001234567");
```

## Uso en Opcion.java

```java
public void VistaUsuarioOpcion(Integer valor) {
    switch (valor) {
        case 1:
            // Mostrar datos del usuario
            UsuarioCrud usuarioCrud = new UsuarioCrud();
            String datosFormateados = usuarioCrud.obtenerDatosFormateados();
            JOptionPane.showMessageDialog(null, datosFormateados, 
                "Mi Información", JOptionPane.INFORMATION_MESSAGE);
            break;
        case 2:
            // Consultar mis préstamos
            break;
        // ... otros casos
    }
}
```

## Vista SQL

La clase `UsuarioCrud` utiliza la vista `vista_usuarios_datos`:

```sql
CREATE VIEW vista_usuarios_datos AS 
SELECT 
    u.usuario_id, u.correo, u.clave, u.estado, u.fecha_creacion, 
    u.nombre_usuario, u.rol, 
    i.primer_nombre, i.segundo_nombre, i.primer_apellido, i.segundo_apellido, 
    i.documento, i.telefono, i.salario, i.fecha_nacimiento 
FROM usuario u 
INNER JOIN informacion i ON u.usuario_id = i.usuario_id_fk;
```

## Flujo Completo Ejemplo

```
1. Usuario inicia la aplicación
   ↓
2. Selecciona "Iniciar Sesión"
   ↓
3. ValidacionUsuario.ValidacionUsuarioExistente() 
   → Valida credenciales
   → Guarda usuario_id en SesionUsuario
   ↓
4. Usuario selecciona "Ver mis datos" (opción 1)
   ↓
5. VistaUsuarioOpcion(1)
   → Crea instancia de UsuarioCrud
   → Obtiene datos con obtenerDatosFormateados()
   → Muestra en JOptionPane
   ↓
6. UsuarioCrud puede estar disponible en cualquier parte de la app
   que tenga acceso a SesionUsuario
```

## Beneficios de esta Arquitectura

✅ **Coherente**: Sigue el patrón CRUD existente  
✅ **Reutilizable**: UsuarioCrud puede usarse en múltiples vistas  
✅ **Mantenible**: Todos los datos del usuario en una clase  
✅ **Escalable**: Fácil agregar nuevos métodos de actualización  
✅ **Seguro**: El usuario_id se obtiene de la sesión activa  

## Próximos Pasos

Puedes usar `UsuarioCrud` en:
- Vista de "Editar mi perfil"
- Consulta de "Mis préstamos"
- Consulta de "Mis pagos"
- Reportes personalizados
