package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CategoriaDAO {
    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

    public boolean registroCategoria(Categoria cat) {
        String sql = "INSERT INTO categoria (nombre, estado) VALUES (?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cat.getNombre());
            ps.setInt(2, 1);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al registrar la categoria");
            return false;
        }
    }

    public List listaCategorias(String valorBusqueda) {
        List<Categoria> listaCats = new ArrayList();
        //consulta a la base de datos
        String sql = "SELECT * FROM categoria ORDER BY estado DESC";
        String sqlBuscar = "SELECT * FROM categoria WHERE nombre LIKE '%" + valorBusqueda + "%'";
        try {
            if (valorBusqueda.equalsIgnoreCase("")) {
                ps = conexion.prepareStatement(sql);
                retorno = ps.executeQuery();
            } else {
                ps = conexion.prepareStatement(sqlBuscar);
                retorno = ps.executeQuery();
            }
            while (retorno.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(retorno.getInt("idCategoria"));
                cat.setNombre(retorno.getString("nombre"));
                cat.setEstado(retorno.getInt("estado"));
                listaCats.add(cat);
            }
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al listar categorias");
        }
        return listaCats;
    }
    
    public boolean modificarCategoria(Categoria cat) {
        String sql = "UPDATE categoria SET nombre = ?, estado = ? WHERE idCategoria = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cat.getNombre());
            ps.setInt(2, 1);
            ps.setInt(3, cat.getIdCategoria());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al modificar la categoria");
            return false;
        }
    }

    public boolean estadoCategoria(int estado, int id) {
        String sql = "UPDATE categoria SET estado = ? WHERE idCategoria = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, estado);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al cambiar de estado la categoria");
            return false;
        }
    }
}


