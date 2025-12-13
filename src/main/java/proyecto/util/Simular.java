package proyecto.util;

import proyecto.validaciones.ValidarNumero;
import proyecto.solicitud.Datos;
import javax.swing.JOptionPane;

public class Simular {

    ValidarNumero validarnumero = new ValidarNumero();
    Datos datos = new Datos();

    public void SimularPrestamo(double valor, double interes, int cuotas) {

        // Convertir la tasa de interés anual a mensual
        double tasaMensual = (interes / 100) / 12;

        // Fórmula de amortización para calcular el pago mensual
        double pagoMensual = (valor * tasaMensual * Math.pow(1 + tasaMensual, cuotas))
                / (Math.pow(1 + tasaMensual, cuotas) - 1);

        // Cálculo del monto total a pagar
        double valorTotal = pagoMensual * cuotas;

        // Mostrar los resultados
        StringBuilder txt = new StringBuilder();
        txt.append("----- Simulación de Préstamo -----\n");
        txt.append("Monto del Préstamo: $").append(String.format("%.2f", valor)).append("\n");
        txt.append("Interés del Préstamo: %").append(String.format("%.2f", interes)).append("\n");
        txt.append("Monto Total a Pagar: $").append(String.format("%.2f", valorTotal)).append("\n");
        txt.append("Número de Cuotas: ").append(cuotas).append("\n");
        txt.append("Pago Mensual: $").append(String.format("%.2f", pagoMensual)).append("\n");

        JOptionPane.showMessageDialog(null, txt.toString());
    }

    public void ejecutarSimulacion() {

        // Solicitar el valor, el interés y las cuotas utilizando los métodos de
        // validación
        Double valor = validarnumero.solicitarDouble(datos.valorPrestamo(), 1_000_000_000_000.0);
        Double interes = validarnumero.solicitarDouble(datos.valorInteres(), 5);
        int cuotas = validarnumero.solicitarEntero(datos.valorCuotas(), 240);

        // Llamar al método SimularPrestamo con los datos ingresados
        SimularPrestamo(valor, interes, cuotas);
    }

}
