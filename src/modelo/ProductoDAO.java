package modelo;

import com.mysql.cj.xdevapi.Statement;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO {

    //se crea un objeto de la clase Conexion y se llama a su metodo static conectar()
    private static Connection conexion = Conexion.conectar();
    //se inicializa la variable PreparedStatement ps que se utiliza para ejecutar consultas SQL
    private static PreparedStatement ps = null;
    //se declara la variable ResultSet retorno que se utiliza para recuperar datos de una bd después de ejecutar una consulta SQL
    private static ResultSet retorno;

    /**
     * el metodo registroProducto ejecuta una sentendia SQL con los datos a
     * registrar de la tabla producto
     *
     * @param prod Instancia de la clase Producto que contiene la informacion
     * que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            retornoRegistro = false;
        }
        return retornoRegistro;
    }

    /**
     * el metodo listaProductos lista los datos de la tabla producto en un
     * ArrayList para luego mostrarlos en la tabla
     *
     * @param String el valor que se desea buscar coincidencias dentro de una
     * columna. Para luego mostrar los datos en la tabla
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
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
        }
        return listaProd;
    }

    /**
     * el metodo modificarProducto ejecuta una senticia SQL para actualizar los
     * datos de la tabla Producto
     *
     * @param prod Instancia de la clase Producto que contiene la informacion
     * que se desea actualizar
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            return false;
        }
    }

    /**
     * el metodo estadoProducto cambia la columna estado de la tabla producto
     *
     * @param int estado valor que se le desea dar a la columna ya sea 1 =
     * activo o 0 = desactiva
     * @param int id para referenciar la fila a la que se desea cambiar el
     * estado
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
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
            return false;
        }
    }

    /**
     * el metodo listarComboCategoria lista el contenido de la tabla categoria
     * para luego mostrarlo en un comboBox
     *
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
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

    /**
     * el metodo listarComboProveedor lista el contenido de la tabla proveedor
     * para luego mostrarlo en un comboBox
     *
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
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

    /**
     * el metodo listarComboProducto lista el contenido de la tabla producto
     * para luego mostrarlo en un comboBox
     *
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
    public List listarComboProducto() {
        List<Producto> producto = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE estado = ? ORDER BY nombre";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, 1);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                Producto prod = new Producto();
                prod.setIdProducto(retorno.getInt("idProducto"));
                prod.setNombre(retorno.getString("nombre"));
                prod.setDescripcion(retorno.getString("descripcion"));
                prod.setCantidad(retorno.getInt("cantidad"));
                prod.setPrecioCompra(retorno.getDouble("precioCompra"));
                prod.setPrecioVenta(retorno.getDouble("precioVenta"));
                prod.setIva(retorno.getInt("iva"));
                prod.setEstado(retorno.getInt("estado"));
                producto.add(prod);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return producto;
    }

    /**
     * el metodo listarComboCliente lista el contenido de la tabla cliente para
     * luego mostrarlo en un comboBox
     *
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
    public List listarComboCliente() {
        List<Cliente> cliente = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE estado = ? ORDER BY nombre";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, 1);
            retorno = ps.executeQuery();
            while (retorno.next()) {
                Cliente client = new Cliente();
                client.setIdCliente(retorno.getInt("idCliente"));
                client.setNombre(retorno.getString("nombre"));
                client.setApellido(retorno.getString("apellido"));
                client.setCedula(retorno.getString("cedula"));
                client.setTelefono(retorno.getString("telefono"));
                client.setEstado(retorno.getInt("estado"));
                cliente.add(client);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return cliente;
    }

    /**
     * el metodo actualizarCantidad, actuliza la cantidad de un producto
     * especifico
     *
     * @param int cant, nueva cantidad del producto
     * @param int id, id del producto del que se desea cambiar su cantidad
     * @return boolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean actualizarCantidad(int cant, int id) {
        String sql = "UPDATE producto SET cantidad = ? WHERE idProducto = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, cant);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * el metodo buscarProducto, busca un producto en especifico y se trae toda
     * su informacion ademas del idCategoria, nombre Categoria, idProveedor,
     * nombre Proveedor de sus respectivas tablas haciendo uso del INNER JOIN
     *
     * @param int idProducto para refereciar de que producto se desea conocer su
     * informacion
     * @return Producto, devuelve un objeto de la clase Producto que contiene la
     * informacion de un producto
     * @throws SQLException exception de SQL
     */
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

    /**
     * el metodo buscarCantidadProd, busca el id, nombre y cantidad de un
     * producto especifico
     *
     * @param int idProducto para refereciar de que producto se desea conocer su
     * informacion
     * @return Producto devuelve un objeto de la clase Producto que contiene la
     * informacion de un producto
     * @throws SQLException exception de SQL
     */
    public Producto buscarCantidadProd(int idProducto) {
        Producto prod = new Producto();
        String sql = "SELECT idProducto, nombre, cantidad FROM producto WHERE idProducto = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);
            retorno = ps.executeQuery();
            if (retorno.next()) {
                prod.setIdProducto(retorno.getInt("idProducto"));
                prod.setNombre(retorno.getString("nombre"));
                prod.setCantidad(retorno.getInt("cantidad"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return prod;
    }

    /**
     * el metodo registroEncabezado ejecuta una sentendia SQL con los datos a
     * registrar de la tabla encabezadoVenta
     *
     * @param encabezado Instancia de la clase encabezadoVenta que contiene la
     * informacion que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean registroEncabezado(encabezadoVenta encabezado) {
        boolean retornoRegistro = false;
        String sql = "INSERT INTO encabezadoventa (idCliente_fk, idEmpresa_fk, idUsuario_fk, valorPagar, fechaVenta, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, encabezado.getIdCliente_fk());
            ps.setInt(2, encabezado.getIdEmpresa_fk());
            ps.setInt(3, encabezado.getIdUsuario_fk());
            ps.setDouble(4, encabezado.getValorPagar());
            ps.setString(5, encabezado.getFechaVenta());
            ps.setInt(6, 1);
            ps.execute();
            retornoRegistro = true;
        } catch (SQLException e) {
            System.out.println(e);
            retornoRegistro = false;
        }
        return retornoRegistro;
    }

    /**
     * el metodo obtenerIdEncabezado busca el ultimo id insertado en la tabla de
     * encabezadoVenta
     *
     * @return int id, ultimo id regsitrado en dicha tabla
     * @throws SQLException exception de SQL
     */
    public int obtenerIdEncabezado() {
        int id = 0;
        String sql = "SELECT max(idEncabezadoVenta) as id FROM encabezadoventa";
        try {
            ps = conexion.prepareStatement(sql);
            retorno = ps.executeQuery();
            if (retorno.next()) {
                id = (retorno.getInt("id"));

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return id;
    }

    /**
     * el metodo buscarUsuario busca un usuario en la base de datos y obtiene su
     * id
     *
     * @return int id del usuario encontrado en la base de datos
     * @throws SQLException exception de SQL
     */
    public int buscarUsuario(String usuario) {
        int id = -1;
        String sql = "SELECT idUsuario FROM usuario WHERE usuario = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario);
            retorno = ps.executeQuery();
            if (retorno.next()) {
                id = (retorno.getInt("idUsuario"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return id;
    }

    /**
     * el metodo registroDetalle ejecuta una sentendia SQL con los datos a
     * registrar de la tabla detalleVenta
     *
     * @param detalle Instancia de la clase detalleVenta que contiene la
     * informacion que se desea guardar
     * @return booolean true si la consulta se ejecuta, false si ocurre un error
     * al ejecutar la consulta
     * @throws SQLException exception de SQL
     */
    public boolean registroDetalle(detalleVenta detalle) {
        boolean retornoRegistro = false;
        String sql = "INSERT INTO detalleVenta (idEncabezadoVenta_fk, idProducto_fk, cantidad, precioUnitario, subtotal, iva, totalPagar, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, detalle.getIdEncabezadoVenta_fk());
            ps.setInt(2, detalle.getIdProducto_fk());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioUnitario());
            ps.setDouble(5, detalle.getSubtotal());
            ps.setDouble(6, detalle.getIva());
            ps.setDouble(7, detalle.getTotalPagar());
            ps.setInt(8, 1);
            ps.execute();
            retornoRegistro = true;
        } catch (SQLException e) {
            System.out.println(e);
            retornoRegistro = false;
        }
        return retornoRegistro;
    }

    /**
     * el metodo buscarCliente busca un cliente en la base de datos por su
     * cedula
     *
     * @param String cedulaCliente
     * @return booolean true si se encuentra un cliente con dicha cedula, false
     * si no se encuentra ningun usuario
     * @throws SQLException exception de SQL
     */
    public boolean buscarCliente(String cedulaCliente) {
        String sql = "SELECT cedula FROM cliente WHERE cedula = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cedulaCliente);
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
     * el metodo buscarFecha busca entre un rango de fechas ventas
     *
     * @param String fechaInicio, fecha desde la cual se va buscar
     * @param String fechaFinal, fecha hasta la cual se va buscar
     * @return booolean true si se encuentran ventas en dichos rangos de fechas,
     * false si no se encuentran ventas
     * @throws SQLException exception de SQL
     */
    public boolean buscarFecha(String fechaInicio, String fechaFinal) {
        String sql = "SELECT encabezado.*, cliente.nombre as nombreCliente, cliente.apellido as apellidoCliente, "
                + "cliente.cedula as cedulaCliente FROM encabezadoventa encabezado INNER JOIN cliente "
                + "ON encabezado.idCliente_fk = cliente.idCliente WHERE fechaVenta BETWEEN ? AND ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
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
     * el metodo buscarClienteFecha busca entre un rango de fechas compras
     * realizadas por un cliente
     *
     * @param String cedulaCliente, del cual se desea conocer sus compras
     * @param String fechaInicio, fecha desde la cual se va buscar
     * @param String fechaFinal, fecha hasta la cual se va buscar
     * @return booolean true si se encuentran compras de un cliente en dichos
     * rangos de fechas, false si no se encuentran compras
     * @throws SQLException exception de SQL
     */
    public boolean buscarClienteFecha(String fechaInicio, String fechaFinal, String cedulaCliente) {
        String sql = "SELECT encabezado.*, cliente.nombre as nombreCliente, cliente.apellido as apellidoCliente, "
                + "cliente.cedula as cedulaCliente FROM encabezadoventa encabezado INNER JOIN cliente "
                + "ON encabezado.idCliente_fk = cliente.idCliente WHERE fechaVenta BETWEEN ? AND ? AND cliente.cedula = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
            ps.setString(3, cedulaCliente);
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
     * el metodo listaVentas lista los datos de la tabla encabezadoVenta en un
     * ArrayList para luego mostrarlos en la tabla de ventas
     *
     * @param String cedulaCliente, del cual se desea conocer sus compras
     * @param String fechaInicio, fecha desde la cual se va buscar
     * @param String fechaFinal, fecha hasta la cual se va buscar
     * @return List una lista de los datos recuperados de la consulta SQL
     * @throws SQLException exception de SQL
     */
    public List listaVentas(String fechaInicio, String fechaFinal, String cedula) {
        // ArrayList que contendra los datos recuperados de la base de datos
        List<encabezadoVenta> listaVentas = new ArrayList();
        // cadena de consulta para listar todas las ventas
        String sql = "SELECT encabezado.*, cliente.nombre as nombreCliente, cliente.apellido as apellidoCliente "
                + "FROM encabezadoventa encabezado INNER JOIN cliente ON encabezado.idCliente_fk = cliente.idCliente";
        // cadena de consulta para listar las ventas entre cierto rango de fechas
        String sqlBusquedaFecha = "SELECT encabezado.*, cliente.nombre as nombreCliente, cliente.apellido as apellidoCliente "
                + "FROM encabezadoventa encabezado INNER JOIN cliente ON encabezado.idCliente_fk = cliente.idCliente "
                + "WHERE fechaVenta BETWEEN ? AND ?";
        // cadena de consulta para listar todas las compras de un cliente
        String sqlBusquedaCliente = "SELECT encabezado.*, cliente.nombre as nombreCliente, cliente.apellido as apellidoCliente, "
                + "cliente.cedula as cedulaCliente FROM encabezadoventa encabezado INNER JOIN cliente "
                + "ON encabezado.idCliente_fk = cliente.idCliente WHERE cliente.cedula = ?";
        // cadena de consulta para listar todas las compras de un cliente en un rango de fechas
        String sqlBusquedaClienteFecha = "SELECT encabezado.*, cliente.nombre as nombreCliente, cliente.apellido as apellidoCliente, "
                + "cliente.cedula as cedulaCliente FROM encabezadoventa encabezado INNER JOIN cliente "
                + "ON encabezado.idCliente_fk = cliente.idCliente WHERE fechaVenta BETWEEN ? AND ? AND cliente.cedula = ?";
        try {
            // si la fecha de inico, final y cedula son vacios se muestran todas las ventas
            if (fechaInicio.isEmpty() && fechaFinal.isEmpty() && cedula.isEmpty()) {
                ps = conexion.prepareStatement(sql);
            // si la fecha de inicio y final son vacias, semuestran todas las compras de un cliente
            } else if (fechaInicio.isEmpty() && fechaFinal.isEmpty() && !cedula.isEmpty()) {
                ps = conexion.prepareStatement(sqlBusquedaCliente);
                ps.setString(1, cedula);
            // si la cedula del cliente es vacia, se muestras todas ventas en el rango de fechas especificado
            } else if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty() && cedula.isEmpty()) {
                ps = conexion.prepareStatement(sqlBusquedaFecha);
                ps.setString(1, fechaInicio);
                ps.setString(2, fechaFinal);
            // si ninguna variable es vacia se muestran todas las compras de un cliente entre el rango de fecha especdificado
            } else if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty() && !cedula.isEmpty()) {
                ps = conexion.prepareStatement(sqlBusquedaClienteFecha);
                ps.setString(1, fechaInicio);
                ps.setString(2, fechaFinal);
                ps.setString(3, cedula);
            }
            retorno = ps.executeQuery();
            // si se ha obtenido informacion de la consulta
            while (retorno.next()) {
                encabezadoVenta encabezado = new encabezadoVenta();
                // se establece la informacion de las ventas en el objeto encabezado
                encabezado.setIdEncabezadoVenta(retorno.getInt("idEncabezadoVenta"));
                encabezado.setValorPagar(retorno.getDouble("valorPagar"));
                encabezado.setFechaVenta(retorno.getString("fechaVenta"));
                encabezado.setNombreCliente(retorno.getString("nombreCliente") + " " + retorno.getString("apellidoCliente"));
                // se añade el objeto al ArrayList
                listaVentas.add(encabezado);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        // retorna el ArrayList
        return listaVentas;
    }
}
