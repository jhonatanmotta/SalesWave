package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import conexion.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import metodos.PlaceholderTextfield;
import modelo.UsuarioDAO;
import vista.Menu;
import vista.Login;

public class Ctrl_login implements ActionListener {

    Login log = new Login();
    Menu menu = new Menu();  

    public Ctrl_login(Login log) {
        this.log = log;
        this.log.btn_IniciarSesion.addActionListener(this);
        styleLogin();
    }
    
    public void styleLogin() {
        log.txt_password.putClientProperty(FlatClientProperties.STYLE,"" + "showRevealButton:true");
        log.txt_user.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Ingresa tu usuario");
        log.txt_password.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Ingresa tu contraseña");
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
                log.setVisible(false);
                menu.setVisible(true);
            } else {
                // Mensaje de usuario y contreseña incorrectos
                JOptionPane.showMessageDialog(null, "El usuario o la contraseña son incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    public void cerrarSesion(){
//        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de cerrar sesion?", 
//                "Pregunta", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
//        if (confirmacion == 0){
//            System.exit(0);
//        }
//    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == log.btn_IniciarSesion) {
            iniciarSesion();
        }
    }

}
