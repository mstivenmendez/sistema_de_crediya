package proyecto.ui;

import javax.swing.JOptionPane;

import proyecto.crud.ClienteCrud;
import proyecto.crud.EmpleadoCrud;
import proyecto.crud.UsuarioCrud;
import proyecto.notificacion.Notificacion;
import proyecto.pagos.CrudPago;
import proyecto.pagos.Pago;
import proyecto.personal.Cliente;
import proyecto.personal.Empleado;
import proyecto.prestamo.CrudPrestamo;
import proyecto.prestamo.Prestamo;
import proyecto.reportes.Reportes;
import proyecto.validaciones.*;
import proyecto.solicitud.Datos;
import proyecto.util.Simular;
import proyecto.util.SesionUsuario;

public class Opcion {
   Menu ingreso = new Menu();
   ValidarNumero numero = new ValidarNumero();
   Datos datos = new Datos();
   ValidacionUsuario validacionUsuario = new ValidacionUsuario();
   Validacion validar = new Validacion();
   ClienteCrud clienteCrud = new ClienteCrud();
   Cliente cliente = new Cliente();
   CrudPrestamo crudPrestamo = new CrudPrestamo();
   EmpleadoCrud empleadoCrud = new EmpleadoCrud();
   Empleado empleado = new Empleado();
   Simular simulacion = new Simular();
   String Cedula = "";
   Prestamo prestamo = new Prestamo();
   UsuarioCrud usuarioCrud = new UsuarioCrud();
   CrudPago crudPago = new CrudPago();
   int id = 0;
   Pago pago = new Pago();
   Reportes reportes = new Reportes();
   Notificacion notificacion = new Notificacion();

