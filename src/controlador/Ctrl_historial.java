package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import com.toedter.calendar.JDateChooser;
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
import java.util.Date;
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

    private ProductoDAO prodDao;
    private encabezadoVenta encabezado;
    private Menu menu;
    List<encabezadoVenta> listaVentas = new ArrayList<>();
    DefaultTableModel modeloTablaHistorial = new DefaultTableModel();
    private int filaSeleccionada = -1;
    
    public Ctrl_historial(ProductoDAO prodDao, encabezadoVenta encabezado, Menu menu) {
        this.menu = menu;
        this.prodDao = prodDao;
        this.encabezado = encabezado;
        this.menu.btnHistorial.addMouseListener(this);
        this.menu.btn_buscarFactura.addActionListener(this);
        this.menu.btn_abrirFactura.addActionListener(this);
        this.menu.tableHistorial.addMouseListener(this);
        styleHistorial();
    }

    public void styleHistorial() {
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
        menu.textBuscarClientHistorial.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Documento Cliente");
    }

    public void listaFacturasInicial() {
        String cedula = menu.textBuscarClientHistorial.getText().trim();
        String fechaInicio = "";
        String fechaFinal = "";

        if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
        }

        List<encabezadoVenta> lista = prodDao.listaVentas(fechaInicio, fechaFinal, cedula);
        modeloTablaHistorial = (DefaultTableModel) menu.tableHistorial.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdEncabezadoVenta();
            ob[1] = i + 1;
            ob[2] = lista.get(i).getNombreCliente();
            ob[3] = lista.get(i).getValorPagar();
            ob[4] = lista.get(i).getFechaVenta();
            modeloTablaHistorial.addRow(ob);
        }
        menu.tableHistorial.setModel(modeloTablaHistorial);
    }

    public boolean validarCedula(String cedula) {
        if (!cedula.isEmpty()) {
            if (!prodDao.buscarCliente(cedula)) {
                JOptionPane.showMessageDialog(null, "La cedula ingresada no pertenece a ningun cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }

    public boolean validarFechaCliente(String fechaInicio, String fechaFinal, String cedula) {
        if (!prodDao.buscarClienteFecha(fechaInicio, fechaFinal, cedula)) {
            JOptionPane.showMessageDialog(null, "No existen compras de este cliente en el rando de fechas ingresado", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    public boolean validarFecha(String fechaInicio, String fechaFinal) {
        if (!prodDao.buscarFecha(fechaInicio, fechaFinal)) {
            JOptionPane.showMessageDialog(null, "No hay ventas en este rango de fechas", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    public void listarFactura() {
        String cedula = menu.textBuscarClientHistorial.getText().trim();
        String fechaInicio = "";
        String fechaFinal = "";
        boolean validacion = false;

        if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
            String fechaInicialValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
            String fechaFinalValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
            if (validarFecha(fechaInicialValidacion, fechaFinalValidacion)) {
                menu.jDateInicial.setDate(null);
                menu.jDateFinal.setDate(null);
                validacion = false;
            }
        }

        if (validarCedula(cedula)) {
            menu.textBuscarClientHistorial.setText("");
            cedula = "";
        }

        if (!cedula.isEmpty()) {
            if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
                String fechaInicialValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
                String fechaFinalValidacion = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
                if (validarFechaCliente(fechaInicialValidacion, fechaFinalValidacion, cedula)) {
                    menu.jDateInicial.setDate(null);
                    menu.jDateFinal.setDate(null);
                }
            }
        }
        if (validacion) {
            if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
                fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
                fechaFinal = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
            }
        } else {
            if (menu.jDateInicial.getDate() != null && menu.jDateInicial.getDate() != null) {
                fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateInicial.getDate());
                fechaFinal = new SimpleDateFormat("yyyy-MM-dd").format(menu.jDateFinal.getDate());
            }
        }

        List<encabezadoVenta> lista = prodDao.listaVentas(fechaInicio, fechaFinal, cedula);
        modeloTablaHistorial = (DefaultTableModel) menu.tableHistorial.getModel();
        Object[] ob = new Object[5];
        for (int i = 0;
                i < lista.size();
                i++) {
            ob[0] = lista.get(i).getIdEncabezadoVenta();
            ob[1] = i + 1;
            ob[2] = lista.get(i).getNombreCliente();
            ob[3] = lista.get(i).getValorPagar();
            ob[4] = lista.get(i).getFechaVenta();
            modeloTablaHistorial.addRow(ob);
        }
        menu.tableHistorial.setModel(modeloTablaHistorial);
    }

    public void abirFactura () throws IOException {
        if (filaSeleccionada != -1) {
        int id = Integer.parseInt(menu.tableHistorial.getValueAt(filaSeleccionada, 0).toString());
            String nombreCliente = menu.tableHistorial.getValueAt(filaSeleccionada, 2).toString();
            String fecha = menu.tableHistorial.getValueAt(filaSeleccionada, 4).toString();
            File archivo = new File("src/facturas/Factura_" + id + "_" + nombreCliente + "_" + obtenerFecha(fecha) + ".pdf");
            Desktop.getDesktop().open(archivo);
        }
    }
        
     public String obtenerFecha(String fecha) {
        String fechaNueva = "";
        for (int i = 0; i < fecha.length(); i++) {
            if (fecha.charAt(i) == '-') {
                fechaNueva = fecha.replace('-', '_');
            }
        }
        return fechaNueva;
    }
    public void limpiarTabla() {
        for (int i = 0; i < modeloTablaHistorial.getRowCount(); i++) {
            modeloTablaHistorial.removeRow(i);
            i -= 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_buscarFactura) {
            limpiarTabla();
            listarFactura();
        } else if (e.getSource() == menu.btn_abrirFactura) {
            try {
                abirFactura();
            } catch (IOException ex) {
                Logger.getLogger(Ctrl_historial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btnHistorial) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(7);
            botones.cambiarColor(menu.btnHistorial);
            botones.cambiarTitulo("Historial de Facturas");
            limpiarTabla();
            listaFacturasInicial();
        } else if (e.getSource() == menu.tableHistorial) {
            filaSeleccionada = menu.tableHistorial.rowAtPoint(e.getPoint());
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
