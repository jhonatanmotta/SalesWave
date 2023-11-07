package modelo;

public class detalleVenta {
    
    //atributos detalleVenta
    private int idDetalleVenta;
    private int idEncabezadoVenta_fk;
    private int idProducto_fk;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double iva;
    private double totalPagar;
    private int estado;

    public detalleVenta() {
    }

    public detalleVenta(int idDetalleVenta, int idEncabezadoVenta_fk, int idProducto_fk, String nombreProducto, int cantidad, double precioUnitario, double subtotal, double iva, double totalPagar, int estado) {
        this.idDetalleVenta = idDetalleVenta;
        this.idEncabezadoVenta_fk = idEncabezadoVenta_fk;
        this.idProducto_fk = idProducto_fk;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.iva = iva;
        this.totalPagar = totalPagar;
        this.estado = estado;
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getIdEncabezadoVenta_fk() {
        return idEncabezadoVenta_fk;
    }

    public void setIdEncabezadoVenta_fk(int idEncabezadoVenta_fk) {
        this.idEncabezadoVenta_fk = idEncabezadoVenta_fk;
    }

    public int getIdProducto_fk() {
        return idProducto_fk;
    }

    public void setIdProducto_fk(int idProducto_fk) {
        this.idProducto_fk = idProducto_fk;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
}