   public void VistaSesionOpcion(int valor) {
      switch (valor) {
         case 1:
            var opcion = ingreso.VistaInicio();
            int resultado = numero.solicitarEntero(opcion, 2);
            VistaInicioOpcion(resultado);
            break;
         case 2:
            id = clienteCrud.Guardar(cliente,
                  "INSERT INTO informacion (primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, documento, telefono, fecha_nacimiento, usuario_id_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            break;
         case 0:
            JOptionPane.showMessageDialog(null, "Saliendo del Programa...");
            break;
      }
   }

   public void VistaInicioOpcion(Integer valor) {

      switch (valor) {
         case 1:

            boolean esValido = validacionUsuario.ValidacionUsuarioExistente();

            if (esValido) {

               VistaUsuarioOpcion(numero.solicitarEntero(ingreso.VistaUsuario(), 7));
            }
            break;
         case 2:
            // Bucle para mantener en el menú de administrador
            boolean continuarAdmin = validacionUsuario.ValidacionAdminExistente();
            while (continuarAdmin) {
               Integer opcionAdmin = numero.solicitarEntero(ingreso.VistaAdministrador(), 5);
               if (opcionAdmin == null)
                  continue;
               if (opcionAdmin == 0) {
                  JOptionPane.showMessageDialog(null, "Regresando al menú principal...");
                  continuarAdmin = false;
               } else {
                  VistaAdministradorOpcion(opcionAdmin);
               }
            }
            break;
         case 0:
            JOptionPane.showMessageDialog(null, "Regresando al menú principal...");
            break;
      }
   }

   public void VistaUsuarioOpcion(Integer valor) {
      if (valor == null)
         return;

      // Bucle para mantener en el menú de usuario
      boolean continuarUsuario = true;
      while (continuarUsuario) {
         if (valor == 0) {
            JOptionPane.showMessageDialog(null, "Regresando al menú principal...");
            continuarUsuario = false;
         } else {
            switch (valor) {
               case 1:
                  String datosFormateados = usuarioCrud.obtenerDatosFormateados();
                  JOptionPane.showMessageDialog(null, datosFormateados, "Mi Información",
                        JOptionPane.INFORMATION_MESSAGE);
                  break;
               case 2:
                  crudPrestamo.buscarMisPrestamos();
                  break;
               case 3:
                  JOptionPane.showMessageDialog(null, "N mis pagos");
                  break;
               case 4:
                  JOptionPane.showMessageDialog(null, "Realizar un pago");
                  break;
               case 5:
                  simulacion.ejecutarSimulacion();
                  break;
               case 6:
                  JOptionPane.showMessageDialog(null, "Reportes (notificaciones)");
                  break;
            }
            // Pedir siguiente opción
            valor = numero.solicitarEntero(ingreso.VistaUsuario(), 7);
            if (valor == null)
               continue;
         }
      }
   }

   public void VistaAdministradorOpcion(int valor) {
      switch (valor) {
         case 1:
            // Bucle para gestión de empleados
            boolean continuarEmpleado = true;
            while (continuarEmpleado) {
               Integer opcion = numero.solicitarEntero(ingreso.VistaEmpleado(), 4);
               if (opcion == null)
                  continue;
               if (opcion == 0) {
                  JOptionPane.showMessageDialog(null, "Regresando al menú de administrador...");
                  continuarEmpleado = false;
               } else {
                  VistaAdministradorEmpledaoOpcion(opcion);
               }
            }
            break;
         case 2:
            // Bucle para gestión de clientes
            boolean continuarClientes = true;
            while (continuarClientes) {
               Integer opcion = numero.solicitarEntero(ingreso.VistaGestionClientes(), 3);
               if (opcion == null)
                  continue;
               if (opcion == 0) {
                  JOptionPane.showMessageDialog(null, "Regresando al menú de administrador...");
                  continuarClientes = false;
               } else {
                  VistaGestionClientesOpcion(opcion);
               }
            }
            break;
         case 3:
            // Bucle para gestión de préstamos
            boolean continuarPrestamos = true;
            while (continuarPrestamos) {
               Integer opcion = numero.solicitarEntero(ingreso.VistaGestionPrestamos(), 3);
               if (opcion == null)
                  continue;
               if (opcion == 0) {
                  JOptionPane.showMessageDialog(null, "Regresando al menú de administrador...");
                  continuarPrestamos = false;
               } else {
                  VistaGestionPrestamosOpcion(opcion);
               }
            }
            break;
         case 4:
            // Bucle para gestión de pagos
            boolean continuarPagos = true;
            while (continuarPagos) {
               Integer opcion = numero.solicitarEntero(ingreso.VistaGestionPagos(), 3);
               if (opcion == null)
                  continue;
               if (opcion == 0) {
                  JOptionPane.showMessageDialog(null, "Regresando al menú de administrador...");
                  continuarPagos = false;
               } else {
                  VistaGestionPagosOpcion(opcion);
               }
            }
            break;
         case 5:
            // Bucle para gestión de reportes
            boolean continuarReportes = true;
            while (continuarReportes) {
               Integer opcion = numero.solicitarEntero(ingreso.VistaGestionReportes(), 5);
               if (opcion == null)
                  continue;
               if (opcion == 0) {
                  JOptionPane.showMessageDialog(null, "Regresando al menú de administrador...");
                  continuarReportes = false;
               } else {
                  VistaGestionReportesOpcion(opcion);
               }
            }
            break;
      }
   }

   public void VistaAdministradorEmpledaoOpcion(int valor) {
      switch (valor) {
         case 1:
            empleadoCrud.Guardar(empleado,
                  "INSERT INTO informacion (primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, documento, salario, telefono, fecha_nacimiento, usuario_id_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            break;
         case 2:
            empleadoCrud.generarReporteEmpleados();
            break;
         case 3:
            String cedula3 = validar.ValidarDocumento(datos.Cedula());
            if (validacionUsuario.ValidarCedula(cedula3)) {
               VistaEmpleadoActualizarOpcion(numero.solicitarEntero(ingreso.VistaEmpleadoActualizar(), 4));
               Cedula = cedula3;
            }
            break;
         case 4:
            String cedula4 = validar.ValidarDocumento(datos.Cedula());
            if (validacionUsuario.ValidarCedula(cedula4)) {
               empleadoCrud.Elimnar(empleado, null, cedula4);
            }
            break;
      }
   }

   public void VistaEmpleadoActualizarOpcion(int valor) {
      switch (valor) {
         case 1:
            empleadoCrud.Actualizar(empleado, Cedula, "telefono = ?");
            JOptionPane.showMessageDialog(null, "telefono Actualizado");
            break;
         case 2:
            clienteCrud.ActualizarUsuario(cliente, Cedula, "correo = ?");
            JOptionPane.showMessageDialog(null, "correo actualizado");
         case 3:
            empleadoCrud.Actualizar(empleado, Cedula, "salario = ?");
            JOptionPane.showMessageDialog(null, "salario Actualizado");
         case 4:
            clienteCrud.ActualizarUsuario(cliente, Cedula, "nombre_usuario = ?");
            JOptionPane.showMessageDialog(null, "Usuario actualizado Al");
      }
   }

   public void VistaGestionClientesOpcion(int valor) {
      switch (valor) {
         case 1:
            id = clienteCrud.Guardar(cliente,
                  "INSERT INTO informacion (primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, documento, telefono, fecha_nacimiento, usuario_id_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            break;
         case 2:
            clienteCrud.generarReporteClientes();
            break;
         case 3:
            String cedula1 = validar.ValidarDocumento(datos.Cedula());
            if (validacionUsuario.ValidarCedula(cedula1)) {
               // Obtener el usuario_id del cliente por su documento
               Integer clienteId = validacionUsuario.validarCedulaYObtener(cedula1);
               if (clienteId != null) {
                  crudPrestamo.buscarPrestamosDeCliente(clienteId);
               }
            }
            break;
      }
   }

   public void VistaGestionPrestamosOpcion(int valor) {
      switch (valor) {
         case 1:
            crudPrestamo.Guardar(prestamo,
                  "INSERT INTO prestamo (cliente_usuario_id_fk, empleado_usuario_id_fk, valor, interes, cuotas ) VALUES(?, ?, ?, ?, ?)");
            break;
         case 2:
            // Listar todos los préstamos que ha aprobado este empleado
            int empleadoId = SesionUsuario.getUsuarioId();
            crudPrestamo.buscarPrestamosAprobadosPorEmpleado(empleadoId);
            break;
         case 3:
            String ingreso = datos.NumeroPrestamo();
            crudPago.GenerarCuotas(ingreso);
            break;
      }
   }

   public void VistaGestionPagosOpcion(int valor) {
      switch (valor) {
         case 1:
            String sqlInsertPago = "INSERT INTO pago (prestamo_id_fk, valor, estado, fecha_pago) VALUES (?, ?, ?, NOW())";
            crudPago.Guardar(pago, sqlInsertPago);
            break;
         case 2:
            // Buscar todos los pagos
            crudPago.Buscar("");
            break;
         case 3:
            // Ver préstamos activos con saldo pendiente
            crudPago.BuscarActivos("");
            break;
      }
   }

   public void VistaGestionReportesOpcion(int valor) {
      switch (valor) {
         case 1:
            crudPago.BuscarActivos("");
            break;
         case 2:
            crudPago.BuscarInactivos("");
            break;
         case 3:
            crudPago.BuscarEstado("mora");
            break;
         case 4:
            String sqlInsertNotificacion = "INSERT INTO notificacion (fk_usuario, fk_empleado, nombre, mensaje) VALUES (?, ?, ?, ?)";
            reportes.notificacionPersonalizada(notificacion, sqlInsertNotificacion);
            break;
         case 5:
            reportes.BuscarPrestamosReporte("");
            break;
      }
   }

}
