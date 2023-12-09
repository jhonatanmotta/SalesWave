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
import modelo.Proveedor;
import modelo.ProveedorDAO;
import vista.Menu;

public class Ctrl_proveedor implements ActionListener, MouseListener, KeyListener {

    // atributos del controlador proveedor
    private Proveedor prov; // atributo de tipo Proveedor para asignar una instancia de este
    private ProveedorDAO provDao; // atributo de tipo ProveedorDAO para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTablaProv = new DefaultTableModel();

    // Constructor con parámetros
    public Ctrl_proveedor(Proveedor prov, ProveedorDAO provDao, Menu menu) {
        this.prov = prov; // Se le asigna al atributo prov la instancia que llega por parametro
        this.provDao = provDao; // Se le asigna al atributo provDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btn_registrarProv.addActionListener(this); // se le añade el ActionListener al boton btn_registrarProv
        this.menu.btn_modificarProv.addActionListener(this); // se le añade el ActionListener al boton btn_modificarProv
        this.menu.btn_limpiarProv.addActionListener(this); // se le añade el ActionListener al boton btn_limpiarProv
        this.menu.jMenuEliminarProv.addActionListener(this); // se le añade el ActionListener al boton jMenuEliminarProv
        this.menu.jMenuHabilitarProv.addActionListener(this); // se le añade el ActionListener al boton jMenuHabilitarProv
        this.menu.textBuscarProv.addKeyListener(this); // se le añade el KeyListener al textField textBuscarProv
        this.menu.tableProveedor.addMouseListener(this); // se le añade el MouseListener a la tabla tableProveedor
        this.menu.btnProveedor.addMouseListener(this); // se le añade el MouseListener al boton btnProveedor
        styleProveedor(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleProveedor() {
        this.menu.textIdProv.setVisible(false); // el textField de id estara oculto
        // se le asigna un placeholder al textField textBuscarProv
        menu.textBuscarProv.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar proveedor");
    }

    // metodo para registrar un nuevo proveedor
    public void registrarProv() {
        // se obtiene la informacion del cliente de los textField
        String nombre = menu.textNombreProv.getText().trim();
        String apellido = menu.textApellidoProv.getText().trim();
        String direccion = menu.textDireccionProv.getText().trim();
        String telefono = menu.textTelefonoProv.getText().trim();
        // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
        if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios, excepto el apellido", nombre, direccion, telefono)) {
            return;
        } else {
            // valida que el telefono tenga minimo 10 numeros
            if (!Validaciones.validarRangoCaracteres(telefono, 10, 15, "El número de telefono debe tener un minimo de 10 caracteres")) {
                return;
            } else {
                // se establece la informacion del proveedor  en el objeto prov
                prov.setNombre(nombre);
                prov.setApellido(apellido);
                prov.setDireccion(direccion);
                prov.setTelefono(telefono);
                // metodo de insercion de datos
                if (provDao.registroProv(prov)) {
                    // limpia el contenido de la tabla
                    limpiarTabla();
                    // añade contenido a la tabla 
                    listarProv();
                    // limpia los campos
                    limpiarContenidoInput();
                    // mensaje informativo
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con exito");
                } else {
                    // mensaje de error
                    JOptionPane.showMessageDialog(null, "Error al registrar el proveedor");
                }
            }
        }
    }

    //metodo para modicar los datos del proveedor en la base de datos
    public void modificarProv() {
        // valida si se ha seleccionado una fila para modificar
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para modificar los datos de un proveedor", menu.textIdProv.getText())) {
            return;
        } else {
            // se obtiene la informacion del cliente de los textField
            int id = Integer.parseInt(menu.textIdProv.getText());
            String nombre = menu.textNombreProv.getText().trim();
            String apellido = menu.textApellidoProv.getText().trim();
            String direccion = menu.textDireccionProv.getText().trim();
            String telefono = menu.textTelefonoProv.getText().trim();
            // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
            if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios, excepto el apellido", nombre, direccion, telefono)) {
                return;
            } else {
                // valida que el telefono tenga minimo 10 numeros
                if (!Validaciones.validarRangoCaracteres(telefono, 10, 15, "El número de telefono debe tener un minimo de 10 caracteres")) {
                return;
                }
                else {
                    // se establece la informacion del proveedor en el objeto prov
                    prov.setIdProveedor(id);
                    prov.setNombre(nombre);
                    prov.setApellido(apellido);
                    prov.setDireccion(direccion);
                    prov.setTelefono(telefono);
                    // metodo para modificar el proveedor en la base de datos
                    if (provDao.modificarProv(prov)) {
                        // limpia el contenido de la tabla
                        limpiarTabla();
                        // añade contenido a la tabla 
                        listarProv();
                        // limpia los campos
                        limpiarContenidoInput();
                        // mensaje informativo
                        JOptionPane.showMessageDialog(null, "Proveedor modificado con exito");
                    } else {
                        // mensaje de error
                        JOptionPane.showMessageDialog(null, "Error al modificar el proveedor");
                    }
                }
            }
        }
    }

    // metodo para eliminar el proveedor, es decir cambiarlo de estado
    public void eliminarProv() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar un proveedor", menu.textIdProv.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdProv.getText());
            // metodo para modificar el estado del proveedor
            if (provDao.estadoProv(0, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarProv();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Proveedor eliminado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al eliminar proveedor");
            }
        }
    }

    // metodo para habilitar el proveedor, es decir cambiarlo de estado
    public void habilitarProv() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar un proveedor", menu.textIdProv.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdProv.getText());
            // metodo para modificar el estado del proveedor
            if (provDao.estadoProv(1, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarProv();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Proveedor habilitado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al habilitar proveedor");
            }
        }
    }

    // metodo para mostrar los datos en la tabla de la proveedor
    public void listarProv() {
        // lista con la informacion de la categoria
        List<Proveedor> lista = provDao.listaProv(menu.textBuscarProv.getText().trim());
        // obtiene el modelo de la tabla
        modeloTablaProv = (DefaultTableModel) menu.tableProveedor.getModel();
        // arreglo con la cantidad de columnas
        Object[] ob = new Object[5];
        // for que recorre la lista y va agregando la informacion a las filas de la tabla
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProveedor();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getApellido();
            ob[3] = lista.get(i).getTelefono();
            if (lista.get(i).getEstado() == 1) {
                ob[4] = "Habilitado";
            } else {
                ob[4] = "Deshabilitado";
            }
            modeloTablaProv.addRow(ob);
        }
        // se le pone el nuevo modelo a la tabla
        menu.tableProveedor.setModel(modeloTablaProv);

        // se oculta la columna del id
        menu.tableProveedor.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableProveedor.getColumnModel().getColumn(0).setMaxWidth(0);

        // se obtiene el header de la tabla
        JTableHeader header = menu.tableProveedor.getTableHeader();
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
        for (int i = 0; i < modeloTablaProv.getRowCount(); i++) {
            modeloTablaProv.removeRow(i);
            i -= 1;
        }
    }

    // metodo para agregar contenido a los inputs luego de dar click en un campo de la tabla
    public void agregarContenidoInput(int fila) {
        String estado = menu.tableProveedor.getValueAt(fila, 4).toString();
        if (estado == "Habilitado") {
            // pone visible el boton de eliminar proveedor
            menu.jMenuEliminarProv.setVisible(true);
            menu.jMenuHabilitarProv.setVisible(false);
        } else {
            // pone visible el boton de habilitar proveedor
            menu.jMenuHabilitarProv.setVisible(true);
            menu.jMenuEliminarProv.setVisible(false);
        }
        // inhabilita el boton de registrar y habilita el de modificar
        menu.btn_modificarProv.setEnabled(true);
        menu.btn_registrarProv.setEnabled(false);

        //obtiene el id de la tabla en la fila especificada
        int idProv = (int) menu.tableProveedor.getValueAt(fila, 0);

        // busca los datos del proveedor con respecto a su id
        List<Proveedor> lista = provDao.llenarInput(idProv);
        // se obtiene el indiice 0 de la lista
        Proveedor valor = lista.get(0);

        // agrega contenido a los inputs
        menu.textIdProv.setText(String.valueOf(valor.getIdProveedor()));
        menu.textNombreProv.setText(valor.getNombre());
        menu.textApellidoProv.setText(valor.getApellido());
        menu.textDireccionProv.setText(valor.getDireccion());
        menu.textTelefonoProv.setText(valor.getTelefono());
    }

    //metodo para limpiar el contenido de los inputs
    public void limpiarContenidoInput() {
        menu.textNombreProv.setText("");
        menu.textApellidoProv.setText("");
        menu.textDireccionProv.setText("");
        menu.textIdProv.setText("");
        menu.textTelefonoProv.setText("");
        menu.btn_registrarProv.setEnabled(true); // habilita el boton de registrar
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
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

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableProveedor) {
            int fila = menu.tableProveedor.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
        } else if (e.getSource() == menu.btnProveedor) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(5);
            botones.cambiarColor(menu.btnProveedor);
            botones.cambiarTitulo("Gestion de Proveedor");
            limpiarTabla();
            listarProv();
        }
    }

    // keyReleased que recibe los eventos al soltar una letra del teclado y ejecuta metodos
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
