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
import modelo.Usuario;
import modelo.UsuarioDAO;
import vista.Menu;

public class Ctrl_usuario implements ActionListener, MouseListener, KeyListener {

    private Usuario user;
    private UsuarioDAO userDao;
    private Menu menu;
    DefaultTableModel modeloTabla = new DefaultTableModel();

    public Ctrl_usuario(Usuario user, UsuarioDAO userDao, Menu menu) {
        this.user = user;
        this.userDao = userDao;
        this.menu = menu;
        this.menu.btn_registrarUsuario.addActionListener(this);
        this.menu.btn_modificarUsuario.addActionListener(this);
        this.menu.btn_limpiarUsuario.addActionListener(this);
        this.menu.jMenuEliminarUsuario.addActionListener(this);
        this.menu.jMenuHabilitarUsuario.addActionListener(this);
        this.menu.textBuscarUsuario.addKeyListener(this);
        this.menu.tableUsuarios.addMouseListener(this);
        this.menu.btnUsuario.addMouseListener(this);
        styleUsuario();
    }

    public void styleUsuario() {
        this.menu.textIdUsuario.setVisible(false);
        menu.txtPassword.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
        menu.textBuscarUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar usuario");
    }

    public void registrarUsuario() {
        String nombre = menu.textNombre.getText().trim();
        String apellido = menu.textApellido.getText().trim();
        String correo = menu.textCorreo.getText().trim();
        String usuario = menu.textUsuario.getText().trim();
        String telefono = menu.textTelefono.getText().trim();
        String password = String.valueOf(menu.txtPassword.getPassword());
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || usuario.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setCorreo(correo);
            user.setUsuario(usuario);
            user.setTelefono(telefono);
            user.setPassword(password);
            if (userDao.registroUsuario(user)) {
                limpiarTabla();
                listarUsuario();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Usuario registrado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el usuario");
            }
        }
    }

    public void modificarUsuario() {
        if (menu.textIdUsuario.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            int id = Integer.parseInt(menu.textIdUsuario.getText());
            String nombre = menu.textNombre.getText().trim();
            String apellido = menu.textApellido.getText().trim();
            String correo = menu.textCorreo.getText().trim();
            String usuario = menu.textUsuario.getText().trim();
            String telefono = menu.textTelefono.getText().trim();
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || usuario.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                user.setIdUsuario(id);
                user.setNombre(nombre);
                user.setApellido(apellido);
                user.setCorreo(correo);
                user.setUsuario(usuario);
                user.setTelefono(telefono);
                if (userDao.modificarUsuario(user)) {
                    limpiarTabla();
                    listarUsuario();
                    limpiarContenidoInput();
                    JOptionPane.showMessageDialog(null, "Usuario modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar el usuario");
                }
            }
        }
    }

    public void eliminarUsuario() {
        if (menu.textIdUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para eliminar un usuario");
        } else {
            int id = Integer.parseInt(menu.textIdUsuario.getText());
            if (userDao.estadoUsuario(0, id)) {
                limpiarTabla();
                listarUsuario();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Usuario eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar usuario");
            }
        }
    }

    public void habilitarUsuario() {
        if (menu.textIdUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para habilitar un usuario");
        } else {
            int id = Integer.parseInt(menu.textIdUsuario.getText());
            if (userDao.estadoUsuario(1, id)) {
                limpiarTabla();
                listarUsuario();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Usuario habilitado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al habilitar usuario");
            }
        }
    }

    public void listarUsuario() {
        List<Usuario> lista = userDao.listaUsuarios(menu.textBuscarUsuario.getText().trim());
        modeloTabla = (DefaultTableModel) menu.tableUsuarios.getModel();
//        Object[] ob = new Object[7];
        Object[] ob = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdUsuario();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getApellido();
            ob[3] = lista.get(i).getUsuario();
            ob[4] = lista.get(i).getTelefono();
            if (lista.get(i).getEstado() == 1) {
                ob[5] = "Habilitado";
            } else {
                ob[5] = "Deshabilitado";
            }
            modeloTabla.addRow(ob);
        }
        menu.tableUsuarios.setModel(modeloTabla);
        
        menu.tableUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JTableHeader header = menu.tableUsuarios.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        header.setOpaque(false);
        header.setBackground(headerColor);
        header.setFont(headerFont);
        header.setForeground(textColor);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i -= 1;
        }
    }

    public void agregarContenidoInput(int fila) {
        String estado = menu.tableUsuarios.getValueAt(fila, 5).toString();
        if (estado == "Habilitado") {
            menu.jMenuEliminarUsuario.setVisible(true);
            menu.jMenuHabilitarUsuario.setVisible(false);
        } else {
            menu.jMenuHabilitarUsuario.setVisible(true);
            menu.jMenuEliminarUsuario.setVisible(false);
        }
        menu.btn_modificarUsuario.setEnabled(true);
        menu.btn_registrarUsuario.setEnabled(false);
        menu.txtPassword.setEnabled(false);
        int idUsuario = (int) menu.tableUsuarios.getValueAt(fila, 0);
        Usuario usuario = userDao.buscarCorreo(idUsuario);
        menu.textIdUsuario.setText(menu.tableUsuarios.getValueAt(fila, 0).toString());
        menu.textNombre.setText(menu.tableUsuarios.getValueAt(fila, 1).toString());
        menu.textApellido.setText(menu.tableUsuarios.getValueAt(fila, 2).toString());
        menu.textCorreo.setText(usuario.getCorreo());
        menu.textUsuario.setText(menu.tableUsuarios.getValueAt(fila, 3).toString());
        menu.textTelefono.setText(menu.tableUsuarios.getValueAt(fila, 4).toString());
    }

    public void limpiarContenidoInput() {
        menu.textNombre.setText("");
        menu.textApellido.setText("");
        menu.textCorreo.setText("");
        menu.textUsuario.setText("");
        menu.textIdUsuario.setText("");
        menu.textTelefono.setText("");
        menu.txtPassword.setText("");
        menu.btn_registrarUsuario.setEnabled(true);
        menu.txtPassword.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_registrarUsuario) {
            registrarUsuario();
        } else if (e.getSource() == menu.btn_modificarUsuario) {
            modificarUsuario();
        } else if (e.getSource() == menu.jMenuEliminarUsuario) {
            eliminarUsuario();
        } else if (e.getSource() == menu.jMenuHabilitarUsuario) {
            habilitarUsuario();
        } else if (e.getSource() == menu.btn_limpiarUsuario) {
            limpiarContenidoInput();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableUsuarios) {
            int fila = menu.tableUsuarios.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
        } else if (e.getSource() == menu.btnUsuario) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(1);
            botones.cambiarColor(menu.btnUsuario);
            botones.cambiarTitulo("Gestion de Usuarios");
            limpiarTabla();
            listarUsuario();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == menu.textBuscarUsuario) {
            limpiarTabla();
            listarUsuario();
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
