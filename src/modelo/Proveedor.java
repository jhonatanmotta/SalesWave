package modelo;

public class Proveedor {

    // Atributos de la tabla proveedor
    private int idProveedor; // Identificador único (id) del proveedor
    private String nombre; // Nombre del proveedor
    private String apellido; // Apellido del proveedor
    private String telefono; // Número de teléfono del proveedor
    private String direccion; // Dirección del proveedor
    private int estado; // Estado del proveedor (activo, inactivo)

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor con parámetros
    public Proveedor(int idProveedor, String nombre, String apellido, String telefono, String direccion, int estado) {
        this.idProveedor = idProveedor; // Asigna el id del proveedor
        this.nombre = nombre; // Asigna el nombre del proveedor
        this.apellido = apellido; // Asigna el apellido del proveedor
        this.telefono = telefono; // Asigna el teléfono del proveedor
        this.direccion = direccion; // Asigna la dirección del proveedor
        this.estado = estado; // Asigna el estado del proveedor
    }

    // Método para obtener el id del proveedor
    public int getIdProveedor() {
        return idProveedor;
    }

    // Método para establecer el id del proveedor
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    // Método para obtener el nombre del proveedor
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre del proveedor
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el apellido del proveedor
    public String getApellido() {
        return apellido;
    }

    // Método para establecer el apellido del proveedor
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Método para obtener el teléfono del proveedor
    public String getTelefono() {
        return telefono;
    }

    // Método para establecer el teléfono del proveedor
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método para obtener la dirección del proveedor
    public String getDireccion() {
        return direccion;
    }

    // Método para establecer la dirección del proveedor
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Método para obtener el estado del proveedor
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado del proveedor
    public void setEstado(int estado) {
        this.estado = estado;
    }
}
