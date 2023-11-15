package metodos;

import java.util.Objects;

public class ComboBox {

    // Atributos de la clase
    private int id;         // Identificador de la tabla 
    private String nombre;  // Nombre de que se desea mostrar en las opciones del comboBox

    // Metodo Constructor
    public ComboBox(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Métodos para obtener y establecer el ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Métodos para obtener y establecer el nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    // Método para representar el ComboBox como cadena (en este caso, el nombre)
    @Override
    public String toString(){
        return this.getNombre();
    }
    
//    // Método para verificar la igualdad de ComboBoxes basándose en su ID
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) { // Si es el mismo objeto, son iguales
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) { // Si el objeto no es una instancia de ComboBox, no son iguales
//            return false;
//        }
//        ComboBox other = (ComboBox) obj; // Se convierte el objeto a ComboBox para comparar IDs
//        return id == other.id; // Se compara el ID de los ComboBoxes
//    }
//
//    // Método para generar un código hash basado en el ID
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }

}

