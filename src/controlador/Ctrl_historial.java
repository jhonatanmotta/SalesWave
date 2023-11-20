package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import metodos.BotonesMenu;
import modelo.ProductoDAO;
import modelo.encabezadoVenta;
import vista.Menu;

public class Ctrl_historial implements ActionListener, MouseListener, KeyListener {
    
    // atributos del controlador historial de ventas
    private ProductoDAO prodDao; // atributo de tipo ProductoDAO para asignar una instancia de este
    private encabezadoVenta encabezado; // atributo de tipo encabezadoVenta para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    List<encabezadoVenta> listaVentas = new ArrayList<>();
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTablaHistorial = new DefaultTableModel();
    
    private int filaSeleccionada = -1; //filaEliminar por defecto
    
    // Constructor con parámetros
    public Ctrl_historial(ProductoDAO prodDao, encabezadoVenta encabezado, Menu menu) {
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.prodDao = prodDao; // Se le asigna al atributo prodDao la instancia que llega por parametro
        this.encabezado = encabezado; // Se le asigna al atributo encabezado la instancia que llega por parametro
        this.menu.btnHistorial.addMouseListener(this); // se le añade el MouseListener al boton btnHistorial
        this.menu.btn_InicioHistorial.addMouseListener(this); // se le añade el MouseListener al boton btn_InicioHistorial
        this.menu.btn_buscarFactura.addActionListener(this); // se le añade el ActionListener al boton btn_buscarFactura
        this.menu.btn_abrirFactura.addActionListener(this); // se le añade el ActionListener al boton btn_abrirFactura
        this.menu.tableHistorial.addMouseListener(this); // se le añade el MouseListener a la tabla tableHistorial
        styleHistorial(); // metodo para inicializar los estilos del JPanel
    }

    public void styleHistorial() {
        // se le da el estilo a la tabla del historial
        JTableHeader headerVenta = menu.tableHistorial.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        headerVenta.setOpaque(false);
        headerVenta.setBackground(headerColor);
        headerVenta.setFont(headerFont);
        headerVenta.setForeground(textColor);
        menu.tableHistorial.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableHistorial.getColumnModel().getColumn(0).setMaxWidth(0);
         // se le asigna un placeholder al textField textBuscarClientHistorial
        menu.textBuscarClientHistorial.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Documento cliente");
    }

    // metodo para listar todas las ventas realizadas recien se ingresa a la vista
    public void listaFacturasInicial() {
        // se obtiene las variables de la vista
//        String cedula = menu.textBuscarClientHistorial.getText().trim();
        String cedula = "";
        String fechaInicio = "";
        String fechaFinal = "";

        // se verifica el estado de los JDate y si son diferentes de null se le asigna la fecha a las varibles fechaInicio, fechaFinal
        if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
        }

