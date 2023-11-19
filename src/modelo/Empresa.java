package modelo;

public class Empresa {
    
    // Atributos de la clase Empresa
    private int idEmpresa; // Identificador único (id) de la empresa
    private String nombre; // Nombre de la empresa
    private String nit; // NIT (Número de Identificación Tributaria) de la empresa
    private String telefono; // Número de teléfono de la empresa
    private String direccion; // Dirección de la empresa

    // Constructor vacío
    public Empresa() {
    }

    // Constructor con parámetros
    public Empresa(int idEmpresa, String nombre, String nit, String telefono, String direccion) {
        this.idEmpresa = idEmpresa; // Asigna el id de la empresa
        this.nombre = nombre; // Asigna el nombre de la empresa
        this.nit = nit; // Asigna el NIT de la empresa
        this.telefono = telefono; // Asigna el teléfono de la empresa
        this.direccion = direccion; // Asigna la dirección de la empresa
    }

    // Método para obtener el id de la empresa
    public int getIdEmpresa() {
        return idEmpresa;
    }

    // Método para establecer el id de la empresa
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    // Método para obtener el nombre de la empresa
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre de la empresa
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el NIT de la empresa
    public String getNit() {
        return nit;
    }

    // Método para establecer el NIT de la empresa
    public void setNit(String nit) {
        this.nit = nit;
    }

    // Método para obtener el teléfono de la empresa
    public String getTelefono() {
        return telefono;
    }

    // Método para establecer el teléfono de la empresa
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método para obtener la dirección de la empresa
    public String getDireccion() {
        return direccion;
    }

    // Método para establecer la dirección de la empresa
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
