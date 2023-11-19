package modelo;

public class detalleVenta {
    
    // Atributos de la tabla detalleVenta
    private int idDetalleVenta; // Identificador único (id) del detalle de venta
    private int idEncabezadoVenta_fk; // ID del encabezado de la venta asociada
    private int idProducto_fk; // ID del producto asociado
    private String nombreProducto; // Nombre del producto
    private int cantidad; // Cantidad de productos vendidos
    private double precioUnitario; // Precio unitario del producto
    private double subtotal; // Subtotal de la venta
    private double iva; // Impuesto sobre el producto
    private double totalPagar; // Total a pagar por el producto
    private int estado; // Estado del detalle de venta (activo, inactivo)

    // Constructor vacío
    public detalleVenta() {
    }

    // Constructor con parámetros
    public detalleVenta(int idDetalleVenta, int idEncabezadoVenta_fk, int idProducto_fk, String nombreProducto, int cantidad, double precioUnitario, double subtotal, double iva, double totalPagar, int estado) {
        this.idDetalleVenta = idDetalleVenta; // Asigna el id del detalle de venta
        this.idEncabezadoVenta_fk = idEncabezadoVenta_fk; // Asigna el id del encabezado de venta asociado
        this.idProducto_fk = idProducto_fk; // Asigna el id del producto asociado
        this.nombreProducto = nombreProducto; // Asigna el nombre del producto
        this.cantidad = cantidad; // Asigna la cantidad de productos vendidos
        this.precioUnitario = precioUnitario; // Asigna el precio unitario del producto
        this.subtotal = subtotal; // Asigna el subtotal de la venta
        this.iva = iva; // Asigna el impuesto sobre el producto
        this.totalPagar = totalPagar; // Asigna el total a pagar por el producto
        this.estado = estado; // Asigna el estado del detalle de venta
    }

    // Método para obtener el id del detalle de venta
    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    // Método para establecer el id del detalle de venta
    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    // Método para obtener el id del encabezado de venta asociado
    public int getIdEncabezadoVenta_fk() {
        return idEncabezadoVenta_fk;
    }

    // Método para establecer el id del encabezado de venta asociado
    public void setIdEncabezadoVenta_fk(int idEncabezadoVenta_fk) {
        this.idEncabezadoVenta_fk = idEncabezadoVenta_fk;
    }

    // Método para obtener el id del producto asociado al detalle de venta
    public int getIdProducto_fk() {
        return idProducto_fk;
    }

    // Método para establecer el id del producto asociado al detalle de venta
    public void setIdProducto_fk(int idProducto_fk) {
        this.idProducto_fk = idProducto_fk;
    }

    // Método para obtener el nombre del producto en el detalle de venta
    public String getNombreProducto() {
        return nombreProducto;
    }

    // Método para establecer el nombre del producto en el detalle de venta
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Método para obtener la cantidad de productos vendidos en el detalle de venta
    public int getCantidad() {
        return cantidad;
    }

    // Método para establecer la cantidad de productos vendidos en el detalle de venta
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Método para obtener el precio unitario del producto en el detalle de venta
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // Método para establecer el precio unitario del producto en el detalle de venta
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    // Método para obtener el subtotal del detalle de venta
    public double getSubtotal() {
        return subtotal;
    }

    // Método para establecer el subtotal del detalle de venta
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // Método para obtener el impuesto sobre el producto en el detalle de venta
    public double getIva() {
        return iva;
    }

    // Método para establecer el impuesto sobre el producto en el detalle de venta
    public void setIva(double iva) {
        this.iva = iva;
    }

    // Método para obtener el total a pagar por el producto en el detalle de venta
    public double getTotalPagar() {
        return totalPagar;
    }

    // Método para establecer el total a pagar por el producto en el detalle de venta
    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    // Método para obtener el estado del detalle de venta
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado del detalle de venta
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
}
