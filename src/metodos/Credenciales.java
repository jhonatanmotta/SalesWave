package metodos;

public class Credenciales {

    // Metodos privados de la clase credenciales
    private String usuario;
    private String contrasena;

    // Instancia de Singleton
    private static Credenciales instancia = null;

    // Constructor privado para evitar la creación de instancias directas,
    // Se utiliza para controlar la creación de instancias y garantizar que solo exista una instancia de la clase en todo el programa.
    private Credenciales() {
    }

    // Método estático para obtener la instancia Singleton
    public static Credenciales getInstancia() {
        // si la instacia es nula entonces crea una instancia de la clase y la retorna
        if (instancia == null) {
            // instancia de la clase Credenciales
            instancia = new Credenciales();
        }
        // retorno de la instancia
        return instancia;
    }

    // Método para obtener el usuario
    public String getUsuario() {
        return usuario;
    }

    // Método para establecer el usuario
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // Método para obtener la contraseña
    public String getContrasena() {
        return contrasena;
    }

    // Método para establecer la contraseña
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}
