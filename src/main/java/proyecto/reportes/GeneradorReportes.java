package proyecto.reportes;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import proyecto.crud.ClienteCrud;
import proyecto.crud.EmpleadoCrud;
import proyecto.excepciones.DatosVaciosException;
import proyecto.excepciones.ValidacionException;
import proyecto.notificacion.Notificacion;
import proyecto.personal.Cliente;
import proyecto.personal.Empleado;
import proyecto.prestamo.Estado;
import proyecto.prestamo.Prestamo;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidacionUsuario;
import proyecto.validaciones.ValidarNumero;

public class GeneradorReportes {
    IngresoDatos conexion = new IngresoDatos();
    Cliente cliente = new Cliente();
    Prestamo prestamo = new Prestamo();
    Datos insertar = new Datos();
    Validacion validar = new Validacion();
    ValidarNumero numero = new ValidarNumero();
    ClienteCrud buscar = new ClienteCrud();
    ValidacionUsuario usuario = new ValidacionUsuario();
    Notificacion notificacion = new Notificacion();
    EmpleadoCrud empleado = new EmpleadoCrud();

    private void buscarPrestamosPorUsuarioId(int usuarioId) {

        conexion.ejecutarProcedimiento(
                "sp_prestamos_por_cliente(?)",

                // Lambda 1: Procesar ResultSet
                rs -> {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("═══════════════════════════════════════════════════\n");
                        sb.append("              MIS PRÉSTAMOS\n");
                        sb.append("═══════════════════════════════════════════════════\n\n");

                        boolean hayResultados = false;
                        int contador = 0;
                        double totalValor = 0;
                        double totalPendiente = 0;

                        while (rs.next()) {
                            hayResultados = true;
                            contador++;

                            double valor = rs.getDouble("valor");
                            double valorTotal = rs.getDouble("valor_total");
                            double valorPendiente = rs.getDouble("valor_pendiente");

                            totalValor += valor;
                            totalPendiente += valorPendiente;

                            sb.append("╔════════════════════════════════════════════════╗\n");
                            sb.append("║  PRÉSTAMO #").append(contador).append("\n");
                            sb.append("╠════════════════════════════════════════════════╣\n");
                            sb.append("  ID Préstamo      : ").append(rs.getInt("prestamo_id")).append("\n");
                            sb.append("  Número Préstamo  : ").append(rs.getString("numero_prestamo")).append("\n");
                            sb.append("  Valor Préstamo   : $").append(String.format("%,.2f", valor)).append("\n");
                            sb.append("  Valor Total      : $").append(String.format("%,.2f", valorTotal)).append("\n");
                            sb.append("  Valor Pendiente  : $").append(String.format("%,.2f", valorPendiente))
                                    .append("\n");
                            sb.append("  Interés          : ").append(rs.getDouble("interes")).append("%\n");
                            sb.append("  Cuotas           : ").append(rs.getInt("cuotas")).append("\n");
                            sb.append("  Fecha Inicio     : ").append(rs.getDate("fecha_inicio")).append("\n");
                            sb.append("  Fecha Límite     : ").append(rs.getDate("fecha_limite")).append("\n");
                            sb.append("  Estado           : ").append(rs.getString("estado")).append("\n");
                            sb.append("╚════════════════════════════════════════════════╝\n\n");
                        }

                        if (!hayResultados) {
                            JOptionPane.showMessageDialog(null,
                                    "Este cliente no tiene préstamos registrados.",
                                    "Sin Préstamos",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // Resumen final
                            sb.append("═══════════════════════════════════════════════════\n");
                            sb.append("                     RESUMEN\n");
                            sb.append("═══════════════════════════════════════════════════\n");
                            sb.append("  Total de Préstamos    : ").append(contador).append("\n");
                            sb.append("  Suma Total Prestada   : $").append(String.format("%,.2f", totalValor))
                                    .append("\n");
                            sb.append("  Total Pendiente       : $").append(String.format("%,.2f", totalPendiente))
                                    .append("\n");
                            sb.append("═══════════════════════════════════════════════════");

                            // Generar archivo .txt
                            generarArchivoMisPrestamos(sb.toString());
                            JOptionPane.showMessageDialog(null,
                                    "Archivo generado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Error al procesar préstamos: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                },

                // Lambda 2: Configurar parámetros del CallableStatement
                cs -> {
                    try {
                        cs.setInt(1, usuarioId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error al configurar parámetros", e);
                    }
                });
    }
}
