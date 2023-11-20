package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import metodos.BotonesMenu;
import metodos.Validaciones;
import modelo.Categoria;
import modelo.CategoriaDAO;
import vista.Menu;

public class Ctrl_categoria implements ActionListener, MouseListener, KeyListener  {
    // atributos del controlador categoria
    private Categoria cat; // atributo de tipo Categoria para asignar una instancia de este
    private CategoriaDAO catDao; // atributo de tipo CategoriaDAO para asignar una instancia de este
    private Menu menu;// atributo de tipo Menu para asignar una instancia de este
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTablaCategoria = new DefaultTableModel();
    // instancia de DefaultTableCellRenderer se utiliza para personalizar la apariencia de las celdas en una tabla
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        
    // Constructor con parámetros
    public Ctrl_categoria(Categoria cat, CategoriaDAO catDao, Menu menu) {
        this.cat = cat; // Se le asigna al atributo cat la instancia que llega por parametro
        this.catDao = catDao; // Se le asigna al atributo catDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btn_registrarCategoria.addActionListener(this); // se le añade el ActionListener al boton btn_registrarCategoria
        this.menu.btn_modificarCategoria.addActionListener(this); // se le añade el ActionListener al boton btn_modificarCategoria
        this.menu.btn_limpiarCategoria.addActionListener(this); // se le añade el ActionListener al boton btn_limpiarCategoria
        this.menu.jMenuEliminarCategoria.addActionListener(this); // se le añade el ActionListener al pop-up jMenuEliminarCategoria
        this.menu.jMenuHabilitarCategoria.addActionListener(this); // se le añade el ActionListener al pop-up jMenuHabilitarCategoria
        this.menu.textBuscarCategoria.addKeyListener(this); // se le añade el KeyListener al textField textBuscarCategoria
        this.menu.tableCategoria.addMouseListener(this);  // se le añade el MouseListener a la tabla tableCategoria
        this.menu.btnCategoria.addMouseListener(this);  // se le añade el MouseListener al boton btnCategoria
        styleCategoria(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleCategoria(){
        this.menu.textIdCategoria.setVisible(false); // el textField de id estara oculto
        // se le asigna un placeholder al textField textBuscarCategoria
        menu.textBuscarCategoria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Buscar categoria");
    }
    
    // metodo para registrar una nueva categoria
    public void registrarCategoria() {
        // se obtiene el nombre de la categoria del textField
        String nombre = menu.textNombreCategoria.getText().trim();
        // valida si esta vacio, si lo esta arroja un cuadro de dialogo con una advertencia
        if (!Validaciones.validarNoVacios("El campo nombre categoria esta vacio",nombre) ||
                !Validaciones.validarCantidadCaracteres(nombre, 30, "El campo nombre sobrepasa la cantidad de caracteres aceptados")) {
            return;
        } else {
            // se establece el nombre en el objeto cat
            cat.setNombre(nombre);
            // metodo de insercion de datos
            if (catDao.registroCategoria(cat)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla 
                listarCategoria();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Categoria registrada con exito");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al registrar la categoria");
            }
        }
    }
    
    //metodo para modicar los datos de la categoria en la base de datos
    public void modificarCategoria() {
    // valida si se ha seleccionado una fila para modificar
    if (!Validaciones.validarNoVacios("Debes seleccionar una fila para modificar el nombre de una categoría", menu.textIdCategoria.getText())) {
        return;
    } else {
        // obtiene el ID de la categoría y el nombre ingresado en el textField
        int id = Integer.parseInt(menu.textIdCategoria.getText());
        String nombre = menu.textNombreCategoria.getText().trim();
        
        // valida si el campo del nombre de la categoría está vacío
        if (!Validaciones.validarNoVacios("El campo nombre categoría está vacío", nombre)) {
            return;
        } else {
            // establece el ID y nombre en el objeto cat
            cat.setIdCategoria(id);
            cat.setNombre(nombre);
            
            // metodo para modificar la categoría en la base de datos
            if (catDao.modificarCategoria(cat)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla 
                listarCategoria();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Categoría modificada con éxito");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al modificar la categoría");
            }
        }
    }
}
    // metodo para eliminar la categoria, es decir cambiarla de estado
    public void eliminarCategoria() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar una categoria",menu.textIdCategoria.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdCategoria.getText());
            // metodo para modificar el estado de la categoria
            if (catDao.estadoCategoria(0, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarCategoria();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Categoria eliminada");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al eliminar categoria");
            }
        }
    }

    // metodo para habilitar la categoria, es decir cambiarla de estado
    public void habilitarCategoria() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar una categoria",menu.textIdCategoria.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdCategoria.getText());
            // metodo para modificar el estado de la categoria
            if (catDao.estadoCategoria(1, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarCategoria();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Categoria habilitada");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al habilitar categoria");
            }
        }
    }
    
    // metodo para mostrar los datos en la tabla de la categoria
    public void listarCategoria() {
        // lista con la informacion de la categoria
        List<Categoria> lista = catDao.listaCategorias(menu.textBuscarCategoria.getText().trim());
        // obtiene el modelo de la tabla de la categoria
        modeloTablaCategoria = (DefaultTableModel) menu.tableCategoria.getModel();
        // arreglo con la cantidad de columnas
        Object[] ob = new Object[3];
        // for que recorre la lista y va agregando la informacion a las filas de la tabla
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdCategoria();
            ob[1] = lista.get(i).getNombre();
            if (lista.get(i).getEstado() == 1) {
                ob[2] = "Disponible";
            } else {
                ob[2] = "Baja";
            }
            modeloTablaCategoria.addRow(ob);
        }
        // se le pone el nuevo modelo a la tabla
        menu.tableCategoria.setModel(modeloTablaCategoria);
        // se oculta la columna del id
        menu.tableCategoria.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableCategoria.getColumnModel().getColumn(0).setMaxWidth(0);
        // se centra la informacion de las columnas 1 y 2
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        menu.tableCategoria.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        menu.tableCategoria.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        // se obtiene el header de la tabla
        JTableHeader header = menu.tableCategoria.getTableHeader();
        // color del header
        Color headerColor = new Color(232, 158, 67);
        // color del texto del header
        Color textColor = Color.WHITE;
        // tipo de letra del header
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        // se quita lo opaco del header, se estable el color de fondo, letra y color
        header.setOpaque(false);
        header.setBackground(headerColor);
        header.setFont(headerFont);
        header.setForeground(textColor);
    }

    // metodo para limpiar la tabla
    public void limpiarTabla() {
        for (int i = 0; i < modeloTablaCategoria.getRowCount(); i++) {
            modeloTablaCategoria.removeRow(i);
            i -= 1;
        }
    }
    
    // metodo para agregar contenido a los inputs luego de dar click en un campo de la tabla
    public void agregarContenidoInput(int fila) {
        String estado = menu.tableCategoria.getValueAt(fila, 2).toString();
        if (estado == "Disponible"){
            // pone visible el boton de eliminar categoria
            menu.jMenuEliminarCategoria.setVisible(true);
            menu.jMenuHabilitarCategoria.setVisible(false);
        } else {
            // pone visible el boton de habilitar categoria
            menu.jMenuHabilitarCategoria.setVisible(true);
            menu.jMenuEliminarCategoria.setVisible(false);
        }
        // inhabilita el boton de registrar y habilita el de modificar
        menu.btn_modificarCategoria.setEnabled(true);
        menu.btn_registrarCategoria.setEnabled(false);
        // agrega contenido a los inputs
        menu.textIdCategoria.setText(menu.tableCategoria.getValueAt(fila, 0).toString());
        menu.textNombreCategoria.setText(menu.tableCategoria.getValueAt(fila, 1).toString());
    }

    //metodo para limpiar el contenido de los inputs
    public void limpiarContenidoInput() {
        menu.textNombreCategoria.setText(""); 
        menu.textIdCategoria.setText("");
        menu.btn_registrarCategoria.setEnabled(true); // habilita el boton de registrar
    }
    
    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_registrarCategoria) {
            registrarCategoria();
        } else if (e.getSource() == menu.btn_modificarCategoria) {
            modificarCategoria();
        } else if (e.getSource() == menu.jMenuEliminarCategoria) {
            eliminarCategoria();
        } else if (e.getSource() == menu.jMenuHabilitarCategoria) {
            habilitarCategoria();
        } else if (e.getSource() == menu.btn_limpiarCategoria) {
            limpiarContenidoInput();
        }
    }

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableCategoria) {
            int fila = menu.tableCategoria.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
        } else if (e.getSource() == menu.btnCategoria) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(4);
            botones.cambiarColor(menu.btnCategoria);
            botones.cambiarTitulo("Gestion de Categorías");
            limpiarTabla();
            listarCategoria();
        }
    }

    // keyReleased que recibe los eventos al soltar una letra del teclado y ejecuta metodos
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == menu.textBuscarCategoria) {
            limpiarTabla();
            listarCategoria();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
}
