package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    
    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;
    
    /**
     * el metodo registroCategoria ejecuta una sentendia SQL con los datos a
     * registrar de la tabla categoria
     *
     * @param cat Instancia de la clase Categoria que contiene la informacion
     * que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            return false;
        }
    }

    /**
     * el metodo listaCategorias lista los datos de la tabla categoria en un
     * ArrayList para luego mostrarlos en la tabla
     *
     * @param String el valor que se desea buscar coincidencias dentro de la
     * columna nombre. Para luego mostrar los datos en la tabla
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
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
        }
        return listaCats;
    }
    
    /**
     * el metodo modificarCategoria ejecuta una senticia SQL para actualizar los
     * datos de la tabla categoria
     *
     * @param cat Instancia de la clase Categoria que contiene la informacion
     * que se desea actualizar
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            return false;
        }
    }

    /**
     * el metodo estadoCategoria cambia la columna estado de la tabla categoria
     *
     * @param int estado valor que se le desea dar a la columna ya sea 1 =
     * activo o 0 = desactiva
     * @param int id para referenciar la fila a la que se desea cambiar el
     * estado
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            return false;
        }
    }
}


