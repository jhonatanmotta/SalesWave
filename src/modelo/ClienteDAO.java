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

    /**
     * el metodo registroCliente ejecuta una sentendia SQL con los datos a
     * registrar de la tabla cliente
     *
     * @param client Instancia de la clase Cliente que contiene la informacion
     * que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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

    /**
     * el metodo listaClientes lista los datos de la tabla cliente en un
     * ArrayList para luego mostrarlos en la tabla
     *
     * @param String el valor que se desea buscar coincidencias dentro de la
     * columna cedula o nombre. Para luego mostrar los datos en la tabla
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
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

    /**
     * el metodo modificarCliente ejecuta una senticia SQL para actualizar los
     * datos de la tabla cliente
     *
     * @param cat Instancia de la clase Cliente que contiene la informacion
     * que se desea actualizar
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
    
    /**
     * el metodo validarCedula verifica si una cedula existe o no en la tabla cliente
     *
     * @param String cedula que se desea buscar en la tabla
     * @return boolean true si encuentra la cedula, false si no encuentra dicha cedula
     * en la tabla
     * @throws SQLException exception de SQL
     */
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
    
    /**
     * el metodo estadoCliente cambia la columna estado de la tabla cliente
     *
     * @param int estado valor que se le desea dar a la columna ya sea 1 =
     * activo o 0 = desactiva
     * @param int id para referenciar la fila a la que se desea cambiar el
     * estado
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
