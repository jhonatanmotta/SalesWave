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
        
    // metodo constructor de la clase pide 3 parametos que son instancias de Categoria, CategoriaDAO y de Menu(formulario)
    public Ctrl_categoria(Categoria cat, CategoriaDAO catDao, Menu menu) {
        this.cat = cat; // Se le asigna al atributo this.cat la instancia que llega por parametro
        this.catDao = catDao; // Se le asigna al atributo this.catDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo this.menu la instancia que llega por parametro
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

    // metedo de estilos
    public void styleCategoria(){
        this.menu.textIdCategoria.setVisible(false); // el textField de id estara oculto
        // se le asigna un placeholder al textField textBuscarCategoria
        menu.textBuscarCategoria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Buscar categoria");
    }
    
    // metodo para registrar categorias en la base de datos
    public void registrarCategoria() {
        // se obtiene el nombre de la categoria del tgextField
        String nombre = menu.textNombreCategoria.getText().trim();
        // valida si esta vacio
        if (!Validaciones.validarNoVacios("Uno o más campos están vacíos",nombre) ||
                !Validaciones.validarCantidadCaracteres(nombre, 30, "El campo nombre sobrepasa la cantidad de caracteres aceptados")) {
            return;
//        if (nombre.isEmpty()) {
            // mensaje de error por campo vacio
//            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            // se llama al objeto cat y el setter se le asigna su nombre
            cat.setNombre(nombre);
            // metodo de insercion de datos en el modelo car
            if (catDao.registroCategoria(cat)) {
                limpiarTabla();
                listarCategoria();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Categoria registrada con exito");
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar la categoria");
            }
        }
    }

    public void modificarCategoria() {
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila",menu.textIdCategoria.getText())) {
            return;
//        if (menu.textIdCategoria.getText().equals("")) {
//            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila");
        } else {
            int id = Integer.parseInt(menu.textIdCategoria.getText());
            String nombre = menu.textNombreCategoria.getText().trim();
            if (!Validaciones.validarNoVacios("Uno o más campos están vacíos",nombre)) {
                return;
//                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                cat.setIdCategoria(id);
                cat.setNombre(nombre);
                if (catDao.modificarCategoria(cat)) {
                    limpiarTabla();
                    listarCategoria();
                    limpiarContenidoInput();
                    JOptionPane.showMessageDialog(null, "Categoria modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar la categoria");
                }
            }
        }
    }

    public void eliminarCategoria() {
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar una categoria",menu.textIdCategoria.getText())) {
            return;
        } else {
            int id = Integer.parseInt(menu.textIdCategoria.getText());
            if (catDao.estadoCategoria(0, id)) {
                limpiarTabla();
                listarCategoria();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Categoria eliminada");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar categoria");
            }
        }
    }

    public void habilitarCategoria() {
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar una categoria",menu.textIdCategoria.getText())) {
            return;
        } else {
            int id = Integer.parseInt(menu.textIdCategoria.getText());
            if (catDao.estadoCategoria(1, id)) {
                limpiarTabla();
                listarCategoria();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Categoria habilitada");
            } else {
                JOptionPane.showMessageDialog(null, "Error al habilitar categoria");
            }
        }
    }

    public void listarCategoria() {
        List<Categoria> lista = catDao.listaCategorias(menu.textBuscarCategoria.getText().trim());
        modeloTablaCategoria = (DefaultTableModel) menu.tableCategoria.getModel();
        Object[] ob = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdCategoria();
            ob[1] = lista.get(i).getNombre();
//            ob[2] = lista.get(i).getEstado();
            if (lista.get(i).getEstado() == 1) {
                ob[2] = "Disponible";
            } else {
                ob[2] = "Baja";
            }
            modeloTablaCategoria.addRow(ob);
        }
        menu.tableCategoria.setModel(modeloTablaCategoria);
        
        menu.tableCategoria.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableCategoria.getColumnModel().getColumn(0).setMaxWidth(0);
        
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        menu.tableCategoria.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        menu.tableCategoria.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        JTableHeader header = menu.tableCategoria.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        header.setOpaque(false);
        header.setBackground(headerColor);
        header.setFont(headerFont);
        header.setForeground(textColor);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modeloTablaCategoria.getRowCount(); i++) {
            modeloTablaCategoria.removeRow(i);
            i -= 1;
        }
    }

    public void agregarContenidoInput(int fila) {
        String estado = menu.tableCategoria.getValueAt(fila, 2).toString();
        if (estado == "Disponible"){
            menu.jMenuEliminarCategoria.setVisible(true);
            menu.jMenuHabilitarCategoria.setVisible(false);
        } else {
            menu.jMenuHabilitarCategoria.setVisible(true);
            menu.jMenuEliminarCategoria.setVisible(false);
        }
        menu.btn_modificarCategoria.setEnabled(true);
        menu.btn_registrarCategoria.setEnabled(false);
        menu.textIdCategoria.setText(menu.tableCategoria.getValueAt(fila, 0).toString());
        menu.textNombreCategoria.setText(menu.tableCategoria.getValueAt(fila, 1).toString());
    }

    public void limpiarContenidoInput() {
        menu.textNombreCategoria.setText("");
        menu.textIdCategoria.setText("");
        menu.btn_registrarCategoria.setEnabled(true);
    }
    
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableCategoria) {
            int fila = menu.tableCategoria.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
        } else if (e.getSource() == menu.btnCategoria) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(4);
            botones.cambiarColor(menu.btnCategoria);
            botones.cambiarTitulo("Registro de Categoria");
            limpiarTabla();
            listarCategoria();
        }
    }

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
