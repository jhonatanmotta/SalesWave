package modelo;

public class encabezadoVenta {
   
     //atributos encabezadoVenta
    private int idEncabezadoVenta;
    private int idCliente_fk;
    private int idEmpresa_fk;
    private int idUsuario_fk;
    private double valorPagar;
    private String fechaVenta;
    private int estado;

    public encabezadoVenta() {
    }

    public encabezadoVenta(int idEncabezadoVenta, int idCliente_fk, int idEmpresa_fk, int idUsuario_fk, double valorPagar, String fechaVenta, int estado) {
        this.idEncabezadoVenta = idEncabezadoVenta;
        this.idCliente_fk = idCliente_fk;
        this.idEmpresa_fk = idEmpresa_fk;
        this.idUsuario_fk = idUsuario_fk;
        this.valorPagar = valorPagar;
        this.fechaVenta = fechaVenta;
        this.estado = estado;
    }


    public int getIdEncabezadoVenta() {
        return idEncabezadoVenta;
    }

    public void setIdEncabezadoVenta(int idEncabezadoVenta) {
        this.idEncabezadoVenta = idEncabezadoVenta;
    }

    public int getIdCliente_fk() {
        return idCliente_fk;
    }

    public void setIdCliente_fk(int idCliente_fk) {
        this.idCliente_fk = idCliente_fk;
    }

    public int getIdEmpresa_fk() {
        return idEmpresa_fk;
    }

    public void setIdEmpresa_fk(int idEmpresa_fk) {
        this.idEmpresa_fk = idEmpresa_fk;
    }

    public int getIdUsuario_fk() {
        return idUsuario_fk;
    }

    public void setIdUsuario_fk(int idUsuario_fk) {
        this.idUsuario_fk = idUsuario_fk;
    }

    public double getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(double valorPagar) {
        this.valorPagar = valorPagar;
    }
    
    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    
    
}
