package modelo;

public class Usuario {

   // Atributos de la tabla Usuario
    private int idUsuario; // Identificador único (id) del usuario
    private String nombre; // Nombre del usuario
    private String apellido; // Apellido del usuario
    private String correo; // Correo electrónico del usuario
    private String usuario; // Nombre de usuario (para inicio de sesión)
    private String password; // Contraseña del usuario
    private String telefono; // Número de teléfono del usuario
    private int estado; // Estado del usuario (activo, inactivo)

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(int idUsuario, String nombre, String apellido, String correo, String usuario, String password, String telefono, int estado) {
        this.idUsuario = idUsuario; // Asigna el id del usuario
        this.nombre = nombre; // Asigna el nombre del usuario
        this.apellido = apellido; // Asigna el apellido del usuario
        this.correo = correo; // Asigna el correo electrónico del usuario
        this.usuario = usuario; // Asigna el nombre de usuario (para inicio de sesión)
        this.password = password; // Asigna la contraseña del usuario
        this.telefono = telefono; // Asigna el número de teléfono del usuario
        this.estado = estado; // Asigna el estado del usuario
    }
    
    // Método para obtener el id del usuario
    public int getIdUsuario() {
        return idUsuario;
    }

    // Método para establecer el id del usuario
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Método para obtener el nombre del usuario
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre del usuario
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el apellido del usuario
    public String getApellido() {
        return apellido;
    }

    // Método para establecer el apellido del usuario
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Método para obtener el correo electrónico del usuario
    public String getCorreo() {
        return correo;
    }

    // Método para establecer el correo electrónico del usuario
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Método para obtener el nombre de usuario
    public String getUsuario() {
        return usuario;
    }

    // Método para establecer el nombre de usuario
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // Método para obtener la contraseña del usuario
    public String getPassword() {
        return password;
    }

    // Método para establecer la contraseña del usuario
    public void setPassword(String password) {
        this.password = password;
    }

    // Método para obtener el teléfono del usuario
    public String getTelefono() {
        return telefono;
    }

    // Método para establecer el teléfono del usuario
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método para obtener el estado del usuario
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado del usuario
    public void setEstado(int estado) {
        this.estado = estado;
    }

}
