package proyecto.pagos;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import proyecto.crud.ClienteCrud;
import proyecto.crud.CrudEntity;
import proyecto.personal.Cliente;
import proyecto.prestamo.CrudPrestamo;
import proyecto.prestamo.Prestamo;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.util.SesionUsuario;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidacionUsuario;
import proyecto.validaciones.ValidarNumero;

public class CrudPago implements CrudEntity<Pago> {

   IngresoDatos conexion = new IngresoDatos();
   Cliente cliente = new Cliente();
   Prestamo prestamo = new Prestamo();
   Datos insertar = new Datos();
   Validacion validar = new Validacion();
   ValidarNumero numero = new ValidarNumero();
   ClienteCrud buscar = new ClienteCrud();
   ValidacionUsuario usuario = new ValidacionUsuario();
   CrudPrestamo crudPrestamo = new CrudPrestamo();

   @Override
   public int Guardar(Pago entity, String sql) {
      try {
         // 1. Solicitar y validar número de préstamo
         String numeroPrestamo = insertar.NumeroPrestamo();

         if (numeroPrestamo == null || numeroPrestamo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Debe ingresar un número de préstamo.",
                  "Campo requerido",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 2. Validar que el préstamo existe y obtener su ID
         String sqlValidar = """
               SELECT prestamo_id, valor_pendiente
               FROM prestamo
               WHERE numero_prestamo = ?
               """;

         final int[] prestamoId = { 0 };
         final double[] valorPendiente = { 0 };

         seleccionar(sqlValidar, rs -> {
            if (rs.next()) {
               prestamoId[0] = rs.getInt("prestamo_id");
               valorPendiente[0] = rs.getDouble("valor_pendiente");
            }
         }, ps -> {
            try {
               ps.setString(1, numeroPrestamo); // ✅ CAMBIO: era 2, ahora es 1
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         });

         // 3. Si no existe el préstamo, salir del método
         if (prestamoId[0] == 0) {
            JOptionPane.showMessageDialog(null,
                  "No existe préstamo con número: " + numeroPrestamo,
                  "Préstamo no encontrado",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 4. Solicitar el valor del pago
         double valorPago = numero.solicitarDouble(insertar.PagarCuota(), 1000000000);

         if (valorPago <= 0) {
            JOptionPane.showMessageDialog(null,
                  "El valor del pago debe ser mayor a 0.",
                  "Valor inválido",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 5. Validar que no exceda el saldo pendiente
         if (valorPago > valorPendiente[0]) {
            JOptionPane.showMessageDialog(null,
                  "El valor ingresado ($" + String.format("%,.2f", valorPago) +
                        ") excede el saldo pendiente ($" + String.format("%,.2f", valorPendiente[0]) + ")",
                  "Valor excedido",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 6. Establecer los valores en la entidad
         entity.setPrestamoIdFk(prestamoId[0]);
         entity.setValor(valorPago);
         entity.setFechaPago(LocalDateTime.now());

         // 7. Ejecutar INSERT con prestamo_id_fk y valor
         int resultado = conexion.ejecutar(sql, ps -> {
            try {
               ps.setInt(1, entity.getPrestamoIdFk()); 
               ps.setDouble(2, entity.getValor()); 

            } catch (SQLException e) {
               JOptionPane.showMessageDialog(null,
                     "Error al configurar parámetros: " + e.getMessage(),
                     "Error",
                     JOptionPane.ERROR_MESSAGE);
               throw new RuntimeException(e);
            }
         });

         if (resultado > 0) {
            JOptionPane.showMessageDialog(null,
                  "✓ Pago registrado exitosamente.\n\n" +
                        "Número Préstamo: " + numeroPrestamo + "\n" +
                        "ID Préstamo: " + prestamoId[0] + "\n" +
                        "Valor: $" + String.format("%,.2f", entity.getValor()),
                  "Éxito",
                  JOptionPane.INFORMATION_MESSAGE);
         } else {
            JOptionPane.showMessageDialog(null,
                  "No se pudo registrar el pago.",
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
         }

         return resultado;

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error durante el registro de pago: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
         return 0;
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null,
               "Error durante el registro de pago: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int Elimnar(Pago entity, String dato, String id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Elimnar'");
   }

   @Override
   public int Actualizar(Pago entity, String id, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
   }

   @Override
   public void Buscar(String dato) {
      String sql = """
            SELECT
               p.pago_id,
               p.valor,
               p.fecha_pago,
               pr.prestamo_id,
               pr.valor AS valor_prestamo,
               i.documento,
               i.primer_nombre,
               i.segundo_nombre,
               i.primer_apellido,
               i.segundo_apellido
            FROM pago p
            INNER JOIN prestamo pr ON p.prestamo_id_fk = pr.prestamo_id
            INNER JOIN informacion i ON pr.cliente_usuario_id_fk = i.usuario_id_fk
            ORDER BY p.fecha_pago DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalPagos = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                    REPORTE DE TODOS LOS PAGOS                  ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               totalPagos += valor;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Pago # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Cédula Cliente        : ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre               : ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido             : ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("ID Pago              : ").append(rs.getInt("pago_id")).append("\n");
               sb.append("Número del Préstamo  : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Valor Pagado         : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", rs.getDouble("valor_prestamo")))
                     .append("\n");
               sb.append("Fecha de Pago        : ").append(rs.getDate("fecha_pago")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron pagos registrados.",
                     "Sin resultados",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de pagos encontrados: ").append(contador).append("\n");
               sb.append("║  Total Pagado: $").append(String.format("%,.2f", totalPagos)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               // Generar archivo
               generarPlan(sb.toString());

               JOptionPane.showMessageDialog(null,
                     "✓ Se encontraron " + contador + " pagos.\n\n" +
                           "Total pagado: $" + String.format("%,.2f", totalPagos) + "\n" +
                           "Archivo generado: Plan de Pagos.txt",
                     "Búsqueda exitosa",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         },
               ps -> {
                  // Sin parámetros, trae todos los pagos
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar pagos: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void BuscarActivos(String dato) {
      String sql = """
            SELECT
               pr.prestamo_id,
               pr.numero_prestamo,
               pr.valor,
               pr.valor_total,
               pr.interes,
               pr.cuotas,
               pr.estado,
               pr.fecha_inicio,
               pr.fecha_limite,
               pr.valor_pendiente,
               i.documento,
               i.primer_nombre,
               i.segundo_nombre,
               i.primer_apellido,
               i.segundo_apellido
            FROM prestamo pr
            INNER JOIN informacion i ON pr.cliente_usuario_id_fk = i.usuario_id_fk
            WHERE pr.estado = 'activo'
            ORDER BY pr.fecha_inicio DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalPrestamos = 0;
            double totalPendiente = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║              REPORTE DE PRÉSTAMOS ACTIVOS                      ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               double valorPendiente = rs.getDouble("valor_pendiente");
               totalPrestamos += valor;
               totalPendiente += valorPendiente;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Préstamo # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Cédula Cliente        : ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre               : ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido             : ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("ID Préstamo          : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Total          : $").append(String.format("%,.2f", rs.getDouble("valor_total")))
                     .append("\n");
               sb.append("Interés              : ").append(rs.getDouble("interes")).append("%\n");
               sb.append("Cuotas               : ").append(rs.getInt("cuotas")).append("\n");
               sb.append("Valor Pendiente      : $").append(String.format("%,.2f", valorPendiente)).append("\n");
               sb.append("Estado               : ").append(rs.getString("estado")).append("\n");
               sb.append("Fecha Inicio         : ").append(rs.getDate("fecha_inicio")).append("\n");
               sb.append("Fecha Límite         : ").append(rs.getDate("fecha_limite")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron préstamos activos.",
                     "Sin resultados",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de préstamos activos: ").append(contador).append("\n");
               sb.append("║  Total Préstamos: $").append(String.format("%,.2f", totalPrestamos)).append("\n");
               sb.append("║  Total Pendiente: $").append(String.format("%,.2f", totalPendiente)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               // Generar archivo
               generarPlan(sb.toString(), "reportePrestamosActivos.txt");

               JOptionPane.showMessageDialog(null,
                     "✓ Se encontraron " + contador + " préstamos activos.\n\n" +
                           "Total préstamos: $" + String.format("%,.2f", totalPrestamos) + "\n" +
                           "Total pendiente: $" + String.format("%,.2f", totalPendiente) + "\n" +
                           "Archivo generado: reportePrestamosActivos.txt",
                     "Búsqueda exitosa",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         },
               ps -> {
                  // Sin parámetros, trae todos los préstamos activos
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar préstamos activos: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void BuscarInactivos(String dato) {
      String sql = """
            SELECT
               pr.prestamo_id,
               pr.numero_prestamo,
               pr.valor,
               pr.valor_total,
               pr.interes,
               pr.cuotas,
               pr.estado,
               pr.fecha_inicio,
               pr.fecha_limite,
               pr.valor_pendiente,
               i.documento,
               i.primer_nombre,
               i.segundo_nombre,
               i.primer_apellido,
               i.segundo_apellido
            FROM prestamo pr
            INNER JOIN informacion i ON pr.cliente_usuario_id_fk = i.usuario_id_fk
            WHERE pr.estado = 'inactivo'
            ORDER BY pr.fecha_inicio DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalPrestamos = 0;
            double totalPendiente = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║             REPORTE DE PRÉSTAMOS INACTIVOS                     ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               double valorPendiente = rs.getDouble("valor_pendiente");
               totalPrestamos += valor;
               totalPendiente += valorPendiente;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Préstamo # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Cédula Cliente        : ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre               : ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido             : ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("ID Préstamo          : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Número Préstamo      : ").append(rs.getString("numero_prestamo")).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Total          : $").append(String.format("%,.2f", rs.getDouble("valor_total")))
                     .append("\n");
               sb.append("Interés              : ").append(rs.getDouble("interes")).append("%\n");
               sb.append("Cuotas               : ").append(rs.getInt("cuotas")).append("\n");
               sb.append("Valor Pendiente      : $").append(String.format("%,.2f", valorPendiente)).append("\n");
               sb.append("Estado               : ").append(rs.getString("estado")).append("\n");
               sb.append("Fecha Inicio         : ").append(rs.getDate("fecha_inicio")).append("\n");
               sb.append("Fecha Límite         : ").append(rs.getDate("fecha_limite")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron préstamos inactivos.",
                     "Sin resultados",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de préstamos inactivos: ").append(contador).append("\n");
               sb.append("║  Total Préstamos: $").append(String.format("%,.2f", totalPrestamos)).append("\n");
               sb.append("║  Total Pendiente: $").append(String.format("%,.2f", totalPendiente)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               // Generar archivo
               generarPlan(sb.toString(), "reportePrestamosInactivos.txt");

               JOptionPane.showMessageDialog(null,
                     "✓ Se encontraron " + contador + " préstamos inactivos.\n\n" +
                           "Total préstamos: $" + String.format("%,.2f", totalPrestamos) + "\n" +
                           "Total pendiente: $" + String.format("%,.2f", totalPendiente) + "\n" +
                           "Archivo generado: reportePrestamosInactivos.txt",
                     "Búsqueda exitosa",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         },
               ps -> {
                  // Sin parámetros, trae todos los préstamos inactivos
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar préstamos inactivos: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void BuscarMisPagos() {
      int usuarioId = SesionUsuario.getUsuarioId();
      
      String sql = """
            SELECT
               p.pago_id,
               p.valor,
               p.fecha_pago,
               pr.prestamo_id,
               pr.numero_prestamo,
               pr.valor AS valor_prestamo,
               i.documento,
               i.primer_nombre,
               i.segundo_nombre,
               i.primer_apellido,
               i.segundo_apellido
            FROM pago p
            INNER JOIN prestamo pr ON p.prestamo_id_fk = pr.prestamo_id
            INNER JOIN informacion i ON pr.cliente_usuario_id_fk = i.usuario_id_fk
            WHERE pr.cliente_usuario_id_fk = ?
            ORDER BY p.fecha_pago DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalPagos = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                      MIS PAGOS REALIZADOS                      ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               totalPagos += valor;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Pago # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("ID Pago              : ").append(rs.getInt("pago_id")).append("\n");
               sb.append("Número Préstamo      : ").append(rs.getString("numero_prestamo")).append("\n");
               sb.append("ID Préstamo          : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Valor Pagado         : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", rs.getDouble("valor_prestamo")))
                     .append("\n");
               sb.append("Fecha de Pago        : ").append(rs.getDate("fecha_pago")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No has realizado pagos en el sistema.",
                     "Sin pagos",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de pagos realizados: ").append(contador).append("\n");
               sb.append("║  Total Pagado: $").append(String.format("%,.2f", totalPagos)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               // Generar archivo
               generarPlan(sb.toString(), "misPagos.txt");
            }
         },
               ps -> {
                  try {
                     ps.setInt(1, usuarioId);
                  } catch (SQLException e) {
                     throw new RuntimeException(e);
                  }
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar tus pagos: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void GenerarCuotas(Prestamo entity, String prestamo) {
      final String numeroPrestamo = prestamo;

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
               i.primer_apellido
            FROM prestamo pr
            INNER JOIN informacion i ON pr.usuario_id_fk = i.usuario_id_fk
            WHERE pr.numero_prestamo = ?
            LIMIT 1
            """;

      try {
         seleccionar(sql, rs -> {
            // si no hay fila, salir del programa
            if (!rs.next()) {
               JOptionPane.showMessageDialog(null,
                     "No existe el préstamo con número: " + numeroPrestamo,
                     "Préstamo no encontrado",
                     JOptionPane.ERROR_MESSAGE);
               System.exit(0);
               return;
            }

            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter tsFmt = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            StringBuilder sb = new StringBuilder();

            int prestamoId = rs.getInt("prestamo_id");
            double valorPrestamo = rs.getDouble("valor_prestamo");
            Double valorCuotaObj = rs.getObject("valor_cuota") != null ? rs.getDouble("valor_cuota") : null;
            int cuotas = rs.getInt("cuotas");
            double valorTotal = rs.getDouble("total");
            double interes = rs.getDouble("interes");
            java.sql.Date sqlDate = rs.getDate("fecha_inicio");
            LocalDate fechaInicio = sqlDate != null ? sqlDate.toLocalDate() : LocalDate.now();
            String cliente = rs.getString("primer_nombre") + " " + rs.getString("segundo_nombre") + " "
                  + rs.getString("primer_apellido");
            String documento = rs.getString("documento");

            // si no hay valor_cuota en la tabla, calcularlo como total/cuotas (fallback)
            double valorCuota = valorCuotaObj != null && valorCuotaObj > 0
                  ? valorCuotaObj
                  : (cuotas > 0 ? Math.round((valorTotal / cuotas) * 100.0) / 100.0 : 0.0);

            double saldoPendiente = valorTotal;

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
            sb.append("Fecha Inicio      : ").append(fechaInicio.format(dateFmt)).append("\n\n");

            for (int i = 1; i <= cuotas; i++) {
               LocalDate fechaCuota = fechaInicio.plusMonths(i);
               sb.append("────────────────────────────────────────────────────────\n");
               sb.append("CUOTA #").append(i).append("\n");
               sb.append("Fecha Pago       : ").append(fechaCuota.format(dateFmt)).append("\n");
               sb.append("Valor a Pagar    : $").append(String.format("%,.2f", valorCuota)).append("\n");
               saldoPendiente -= valorCuota;
               if (saldoPendiente < 0)
                  saldoPendiente = 0;
               sb.append("Saldo Pendiente  : $").append(String.format("%,.2f", saldoPendiente)).append("\n\n");
            }

            sb.append("══════════════════════════════════════════════════════════════\n\n");

            String nombreArchivo = "Plan_Cuotas_" + numeroPrestamo + "_" + LocalDateTime.now().format(tsFmt) + ".txt";
            generarPlan(sb.toString(), nombreArchivo);

         }, ps -> {
            try {
               ps.setString(1, numeroPrestamo);
            } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(null,
                     "Error al configurar parámetros: " + e.getMessage(),
                     "Error",
                     JOptionPane.ERROR_MESSAGE);
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

   public void VerNumeroPrestamo(Prestamo entity, String cedula) {
      conexion.ejecutarProcedimiento("", rs -> {
         try {
            StringBuilder sb = new StringBuilder();
            int contador = 0;
            while (rs.next()) {
               contador++;

               sb.append("╔════════════════════════════════════════════════╗\n");
               sb.append("║  PRÉSTAMO #").append(contador).append("\n");
               sb.append("╠════════════════════════════════════════════════╣\n");
               sb.append("  ID Préstamo      : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("  Valor Préstamo   : $").append(String.format("%,.2f", rs.getDouble("valor"))).append("\n");
               sb.append("  Valor Total      : $").append(String.format("%,.2f", rs.getDouble("valor_total")))
                     .append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());

         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR");
         }
      }, cs -> {
         try {
            cs.setString(1, cedula);
         } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al configurar parámetros", e);
         }
      });
   }

   public void generarPlan(String contenido, String nombreArchivo) {
      try {
         try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write(contenido);
         }
         System.out.println("Archivo generado: " + nombreArchivo);
         JOptionPane.showMessageDialog(null,
               "Archivo generado: " + nombreArchivo,
               "Éxito",
               JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null,
               "Error al generar el archivo: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }

   // conservar método original para compatibilidad (opcional)
   public void generarPlan(String contenido) {
      generarPlan(contenido, "Plan de Pagos.txt");
   }

   // Método auxiliar que debe estar implementado en la clase
   private void seleccionar(String sql,
         ResultSetConsumer rsConsumer,
         PreparedStatementConsumer psConsumer) throws SQLException {
      conexion.seleccionar(sql, 
         rs -> {
            try {
               rsConsumer.accept(rs);
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         },
         ps -> {
            try {
               psConsumer.accept(ps);
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         });
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

}