        // lista de todos los registros de las ventas que devuelve la consulta a la base de datos
        List<encabezadoVenta> lista = prodDao.listaVentas(fechaInicio, fechaFinal, cedula);
        // se obtiene el modelo de la tabla de historial de ventas
        modeloTablaHistorial = (DefaultTableModel) menu.tableHistorial.getModel();
        // se crea un arreglo con la cantidad de columnas de la tabla
        Object[] ob = new Object[5];
        // for que recorre la lista y va asignando cada valor a las filas y columnas correspondientes
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdEncabezadoVenta();
            ob[1] = i + 1;
            ob[2] = lista.get(i).getNombreCliente();
            ob[3] = lista.get(i).getValorPagar();
            ob[4] = lista.get(i).getFechaVenta();
            modeloTablaHistorial.addRow(ob);
        }
        // se le añade el nuevo modelo a la tabla
        menu.tableHistorial.setModel(modeloTablaHistorial);
    }

    // metodo para validar que la cedula a buscar exista en la base de datos
    public boolean validarCedula(String cedula) {
        // verifica que la cedula no sea un String vacio
        if (!cedula.isEmpty()) {
            // verifica si la cedula existe con un consulta a la base de datos
            if (!prodDao.buscarCliente(cedula)) {
                JOptionPane.showMessageDialog(null, "La cedula ingresada no pertenece a ningun cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }

    // valida que el cliente haya realizado compras en ese rango de fechas
    public boolean validarFechaCliente(String fechaInicio, String fechaFinal, String cedula) {
        // verifica que las varibles no sean un String vacio
        if (!prodDao.buscarClienteFecha(fechaInicio, fechaFinal, cedula)) {
            JOptionPane.showMessageDialog(null, "No existen compras de este cliente en el rango de fechas ingresado", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    // valida que si hay compras en un rango de fechas especificado
    public boolean validarFecha(String fechaInicio, String fechaFinal) {
        // valida que en las fechas haya ventas con una consulta a la base de datos
        if (!prodDao.buscarFecha(fechaInicio, fechaFinal)) {
            JOptionPane.showMessageDialog(null, "No hay ventas en este rango de fechas", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    // lista las facturas que haya con parametros especificados
    public void listarFactura() {
        // se obtiene las variables de la vista
        String cedula = menu.textBuscarClientHistorial.getText().trim();
        String fechaInicio = "";
        String fechaFinal = "";

        // se verifica el estado de los JDate y si son diferentes de null se le asigna la fecha a las varibles fechaInicialValidacion, 
        //fechaFinalValidacion
        if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
            String fechaInicialValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
            String fechaFinalValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
            // se valida que hayan ventas en el rango de fechas
            if (validarFecha(fechaInicialValidacion, fechaFinalValidacion)) {
                // si no hay ventas se estable los JDate a null
                menu.jDateInicial.setDate(null);
                menu.jDateFinal.setDate(null);
            }
        }

        // valida que la cedula exista en la base de datos
        if (validarCedula(cedula)) {
            // si la fecha no existe hace el textField de la cedula vacio
            menu.textBuscarClientHistorial.setText("");
            // la varible local sera vacia
            cedula = "";
        }

        // valida que cedula no sea String vacio
        if (!cedula.isEmpty()) {
            // se verifica el estado de los JDate y si son diferentes de null se le asigna la fecha a las varibles fechaInicialValidacion, 
            //fechaFinalValidacion
            if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
                String fechaInicialValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
                String fechaFinalValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
                // se valida que hayan ventas de un cliente en el rango de fechas especificado
                if (validarFechaCliente(fechaInicialValidacion, fechaFinalValidacion, cedula)) {
                    menu.jDateInicial.setDate(null);
                    menu.jDateFinal.setDate(null);
                }
            }
        }
        
        // se verifica el estado de los JDate y si son diferentes de null se le asigna la fecha a las varibles fechaInicio, 
        //fechaFinal
        if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
                fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
                fechaFinal = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
        }

        // lista de las ventas ya sean filtradas o con todos los registros
        List<encabezadoVenta> lista = prodDao.listaVentas(fechaInicio, fechaFinal, cedula);
        modeloTablaHistorial = (DefaultTableModel) menu.tableHistorial.getModel();
        // se crea un arreglo con la cantidad de columnas de la tabla
        Object[] ob = new Object[5];
        // for que recorre la lista y va asignando cada valor a las filas y columnas correspondientes
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdEncabezadoVenta();
            ob[1] = i + 1;
            ob[2] = lista.get(i).getNombreCliente();
            ob[3] = lista.get(i).getValorPagar();
            ob[4] = lista.get(i).getFechaVenta();
            modeloTablaHistorial.addRow(ob);
        }
        // se le añade el nuevo modelo a la tabla
        menu.tableHistorial.setModel(modeloTablaHistorial);
    }

    // metodo para abir una factura luego de ser seleccionada una fila de la tabla
    public void abirFactura () throws IOException {
        // valida si la fila es diferente de -1
        if (filaSeleccionada != -1) {
            // obtiene el id de la cabecera
        int id = Integer.parseInt(menu.tableHistorial.getValueAt(filaSeleccionada, 0).toString());
            //obtiene el nombre del cliente
            String nombreCliente = menu.tableHistorial.getValueAt(filaSeleccionada, 2).toString();
            // obtiene la fecha de la factura
            String fecha = menu.tableHistorial.getValueAt(filaSeleccionada, 4).toString();
            // concatena la informacion para dar el nombre a la factura que se desea buscar
            File archivo = new File("src/facturas/Factura_" + id + "_" + nombreCliente + "_" + obtenerFecha(fecha) + ".pdf");
            // Se utiliza la clase Desktop para abrir la factura en la aplicación predeterminada para archivos PDF
            Desktop.getDesktop().open(archivo);
        } else if (filaSeleccionada == -1) {
            // si no se ha seleccionado ninguna fila de la tabla orroja un cuadro de dialogo
            JOptionPane.showMessageDialog(null, "Debes selecionar una factura primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // se obtiene la fecha en el formato que esta el nombre del archivo 
     public String obtenerFecha(String fecha) {
         // nueva fecha con "_" de separacion
        String fechaNueva = "";
        // for que recorre el String de la fecha
        for (int i = 0; i < fecha.length(); i++) {
            // valida si el caracter es un "-"
            if (fecha.charAt(i) == '-') {
                // cambia un "-" a "_"
                fechaNueva = fecha.replace('-', '_');
            }
        }
        // devuelve la fecha
        return fechaNueva;
    }
     
    // metodo para limpiar la tabla 
    public void limpiarTabla() {
        for (int i = 0; i < modeloTablaHistorial.getRowCount(); i++) {
            modeloTablaHistorial.removeRow(i);
            i -= 1;
        }
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
    @Override
    public void actionPerformed(ActionEvent e) {
        // accion para buscar una venta con parametros
        if (e.getSource() == menu.btn_buscarFactura) {
            limpiarTabla();
            listarFactura();
        // accion para abrir una factura
        } else if (e.getSource() == menu.btn_abrirFactura) {
            try {
                abirFactura();
            } catch (IOException ex) {
                Logger.getLogger(Ctrl_historial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
    @Override
    public void mouseClicked(MouseEvent e) {
        // accion de ir a la vista del historial en el menu del sistema
        if (e.getSource() == menu.btnHistorial) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(7);
            botones.cambiarColor(menu.btnHistorial);
            botones.cambiarTitulo("Historial de Facturas");
            limpiarTabla();
            listaFacturasInicial();
        // accion de click en la tabla para luego abrir la factura
        } else if (e.getSource() == menu.tableHistorial) {
            filaSeleccionada = menu.tableHistorial.rowAtPoint(e.getPoint());
        // accion de ir a historial desde el inicio del sistema    
        } else if (e.getSource() == menu.btn_InicioHistorial) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(7);
            botones.cambiarColor(menu.btnHistorial);
            botones.cambiarTitulo("Historial de Facturas");
            limpiarTabla();
            listaFacturasInicial();
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
