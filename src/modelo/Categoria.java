package modelo;

public class Categoria {

    // Atributos de la clase
    private int idCategoria; // Identificador único (id) de la categoría
    private String nombre; // Nombre de la categoría
    private int estado; // Estado de la categoría (activo, inactivo)

    // Constructor vacío
    public Categoria() {
    }

    // Constructor con parámetros
    public Categoria(int idCategoria, String nombre, String descripcion, int estado) {
        this.idCategoria = idCategoria; // Asigna el id de la categoría
        this.nombre = nombre; // Asigna el nombre de la categoría
        this.estado = estado; // Asigna el estado de la categoría
    }

    // Método para obtener el id de la categoría
    public int getIdCategoria() {
        return idCategoria;
    }

    // Método para establecer el id de la categoría
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    // Método para obtener el nombre de la categoría
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre de la categoría
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el estado de la categoría
    public int getEstado() {
        return estado;
    }

    // Método para establecer el estado de la categoría
    public void setEstado(int estado) {
        this.estado = estado;
    }
}

