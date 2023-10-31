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
import modelo.Proveedor;
import modelo.ProveedorDAO;
import vista.Menu;

public class Ctrl_proveedor implements ActionListener, MouseListener, KeyListener {

    private Proveedor prov;
    private ProveedorDAO provDao;
    private Menu menu;
    DefaultTableModel modeloTablaProv = new DefaultTableModel();

    public Ctrl_proveedor(Proveedor prov, ProveedorDAO provDao, Menu menu) {
        this.prov = prov;
        this.provDao = provDao;
        this.menu = menu;
        this.menu.btn_registrarProv.addActionListener(this);
        this.menu.btn_modificarProv.addActionListener(this);
        this.menu.btn_limpiarProv.addActionListener(this);
        this.menu.jMenuEliminarProv.addActionListener(this);
        this.menu.jMenuHabilitarProv.addActionListener(this);
        this.menu.textBuscarProv.addKeyListener(this);
        this.menu.tableProveedor.addMouseListener(this);
        this.menu.btnProveedor.addMouseListener(this);
        styleProveedor();
    }

    public void styleProveedor() {
        this.menu.textIdProv.setVisible(false);
        menu.textBuscarProv.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar proveedor");
    }

    public void registrarProv() {
        String nombre = menu.textNombreProv.getText();
        String apellido = menu.textApellidoProv.getText();
        String direccion = menu.textDireccionProv.getText();
        String telefono = menu.textTelefonoProv.getText();
        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            prov.setNombre(nombre);
            prov.setApellido(apellido);
            prov.setDireccion(direccion);
            prov.setTelefono(telefono);
            if (provDao.registroProv(prov)) {
                limpiarTabla();
                listarProv();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Proveedor registrado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el proveedor");
            }
        }
    }

    public void modificarProv() {
        if (menu.textIdProv.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila");
        } else {
            int id = Integer.parseInt(menu.textIdProv.getText());
            String nombre = menu.textNombreProv.getText();
            String apellido = menu.textApellidoProv.getText();
            String direccion = menu.textDireccionProv.getText();
            String telefono = menu.textTelefonoProv.getText();
            if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                prov.setIdProveedor(id);
                prov.setNombre(nombre);
                prov.setApellido(apellido);
                prov.setDireccion(direccion);
                prov.setTelefono(telefono);
                if (provDao.modificarProv(prov)) {
                    limpiarTabla();
                    listarProv();
                    limpiarContenidoInput();
                    JOptionPane.showMessageDialog(null, "Proveedor modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar el proveedor");
                }
            }
        }
    }

    public void eliminarProv() {
        if (menu.textIdProv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para eliminar un proveedor");
        } else {
            int id = Integer.parseInt(menu.textIdProv.getText());
            if (provDao.estadoProv(0, id)) {
                limpiarTabla();
                listarProv();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Proveedor eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar proveedor");
            }
        }
    }

    public void habilitarProv() {
        if (menu.textIdProv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para habilitar un proveedor");
        } else {
            int id = Integer.parseInt(menu.textIdProv.getText());
            if (provDao.estadoProv(1, id)) {
                limpiarTabla();
                listarProv();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Proveedor habilitado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al habilitar proveedor");
            }
        }
    }

    public void listarProv() {
        List<Proveedor> lista = provDao.listaProv(menu.textBuscarProv.getText());
        modeloTablaProv = (DefaultTableModel) menu.tableProveedor.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProveedor();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getApellido();
            ob[3] = lista.get(i).getTelefono();
            ob[4] = lista.get(i).getEstado();
            modeloTablaProv.addRow(ob);
        }
        menu.tableProveedor.setModel(modeloTablaProv);
        
        menu.tableProveedor.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableProveedor.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JTableHeader header = menu.tableProveedor.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        header.setOpaque(false);
        header.setBackground(headerColor);
        header.setFont(headerFont);
        header.setForeground(textColor);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modeloTablaProv.getRowCount(); i++) {
            modeloTablaProv.removeRow(i);
            i -= 1;
        }
    }

    public void agregarContenidoInput(int fila) {
        int estado = Integer.parseInt(menu.tableProveedor.getValueAt(fila, 4).toString());
        if (estado == 1) {
            menu.jMenuEliminarProv.setVisible(true);
            menu.jMenuHabilitarProv.setVisible(false);
        } else {
            menu.jMenuHabilitarProv.setVisible(true);
            menu.jMenuEliminarProv.setVisible(false);
        }
        menu.btn_modificarProv.setEnabled(true);
        menu.btn_registrarProv.setEnabled(false);

        int idProv = (int) menu.tableProveedor.getValueAt(fila, 0);

        List<Proveedor> lista = provDao.llenarInput(idProv);
        Proveedor valor = lista.get(0);

        menu.textIdProv.setText(String.valueOf(valor.getIdProveedor()));
        menu.textNombreProv.setText(valor.getNombre());
        menu.textApellidoProv.setText(valor.getApellido());
        menu.textDireccionProv.setText(valor.getDireccion());
        menu.textTelefonoProv.setText(valor.getTelefono());
    }

    public void limpiarContenidoInput() {
        menu.textNombreProv.setText("");
        menu.textApellidoProv.setText("");
        menu.textDireccionProv.setText("");
        menu.textIdProv.setText("");
        menu.textTelefonoProv.setText("");
        menu.btn_registrarProv.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_registrarProv) {
            registrarProv();
        } else if (e.getSource() == menu.btn_modificarProv) {
            modificarProv();
        } else if (e.getSource() == menu.jMenuEliminarProv) {
            eliminarProv();
        } else if (e.getSource() == menu.jMenuHabilitarProv) {
            habilitarProv();
        } else if (e.getSource() == menu.btn_limpiarProv) {
            limpiarContenidoInput();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableProveedor) {
            int fila = menu.tableProveedor.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
        } else if (e.getSource() == menu.btnProveedor) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(5);
            botones.cambiarColor(menu.btnProveedor);
            botones.cambiarTitulo("Registro de Proveedor");
            limpiarTabla();
            listarProv();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == menu.textBuscarProv) {
            limpiarTabla();
            listarProv();
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
