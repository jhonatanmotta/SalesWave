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

    Login log = new Login();
    Menu menu = new Menu();

    public Ctrl_login(Login log) {
        this.log = log;
        this.log.btn_IniciarSesion.addActionListener(this);
        this.log.txt_user.addKeyListener(this);
        this.log.txt_password.addKeyListener(this);
        styleLogin();
    }

    public void styleLogin() {
        log.txt_password.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
        log.txt_user.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu usuario");
        log.txt_password.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu contraseña");
    }

    public void iniciarSesion() {
        if (!Conexion.validarConexion()) {
            JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos");
            return;
        }
        String usuario = log.txt_user.getText();
        String password = String.valueOf(log.txt_password.getPassword());
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == log.btn_IniciarSesion) {
            iniciarSesion();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == log.txt_user) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (log.txt_user.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Debes agregar un usuario", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    log.txt_password.requestFocus();
                }
            }
        } else if (e.getSource() == log.txt_password) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (String.valueOf(log.txt_password.getPassword()).equals("")) {
                    JOptionPane.showMessageDialog(null, "Debes agregar la contraseña", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
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
