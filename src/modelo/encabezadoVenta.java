package modelo;

public class encabezadoVenta {

    // Atributos de la tabla encabezadoVenta
    private int idEncabezadoVenta; // Identificador único (id) del encabezado de venta
    private int idCliente_fk; // ID del cliente asociado a la venta
    private int idEmpresa_fk; // ID de la empresa asociada a la venta
    private int idUsuario_fk; // ID del usuario asociado a la venta
    private double valorPagar; // Valor total a pagar por la venta
    private String fechaVenta; // Fecha de la venta
    private int estado; // Estado del encabezado de venta (activo, inactivo)
    private String nombreCliente; // Nombre del cliente asociado a la venta

    // Constructor vacío
    public encabezadoVenta() {
    }

    // Constructor con parámetros
    public encabezadoVenta(int idEncabezadoVenta, int idCliente_fk, int idEmpresa_fk, int idUsuario_fk, double valorPagar, String fechaVenta, int estado, String nombreCliente) {
        this.idEncabezadoVenta = idEncabezadoVenta; // Asigna el id del encabezado de venta
        this.idCliente_fk = idCliente_fk; // Asigna el id del cliente asociado a la venta
        this.idEmpresa_fk = idEmpresa_fk; // Asigna el id de la empresa asociada a la venta
        this.idUsuario_fk = idUsuario_fk; // Asigna el id del usuario asociado a la venta
        this.valorPagar = valorPagar; // Asigna el valor total a pagar por la venta
        this.fechaVenta = fechaVenta; // Asigna la fecha de la venta
        this.estado = estado; // Asigna el estado del encabezado de venta
        this.nombreCliente = nombreCliente; // Asigna el nombre del cliente asociado a la venta
    }

    // Método para obtener el id del encabezado de venta
    public int getIdEncabezadoVenta() {
        return idEncabezadoVenta;
    }

    // Método para establecer el id del encabezado de venta
    public void setIdEncabezadoVenta(int idEncabezadoVenta) {
        this.idEncabezadoVenta = idEncabezadoVenta;
    }

    // Método para obtener el id del cliente asociado a la venta
    public int getIdCliente_fk() {
        return idCliente_fk;
    }

    // Método para establecer el id del cliente asociado a la venta
    public void setIdCliente_fk(int idCliente_fk) {
        this.idCliente_fk = idCliente_fk;
    }

    // Método para obtener el id de la empresa asociada a la venta
    public int getIdEmpresa_fk() {
        return idEmpresa_fk;
    }

    // Método para establecer el id de la empresa asociada a la venta
    public void setIdEmpresa_fk(int idEmpresa_fk) {
        this.idEmpresa_fk = idEmpresa_fk;
    }

    // Método para obtener el id del usuario asociado a la venta
    public int getIdUsuario_fk() {
        return idUsuario_fk;
    }

    // Método para establecer el id del usuario asociado a la venta
    public void setIdUsuario_fk(int idUsuario_fk) {
        this.idUsuario_fk = idUsuario_fk;
    }

    // Método para obtener el valor total a pagar por la venta
    public double getValorPagar() {
        return valorPagar;
    }

    // Método para establecer el valor total a pagar por la venta
    public void setValorPagar(double valorPagar) {
        this.valorPagar = valorPagar;
    }

    // Método para obtener la fecha de la venta
    public String getFechaVenta() {
        return fechaVenta;
    }

    // Método para establecer la fecha de la venta
    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    // Método para obtener el estado del encabezado de venta
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado del encabezado de venta
    public void setEstado(int estado) {
        this.estado = estado;
    }

    // Método para obtener el nombre del cliente asociado a la venta
    public String getNombreCliente() {
        return nombreCliente;
    }

    // Método para establecer el nombre del cliente asociado a la venta
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
