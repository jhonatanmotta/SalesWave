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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import metodos.BotonesMenu;
import metodos.ComboBox;
import metodos.Validaciones;
import modelo.Cliente;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.detalleVenta;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import vista.Menu;

public class Ctrl_venta implements ActionListener, MouseListener, KeyListener {

    private ProductoDAO prodDao;
    private Menu menu;
    DefaultTableModel modeloTabla = new DefaultTableModel();
    List<detalleVenta> listaProductosVenta = new ArrayList<>();

    private detalleVenta producto;
    private int idProducto = 0;
    private int idDetalle = 1;
    private String nombreProducto = "";
    private int cantidadProducto = 0;
    private int cantidadProductoDataBase = 0;
    private double precioProducto = 0.0;
    private double subtotal = 0.0;
    private int porcentajeIva = 0;
    private double iva = 0.0;
    private double total = 0.0;
    
    //calculos generales 
    private double subtotalVenta = 0.0;
    private double ivaVenta = 0.0;
    private double totalVenta = 0.0;

    public Ctrl_venta(ProductoDAO prodDao, Menu menu) {
        this.menu = menu;
        this.prodDao = prodDao;
        this.menu.btnVenta.addMouseListener(this);
        this.menu.aniadirProdVenta.addActionListener(this);

        styleVenta();
    }

    public void styleVenta() {
        JTableHeader headerVenta = menu.tableProductosVenta.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        headerVenta.setOpaque(false);
        headerVenta.setBackground(headerColor);
        headerVenta.setFont(headerFont);
        headerVenta.setForeground(textColor);

//        this.menu.textIdProd.setVisible(false);
//        this.menu.textIdProdStock.setVisible(false);
//        this.menu.textStockAct.setEnabled(false);
        menu.comboBoxProductosVenta.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un producto");
        AutoCompleteDecorator.decorate(menu.comboBoxProductosVenta);

        menu.comboBoxClientesVenta.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un cliente");
        AutoCompleteDecorator.decorate(menu.comboBoxClientesVenta);
    }

    public void estadoCombo() {
        menu.comboBoxClientesVenta.setSelectedIndex(-1);
        menu.comboBoxProductosVenta.setSelectedIndex(-1);
    }

    public double calcularIva(double precio, int cantidad, int porcentajeIva) {
        double totalIva;
        if (porcentajeIva == 0) {
            totalIva = 0.0;
        } else {
            totalIva = (precio * cantidad) * (porcentajeIva / 100.0);
        }
        return totalIva;
    }

    public void limpiarDatos() {
        menu.comboBoxProductosVenta.setSelectedIndex(-1);
        menu.textCantidadProdVenta.setText("");
    }

//    public static void main(String[] args) {
//        double ivaCalculado = calcularIva(10000.0, 3, 19);
//        System.out.println(ivaCalculado + " hola");
//    }
    public void agregarDetalleVenta() {

        int indexSeleccionado = menu.comboBoxProductosVenta.getSelectedIndex();
        String cantidadProd = menu.textCantidadProdVenta.getText();
        if (indexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductosVenta.getSelectedItem();
            if (!Validaciones.validarNoVacios("Debes ingresar una cantidad", cantidadProd)) {
                return;
            } else {
                if (!Validaciones.validarParseoAEntero(cantidadProd, "La cantidad debe ser un valor numerico")) {
                    return;
                } else {
                    if (!Validaciones.validarNumeroEnteroNoNegativo(cantidadProd, "La cantidad debe ser un valor mayor a cero")) {
                        return;
                    } else {
                        cantidadProducto = Integer.parseInt(cantidadProd);
                        idProducto = productoSeleccionado.getId();
                        Producto datosProducto = prodDao.buscarProducto(idProducto);
                        nombreProducto = datosProducto.getNombre();
                        cantidadProductoDataBase = datosProducto.getCantidad();
                        precioProducto = datosProducto.getPrecioVenta();
                        porcentajeIva = datosProducto.getIva();
                        iva = calcularIva(precioProducto, cantidadProducto, porcentajeIva);
                        if (!buscarValorTabla(nombreProducto)) {
                            if (cantidadProducto <= cantidadProductoDataBase) {
                                iva = (double) Math.round(subtotal * 100) / 100;
                                subtotal = precioProducto * cantidadProducto;
                                subtotal = (double) Math.round(subtotal * 100) / 100;
                                total = subtotal + iva;
                                total = (double) Math.round(total * 100) / 100;
                                producto = new detalleVenta(idDetalle, 1, idProducto, nombreProducto, cantidadProducto, precioProducto, subtotal, iva, total, 1);
                                listaProductosVenta.add(producto);
                                idDetalle++;
                                limpiarDatos();
                                listarVenta();
                                calcularCantidades();
                            } else {
                                JOptionPane.showMessageDialog(null, "La cantidad ingresada supera el stock disponible", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "El producto ya ha sido agregado", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        }
    }

    public void calcularCantidades () {
        double subtotalVenta = 0.0;
        double ivaVenta = 0.0;
        double totalVenta = 0.0;
        for (detalleVenta valor : listaProductosVenta) {
            subtotalVenta += valor.getSubtotal();
            ivaVenta += valor.getIva();
            totalVenta += valor.getTotalPagar();
        }
        
    }
    
    public boolean buscarValorTabla(String productoBusqueda) {
        boolean encontrado = false;
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            Object valor = modeloTabla.getValueAt(fila, 1);
            if (valor != null && valor.toString().equals(productoBusqueda)) {
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }

    public void listarVenta() {
        modeloTabla = (DefaultTableModel) menu.tableProductosVenta.getModel();
        modeloTabla.setRowCount(listaProductosVenta.size());
//        Object[] ob = new Object[7];
        for (int i = 0; i < listaProductosVenta.size(); i++) {
            modeloTabla.setValueAt(i + 1, i, 0);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getNombreProducto(), i, 1);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getCantidad(), i, 2);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getPrecioUnitario(), i, 3);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getSubtotal(), i, 4);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getIva(), i, 5);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getTotalPagar(), i, 6);
        }
        menu.tableProductosVenta.setModel(modeloTabla);
    }

    public boolean validacionFormatoNumero(String valor) {
        try {
            int num = Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void llenarComboBox() {
        List<Producto> listaProductos = prodDao.listarComboProducto();
        for (int i = 0; i < listaProductos.size(); i++) {
            int idProd = listaProductos.get(i).getIdProducto();
            String nombreProd = listaProductos.get(i).getNombre();
            menu.comboBoxProductosVenta.addItem(new ComboBox(idProd, nombreProd));
        }

        List<Cliente> listaClientes = prodDao.listarComboCliente();
        for (int i = 0; i < listaClientes.size(); i++) {
            int idCliente = listaClientes.get(i).getIdCliente();
            String nombreCliente = listaClientes.get(i).getNombre();
            String apellidoCliente = listaClientes.get(i).getApellido();
            menu.comboBoxClientesVenta.addItem(new ComboBox(idCliente, nombreCliente + " " + apellidoCliente));
        }
    }

    public void eliminarItemsCombox() {
        menu.comboBoxProductosVenta.removeAllItems();
        menu.comboBoxClientesVenta.removeAllItems();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.aniadirProdVenta) {
//            menu.textCantidadProdVenta.requestFocus();
            agregarDetalleVenta();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btnVenta) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(6);
            botones.cambiarColor(menu.btnVenta);
            botones.cambiarTitulo("Venta");
            eliminarItemsCombox();
            llenarComboBox();
            estadoCombo();
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
