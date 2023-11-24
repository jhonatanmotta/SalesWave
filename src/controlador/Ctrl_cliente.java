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
import metodos.Validaciones;
import modelo.Cliente;
import modelo.ClienteDAO;
import vista.Menu;

public class Ctrl_cliente implements ActionListener, MouseListener, KeyListener {

    // atributos del controlador cliente
    private Cliente client; // atributo de tipo Cliente para asignar una instancia de este
    private ClienteDAO clientDao; // atributo de tipo ClienteDAO para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTablaCliente = new DefaultTableModel();

    // Constructor con parámetros
    public Ctrl_cliente(Cliente client, ClienteDAO clientDao, Menu menu) {
        this.client = client; // Se le asigna al atributo client la instancia que llega por parametro
        this.clientDao = clientDao; // Se le asigna al atributo clientDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btn_registrarCliente.addActionListener(this); // se le añade el ActionListener al boton btn_registrarCliente
        this.menu.btn_modificarCliente.addActionListener(this); // se le añade el ActionListener al boton btn_modificarCliente
        this.menu.btn_limpiarCliente.addActionListener(this); // se le añade el ActionListener al boton btn_limpiarCliente
        this.menu.jMenuEliminarCliente.addActionListener(this); // se le añade el ActionListener al boton jMenuEliminarCliente
        this.menu.jMenuHabilitarCliente.addActionListener(this); // se le añade el ActionListener al boton jMenuHabilitarCliente
        this.menu.textBuscarCliente.addKeyListener(this); // se le añade el KeyListener al textField textBuscarCliente
        this.menu.tableCliente.addMouseListener(this); // se le añade el MouseListener a la tabla tableCliente
        this.menu.btnCliente.addMouseListener(this); // se le añade el MouseListener al boton btnCliente
        styleCliente(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleCliente() {
        this.menu.textIdCliente.setVisible(false); // el textField de id estara oculto
        // se le asigna un placeholder al textField textBuscarCliente
        menu.textBuscarCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar cliente");
    }

    // metodo para registrar un nuevo cliente
    public void registrarCliente() {
        // se obtiene la informacion del cliente de los textField
        String nombre = menu.textNombreCliente.getText().trim();
        String apellido = menu.textApellidoCliente.getText().trim();
        String cedula = menu.textCedulaCliente.getText().trim();
        String telefono = menu.textTelefonoCliente.getText().trim();
        // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
        if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombre, apellido, cedula, telefono)) {
            return;
        } else {
            // metodo que valida que no exista la cedula en la base de datos
            if (clientDao.validarCedula(cedula)) {
                JOptionPane.showMessageDialog(null, "Parece que este numero de cedula esta asociado a otro usuario", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // valida que el numero de cedula sea numerico
                if (!Validaciones.validarParseoAEntero(cedula, "El número de cedula debe contener solo valores numericos")) {
                    return;
                } else {
                    // valida el rango de caracteres de la cedula 
                    if (!Validaciones.validarRangoCaracteres(cedula, 8, 15, "La cedula debe tener un minimo de 8 caracteres") || !Validaciones.validarRangoCaracteres(telefono, 10, 15, "El número de telefono debe tener un minimo de 10 caracteres")) {
                        return;
                    } else {
                        // se establece la informacion del cliente en el objeto client
                        client.setNombre(nombre);
                        client.setApellido(apellido);
                        client.setCedula(cedula);
                        client.setTelefono(telefono);
                        // metodo de insercion de datos
                        if (clientDao.registroCliente(client)) {
                            // limpia el contenido de la tabla
                            limpiarTabla();
                            // añade contenido a la tabla 
                            listarCliente();
                            // limpia los campos
                            limpiarContenidoInput();
                            // mensaje informativo
                            JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
                        } else {
                            // mensaje de error
                            JOptionPane.showMessageDialog(null, "Error al registrar el usuario");
                        }
                    }
                }
            }
        }
    }

    //metodo para modicar los datos del cliente en la base de datos   
    public void modificarCliente() {
        // valida si se ha seleccionado una fila para modificar
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para modificar los datos del cliente", menu.textIdCliente.getText())) {
            return;
        } else {
            // se obtiene la informacion del cliente de los textField
            int id = Integer.parseInt(menu.textIdCliente.getText());
            String nombre = menu.textNombreCliente.getText().trim();
            String apellido = menu.textApellidoCliente.getText().trim();
            String cedula = menu.textCedulaCliente.getText().trim();
            String telefono = menu.textTelefonoCliente.getText().trim();
            // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
            if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombre, apellido, cedula, telefono)) {
                return;
            } else {
                // valida que el numero de cedula sea numerico
                if (!Validaciones.validarParseoAEntero(cedula, "El número de cedula debe contener solo valores numericos")) {
                    return;
                } else {
                    // valida el rango de caracteres de la cedula 
                    if (!Validaciones.validarRangoCaracteres(cedula, 8, 15, "La cedula debe tener un minimo de 8 caracteres") || !Validaciones.validarRangoCaracteres(telefono, 10, 15, "El número de telefono debe tener un minimo de 10 caracteres")) {
                        return;
                    } else {
                        // se establece la informacion del cliente en el objeto client
                        client.setIdCliente(id);
                        client.setNombre(nombre);
                        client.setApellido(apellido);
                        client.setCedula(cedula);
                        client.setTelefono(telefono);
                        // metodo para modificar el cliente en la base de datos
                        if (clientDao.modificarCliente(client)) {
                            // limpia el contenido de la tabla
                            limpiarTabla();
                            // añade contenido a la tabla 
                            listarCliente();
                            // limpia los campos
                            limpiarContenidoInput();
                            // mensaje informativo
                            JOptionPane.showMessageDialog(null, "Cliente modificado con exito");
                        } else {
                            // mensaje de error
                            JOptionPane.showMessageDialog(null, "Error al modificar el cliente");
                        }
                    }
                }
            }
        }
    }

    // metodo para eliminar el cliente, es decir cambiarlo de estado
    public void eliminarCliente() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar un cliente", menu.textIdCliente.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdCliente.getText());
            // metodo para modificar el estado del cliente
            if (clientDao.estadoCliente(0, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarCliente();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Cliente eliminado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al eliminar cliente");
            }
        }
    }

    // metodo para habilitar el cliente, es decir cambiarlo de estado
    public void habilitarCliente() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar un cliente", menu.textIdCliente.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdCliente.getText());
            // metodo para modificar el estado del cliente
            if (clientDao.estadoCliente(1, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarCliente();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Cliente habilitado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al habilitar cliente");
            }
        }
    }
    
    // metodo para mostrar los datos en la tabla de la cliente
    public void listarCliente() {
        // lista con la informacion de la categoria
        List<Cliente> lista = clientDao.listaClientes(menu.textBuscarCliente.getText().trim());
        // obtiene el modelo de la tabla
        modeloTablaCliente = (DefaultTableModel) menu.tableCliente.getModel();
        // arreglo con la cantidad de columnas
        Object[] ob = new Object[6];
        // for que recorre la lista y va agregando la informacion a las filas de la tabla
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
        // se le pone el nuevo modelo a la tabla
        menu.tableCliente.setModel(modeloTablaCliente);
        
        // se oculta la columna del id
        menu.tableCliente.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableCliente.getColumnModel().getColumn(0).setMaxWidth(0);

        // se obtiene el header de la tabla
        JTableHeader header = menu.tableCliente.getTableHeader();
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
        for (int i = 0; i < modeloTablaCliente.getRowCount(); i++) {
            modeloTablaCliente.removeRow(i);
            i -= 1;
        }
    }

    // metodo para agregar contenido a los inputs luego de dar click en un campo de la tabla
    public void agregarContenidoInput(int fila) {
        String estado = menu.tableCliente.getValueAt(fila, 5).toString();
        if (estado == "Habilitado") {
            // pone visible el boton de eliminar cliente
            menu.jMenuEliminarCliente.setVisible(true);
            menu.jMenuHabilitarCliente.setVisible(false);
        } else {
            // pone visible el boton de habilitar cliente
            menu.jMenuHabilitarCliente.setVisible(true);
            menu.jMenuEliminarCliente.setVisible(false);
        }
        // inhabilita el boton de registrar y habilita el de modificar
        menu.btn_modificarCliente.setEnabled(true);
        menu.btn_registrarCliente.setEnabled(false);
        // agrega contenido a los inputs
        menu.textIdCliente.setText(menu.tableCliente.getValueAt(fila, 0).toString());
        menu.textNombreCliente.setText(menu.tableCliente.getValueAt(fila, 1).toString());
        menu.textApellidoCliente.setText(menu.tableCliente.getValueAt(fila, 2).toString());
        menu.textCedulaCliente.setText(menu.tableCliente.getValueAt(fila, 3).toString());
        menu.textTelefonoCliente.setText(menu.tableCliente.getValueAt(fila, 4).toString());
    }

    //metodo para limpiar el contenido de los inputs
    public void limpiarContenidoInput() {
        menu.textNombreCliente.setText("");
        menu.textApellidoCliente.setText("");
        menu.textCedulaCliente.setText("");
        menu.textIdCliente.setText("");
        menu.textTelefonoCliente.setText("");
        menu.btn_registrarCliente.setEnabled(true); // habilita el boton de registrar
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
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

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
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

    // keyReleased que recibe los eventos al soltar una letra del teclado y ejecuta metodos
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
