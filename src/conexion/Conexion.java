package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    // Se definen las variables de acceso a la base de datos
    private static final String bd = "bdsistemaventas";
    private static final String url = "jdbc:mysql://localhost/";
    private static final String user = "root";
    private static final String password = "";
    private static Connection cn;
    
    // Método para la conexión a la base de datos
    public static Connection conectar() {
        try {
            // Se realiza la conexión utilizando la URL, la base de datos, el usuario y la contraseña
            cn = DriverManager.getConnection(url + bd, user, password);
            // Mensaje de éxito al conectarse a la base de datos
            System.out.println("La conexión a la base de datos " + bd + " fue exitosa");
            return cn;
        } catch (SQLException ex) {
            // Mensaje de error al no poder conectarse a la base de datos
            System.out.println("No se pudo conectar a la base de datos");
        }
        return null;
    }
    
    // Método para validar la conexión a la base de datos
    public static boolean validarConexion() {
        try {
            cn =  DriverManager.getConnection(url + bd, user, password);
            return true;
        } catch (SQLException e) {
            System.out.println("Error" + e);
        }
        return false;
    }
}


