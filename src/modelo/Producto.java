package modelo;

public class Producto {
//atributos
    private int idProducto;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precioCompra;
    private double precioVenta;
    private int iva;
    private int idCategoria_fk;
    private int idProveedor_fk;
    private int estado;
    private String categoria;
    private String proveedor;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String descripcion, int cantidad, double precioCompra, double precioVenta, int iva, int idCategoria_fk, int idProveedor_fk, int estado, String categoria, String proveedor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.iva = iva;
        this.idCategoria_fk = idCategoria_fk;
        this.idProveedor_fk = idProveedor_fk;
        this.estado = estado;
        this.categoria = categoria;
        this.proveedor = proveedor;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public int getIdCategoria_fk() {
        return idCategoria_fk;
    }

    public void setIdCategoria_fk(int idCategoria_fk) {
        this.idCategoria_fk = idCategoria_fk;
    }

    public int getIdProveedor_fk() {
        return idProveedor_fk;
    }

    public void setIdProveedor_fk(int idProveedor_fk) {
        this.idProveedor_fk = idProveedor_fk;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    
    
}
