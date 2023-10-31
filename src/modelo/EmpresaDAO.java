package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EmpresaDAO {

    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

    public EmpresaDAO() {
    }

    public Empresa informacionEmpresa() {
        String sql = "SELECT * FROM empresa WHERE idEmpresa = ?";
        Empresa emp = new Empresa();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, 1); 
            retorno = ps.executeQuery();
            while (retorno.next()) {
                emp.setIdEmpresa(retorno.getInt("idEmpresa"));
                emp.setNombre(retorno.getString("nombre"));
                emp.setNit(retorno.getString("nit"));
                emp.setTelefono(retorno.getString("telefono"));
                emp.setDireccion(retorno.getString("direccion"));
            }
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al registrar el cliente");
        }
        return emp;
    }
    
    public boolean modificarEmpresa(Empresa emp) {
        String sql = "UPDATE empresa SET nombre = ?, nit = ?, telefono = ?, direccion = ? WHERE idEmpresa = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getNit());
            ps.setString(3, emp.getTelefono());
            ps.setString(4, emp.getDireccion());
            ps.setInt(5, 1);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de la empresa");
            return false;
        }
    }
}
