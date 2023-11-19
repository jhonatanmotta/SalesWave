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

    /**
     * el metodo informacionEmpresa muestra la informacion almacenada
     * en la tabla empresa
     *
     * @return emp Instancia de la clase Empresa que contiene
     * los atributos que hacen referencia a cada columna de la tabla empresa
     * @throws SQLException exception de SQL
     */
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
        }
        return emp;
    }
    
    /**
     * el metodo modificarEmpresa modifica la informacion de la empresa
     *
     * @param emp Instancia de la clase Empresa que contiene la nueva informacion
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            return false;
        }
    }
}
