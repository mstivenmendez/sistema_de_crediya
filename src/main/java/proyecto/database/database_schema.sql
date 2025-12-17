-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema crediya_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema crediya_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `crediya_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `crediya_db` ;

-- -----------------------------------------------------
-- Table `crediya_db`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crediya_db`.`usuario` (
  `usuario_id` INT NOT NULL AUTO_INCREMENT,
  `correo` VARCHAR(80) NOT NULL,
  `clave` VARCHAR(45) NOT NULL,
  `estado` ENUM('activo', 'inactivo') NULL DEFAULT 'activo',
  `fecha_creacion` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `nombre_usuario` VARCHAR(255) NOT NULL,
  `rol` ENUM('empleado', 'cliente') NOT NULL DEFAULT 'cliente',
  PRIMARY KEY (`usuario_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crediya_db`.`informacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crediya_db`.`informacion` (
  `usuario_id_fk` INT NOT NULL,
  `primer_nombre` VARCHAR(45) NOT NULL,
  `segundo_nombre` VARCHAR(45) NULL DEFAULT NULL,
  `primer_apellido` VARCHAR(45) NOT NULL,
  `segundo_apellido` VARCHAR(45) NULL DEFAULT NULL,
  `documento` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(45) NULL DEFAULT NULL,
  `salario` DECIMAL(10,2) NULL DEFAULT NULL,
  `fecha_nacimiento` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`usuario_id_fk`),
  INDEX `fk_empleado_usuario1_idx` (`usuario_id_fk` ASC) VISIBLE,
  CONSTRAINT `fk_empleado_usuario1`
    FOREIGN KEY (`usuario_id_fk`)
    REFERENCES `crediya_db`.`usuario` (`usuario_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crediya_db`.`notificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notificacion` (
  `notificacion_id` INT NOT NULL AUTO_INCREMENT,
  `fk_usuario` INT NOT NULL,
  `fk_empleado` INT DEFAULT NULL,
  `nombre` VARCHAR(150) NOT NULL,
  `mensaje` TEXT NOT NULL,
  `fecha_envio` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` ENUM('pendiente','enviada','leida') NOT NULL DEFAULT 'pendiente',
  PRIMARY KEY (`notificacion_id`),
  INDEX `idx_notificacion_fk_usuario` (`fk_usuario`),
  INDEX `idx_notificacion_fk_empleado` (`fk_empleado`)
  -- Si tu tabla de usuarios se llama 'usuario' y la PK 'usuario_id', puedes activar las FK:
  -- ,CONSTRAINT `fk_notif_usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `usuario`(`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE
  -- ,CONSTRAINT `fk_notif_empleado` FOREIGN KEY (`fk_empleado`) REFERENCES `usuario`(`usuario_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
da') NOT NULL DEFAULT 'pendiente',
  PRIMARY KEY (`notificacion_id`),
  INDEX `idx_notificacion_fk_usuario` (`fk_usuario`),
  INDEX `idx_notificacion_fk_empleado` (`fk_empleado`)
  -- Si tu tabla de usuarios se llama 'usuario' y la PK 'usuario_id', puedes activar las FK:
  -- ,CONSTRAINT `fk_notif_usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `usuario`(`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE
  -- ,CONSTRAINT `fk_notif_empleado` FOREIGN KEY (`fk_empleado`) REFERENCES `usuario`(`usuario_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `crediya_db`.`prestamo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crediya_db`.`prestamo` (
  `prestamo_id` INT NOT NULL AUTO_INCREMENT,
  `cliente_usuario_id_fk` INT NOT NULL,
  `empleado_usuario_id_fk` INT NOT NULL,
  `valor` DECIMAL(12,2) NOT NULL,
  `valor_total` DECIMAL(15,2) NULL DEFAULT NULL,
  `valor_pendiente` DECIMAL(15,2) NULL DEFAULT NULL,
  `interes` DECIMAL(10,2) NOT NULL,
  `cuotas` INT NOT NULL,
  `valor_cuota` DECIMAL(15,2) NOT NULL,
  `cuota_pendiente` INT NULL DEFAULT NULL,
  `fecha_inicio` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_limite` DATETIME NOT NULL,
  `estado` ENUM('activo', 'inactivo', 'mora') NOT NULL DEFAULT 'activo',
  `numero_prestamo` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`prestamo_id`),
  UNIQUE INDEX `prestamo_id_UNIQUE` (`prestamo_id` ASC) VISIBLE,
  UNIQUE INDEX `numero_prestamo` (`numero_prestamo` ASC) VISIBLE,
  INDEX `fk_prestamo_usuario1_idx` (`empleado_usuario_id_fk` ASC) VISIBLE,
  INDEX `fk_prestamo_usuario2_idx` (`cliente_usuario_id_fk` ASC) VISIBLE,
  CONSTRAINT `fk_prestamo_usuario1`
    FOREIGN KEY (`empleado_usuario_id_fk`)
    REFERENCES `crediya_db`.`usuario` (`usuario_id`),
  CONSTRAINT `fk_prestamo_usuario2`
    FOREIGN KEY (`cliente_usuario_id_fk`)
    REFERENCES `crediya_db`.`usuario` (`usuario_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crediya_db`.`pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crediya_db`.`pago` (
  `pago_id` INT NOT NULL AUTO_INCREMENT,
  `prestamo_id_fk` INT NOT NULL,
  `valor` DECIMAL(12,2) NOT NULL,
  `fecha_pago` DATETIME NULL DEFAULT NULL,
  `numero_pago` VARCHAR(25) NULL DEFAULT NULL,
  PRIMARY KEY (`pago_id`),
  INDEX `fk_pago_prestamo1_idx` (`prestamo_id_fk` ASC) VISIBLE,
  CONSTRAINT `fk_pago_prestamo1`
    FOREIGN KEY (`prestamo_id_fk`)
    REFERENCES `crediya_db`.`prestamo` (`prestamo_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `crediya_db` ;

-- -----------------------------------------------------
-- Placeholder table for view `crediya_db`.`vista_empleados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crediya_db`.`vista_empleados` (`usuario_id` INT, `documento` INT, `nombre_usuario` INT, `primer_nombre` INT, `segundo_nombre` INT, `primer_apellido` INT, `segundo_apellido` INT, `telefono` INT, `correo` INT, `estado` INT, `rol` INT, `salario` INT);

-- -----------------------------------------------------
-- Placeholder table for view `crediya_db`.`vista_usuarios_datos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crediya_db`.`vista_usuarios_datos` (`usuario_id` INT, `correo` INT, `clave` INT, `estado` INT, `fecha_creacion` INT, `nombre_usuario` INT, `rol` INT, `primer_nombre` INT, `segundo_nombre` INT, `primer_apellido` INT, `segundo_apellido` INT, `documento` INT, `telefono` INT, `salario` INT, `fecha_nacimiento` INT);

-- -----------------------------------------------------
-- procedure datos
-- -----------------------------------------------------

DELIMITER $$
USE `crediya_db`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `datos`()
BEGIN
    -- Seleccionar los datos del usuario y su información
    SELECT 
        u.usuario_id, 
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
    FROM 
        usuario u
    JOIN 
        informacion i ON u.usuario_id = i.usuario_id_fk;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_insertar_pago
-- -----------------------------------------------------

DELIMITER $$
USE `crediya_db`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertar_pago`(
    IN p_prestamo_id INT,
    IN p_valor_pago DECIMAL(12,2)
)
BEGIN
    DECLARE v_valor_pendiente DECIMAL(15,2);
    DECLARE v_estado_prestamo VARCHAR(20);

    -- 1️⃣ Validar valor del pago
    IF p_valor_pago <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El valor del pago debe ser mayor a cero';
    END IF;

    -- 2️⃣ Verificar que el préstamo exista y obtener datos
    SELECT valor_pendiente, estado
    INTO v_valor_pendiente, v_estado_prestamo
    FROM prestamo
    WHERE prestamo_id = p_prestamo_id;

    IF v_valor_pendiente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El préstamo no existe';
    END IF;

    -- 3️⃣ Verificar que el préstamo no esté ya pagado
    IF v_valor_pendiente <= 0 OR v_estado_prestamo = 'PAGADO' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El préstamo ya está pagado';
    END IF;

    -- 4️⃣ Validar que el pago no exceda el saldo pendiente
    IF p_valor_pago > v_valor_pendiente THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El pago excede el valor pendiente del préstamo';
    END IF;

    -- 5️⃣ Insertar el pago
    INSERT INTO pago (
        prestamo_id_fk,
        valor,
        fecha_pago,
        estado
    ) VALUES (
        p_prestamo_id,
        p_valor_pago,
        NOW(),
        'pagado'
    );

    -- 6️⃣ Actualizar el saldo pendiente del préstamo
    UPDATE prestamo
    SET valor_pendiente = valor_pendiente - p_valor_pago
    WHERE prestamo_id = p_prestamo_id;

    -- 7️⃣ Si el saldo llega a cero, marcar préstamo como PAGADO
    IF (v_valor_pendiente - p_valor_pago) = 0 THEN
        UPDATE prestamo
        SET estado = 'PAGADO'
        WHERE prestamo_id = p_prestamo_id;
    END IF;

END$$

DELIMITER ;

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

-- -----------------------------------------------------
-- View `crediya_db`.`vista_empleados`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crediya_db`.`vista_empleados`;
USE `crediya_db`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crediya_db`.`vista_empleados` AS select `u`.`usuario_id` AS `usuario_id`,`i`.`documento` AS `documento`,`u`.`nombre_usuario` AS `nombre_usuario`,`i`.`primer_nombre` AS `primer_nombre`,`i`.`segundo_nombre` AS `segundo_nombre`,`i`.`primer_apellido` AS `primer_apellido`,`i`.`segundo_apellido` AS `segundo_apellido`,`i`.`telefono` AS `telefono`,`u`.`correo` AS `correo`,`u`.`estado` AS `estado`,`u`.`rol` AS `rol`,`i`.`salario` AS `salario` from (`crediya_db`.`usuario` `u` join `crediya_db`.`informacion` `i` on((`u`.`usuario_id` = `i`.`usuario_id_fk`)));

-- -----------------------------------------------------
-- View `crediya_db`.`vista_usuarios_datos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crediya_db`.`vista_usuarios_datos`;
USE `crediya_db`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crediya_db`.`vista_usuarios_datos` AS select `u`.`usuario_id` AS `usuario_id`,`u`.`correo` AS `correo`,`u`.`clave` AS `clave`,`u`.`estado` AS `estado`,`u`.`fecha_creacion` AS `fecha_creacion`,`u`.`nombre_usuario` AS `nombre_usuario`,`u`.`rol` AS `rol`,`i`.`primer_nombre` AS `primer_nombre`,`i`.`segundo_nombre` AS `segundo_nombre`,`i`.`primer_apellido` AS `primer_apellido`,`i`.`segundo_apellido` AS `segundo_apellido`,`i`.`documento` AS `documento`,`i`.`telefono` AS `telefono`,`i`.`salario` AS `salario`,`i`.`fecha_nacimiento` AS `fecha_nacimiento` from (`crediya_db`.`usuario` `u` join `crediya_db`.`informacion` `i` on((`u`.`usuario_id` = `i`.`usuario_id_fk`)));
USE `crediya_db`;

DELIMITER $$
USE `crediya_db`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `crediya_db`.`trg_prestamo_autocalculo`
BEFORE INSERT ON `crediya_db`.`prestamo`
FOR EACH ROW
BEGIN
  DECLARE tasa_mensual DECIMAL(18,10);
  DECLARE factor DECIMAL(18,10);
  DECLARE pago_mensual DECIMAL(18,10);

  -- 0) Numero de prestamo (VARCHAR)
  SET NEW.numero_prestamo = CONCAT(
      LPAD(FLOOR(RAND() * 1000000), 6, '0')
  );

  -- 1) Si no envían fecha_inicio, usar NOW()
  IF NEW.fecha_inicio IS NULL THEN
    SET NEW.fecha_inicio = NOW();
  END IF;

  -- 2) fecha_limite = fecha_inicio + cuotas meses
  SET NEW.fecha_limite = DATE_ADD(NEW.fecha_inicio, INTERVAL NEW.cuotas MONTH);

  -- 3) cuota_pendiente = cuotas
  SET NEW.cuota_pendiente = NEW.cuotas;

  -- 4) Fórmula amortización (interés anual → mensual)
  SET tasa_mensual = (NEW.interes / 100) / 12;

  IF tasa_mensual = 0 THEN
    SET pago_mensual = NEW.valor / NEW.cuotas;
  ELSE
    SET factor = POW(1 + tasa_mensual, NEW.cuotas);
    SET pago_mensual = (NEW.valor * tasa_mensual * factor) / (factor - 1);
  END IF;

  -- 5) Guardar valor de cada cuota
  SET NEW.valor_cuota = ROUND(pago_mensual, 2);

  -- 6) valor_total y valor_pendiente
  SET NEW.valor_total = ROUND(pago_mensual * NEW.cuotas, 2);
  SET NEW.valor_pendiente = NEW.valor_total;

END$$

USE `crediya_db`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `crediya_db`.`trg_pago_after_insert`
AFTER INSERT ON `crediya_db`.`pago`
FOR EACH ROW
BEGIN
    -- Actualizar valores del préstamo
    UPDATE prestamo
    SET 
        valor_pendiente = IF(valor_pendiente - NEW.valor < 0, 0, valor_pendiente - NEW.valor),
        cuota_pendiente = IF(cuota_pendiente > 0, cuota_pendiente - 1, 0),
        estado = CASE
            WHEN valor_pendiente - NEW.valor <= 0 THEN 'inactivo'
            ELSE 'activo'
        END
    WHERE prestamo_id = NEW.prestamo_id_fk;
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
