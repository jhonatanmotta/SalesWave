package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import conexion.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import metodos.Credenciales;
import modelo.UsuarioDAO;
import vista.Menu;
import vista.Login;

public class Ctrl_login implements ActionListener, KeyListener {

    // atributos del controlador login
    Login log = new Login(); // Objeto de la clase Login
    Menu menu = new Menu(); // Objeto de la clase Menu

    // Constructor con parámetros
    public Ctrl_login(Login log) {
        this.log = log; // Se le asigna al atributo log la instancia que llega por parametro
        this.log.btn_IniciarSesion.addActionListener(this); // se le añade el ActionListener al boton btn_IniciarSesion
        this.log.txt_user.addKeyListener(this); // se le añade el KeyListener al textField txt_user
        this.log.txt_password.addKeyListener(this); // se le añade el KeyListener al passwordField txt_password
        styleLogin(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleLogin() {
        // se le asigna el boton de ver contraseñas al passwordField txt_password
        log.txt_password.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
        // se le asigna un placeholder al textField txt_user
        log.txt_user.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu usuario");
        // se le asigna un placeholder al passwordField txt_password
        log.txt_password.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu contraseña");
    }

    // metodo para iniciar sesion e ingresar al sistema
    public void iniciarSesion() {
        // valida si la conexion a la base de datos es exitosa
        if (!Conexion.validarConexion()) {
            JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos");
            return;
        }
        // obtiene la informacion de los textField de la vista
        String usuario = log.txt_user.getText();
        String password = String.valueOf(log.txt_password.getPassword());
        // valida que los campos de usuario y password no sean vacios
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            // metodo que asegura que el usuario y el password sean correctos
            if (UsuarioDAO.LoginAutenticacion(usuario, password)) {
                // Hago invisible el formulario del Login
                log.setVisible(false);
                // Hago visible el formulario del Menu
                menu.setVisible(true);
                // Obtener la instancia de Credenciales
                Credenciales credenciales = Credenciales.getInstancia();
                // Establecer usuario y contraseña
                credenciales.setUsuario(usuario);
                credenciales.setContrasena(password);
            } else {
                // Mensaje de usuario y contreseña incorrectos
                JOptionPane.showMessageDialog(null, "El usuario o la contraseña son incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta el metodo de iniciarSesion
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == log.btn_IniciarSesion) {
            iniciarSesion();
        }
    }

    // keyPressed que recibe los eventos al presionar una tecla y ejecuta acciones
    @Override
    public void keyPressed(KeyEvent e) {
        // obtiene la accion del textField txt_user
        if (e.getSource() == log.txt_user) {
            // valida la tecla presionado es el ENTER
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // valida si el texto dentro del textField es vacio
                if (log.txt_user.getText().equals("")) {
                    // arroja un mensaje informativo
                    JOptionPane.showMessageDialog(null, "Debes agregar un usuario", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // si el textField no es vacio solicita el focus en el campo de password
                    log.txt_password.requestFocus();
                }
            }
        // obtiene la accion del passwordField txt_password    
        } else if (e.getSource() == log.txt_password) {
            // valida la tecla presionado es el ENTER
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // valida si el texto dentro del textField es vacio
                if (String.valueOf(log.txt_password.getPassword()).equals("")) {
                    // arroja un mensaje informativo
                    JOptionPane.showMessageDialog(null, "Debes agregar la contraseña", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // si el textField no es vacio ejecuta el metodo de iniciar sesion
                    iniciarSesion();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
