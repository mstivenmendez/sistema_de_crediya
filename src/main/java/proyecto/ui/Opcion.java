package proyecto.ui;

import javax.swing.JOptionPane;

import proyecto.crud.ClienteCrud;
import proyecto.crud.EmpleadoCrud;
import proyecto.personal.Cliente;
import proyecto.personal.Empleado;
import proyecto.prestamo.CrudPrestamo;
import proyecto.validaciones.*;
import proyecto.solicitud.Datos;
import proyecto.util.Simular;

public class Opcion {
   Menu ingreso = new Menu();
   ValidarNumero numero = new ValidarNumero();
   Datos datos = new Datos();
   ValidacionUsuario validacionUsuario = new ValidacionUsuario();
   Validacion  validar = new Validacion();
   ClienteCrud clienteCrud = new ClienteCrud();
   Cliente cliente = new Cliente();
   CrudPrestamo crudPrestamo = new CrudPrestamo();
   EmpleadoCrud empleadoCrud = new EmpleadoCrud();
   Empleado empleado = new Empleado();
   Simular simulacion = new Simular();

   public void VistaSesionOpcion(int valor) {
      switch (valor) {
         case 1:
            var opcion = ingreso.VistaInicio();
            int resultado = numero.solicitarEntero(opcion, 2);
            VistaInicioOpcion(resultado);
            break;
         case 2:
            clienteCrud.Guardar(cliente,
                  "INSERT INTO informacion (primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, documento, telefono, fecha_nacimiento, usuario_id_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            break;
         case 0:
            JOptionPane.showMessageDialog(null, "Saliendo del Programa...");
            break;
      }
   }

   public void VistaInicioOpcion(Integer valor) {
      if (valor == null)
         return;

      switch (valor) {
         case 1:

            boolean esValido = validacionUsuario.ValidacionUsuarioExistente();

            if (esValido) {

               VistaUsuarioOpcion(numero.solicitarEntero(ingreso.VistaUsuario(), 7));
            }
            break;
         case 2:
            // Bucle para mantener en el menú de administrador
            boolean continuarAdmin = true;
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
                  // String cc = datos.Cedula();
                  // validar.ValidarDocumento(cc);
                  // clienteCrud.Buscar(cc);
                  break;
               case 2:
                  JOptionPane.showMessageDialog(null, "Consultar mis préstamos");
                  break;
               case 3:
                  JOptionPane.showMessageDialog(null, "Ver mis pagos");
                  break;
               case 4:
                  JOptionPane.showMessageDialog(null, "Realizar un pago");
                  break;
               case 5:
                  JOptionPane.showMessageDialog(null, "Realizar Solicitud De Préstamo");
                  break;
               case 6:
                  simulacion.ejecutarSimulacion();
                  break;
               case 7:
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
            JOptionPane.showMessageDialog(null, "Registrar empleado");
            break;
         case 2:
            String cedula2 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula2)){
               empleadoCrud.Buscar(cedula2);
               JOptionPane.showMessageDialog(null, "Empleados Alistados");
            }
            break;
         case 3:
            String cedula3 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula3)){
               VistaEmpleadoActualizarOpcion(numero.solicitarEntero(ingreso.VistaEmpleadoActualizar(),4));
            }
            break;
         case 4:
            String cedula4 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula4)){
               empleadoCrud.Elimnar(empleado, "documento", cedula4);
            }
            break;
      }
   }

   public void VistaEmpleadoActualizarOpcion(int valor){
      switch (valor) {
         case 1:
            String cedula1 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula1)){
               empleadoCrud.Actualizar(empleado, cedula1, "telefono = ?");
               JOptionPane.showMessageDialog(null, "Empleados Alistados");
            }
            break;
         case 2:
            String cedula2 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula2)){
               clienteCrud.ActualizarUsuario(cliente, cedula2, "correo = ?");
               JOptionPane.showMessageDialog(null, "Empleados Alistados");
            }
         case 3:
            String cedula3 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula3)){
               empleadoCrud.Actualizar(empleado, cedula3, "salario = ?");
               JOptionPane.showMessageDialog(null, "Empleados Alistados");
            }
         case 4:
            String cedula4 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula4)){
               clienteCrud.ActualizarUsuario(cliente, cedula4, "nombre_usuario = ?");
               JOptionPane.showMessageDialog(null, "Empleados Alistados");
            }
      }
   }

   public void VistaGestionClientesOpcion(int valor) {
      switch (valor) {
         case 1:
            break;
         case 2:
            String cedula = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula)){
               clienteCrud.Buscar(cedula);
               JOptionPane.showMessageDialog(null, "Cliente Alistados");
            }
         case 3:
            String cedula2 = validar.ValidarDocumento(datos.Cedula());
            if(validacionUsuario.ValidarCedula(cedula2)){
               clienteCrud.Buscar(cedula2);
               JOptionPane.showMessageDialog(null, "Prestamos  alistados");
            }
      }
   }

   public void VistaGestionPrestamosOpcion(int valor) {
      switch (valor) {
         case 1:
            JOptionPane.showMessageDialog(null, "Crear préstamo");
            break;
         case 2:
            JOptionPane.showMessageDialog(null, "Listar préstamos");
            break;
         case 3:
            JOptionPane.showMessageDialog(null, "Cambiar estado del préstamo");
            break;
      }
   }

   public void VistaGestionPagosOpcion(int valor) {
      switch (valor) {
         case 1:
            JOptionPane.showMessageDialog(null, "Registrar pago");
            break;
         case 2:
            JOptionPane.showMessageDialog(null, "Consultar historial de pagos");
            break;
         case 3:
            JOptionPane.showMessageDialog(null, "Ver saldo pendiente");
            break;
      }
   }

   public void VistaGestionReportesOpcion(int valor) {
      switch (valor) {
         case 1:
            JOptionPane.showMessageDialog(null, "Préstamos activos");
            break;
         case 2:
            JOptionPane.showMessageDialog(null, "Préstamos vencidos");
            break;
         case 3:
            JOptionPane.showMessageDialog(null, "Clientes morosos");
            break;
         case 4:
            JOptionPane.showMessageDialog(null, "Generar reporte automático");
            break;
         case 5:
            JOptionPane.showMessageDialog(null, "Historial completo de préstamos");
            break;
      }
   }

}
