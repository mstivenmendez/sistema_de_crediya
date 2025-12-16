-- -----------------------------------------------------
-- procedure sp_prestamos_por_cliente
-- -----------------------------------------------------

DELIMITER $$
USE `crediya_db`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_prestamos_por_cliente`(
    IN p_cliente_id INT
)
BEGIN
    SELECT 
        prestamo_id,
        numero_prestamo,
        valor,
        valor_total,
        valor_pendiente,
        interes,
        cuotas,
        fecha_inicio,
        fecha_limite,
        estado
    FROM prestamo
    WHERE cliente_usuario_id_fk = p_cliente_id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_prestamos_por_empleado
-- -----------------------------------------------------

DELIMITER $$
USE `crediya_db`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_prestamos_por_empleado`(
    IN p_empleado_usuario_id INT
)
BEGIN
    SELECT 
        p.prestamo_id,
        p.numero_prestamo,
        p.valor,
        p.valor_total,
        p.valor_pendiente,
        p.interes,
        p.cuotas,
        p.fecha_inicio,
        p.fecha_limite,
        p.estado,
        i.primer_nombre,
        i.segundo_nombre,
        i.primer_apellido,
        i.segundo_apellido,
        i.documento AS cliente_documento

    FROM prestamo p
    INNER JOIN informacion i 
        ON p.cliente_usuario_id_fk = i.usuario_id_fk
    INNER JOIN usuario u 
        ON u.usuario_id = p.cliente_usuario_id_fk

    WHERE p.empleado_usuario_id_fk = p_empleado_usuario_id;
END$$

DELIMITER ;
