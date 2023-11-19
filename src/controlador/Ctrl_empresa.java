package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import metodos.BotonesMenu;
import metodos.Credenciales;
import metodos.Validaciones;
import modelo.Empresa;
import modelo.EmpresaDAO;
import modelo.Usuario;
import modelo.UsuarioDAO;
import vista.Menu;

public class Ctrl_empresa implements ActionListener, MouseListener, KeyListener {

    // atributos del controlador empresa 
    private Empresa emp; // atributo de tipo Empresa para asignar una instancia de este
    private Usuario user; // atributo de tipo Usuario para asignar una instancia de este
    private EmpresaDAO empDao; // atributo de tipo EmpresaDAO para asignar una instancia de este
    private UsuarioDAO userDao; // atributo de tipo UsuarioDAO para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este

    // Constructor con parámetros
    public Ctrl_empresa(Empresa emp, Usuario user, EmpresaDAO empDao, UsuarioDAO userDao, Menu menu) {
        this.emp = emp; // Se le asigna al atributo emp la instancia que llega por parametro
        this.user = user; // Se le asigna al atributo user la instancia que llega por parametro
        this.empDao = empDao; // Se le asigna al atributo empDao la instancia que llega por parametro
        this.userDao = userDao; // Se le asigna al atributo userDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btn_modificarEmp.addActionListener(this); // se le añade el ActionListener al boton btn_modificarEmp
        this.menu.btn_modificarPassword.addActionListener(this); // se le añade el ActionListener al boton btn_modificarPassword
        this.menu.btnEmpresa.addMouseListener(this); // se le añade el MouseListener al boton btnEmpresa
        styleEmpresa(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleEmpresa() {
        // se le asigna el boton de ver contraseñas a los passwordField
        menu.txtPasswordCurrent.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
        menu.txtPasswordNew.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
        menu.txtPasswordNewConfirm.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true");
    }

    // metodo para llenar la informacion de la empresa en los textField
    public void llenarInformacion() {
        Empresa contenido = empDao.informacionEmpresa();
        menu.textNitEmp.setText(contenido.getNit());
        menu.textNombreEmp.setText(contenido.getNombre());
        menu.textTelefonoEmp.setText(contenido.getTelefono());
        menu.textDireccionEmp.setText(contenido.getDireccion());
    }

    // metodo para limpiar los passwordField
    public void limpiarCampos() {
        menu.txtPasswordCurrent.setText("");
        menu.txtPasswordNew.setText("");
        menu.txtPasswordNewConfirm.setText("");
    }

    // metodo para modificar la informacion de la empresa
    public void modificarInformacion() {
        // obtiene la informacion de los textField
        String nombre = menu.textNombreEmp.getText().trim();
        String nit = menu.textNitEmp.getText().trim();
        String telefono = menu.textTelefonoEmp.getText().trim();
        String direccion = menu.textDireccionEmp.getText().trim();
        // valida que los textField no esten vacios
        if (nombre.isEmpty() || nit.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            // Instancia un objeto de tipo JPasswordField para añadirlo al JOptionPane.showConfirmDialog
            JPasswordField passwordField = new JPasswordField();
            // Muestra un cuadro de diálogo con el campo para añadir una contraseña
            int result = JOptionPane.showConfirmDialog(null, passwordField, "Ingrese su contraseña", JOptionPane.OK_CANCEL_OPTION);
            // valida si el boton presionado en cuadro de dialogo es OK
            if (result == JOptionPane.OK_OPTION) {
                char[] passwordChars = passwordField.getPassword(); // Obtener la contraseña como un arreglo de caracteres
                String password = new String(passwordChars); // Convertir el arreglo de caracteres a una cadena
                // metodo para validar las contraseñas la ingresada y la del usuario logeado
                if (validarContrasena(password)) {
                    //se establece la informacion de la empresa en el objeto emp
                    emp.setNombre(nombre);
                    emp.setNit(nit);
                    emp.setTelefono(telefono);
                    emp.setDireccion(direccion);
                    // metodo para modificar la informacion de la empresa en la base de datos
                    if (empDao.modificarEmpresa(emp)) {
                        // mensaje informativo
                        JOptionPane.showMessageDialog(null, "Los datos de la empresa han sido modficados con exito");
                    }
                } else {
                    // mensaje de error
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta. No puedes modificar datos de la empresa");
                }
            }
        }
    }

    // metodo para modificar la contraseña del usuario logeado
    public void modificarPassword() {
        // obtiene las contraseñas ingresadas por el usuario
        String passwordCurrent = String.valueOf(menu.txtPasswordCurrent.getPassword()).trim();;
        String passwordNew = String.valueOf(menu.txtPasswordNew.getPassword()).trim();;
        String passwordNewConfirm = String.valueOf(menu.txtPasswordNewConfirm.getPassword()).trim();;
        // valida que la contraseña actual no este vacia
        if (!Validaciones.validarNoVacios("Debes ingresar tu contraseña actual primero", passwordCurrent)) {
            return;
        } else {
            // valida que la contraseña nueva no este vacia
            if (!Validaciones.validarNoVacios("Ingresa la nueva contraseña", passwordNew)) {
                return;
            } else {
                // valida que la confirmacion contraseña nueva no este vacia
                if (!Validaciones.validarNoVacios("Olvidaste confirmar la contraseña, es mejor estar seguro :)", passwordNewConfirm)) {
                    return;
                } else {
                    // valida que la nueva contraseña contenga cierta cantidad de caracteres
                    if (!Validaciones.validarRangoCaracteres(passwordNew, 10, 30, "La nueva contraseña debe tener un minimo de 10 caracteres, es por seguridad")) {
                        return;
                    } else {
                        // valida que la nueva contraseña contenga ciertos criterios de seguridad
                        if (!Validaciones.validarContrasena(passwordNew)) {
                            JOptionPane.showMessageDialog(null, "La nueva contraseña debe tener minus, MAYUS, números y un caracter especial", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        } else {
                            // valida que la nueva contraseña y la confirmacion de la nueva contraseña sean iguales
                            if (!passwordNew.equals(passwordNewConfirm)) {
                                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            } else {
                                // valida que la contraseña ingreasa sea correcta
                                if (validarContrasena(passwordCurrent)) {
                                    //establece la informacion de la nueva contraseña y el usuario logeado en el objeto user
                                    user.setUsuario(obtenerUsuario());
                                    user.setPassword(passwordNew);
                                    // metodo para cambiar la contraseña de un usuario
                                    if (userDao.modificarPassword(user)) {
                                        // mensaje informativo
                                        JOptionPane.showMessageDialog(null, "La contraseña ha sido modificada con exito");
                                        // limpia los campos
                                        limpiarCampos();
                                    }
                                } else {
                                    // mensaje de error
                                    JOptionPane.showMessageDialog(null, "Contraseña actual es incorrecta");
                                    // limpia los campos
                                    limpiarCampos();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // metodo para validar la contraseña que se pasa por parametro a la contraseña del usuario logeado
    public boolean validarContrasena(String password) {
        // Obtener contraseña del usuario que se logeo al inicio
        Credenciales credenciales = Credenciales.getInstancia(); // Se obtiene la instancia de Credenciales
        String passwordLogin = credenciales.getContrasena(); // Se obtienen la contrasena usando el metodo get de Credenciales
        // valida si la contraseña ingresado y la contraseña del usuario logeado son las mismas
        if (password.equals(passwordLogin)) {
            // si son las mismas envia true
            return true;
        } else {
            // si son diferentes envia false
            return false;
        }
    }
    
    // metodo para obtener el usuario que se ha logeado en el sistema
    public String obtenerUsuario() {
        Credenciales credenciales = Credenciales.getInstancia();
        String userLogin = credenciales.getUsuario();
        return userLogin;
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_modificarEmp) {
            modificarInformacion();
            llenarInformacion();
        } else if (e.getSource() == menu.btn_modificarPassword) {
            modificarPassword();
        }
    }

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btnEmpresa) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(10);
            botones.cambiarColor(menu.btnEmpresa);
            botones.cambiarTitulo("Datos de Empresa");
            llenarInformacion();
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

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
