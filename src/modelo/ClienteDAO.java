package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDAO {

    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

    public ClienteDAO() {
    }

    public boolean registroCliente(Cliente client) {
        String sql = "INSERT INTO cliente (nombre, apellido, cedula, telefono, estado) VALUES (?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, client.getNombre());
            ps.setString(2, client.getApellido());
            ps.setString(3, client.getCedula());
            ps.setString(4, client.getTelefono());
            ps.setInt(5, 1);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public List listaClientes(String valorBusqueda) {
        List<Cliente> listaClients = new ArrayList();
        //consulta a la base de datos
        String sql = "SELECT * FROM cliente ORDER BY estado DESC";
        String sqlBuscar = "SELECT * FROM cliente WHERE cedula LIKE '%" + valorBusqueda + "%' OR nombre LIKE '%" + valorBusqueda + "%'";
        try {
            if (valorBusqueda.equalsIgnoreCase("")) {
                ps = conexion.prepareStatement(sql);
                retorno = ps.executeQuery();
            } else {
                ps = conexion.prepareStatement(sqlBuscar);
                retorno = ps.executeQuery();
            }
            while (retorno.next()) {
                Cliente client = new Cliente();
                client.setIdCliente(retorno.getInt("idCliente"));
                client.setNombre(retorno.getString("nombre"));
                client.setApellido(retorno.getString("apellido"));
                client.setCedula(retorno.getString("cedula"));
                client.setTelefono(retorno.getString("telefono"));
                client.setEstado(retorno.getInt("estado"));
                listaClients.add(client);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listaClients;
    }

    public boolean modificarCliente(Cliente client) {
        String sql = "UPDATE cliente SET nombre = ?, apellido = ?, cedula = ?, telefono = ?, estado = ? WHERE idCliente = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, client.getNombre());
            ps.setString(2, client.getApellido());
            ps.setString(3, client.getCedula());
            ps.setString(4, client.getTelefono());
            ps.setInt(5, 1);
            ps.setInt(6, client.getIdCliente());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
    public boolean validarCedula (String cedula) {
        String sql = "SELECT cedula FROM cliente WHERE cedula = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cedula);
            retorno = ps.executeQuery();
            if (retorno.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
    
    public boolean estadoCliente (int estado, int id) {
        String sql = "UPDATE cliente SET estado = ? WHERE idCliente = ?";
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
}
