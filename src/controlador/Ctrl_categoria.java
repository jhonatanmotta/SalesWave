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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import metodos.BotonesMenu;
import modelo.Categoria;
import modelo.CategoriaDAO;
import vista.Menu;

public class Ctrl_categoria implements ActionListener, MouseListener, KeyListener  {

    private Categoria cat;
    private CategoriaDAO catDao;
    private Menu menu;
    DefaultTableModel modeloTablaCategoria = new DefaultTableModel();

    public Ctrl_categoria(Categoria cat, CategoriaDAO catDao, Menu menu) {
        this.cat = cat;
        this.catDao = catDao;
        this.menu = menu;
        this.menu.btn_registrarCategoria.addActionListener(this);
        this.menu.btn_modificarCategoria.addActionListener(this);
        this.menu.btn_limpiarCategoria.addActionListener(this);
        this.menu.jMenuEliminarCategoria.addActionListener(this);
        this.menu.jMenuHabilitarCategoria.addActionListener(this);
        this.menu.textBuscarCategoria.addKeyListener(this);
        this.menu.tableCategoria.addMouseListener(this);
        this.menu.btnCategoria.addMouseListener(this);
        styleCategoria();
    }

    public void styleCategoria(){
        this.menu.textIdCategoria.setVisible(false);
        menu.textBuscarCategoria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Buscar categoria");
    }
    
    public void registrarCategoria() {
        String nombre = menu.textNombreCategoria.getText();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            cat.setNombre(nombre);
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
        if (menu.textIdCategoria.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila");
        } else {
            int id = Integer.parseInt(menu.textIdCategoria.getText());
            String nombre = menu.textNombreCategoria.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
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
        if (menu.textIdCategoria.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para eliminar una categoria");
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
        if (menu.textIdCategoria.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para habilitar una categoria");
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
        List<Categoria> lista = catDao.listaCategorias(menu.textBuscarCategoria.getText());
        modeloTablaCategoria = (DefaultTableModel) menu.tableCategoria.getModel();
        Object[] ob = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdCategoria();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getEstado();
            modeloTablaCategoria.addRow(ob);
        }
        menu.tableCategoria.setModel(modeloTablaCategoria);
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
        int estado = Integer.parseInt(menu.tableCategoria.getValueAt(fila, 2).toString());
        if (estado == 1){
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
    
//    public void llenarComboBox(){
//        List<Categoria> lista = catDao.listaCategorias(menu.textBuscarCategoria.getText());
////        menu.comboBoxCategoriaProd.addItem("Seleccione categoria");
//        for (int i = 0; i < lista.size(); i++) {
//            int id = lista.get(i).getIdCategoria();
//            String nombre = lista.get(i).getNombre();
//            menu.comboBoxCategoriaProd.addItem(new ComboBox(id, nombre));
//        }
//    }
    

    
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
