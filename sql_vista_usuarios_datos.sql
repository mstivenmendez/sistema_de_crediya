-- Vista que combina datos de usuario con información personal
-- Ejecutar esto en tu base de datos
CREATE VIEW vista_usuarios_datos AS
SELECT u.usuario_id,
   u.correo,
   u.clave,
   u.estado,
   u.fecha_creacion,
   u.nombre_usuario,
   u.rol,
   i.primer_nombre,
   i.segundo_nombre,
   i.primer_apellido,
   i.segundo_apellido,
   i.documento,
   i.telefono,
   i.salario,
   i.fecha_nacimiento
FROM usuario u
   INNER JOIN informacion i ON u.usuario_id = i.usuario_id_fk;
-- Verificar que la vista fue creada correctamente
-- SELECT * FROM vista_usuarios_datos;
