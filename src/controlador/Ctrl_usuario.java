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
import metodos.Credenciales;
import metodos.Validaciones;
import modelo.Usuario;
import modelo.UsuarioDAO;
import vista.Menu;

public class Ctrl_usuario implements ActionListener, MouseListener, KeyListener {

    // atributos del controlador usuario
    private Usuario user; // atributo de tipo Usuario para asignar una instancia de este
    private UsuarioDAO userDao; // atributo de tipo UsuarioDAO para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTabla = new DefaultTableModel();
    
    // Constructor con parámetros
    public Ctrl_usuario(Usuario user, UsuarioDAO userDao, Menu menu) {
        this.user = user; // Se le asigna al atributo user la instancia que llega por parametro
        this.userDao = userDao; // Se le asigna al atributo userDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btn_registrarUsuario.addActionListener(this); // se le añade el ActionListener al boton btn_registrarUsuario
        this.menu.btn_modificarUsuario.addActionListener(this); // se le añade el ActionListener al boton btn_modificarUsuario
        this.menu.btn_limpiarUsuario.addActionListener(this); // se le añade el ActionListener al boton btn_limpiarUsuario
        this.menu.jMenuEliminarUsuario.addActionListener(this); // se le añade el ActionListener al boton jMenuEliminarUsuario
        this.menu.jMenuHabilitarUsuario.addActionListener(this); // se le añade el ActionListener al boton jMenuHabilitarUsuario
        this.menu.textBuscarUsuario.addKeyListener(this); // se le añade el KeyListener al textField textBuscarUsuario
        this.menu.tableUsuarios.addMouseListener(this); // se le añade el MouseListener a la tabla tableUsuarios
        this.menu.btnUsuario.addMouseListener(this); // se le añade el MouseListener al boton btnUsuario
        styleUsuario(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleUsuario() {
        this.menu.textIdUsuario.setVisible(false); // el textField de id estara oculto
        // se le asigna el boton de ver contraseñas al passwordField txtPassword
        menu.txtPassword.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
        // se le asigna un placeholder al textField textBuscarUsuario
        menu.textBuscarUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar usuario");
    }
    
    // metodo para registrar un nuevo usuario
    public void registrarUsuario() {
        // se obtiene la informacion del usuario de los textField
        String nombre = menu.textNombre.getText().trim();
        String apellido = menu.textApellido.getText().trim();
        String correo = menu.textCorreo.getText().trim();
        String usuario = menu.textUsuario.getText().trim();
        String telefono = menu.textTelefono.getText().trim();
        String password = String.valueOf(menu.txtPassword.getPassword());
        // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
        if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombre, apellido, correo, usuario, telefono, password)) {
            return;
        } else {
            // valida que el correo a registrar cumpla con ciertos criterios
            if (!Validaciones.validarCorreo(correo)) {
                JOptionPane.showMessageDialog(null, "El correo parece no poseer el formato esperado", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // valida que el correo no este asociado a otro usuario ya que debe ser unico
                if (userDao.validarCorreo(correo)) {
                    JOptionPane.showMessageDialog(null, "El correo ya esta asociado a otro usuario", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // valida que el usuario ya no este registrado en el sistema
                    if (userDao.validarUsuario(usuario)) {
                        JOptionPane.showMessageDialog(null, "Ya existe este usuario en el sistema", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // valida el rango de caracteres de una contraseña y del numero de telefono
                        if (!Validaciones.validarRangoCaracteres(password, 10, 30, "La contraseña debe tener un minimo de 10 caracteres")
                                || !Validaciones.validarRangoCaracteres(telefono, 10, 15, "El número de telefono debe tener un minimo de 10 caracteres")) {
                            return;
                        } else {
                            // valida que la contraseña tenga ciertos parametros antes de registrar un cliente
                            if (!Validaciones.validarContrasena(password)) {
                                JOptionPane.showMessageDialog(null, "La contraseña debe tener minus, MAYUS, números y un caracter especial", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            } else {
                                // se establece la informacion del usuario en el objeto user
                                user.setNombre(nombre);
                                user.setApellido(apellido);
                                user.setCorreo(correo);
                                user.setUsuario(usuario);
                                user.setTelefono(telefono);
                                user.setPassword(password);
                                // metodo de insercion de datos
                                if (userDao.registroUsuario(user)) {
                                    // limpia el contenido de la tabla
                                    limpiarTabla();
                                    // añade contenido a la tabla 
                                    listarUsuario();
                                    // limpia los campos
                                    limpiarContenidoInput();
                                    // mensaje informativo
                                    JOptionPane.showMessageDialog(null, "Usuario registrado con exito");
                                } else {
                                    // mensaje de error
                                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //metodo para modicar los datos del cliente en la base de datos   
    public void modificarUsuario() {
        // valida si se ha seleccionado una fila para modificar
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para modificar los datos de un usuario", menu.textIdUsuario.getText())) {
            return;
        } else {
            // se obtiene la informacion del usuario de los textField
            int id = Integer.parseInt(menu.textIdUsuario.getText());
            String nombre = menu.textNombre.getText().trim();
            String apellido = menu.textApellido.getText().trim();
            String correo = menu.textCorreo.getText().trim();
            String usuario = menu.textUsuario.getText().trim();
            String telefono = menu.textTelefono.getText().trim();
            // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
            if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombre, apellido, correo, usuario, telefono)) {
                return;
            } else {
                // valida que el correo a registrar cumpla con ciertos criterios
                if (!Validaciones.validarCorreo(correo)) {
                JOptionPane.showMessageDialog(null, "El correo parece no poseer el formato esperado", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // valida el rango de caracteres del numero de telefono
                    if (!Validaciones.validarRangoCaracteres(telefono, 10, 15, "El número de telefono debe tener un minimo de 10 caracteres")) {
                        return;
                    } else {
                        // se establece la informacion del usuario en el objeto user
                        user.setIdUsuario(id);
                        user.setNombre(nombre);
                        user.setApellido(apellido);
                        user.setCorreo(correo);
                        user.setUsuario(usuario);
                        user.setTelefono(telefono);
                        // metodo para modificar el usuario en la base de datos
                        if (userDao.modificarUsuario(user)) {
                            // limpia el contenido de la tabla
                            limpiarTabla();
                            // añade contenido a la tabla 
                            listarUsuario();
                            // limpia los campos
                            limpiarContenidoInput();
                            // mensaje informativo
                            JOptionPane.showMessageDialog(null, "Usuario modificado con exito");
                        } else {
                            // mensaje de error
                            JOptionPane.showMessageDialog(null, "Error al modificar el usuario");
                        }
                    }
                }
            }
        }
    }

    // metodo para eliminar el usuario, es decir cambiarlo de estado
    public void eliminarUsuario() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar un usuario", menu.textIdUsuario.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdUsuario.getText());
            // metodo para modificar el estado del usuario
            if (userDao.estadoUsuario(0, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarUsuario();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Usuario eliminado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al eliminar usuario");
            }
        }
    }
    
    // metodo para habilitar el usuario, es decir cambiarlo de estado
    public void habilitarUsuario() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar un usuario", menu.textIdUsuario.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdUsuario.getText());
            // metodo para modificar el estado del usuario
            if (userDao.estadoUsuario(1, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarUsuario();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Usuario habilitado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al habilitar usuario");
            }
        }
    }
    
    // metodo para mostrar los datos en la tabla de la cliente
    public void listarUsuario() {
        // lista con la informacion del usuario
        List<Usuario> lista = userDao.listaUsuarios(menu.textBuscarUsuario.getText().trim());
        // obtiene el modelo de la tabla
        modeloTabla = (DefaultTableModel) menu.tableUsuarios.getModel();
        // arreglo con la cantidad de columnas
        Object[] ob = new Object[6];
        // for que recorre la lista y va agregando la informacion a las filas de la tabla
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
        // se le pone el nuevo modelo a la tabla
        menu.tableUsuarios.setModel(modeloTabla);

        // se oculta la columna del id
        menu.tableUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);

         // se obtiene el header de la tabla
        JTableHeader header = menu.tableUsuarios.getTableHeader();
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
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i -= 1;
        }
    }

    // metodo para obtener el usuario que se ha logeado en el sistema
     public String obtenerUsuario() {
        Credenciales credenciales = Credenciales.getInstancia();
        String userLogin = credenciales.getUsuario();
        return userLogin;
    }
     
    // metodo para agregar contenido a los inputs luego de dar click en un campo de la tabla
    public void agregarContenidoInput(int fila) {
        String estado = menu.tableUsuarios.getValueAt(fila, 5).toString();
        if (estado == "Habilitado") {
            // pone visible el boton de eliminar usuario
            menu.jMenuEliminarUsuario.setVisible(true);
            menu.jMenuHabilitarUsuario.setVisible(false);
        } else {
            // pone visible el boton de habilitar usuario
            menu.jMenuHabilitarUsuario.setVisible(true);
            menu.jMenuEliminarUsuario.setVisible(false);
        }
        // inhabilita el boton de registrar y habilita el de modificar
        menu.btn_registrarUsuario.setEnabled(false);
        menu.txtPassword.setEnabled(false);
        
        // valida si el usuario que quiere modificar a otro es quien esta logeado
        if (obtenerUsuario().equals(menu.tableUsuarios.getValueAt(fila, 3).toString())) {
            menu.textUsuario.setEnabled(true);
        } else {
            menu.textUsuario.setEnabled(false);
        }
        
        // agrega contenido a los inputs
        int idUsuario = (int) menu.tableUsuarios.getValueAt(fila, 0);
        Usuario usuario = userDao.buscarCorreo(idUsuario);
        menu.textIdUsuario.setText(menu.tableUsuarios.getValueAt(fila, 0).toString());
        menu.textNombre.setText(menu.tableUsuarios.getValueAt(fila, 1).toString());
        menu.textApellido.setText(menu.tableUsuarios.getValueAt(fila, 2).toString());
        menu.textCorreo.setText(usuario.getCorreo());
        menu.textUsuario.setText(menu.tableUsuarios.getValueAt(fila, 3).toString());
        menu.textTelefono.setText(menu.tableUsuarios.getValueAt(fila, 4).toString());
    }

    //metodo para limpiar el contenido de los inputs
    public void limpiarContenidoInput() {
        menu.textNombre.setText("");
        menu.textApellido.setText("");
        menu.textCorreo.setText("");
        menu.textUsuario.setText("");
        menu.textIdUsuario.setText("");
        menu.textTelefono.setText("");
        menu.txtPassword.setText("");
        menu.btn_registrarUsuario.setEnabled(true); // habilita el boton de registrar
        menu.txtPassword.setEnabled(true); // habilita el textField de password
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
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

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
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

    // keyReleased que recibe los eventos al soltar una letra del teclado y ejecuta metodos
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
