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

    //getters y setters de la clase Credenciales
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}
