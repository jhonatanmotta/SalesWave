package modelo;

public class Cliente {
    
    // Atributos del cliente
    private int idCliente; // Identificador único (id) del cliente
    private String nombre; // Nombre del cliente
    private String apellido; // Apellido del cliente
    private String cedula; // Cédula de identidad del cliente
    private String telefono; // Número de teléfono del cliente
    private int estado; // Estado del cliente (activo, inactivo)

    // Constructor vacío
    public Cliente() {
    }

    // Constructor con parámetros
    public Cliente(int idCliente, String nombre, String apellido, String cedula, String telefono, int estado) {
        this.idCliente = idCliente; // Asigna el id del cliente
        this.nombre = nombre; // Asigna el nombre del cliente
        this.apellido = apellido; // Asigna el apellido del cliente
        this.cedula = cedula; // Asigna la cédula del cliente
        this.telefono = telefono; // Asigna el teléfono del cliente
        this.estado = estado; // Asigna el estado del cliente
    }

    // Método para obtener el id del cliente
    public int getIdCliente() {
        return idCliente;
    }

    // Método para establecer el id del cliente
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    // Método para obtener el nombre del cliente
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre del cliente
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el apellido del cliente
    public String getApellido() {
        return apellido;
    }

    // Método para establecer el apellido del cliente
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Método para obtener la cédula del cliente
    public String getCedula() {
        return cedula;
    }

    // Método para establecer la cédula del cliente
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    // Método para obtener el teléfono del cliente
    public String getTelefono() {
        return telefono;
    }

    // Método para establecer el teléfono del cliente
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método para obtener el estado del cliente
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado del cliente
    public void setEstado(int estado) {
        this.estado = estado;
    }
}
