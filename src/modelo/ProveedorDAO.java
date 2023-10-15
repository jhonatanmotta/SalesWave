package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class ProveedorDAO {
    
    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

    public ProveedorDAO() {
    }

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
            JOptionPane.showMessageDialog(null, "Error al registrar el proveedor");
            return false;
        }
    }

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
            JOptionPane.showMessageDialog(null, "Error al listar proveedor");
        }
        return listaProv;
    }
    
    public List llenarInput(int idProveedor){
        List<Proveedor> listaProv = new ArrayList();
        String sql = "SELECT * FROM proveedor WHERE idProveedor = ?";
        try{
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
        } catch (SQLException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al llenar los inputs de proveedor");
        }
        return listaProv;
    }
            
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
            JOptionPane.showMessageDialog(null, "Error al modificar el proveedor");
            return false;
        }
    }
    
    public boolean estadoProv (int estado, int id) {
        String sql = "UPDATE proveedor SET estado = ? WHERE idProveedor = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, estado);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al cambiar de estado el proveedor");
            return false;
        }
    }
}
