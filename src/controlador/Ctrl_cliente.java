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
import modelo.Cliente;
import modelo.ClienteDAO;
import vista.Menu;

public class Ctrl_cliente implements ActionListener, MouseListener, KeyListener {

    private Cliente client;
    private ClienteDAO clientDao;
    private Menu menu;
    DefaultTableModel modeloTablaCliente = new DefaultTableModel();

    public Ctrl_cliente(Cliente client, ClienteDAO clientDao, Menu menu) {
        this.client = client;
        this.clientDao = clientDao;
        this.menu = menu;
        this.menu.btn_registrarCliente.addActionListener(this);
        this.menu.btn_modificarCliente.addActionListener(this);
        this.menu.btn_limpiarCliente.addActionListener(this);
        this.menu.jMenuEliminarCliente.addActionListener(this);
        this.menu.jMenuHabilitarCliente.addActionListener(this);
        this.menu.textBuscarCliente.addKeyListener(this);
        this.menu.tableCliente.addMouseListener(this);
        this.menu.btnCliente.addMouseListener(this);
        styleCliente();
    }

    public void styleCliente() {
        this.menu.textIdCliente.setVisible(false);
        menu.textBuscarCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar cliente");
    }

    public void registrarCliente() {
        String nombre = menu.textNombreCliente.getText().trim();
        String apellido = menu.textApellidoCliente.getText().trim();
        String cedula = menu.textCedulaCliente.getText().trim();
        String telefono = menu.textTelefonoCliente.getText().trim();
        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            client.setNombre(nombre);
            client.setApellido(apellido);
            client.setCedula(cedula);
            client.setTelefono(telefono);
            if (clientDao.registroCliente(client)) {
                limpiarTabla();
                listarCliente();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el usuario");
            }
        }
    }

    public void modificarCliente() {
        if (menu.textIdCliente.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila");
        } else {
            int id = Integer.parseInt(menu.textIdCliente.getText());
            String nombre = menu.textNombreCliente.getText().trim();
            String apellido = menu.textApellidoCliente.getText().trim();
            String cedula = menu.textCedulaCliente.getText().trim();
            String telefono = menu.textTelefonoCliente.getText().trim();
            if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                client.setIdCliente(id);
                client.setNombre(nombre);
                client.setApellido(apellido);
                client.setCedula(cedula);
                client.setTelefono(telefono);
                if (clientDao.modificarCliente(client)) {
                    limpiarTabla();
                    listarCliente();
                    limpiarContenidoInput();
                    JOptionPane.showMessageDialog(null, "Cliente modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar el cliente");
                }
            }
        }
    }

    public void eliminarCliente() {
        if (menu.textIdCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para eliminar un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            int id = Integer.parseInt(menu.textIdCliente.getText());
            if (clientDao.estadoCliente(0, id)) {
                limpiarTabla();
                listarCliente();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Cliente eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar cliente");
            }
        }
    }

    public void habilitarCliente() {
        if (menu.textIdCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para habilitar un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            int id = Integer.parseInt(menu.textIdCliente.getText());
            if (clientDao.estadoCliente(1, id)) {
                limpiarTabla();
                listarCliente();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Cliente habilitado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al habilitar cliente");
            }
        }
    }

    public void listarCliente() {
        List<Cliente> lista = clientDao.listaClientes(menu.textBuscarCliente.getText().trim());
        modeloTablaCliente = (DefaultTableModel) menu.tableCliente.getModel();
        Object[] ob = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdCliente();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getApellido();
            ob[3] = lista.get(i).getCedula();
            ob[4] = lista.get(i).getTelefono();
            if (lista.get(i).getEstado() == 1) {
                ob[5] = "Habilitado";
            } else {
                ob[5] = "Deshabilitado";
            }
            modeloTablaCliente.addRow(ob);
        }
        menu.tableCliente.setModel(modeloTablaCliente);
        
        menu.tableCliente.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableCliente.getColumnModel().getColumn(0).setMaxWidth(0); 
        
        JTableHeader header = menu.tableCliente.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        header.setOpaque(false);
        header.setBackground(headerColor);
        header.setFont(headerFont);
        header.setForeground(textColor);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modeloTablaCliente.getRowCount(); i++) {
            modeloTablaCliente.removeRow(i);
            i -= 1;
        }
    }

    public void agregarContenidoInput(int fila) {
        String estado = menu.tableCliente.getValueAt(fila, 5).toString();
        if (estado == "Habilitado") {
            menu.jMenuEliminarCliente.setVisible(true);
            menu.jMenuHabilitarCliente.setVisible(false);
        } else {
            menu.jMenuHabilitarCliente.setVisible(true);
            menu.jMenuEliminarCliente.setVisible(false);
        }
        menu.btn_modificarCliente.setEnabled(true);
        menu.btn_registrarCliente.setEnabled(false);
        menu.textIdCliente.setText(menu.tableCliente.getValueAt(fila, 0).toString());
        menu.textNombreCliente.setText(menu.tableCliente.getValueAt(fila, 1).toString());
        menu.textApellidoCliente.setText(menu.tableCliente.getValueAt(fila, 2).toString());
        menu.textCedulaCliente.setText(menu.tableCliente.getValueAt(fila, 3).toString());
        menu.textTelefonoCliente.setText(menu.tableCliente.getValueAt(fila, 4).toString());
    }

    public void limpiarContenidoInput() {
        menu.textNombreCliente.setText("");
        menu.textApellidoCliente.setText("");
        menu.textCedulaCliente.setText("");
        menu.textIdCliente.setText("");
        menu.textTelefonoCliente.setText("");
        menu.btn_registrarCliente.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_registrarCliente) {
            registrarCliente();
        } else if (e.getSource() == menu.btn_modificarCliente) {
            modificarCliente();
        } else if (e.getSource() == menu.jMenuEliminarCliente) {
            eliminarCliente();
        } else if (e.getSource() == menu.jMenuHabilitarCliente) {
            habilitarCliente();
        } else if (e.getSource() == menu.btn_limpiarCliente) {
            limpiarContenidoInput();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableCliente) {
            int fila = menu.tableCliente.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
        } else if (e.getSource() == menu.btnCliente) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(3);
            botones.cambiarColor(menu.btnCliente);
            botones.cambiarTitulo("Gestion de Clientes");
            limpiarTabla();
            listarCliente();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == menu.textBuscarCliente) {
            limpiarTabla();
            listarCliente();
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
