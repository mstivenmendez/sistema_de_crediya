package proyecto.gestorpagos;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import proyecto.crud.CrudEntity;
import proyecto.exception.PrestamoNoEncontrado;
import proyecto.prestamo.Prestamo;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;

public class GestorPagos extends Prestamo implements CrudEntity<pagos>{

    IngresoDatos consultar = new IngresoDatos();
    PrestamoNoEncontrado fallo = new PrestamoNoEncontrado();
    Datos insertar = new Datos();

    public boolean BuscarPrestamoId(String numero) throws PrestamoNoEncontrado {

        String sql = """
                SELECT DISTINCT
                   prestamo_id,
                   numero_prestamo,
                FROM prestamo
                WHERE numero_prestamo = ?
                """;

        try {
            seleccionar(sql, rs -> {
                String credito = rs.getString("numero_prestamo");
                if (credito == numero) {
                    JOptionPane.showMessageDialog(null, "EL PRESTAMO EXISTE");
                }

            }, ps -> {

                try {
                    ps.setString(1, numero);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public void PagosCreado(String sql) throws PrestamoNoEncontrado {

        String credito = insertar.NumeroPrestamo();

        if (BuscarPrestamoId(credito)) {
            try {
                seleccionar(sql, rs -> {

                }, ps -> {

                });

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        String s = """
                """;
    }

    public void GenerarHistorial() {
        String sql = """
                SELECT
                   pr.prestamo_id,
                   pr.numero_prestamo,
                   pr.valor        AS valor_prestamo,
                   pr.valor_cuota,
                   pr.cuotas,
                   pr.valor_total  AS total,
                   pr.interes,
                   pr.fecha_inicio,
                   i.documento,
                   i.primer_nombre,
                   i.segundo_nombre,
                   i.primer_apellido,
                   p.moton,
                   p,fecha_pago
                FROM prestamo pr
                INNER JOIN informacion i ON pr.cliente_usuario_id_fk = i.usuario_id_fk
                INNER JOIN pago p ON pr.prestamo_id = p.prestamo_id_fk
                WHERE pr.numero_prestamo = ?
                LIMIT 1
                """;

        try {
            seleccionar(sql, rs -> {

                try {
                    StringBuilder sb = new StringBuilder();
                    while (rs.next()) {
                        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        int prestamoId = rs.getInt("prestamo_id");
                        String numeroPrestamo = rs.getString("numero_prestamo");
                        double valorPrestamo = rs.getDouble("valor_prestamo");
                        Double valorCuotaObj = rs.getObject("valor_cuota") != null ? rs.getDouble("valor_cuota") : null;
                        int cuotas = rs.getInt("cuotas");
                        double valorTotal = rs.getDouble("total");
                        double interes = rs.getDouble("interes");
                        java.sql.Date sqlDate = rs.getDate("fecha_inicio");
                        String cliente = rs.getString("primer_nombre") + " " + rs.getString("segundo_nombre") + " "
                                + rs.getString("primer_apellido");
                        String documento = rs.getString("documento");
                        java.sql.Date sqlDate2 = rs.getDate("fecha_inicio");
                        LocalDate fechaPago = sqlDate2 != null ? sqlDate.toLocalDate() : LocalDate.now();

                        double valorCuota = valorCuotaObj != null && valorCuotaObj > 0
                                ? valorCuotaObj
                                : (cuotas > 0 ? Math.round((valorTotal / cuotas) * 100.0) / 100.0 : 0.0);

                        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
                        sb.append("║                       PLAN DE CUOTAS                         ║\n");
                        sb.append("╚══════════════════════════════════════════════════════════════╝\n\n");
                        sb.append("Préstamo ID       : ").append(prestamoId).append("\n");
                        sb.append("Número Préstamo   : ").append(numeroPrestamo).append("\n");
                        sb.append("Cliente           : ").append(cliente).append("\n");
                        sb.append("Documento Cliente : ").append(documento).append("\n");
                        sb.append("Valor Préstamo    : $").append(String.format("%,.2f", valorPrestamo)).append("\n");
                        sb.append("Valor Total       : $").append(String.format("%,.2f", valorTotal)).append("\n");
                        sb.append("Interés           : ").append(String.format("%,.2f", interes)).append("%\n");
                        sb.append("Cuotas            : ").append(cuotas).append("\n");
                        sb.append("Valor Cuota       : $").append(String.format("%,.2f", valorCuota)).append("\n");
                        sb.append("Fecha Pago      : ").append(fechaPago.format(dateFmt)).append("\n\n");
                        sb.append("══════════════════════════════════════════════════════════════\n\n");

                    }

                    String nombreArchivo = "Plan_Cuotas.txt";

                    FileWriter writer = new FileWriter(nombreArchivo);
                    writer.write(sb.toString());
                    writer.close();

                    System.out.println("Archivo generado: " + nombreArchivo);
                    JOptionPane.showMessageDialog(null,
                            "✓ Plan de cuotas generado exitosamente.\n" +
                                    "Archivo: " + nombreArchivo,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error al generar el archivo: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error al procesar los datos: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }, ps -> {

            /////
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al consultar préstamo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Generarpagonumero(String numeroPrestamo) {
        String sql = """
                SELECT
                   pr.prestamo_id,
                   pr.numero_prestamo,
                   pr.valor        AS valor_prestamo,
                   pr.valor_cuota,
                   pr.cuotas,
                   pr.valor_total  AS total,
                   pr.interes,
                   pr.fecha_inicio,
                   i.documento,
                   i.primer_nombre,
                   i.segundo_nombre,
                   i.primer_apellido,
                   p.moton,
                   p,fecha_pago
                FROM prestamo pr
                INNER JOIN informacion i ON pr.cliente_usuario_id_fk = i.usuario_id_fk
                INNER JOIN pago p ON pr.prestamo_id = p.prestamo_id_fk
                WHERE pr.numero_prestamo = ?
                LIMIT 1
                """;

        try {
            seleccionar(sql, rs -> {

                try {
                    StringBuilder sb = new StringBuilder();
                    while (rs.next()) {
                        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        int prestamoId = rs.getInt("prestamo_id");
                        double valorPrestamo = rs.getDouble("valor_prestamo");
                        Double valorCuotaObj = rs.getObject("valor_cuota") != null ? rs.getDouble("valor_cuota") : null;
                        int cuotas = rs.getInt("cuotas");
                        double valorTotal = rs.getDouble("total");
                        double interes = rs.getDouble("interes");
                        java.sql.Date sqlDate = rs.getDate("fecha_inicio");
                        String cliente = rs.getString("primer_nombre") + " " + rs.getString("segundo_nombre") + " "
                                + rs.getString("primer_apellido");
                        String documento = rs.getString("documento");
                        java.sql.Date sqlDate2 = rs.getDate("fecha_inicio");
                        LocalDate fechaPago = sqlDate2 != null ? sqlDate.toLocalDate() : LocalDate.now();

                        double valorCuota = valorCuotaObj != null && valorCuotaObj > 0
                                ? valorCuotaObj
                                : (cuotas > 0 ? Math.round((valorTotal / cuotas) * 100.0) / 100.0 : 0.0);

                        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
                        sb.append("║                       PLAN DE CUOTAS                         ║\n");
                        sb.append("╚══════════════════════════════════════════════════════════════╝\n\n");
                        sb.append("Préstamo ID       : ").append(prestamoId).append("\n");
                        sb.append("Número Préstamo   : ").append(numeroPrestamo).append("\n");
                        sb.append("Cliente           : ").append(cliente).append("\n");
                        sb.append("Documento Cliente : ").append(documento).append("\n");
                        sb.append("Valor Préstamo    : $").append(String.format("%,.2f", valorPrestamo)).append("\n");
                        sb.append("Valor Total       : $").append(String.format("%,.2f", valorTotal)).append("\n");
                        sb.append("Interés           : ").append(String.format("%,.2f", interes)).append("%\n");
                        sb.append("Cuotas            : ").append(cuotas).append("\n");
                        sb.append("Valor Cuota       : $").append(String.format("%,.2f", valorCuota)).append("\n");
                        sb.append("Fecha Pago      : ").append(fechaPago.format(dateFmt)).append("\n\n");
                        sb.append("══════════════════════════════════════════════════════════════\n\n");

                    }

                    String nombreArchivo = "Plan_Cuotas.txt";

                    FileWriter writer = new FileWriter(nombreArchivo);
                    writer.write(sb.toString());
                    writer.close();

                    System.out.println("Archivo generado: " + nombreArchivo);
                    JOptionPane.showMessageDialog(null,
                            "✓ Plan de cuotas generado exitosamente.\n" +
                                    "Archivo: " + nombreArchivo,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error al generar el archivo: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error al procesar los datos: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }, ps -> {
                try {
                    ps.setString(1, numeroPrestamo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al consultar préstamo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    

    /*
     * implementa una nueva clase llamada GestorPagos que cumpla con los siguientes
     * requerimientos:
     * 
     * Registrar un nuevo pago asociado a un préstamo existente:
     * Solicitar el ID del préstamo, el monto pagado y la fecha del pago.
     * Verificar que el préstamo exista en la base de datos antes de registrar el
     * pago.
     * Si el préstamo no existe, lanzar una excepción personalizada
     * PrestamoNoEncontradoException.
     * Actualizar el saldo restante del préstamo en la base de datos.
     * Si el pago cubre la totalidad del monto pendiente, actualizar el estado del
     * préstamo a "CANCELADO".
     * Mostrar el historial de pagos de un préstamo en consola:
     * Consultar y listar los pagos asociados a un préstamo en orden cronológico.
     * Usar colecciones y Stream API para filtrar pagos realizados dentro de un
     * rango de fechas (por ejemplo, pagos del último mes).
     * Manejar las excepciones con bloques try-catch, incluyendo mensajes
     * descriptivos y un bloque finally que cierre correctamente las conexiones
     * JDBC.
     * Script .sql con la tabla pagos:
     * 
     * CREATE TABLE IF NOT EXISTS `crediya_db`.`prestamo` (
     * `prestamo_id` INT NOT NULL AUTO_INCREMENT,
     * `cliente_usuario_id_fk` INT NOT NULL,
     * `empleado_usuario_id_fk` INT NOT NULL,
     * `valor` DECIMAL(12,2) NOT NULL,
     * `valor_total` DECIMAL(15,2) NULL DEFAULT NULL,
     * `valor_pendiente` DECIMAL(15,2) NULL DEFAULT NULL,
     * `interes` DECIMAL(10,2) NOT NULL,
     * `cuotas` INT NOT NULL,
     * `valor_cuota` DECIMAL(15,2) NOT NULL,
     * `cuota_pendiente` INT NULL DEFAULT NULL,
     * `fecha_inicio` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     * `fecha_limite` DATETIME NOT NULL,
     * `estado` ENUM('activo', 'inactivo', 'mora') NOT NULL DEFAULT 'activo',
     * `numero_prestamo` VARCHAR(20) NOT NULL,
     * PRIMARY KEY (`prestamo_id`),
     * UNIQUE INDEX `prestamo_id_UNIQUE` (`prestamo_id` ASC) VISIBLE,
     * UNIQUE INDEX `numero_prestamo` (`numero_prestamo` ASC) VISIBLE,
     * INDEX `fk_prestamo_usuario1_idx` (`empleado_usuario_id_fk` ASC) VISIBLE,
     * INDEX `fk_prestamo_usuario2_idx` (`cliente_usuario_id_fk` ASC) VISIBLE,
     * CONSTRAINT `fk_prestamo_usuario1`
     * FOREIGN KEY (`empleado_usuario_id_fk`)
     * REFERENCES `crediya_db`.`usuario` (`usuario_id`),
     * CONSTRAINT `fk_prestamo_usuario2`
     * FOREIGN KEY (`cliente_usuario_id_fk`)
     * REFERENCES `crediya_db`.`usuario` (`usuario_id`))
     * ENGINE = InnoDB
     * AUTO_INCREMENT = 13
     * DEFAULT CHARACTER SET = utf8mb4
     * COLLATE = utf8mb4_0900_ai_ci;
     * 
     * 
     * 
     * CREATE TABLE pagos (
     * 
     * id INT AUTO_INCREMENT PRIMARY KEY,
     * 
     * prestamo_id_fk INT,
     * 
     * monto DECIMAL(10,2),
     * 
     * fecha_pago DATE,
     * 
     * FOREIGN KEY (prestamo_id_fk) REFERENCES prestamos(prestamo_id)
     * 
     * );
     */

    private void seleccionar(String sql,
            ResultSetConsumer rsConsumer,
            PreparedStatementConsumer psConsumer) throws SQLException {
        // Implementación del método que ejecuta la consulta
        // y llama a los consumers apropiados
    }

    // Interfaces funcionales para los lambdas
    @FunctionalInterface
    interface ResultSetConsumer {
        void accept(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    interface PreparedStatementConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }

    @Override
    public int Guardar(pagos entity, String dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Guardar'");
    }

    @Override
    public int Elimnar(pagos entity, String dato, String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Elimnar'");
    }

    @Override
    public int Actualizar(pagos entity, String id, String dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
    }

    @Override
    public void Buscar(String dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Buscar'");
    }
}
