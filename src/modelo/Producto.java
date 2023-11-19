package modelo;

public class Producto {

    // Atributos de la clase Producto
    private int idProducto; // Identificador único (id) del producto
    private String nombre; // Nombre del producto
    private String descripcion; // Descripción del producto
    private int cantidad; // Cantidad del producto
    private double precioCompra; // Precio de compra del producto
    private double precioVenta; // Precio de venta del producto
    private int iva; // Impuesto sobre el producto
    private int idCategoria_fk; // ID de la categoría a la que pertenece el producto
    private int idProveedor_fk; // ID del proveedor que distrubuye el producto
    private int estado; // Estado del producto (activo, inactivo)
    private String categoria; // Nombre de la categoría del producto
    private String proveedor; // Nombre del proveedor del producto

    // Constructor vacío
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(int idProducto, String nombre, String descripcion, int cantidad, double precioCompra, double precioVenta, int iva, int idCategoria_fk, int idProveedor_fk, int estado, String categoria, String proveedor) {
        this.idProducto = idProducto; // Asigna el id del producto
        this.nombre = nombre; // Asigna el nombre del producto
        this.descripcion = descripcion; // Asigna la descripción del producto
        this.cantidad = cantidad; // Asigna la cantidad del producto
        this.precioCompra = precioCompra; // Asigna el precio de compra del producto
        this.precioVenta = precioVenta; // Asigna el precio de venta del producto
        this.iva = iva; // Asigna el impuesto sobre el producto
        this.idCategoria_fk = idCategoria_fk; // Asigna el id de la categoría del producto
        this.idProveedor_fk = idProveedor_fk; // Asigna el id del proveedor que distrubuye el producto
        this.estado = estado; // Asigna el estado del producto
        this.categoria = categoria; // Asigna la categoría del producto
        this.proveedor = proveedor; // Asigna el proveedor del producto
    }

    // Método para obtener el id del producto
    public int getIdProducto() {
        return idProducto;
    }

    // Método para establecer el id del producto
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    // Método para obtener el nombre del producto
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre del producto
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener la descripción del producto
    public String getDescripcion() {
        return descripcion;
    }

    // Método para establecer la descripción del producto
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método para obtener la cantidad del producto
    public int getCantidad() {
        return cantidad;
    }

    // Método para establecer la cantidad del producto
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Método para obtener el precio de compra del producto
    public double getPrecioCompra() {
        return precioCompra;
    }

    // Método para establecer el precio de compra del producto
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    // Método para obtener el precio de venta del producto
    public double getPrecioVenta() {
        return precioVenta;
    }

    // Método para establecer el precio de venta del producto
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    // Método para obtener el impuesto sobre el producto
    public int getIva() {
        return iva;
    }

    // Método para establecer el impuesto sobre el producto
    public void setIva(int iva) {
        this.iva = iva;
    }

    // Método para obtener el ID de la categoría a la que pertenece el producto
    public int getIdCategoria_fk() {
        return idCategoria_fk;
    }

    // Método para establecer el ID de la categoría a la que pertenece el producto
    public void setIdCategoria_fk(int idCategoria_fk) {
        this.idCategoria_fk = idCategoria_fk;
    }

    // Método para obtener el ID del proveedor que distrubuye el producto
    public int getIdProveedor_fk() {
        return idProveedor_fk;
    }

    // Método para establecer el ID del proveedor que distrubuye el producto
    public void setIdProveedor_fk(int idProveedor_fk) {
        this.idProveedor_fk = idProveedor_fk;
    }

    // Método para obtener el estado del producto
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado del producto
    public void setEstado(int estado) {
        this.estado = estado;
    }

    // Método para obtener el nombre de la categoría del producto
    public String getCategoria() {
        return categoria;
    }

    // Método para establecer el nombre de la categoría del producto
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Método para obtener el proveedor del producto
    public String getProveedor() {
        return proveedor;
    }

    // Método para establecer el proveedor del producto
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}
