package controlador;

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
import modelo.Empresa;
import modelo.EmpresaDAO;
import vista.Menu;

public class Ctrl_empresa implements ActionListener, MouseListener, KeyListener {

    // atributos del controlador empresa 
    private Empresa emp; // atributo de tipo Empresa para asignar una instancia de este
    private EmpresaDAO empDao; // atributo de tipo EmpresaDAO para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este

    // metodo constructor de la clase pide 3 parametos que son instancias de Empresa, EmpresaDAO y de Menu(formulario)
    public Ctrl_empresa(Empresa emp, EmpresaDAO empDao, Menu menu) {
        this.emp = emp; // Se le asigna al atributo this.emp la instancia que llega por parametro
        this.empDao = empDao; // Se le asigna al atributo this.empDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo this.menu la instancia que llega por parametro
        this.menu.btn_modificarEmp.addActionListener(this); // se le añade el ActionListener al boton btn_modificarEmp
        this.menu.btnEmpresa.addMouseListener(this);
        styleEmpresa();
    }

    public void styleEmpresa() {
//        this.menu.textNitEmp.setEnabled(false);
//        this.menu.textNombreEmp.setEnabled(false);
//        this.menu.textTelefonoEmp.setEnabled(false);
//        this.menu.textDireccionEmp.setEnabled(false);
    }

    public void llenarInformacion() {
        Empresa contenido = empDao.informacionEmpresa();
        menu.textNitEmp.setText(contenido.getNit());
        menu.textNombreEmp.setText(contenido.getNombre());
        menu.textTelefonoEmp.setText(contenido.getTelefono());
        menu.textDireccionEmp.setText(contenido.getDireccion());
    }

//    public void estadoInputs (){
//        menu.textNitEmp.setEnabled(false);
//        menu.textNombreEmp.setEnabled(false);
//        menu.textTelefonoEmp.setEnabled(false);
//        menu.textDireccionEmp.setEnabled(false);
//    }
    public void modificarInformacion() {
        String nombre = menu.textNombreEmp.getText().trim();
        String nit = menu.textNitEmp.getText().trim();
        String telefono = menu.textTelefonoEmp.getText().trim();
        String direccion = menu.textDireccionEmp.getText().trim();
        if (nombre.isEmpty() || nit.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            // Instancia un objeto de tipo JPasswordField para añadirlo al JOptionPane.showConfirmDialog
            JPasswordField passwordField = new JPasswordField();
            // Muestra un cuadro de diálogo con el campo para añadir una contraseña
            int result = JOptionPane.showConfirmDialog(null, passwordField, "Ingrese su contraseña", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                char[] passwordChars = passwordField.getPassword(); // Obtener la contraseña como un arreglo de caracteres
                String password = new String(passwordChars); // Convertir el arreglo de caracteres a una cadena
                if (validarContrasena(password)) {
                    emp.setNombre(nombre);
                    emp.setNit(nit);
                    emp.setTelefono(telefono);
                    emp.setDireccion(direccion);
                    if (empDao.modificarEmpresa(emp)) {
                        JOptionPane.showMessageDialog(null, "Los datos de la empresa han sido modficados con exito");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta. No puedes modificar datos de la empresa");
                }
            }
        }
    }

    public boolean validarContrasena(String password) {
        // Obtener usuario y contraseña del usuario que se logeo al inicio
        Credenciales credenciales = Credenciales.getInstancia(); // Se obtiene la instancia de Credenciales
        String passwordLogin = credenciales.getContrasena(); // Se obtienen la contrasena usando el metodo get de Credenciales
        if (password.equals(passwordLogin)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_modificarEmp) {
            modificarInformacion();
            llenarInformacion();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btnEmpresa) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(10);
            botones.cambiarColor(menu.btnEmpresa);
            botones.cambiarTitulo("Datos de Empresa");
            llenarInformacion();
//            estadoInputs();
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
