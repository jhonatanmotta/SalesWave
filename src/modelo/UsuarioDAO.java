package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UsuarioDAO {
    
    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

     /**
     * el metodo LoginAutenticacion se usa para validar que el usuario y contraseña
     * ingresada en el login coincidan con alguna de las almacenadas en la base de datos
     * y ademas pertenezcan a la misma fila, es decir que la contraseña este asociada a dicho usuario
     *
     * @param String usuario que se desea buscar
     * @param String password para verificar si es la misma asociada al usuario
     * @return boolean true si se ha encontrado coincidencias con algun usuario y contraseña
     * false si por el contrario no se ha encontrado ningun usuario o la contraseña asociada a dicho usuario es incorrecta
     * @throws SQLException exception de SQL
     */
    //metodo para validar la existencia de los usuarios en la bd, se solucita el usuario y la contraseña
    public static boolean LoginAutenticacion(String usuario, String password) {
        //consulta a la base de datos
        String sql = "SELECT usuario, password FROM usuario WHERE usuario = ? AND password = ? AND estado = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, password);
            ps.setInt(3, 1);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * el metodo registroUsuario ejecuta una sentendia SQL con los datos a
     * registrar de la tabla usuario
     *
     * @param user Instancia de la clase Usuario que contiene la informacion
     * que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean registroUsuario(Usuario user) {
        String sql = "INSERT INTO usuario (nombre, apellido, correo, usuario, password, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            ps.setString(3, user.getCorreo());
            ps.setString(4, user.getUsuario());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getTelefono());
            ps.setInt(7, 1);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * el metodo listaUsuarios lista los datos de la tabla usuario en un
     * ArrayList para luego mostrarlos en la tabla
     *
     * @param String el valor que se desea buscar coincidencias dentro de la
     * columna nombre o usuario. Para luego mostrar los datos en la tabla
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
    public List listaUsuarios(String valorBusqueda) {
        List<Usuario> listaUsers = new ArrayList();
        String sql = "SELECT * FROM usuario ORDER BY estado DESC";
        String sqlBuscar = "SELECT * FROM usuario WHERE usuario LIKE '%" + valorBusqueda + "%' OR nombre LIKE '%" + valorBusqueda + "%'";
        try {
            if (valorBusqueda.equalsIgnoreCase("")) {
                ps = conexion.prepareStatement(sql);
                retorno = ps.executeQuery();
            } else {
                ps = conexion.prepareStatement(sqlBuscar);
                retorno = ps.executeQuery();
            }
            while (retorno.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(retorno.getInt("idUsuario"));
                user.setNombre(retorno.getString("nombre"));
                user.setApellido(retorno.getString("apellido"));
                user.setUsuario(retorno.getString("usuario"));
                user.setTelefono(retorno.getString("telefono"));
                user.setEstado(retorno.getInt("estado"));
                listaUsers.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listaUsers;
    }

    /**
     * el metodo modificarUsuario ejecuta una senticia SQL para actualizar los
     * datos de la tabla usuario
     *
     * @param user Instancia de la clase Usuario que contiene la informacion
     * que se desea actualizar
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean modificarUsuario(Usuario user) {
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, correo = ?, usuario = ?, telefono = ?, estado = ? WHERE idUsuario = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            ps.setString(3, user.getCorreo());
            ps.setString(4, user.getUsuario());
            ps.setString(5, user.getTelefono());
            ps.setInt(6, 1);
            ps.setInt(7, user.getIdUsuario());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * el metodo buscarCorreo busca un correo dentro de la tabla usuario donde
     * el id sea igual al que pasa por parametro
     *
     * @param int idUsuario, id del usuario del que se desea conocer el correo
     * @return user Instancia de la clase Usuario que contiene el correo si se encontro 
     * de lo contrario la instancia ira vacia
     * @throws SQLException exception de SQL
     */
    public Usuario buscarCorreo (int idUsuario) {
        String sql = "SELECT correo FROM usuario WHERE idUsuario = ?";
        Usuario user = new Usuario();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            retorno = ps.executeQuery();
            if (retorno.next()) {
                user.setCorreo(retorno.getString("correo"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return user;
    }
    
    /**
     * el metodo validarUsuario valida que no exita un usuario en la base de datos con el mismo nombre de usuario
     * antes de crear un nuevo registro, ya que el nombre de usuario es un campo unico en la tabla usuario
     *
     * @param String user a buscar en la tabla
     * @return boolean true si se encontro un usuario ya asociado a ese nombre de usuario,
     * false si no hubo ninguna coincidencia
     * @throws SQLException exception de SQL
     */
    public boolean validarUsuario (String user) {
        String sql = "SELECT usuario FROM usuario WHERE usuario = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, user);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
    
    /**
     * el metodo validarCorreo valida que no exita un usuario en la base de datos con el mismo correo
     * antes de crear un nuevo registro, ya que el correo es un campo unico en la tabla usuario
     *
     * @param String email a buscar en la tabla
     * @return boolean true si se encontro un usuario ya asociado a ese correo,
     * false si no hubo ninguna coincidencia
     * @throws SQLException exception de SQL
     */
    public boolean validarCorreo (String email) {
        String sql = "SELECT correo FROM usuario WHERE correo = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            retorno = ps.executeQuery();
            if (retorno.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
    
    /**
     * el metodo estadoUsuario cambia la columna estado de la tabla usuario
     *
     * @param int estado valor que se le desea dar a la columna ya sea 1 =
     * activo o 0 = desactiva
     * @param int id para referenciar la fila a la que se desea cambiar el
     * estado
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean estadoUsuario(int estado, int id) {
        String sql = "UPDATE usuario SET estado = ? WHERE idUsuario = ?";

        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, estado);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
    /**
     * el metodo modificarPassword se usa para cambiar la contraseña de un usuario
     *
     * @param user Instancia de la clase Usuario que contiene la informacion de la nueva
     * contraseña y el usuario
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean modificarPassword (Usuario user) {
        String sql = "UPDATE usuario SET password = ? WHERE usuario = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getUsuario());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
