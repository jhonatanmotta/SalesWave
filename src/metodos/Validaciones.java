package metodos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Validaciones {

    // Método para validar que todos los strings en el arreglo sean diferentes de vacío
    public static boolean validarNoVacios(String mensajeError, String... strings) {
        for (String str : strings) {
            if (str.isEmpty()) {
                JOptionPane.showMessageDialog(null, mensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
                return false; // Al menos uno está vacío, devuelve false
            }
        }
        return true; // Todos son diferentes de vacío, devuelve true
    }

    // Método para validar la cantidad de caracteres en un String
    public static boolean validarCantidadCaracteres(String str, int cantidadMaxima, String MensajeError) {
        if (str.length() <= cantidadMaxima) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, MensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    // Método para validar si un String se puede parsear a un entero
    public static boolean validarParseoAEntero(String str, String MensajeError) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, MensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    // Método para validar si un String se puede parsear a un double
    public static boolean validarParseoADouble(String str, String MensajeError) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, MensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    // Método para validar si un String es un número entero mayor, menor o igual a cero
    public static boolean validarNumeroEnteroNoNegativo(String str, String MensajeError) {
        try {
            int numero = Integer.parseInt(str);
            if (numero > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, MensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Hay un error revisa la cantidad que estas ingresando", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public static boolean valorMenor(double totalPago, double montoPago, String MensajeError) {
        if (montoPago < totalPago) {
            JOptionPane.showMessageDialog(null, MensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public static boolean validarCorreo(String correo) {
        // Expresión regular para verificar el formato de un correo electrónico
        String expresion = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // Compilar la expresión regular
        Pattern patron = Pattern.compile(expresion);

        // Comparar el texto ingresado con la expresión regular
        Matcher matcher = patron.matcher(correo);

        // Devolver true si coincide con el formato de correo electrónico
        return matcher.matches();
    }

    public static boolean validarRangoCaracteres(String texto, int minimo, int maximo, String MensajeError) {
        int longitudTexto = texto.length();
        if (longitudTexto >= minimo && longitudTexto <= maximo) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, MensajeError, "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public static boolean validarContrasena(String contraseña) {
            // Expresión regular para verificar el formato de la contraseña
            String expresion = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!¡?¿*(){}\\[\\]_|<>,:;/-]).{10,}$";

            // Compilar la expresión regular
            Pattern patron = Pattern.compile(expresion);

            // Comparar la contraseña con la expresión regular
            Matcher matcher = patron.matcher(contraseña);

            // Devolver true si coincide con el formato de contraseña
            return matcher.matches();
    }
}
