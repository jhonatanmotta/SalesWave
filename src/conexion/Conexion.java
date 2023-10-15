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
    
    //metodo de conexion a la base de datos
    public static Connection conectar() {
        try {
            // Cadena de coneccion se pasa la url, bd, el usuario y contraseña como parametros
            cn = DriverManager.getConnection(url + bd, user, password);
            // Mensaje de exito conectandose a la bd
            System.out.println("La conexion a la base de datos " + bd + " fue existosa");
            return cn;
        } catch (SQLException ex) {
            // Mensaje de error conectandose a la base de datos
            System.out.println("No se conecto a la base de datos");
        }
        return null;
    }
    
    //metodo de validacion de conexion a la base de datos
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

