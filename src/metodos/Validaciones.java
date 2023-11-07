package metodos;

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
            JOptionPane.showMessageDialog(null, "", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

//    public static void main(String[] args) {
//        // Ejemplo de uso de los métodos
//        validarCantidadCaracteres("123456", 5, "Debe tener como maximo 5 carecteres");
//        validarParseoAEntero("1", "No posee el formato de numero");
//        validarNumeroEnteroNoNegativo("10", "El numero no debe ser menor o igual a cero");
//    }
}
