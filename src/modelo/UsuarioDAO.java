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
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion");
        }
        return false;
    }

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
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario");
            return false;
        }
    }

    public List listaUsuarios(String valorBusqueda) {
        List<Usuario> listaUsers = new ArrayList();
        //consulta a la base de datos
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
            JOptionPane.showMessageDialog(null, "Error al listar usuarios");
        }
        return listaUsers;
    }

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
            JOptionPane.showMessageDialog(null, "Error al modificar el usuario");
            return false;
        }
    }

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
            JOptionPane.showMessageDialog(null, "Error al cambiar de estado el usuario");
            return false;
        }
    }
}
