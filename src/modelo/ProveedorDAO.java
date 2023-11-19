package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

    /**
     * el metodo registroProv ejecuta una sentendia SQL con los datos a
     * registrar de la tabla proveedor
     *
     * @param prov Instancia de la clase Proveedor que contiene la informacion
     * que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean registroProv(Proveedor prov) {
        String sql = "INSERT INTO proveedor (nombre, apellido, direccion, telefono, estado) VALUES (?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, prov.getNombre());
            ps.setString(2, prov.getApellido());
            ps.setString(3, prov.getDireccion());
            ps.setString(4, prov.getTelefono());
            ps.setInt(5, 1);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * el metodo listaProv lista los datos de la tabla proveedor en un ArrayList
     * para luego mostrarlos en la tabla
     *
     * @param String el valor que se desea buscar coincidencias dentro de la
     * columna nombre. Para luego mostrar los datos en la tabla
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
    public List listaProv(String valorBusqueda) {
        List<Proveedor> listaProv = new ArrayList();
        //consulta a la base de datos
        String sql = "SELECT * FROM proveedor ORDER BY estado DESC";
        String sqlBuscar = "SELECT * FROM proveedor WHERE nombre LIKE '%" + valorBusqueda + "%'";
        try {
            if (valorBusqueda.equalsIgnoreCase("")) {
                ps = conexion.prepareStatement(sql);
                retorno = ps.executeQuery();
            } else {
                ps = conexion.prepareStatement(sqlBuscar);
                retorno = ps.executeQuery();
            }
            while (retorno.next()) {
                Proveedor prov = new Proveedor();
                prov.setIdProveedor(retorno.getInt("idProveedor"));
                prov.setNombre(retorno.getString("nombre"));
                prov.setApellido(retorno.getString("apellido"));
                prov.setDireccion(retorno.getString("direccion"));
                prov.setTelefono(retorno.getString("telefono"));
                prov.setEstado(retorno.getInt("estado"));
                listaProv.add(prov);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listaProv;
    }

    /**
     * el metodo llenarInput lista los datos de la tabla proveedor en un ArrayList
     * donde la informacion obtenida es referenciada por el id del proveedor, y se usa para 
     * llenar los inputs de la vista del proveedor 
     *
     * @param int idProveedor del cual se desea saber su informacion asociada
     * @return List una lista de los datos recuperados de la consulta SQL refeenciando 
     * la informacion por el id
     * @throws SQLException exception de SQL
     */
    public List llenarInput(int idProveedor) {
        List<Proveedor> listaProv = new ArrayList();
        String sql = "SELECT * FROM proveedor WHERE idProveedor = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProveedor);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                Proveedor prov = new Proveedor();
                prov.setIdProveedor(retorno.getInt("idProveedor"));
                prov.setNombre(retorno.getString("nombre"));
                prov.setApellido(retorno.getString("apellido"));
                prov.setDireccion(retorno.getString("direccion"));
                prov.setTelefono(retorno.getString("telefono"));
                prov.setEstado(retorno.getInt("estado"));
                listaProv.add(prov);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listaProv;
    }

    /**
     * el metodo modificarProv ejecuta una senticia SQL para actualizar los
     * datos de la tabla proveedor
     *
     * @param prov Instancia de la clase Proveedor que contiene la informacion
     * que se desea actualizar
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean modificarProv(Proveedor prov) {
        String sql = "UPDATE proveedor SET nombre = ?, apellido = ?, direccion = ?, telefono = ?, estado = ? WHERE idProveedor = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, prov.getNombre());
            ps.setString(2, prov.getApellido());
            ps.setString(3, prov.getDireccion());
            ps.setString(4, prov.getTelefono());
            ps.setInt(5, 1);
            ps.setInt(6, prov.getIdProveedor());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * el metodo estadoProv cambia la columna estado de la tabla proveedor
     *
     * @param int estado valor que se le desea dar a la columna ya sea 1 =
     * activo o 0 = desactiva
     * @param int id para referenciar la fila a la que se desea cambiar el
     * estado
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean estadoProv(int estado, int id) {
        String sql = "UPDATE proveedor SET estado = ? WHERE idProveedor = ?";
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
