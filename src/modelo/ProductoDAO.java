package modelo;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO {

    //se trae la conexion a la bd, se declara el PreparedStatement y el ResultSet
    private static Connection conexion = Conexion.conectar();
    private static PreparedStatement ps = null;
    private static ResultSet retorno;

    public boolean registroProducto(Producto prod) {
        boolean retornoRegistro = false;
        String sql = "INSERT INTO producto (nombre, descripcion, cantidad, precioCompra, precioVenta, iva, idCategoria_fk, idProveedor_fk, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, prod.getNombre());
            ps.setString(2, prod.getDescripcion());
            ps.setInt(3, prod.getCantidad());
            ps.setDouble(4, prod.getPrecioCompra());
            ps.setDouble(5, prod.getPrecioVenta());
            ps.setDouble(6, prod.getIva());
            ps.setInt(7, prod.getIdCategoria_fk());
            ps.setInt(8, prod.getIdProveedor_fk());
            ps.setInt(9, 1);
            ps.execute();
            retornoRegistro = true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al registrar el producto");
            retornoRegistro = false;
        }
        return retornoRegistro;
    }

    public List listaProductos(String valorBusqueda) {
        List<Producto> listaProd = new ArrayList();
        //consulta a la base de datos
        String sql = "SELECT * FROM producto ORDER BY estado DESC";
        String sqlBuscar = "SELECT * FROM producto WHERE nombre LIKE '%" + valorBusqueda + "%'";
        try {
            if (valorBusqueda.equalsIgnoreCase("")) {
                ps = conexion.prepareStatement(sql);
                retorno = ps.executeQuery();
            } else {
                ps = conexion.prepareStatement(sqlBuscar);
                retorno = ps.executeQuery();
            }
            while (retorno.next()) {
                Producto prod = new Producto();
                prod.setIdProducto(retorno.getInt("idProducto"));
                prod.setNombre(retorno.getString("nombre"));
                prod.setDescripcion(retorno.getString("descripcion"));
                prod.setCantidad(retorno.getInt("cantidad"));
                prod.setEstado(retorno.getInt("estado"));
                listaProd.add(prod);
            }
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al listar productos");
        }
        return listaProd;
    }

    public boolean modificarProducto(Producto prod) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precioCompra = ?, precioVenta = ?, iva = ?,"
                + " estado = ?, idCategoria_fk = ?, idProveedor_fk = ? WHERE idProducto = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, prod.getNombre());
            ps.setString(2, prod.getDescripcion());
            ps.setDouble(3, prod.getPrecioCompra());
            ps.setDouble(4, prod.getPrecioVenta());
            ps.setInt(5, prod.getIva());
            ps.setInt(6, 1);
            ps.setInt(7, prod.getIdCategoria_fk());
            ps.setInt(8, prod.getIdProveedor_fk());
            ps.setInt(9, prod.getIdProducto());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al modificar el producto");
            return false;
        }
    }

    public boolean estadoProducto(int estado, int id) {
        String sql = "UPDATE producto SET estado = ? WHERE idProducto = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, estado);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al cambiar de estado del producto");
            return false;
        }
    }

    public List listarComboCategoria() {
        List<Categoria> categoria = new ArrayList<>();
        String sql = "SELECT * FROM categoria WHERE estado = ? ORDER BY nombre";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, 1);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(retorno.getInt("idCategoria"));
                cat.setNombre(retorno.getString("nombre"));
                cat.setEstado(retorno.getInt("estado"));
                categoria.add(cat);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return categoria;
    }

    public List listarComboProveedor() {
        List<Proveedor> proveedor = new ArrayList<>();
        String sql = "SELECT * FROM proveedor WHERE estado = ? ORDER BY nombre";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, 1);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                Proveedor prov = new Proveedor();
                prov.setIdProveedor(retorno.getInt("idProveedor"));
                prov.setNombre(retorno.getString("nombre"));
                prov.setApellido(retorno.getString("apellido"));
                prov.setDireccion(retorno.getString("direccion"));
                prov.setTelefono(retorno.getString("telefono"));
                prov.setEstado(retorno.getInt("estado"));
                proveedor.add(prov);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return proveedor;
    }

    public Producto buscarProducto(int idProducto) {
        String sql = "SELECT prod.*, cat.idCategoria, cat.nombre as categoria, prov.idProveedor, prov.nombre "
                + "as proveedor FROM producto prod INNER JOIN categoria cat ON prod.idCategoria_fk = cat.idCategoria "
                + "INNER JOIN proveedor prov ON prod.idProveedor_fk = prov.idProveedor WHERE prod.idProducto = ?";
        Producto prod = new Producto();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);
            retorno = ps.executeQuery();
            if (retorno.next()) {
//                prod.setIdProducto(retorno.getInt("idProducto"));
                prod.setNombre(retorno.getString("nombre"));
                prod.setDescripcion(retorno.getString("descripcion"));
                prod.setCantidad(retorno.getInt("cantidad"));
                prod.setPrecioCompra(retorno.getDouble("precioCompra"));
                prod.setPrecioVenta(retorno.getDouble("precioVenta"));
                prod.setIva(retorno.getInt("iva"));
                prod.setIdCategoria_fk(retorno.getInt("idCategoria_fk"));
                prod.setIdProveedor_fk(retorno.getInt("idProveedor_fk"));
                prod.setCategoria(retorno.getString("categoria"));
                prod.setProveedor(retorno.getString("proveedor"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return prod;
    }
}
