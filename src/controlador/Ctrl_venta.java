package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import com.itextpdf.text.BadElementException;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
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
import metodos.ComboBox;
import metodos.Credenciales;
import metodos.GeneradorPDFFactura;
import metodos.Validaciones;
import modelo.Cliente;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.detalleVenta;
import modelo.encabezadoVenta;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import vista.Menu;

public class Ctrl_venta implements ActionListener, MouseListener, KeyListener {

    private ProductoDAO prodDao;
    private encabezadoVenta encabezado;
    private detalleVenta detalle;
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

    private int filaEliminar = -1;

    public Ctrl_venta(ProductoDAO prodDao, encabezadoVenta encabezado, detalleVenta detalle, Menu menu) {
        this.menu = menu;
        this.prodDao = prodDao;
        this.encabezado = encabezado;
        this.detalle = detalle;
        this.menu.btnVenta.addMouseListener(this);
        this.menu.aniadirProdVenta.addActionListener(this);
        this.menu.pagarVenta.addActionListener(this);
        this.menu.tableProductosVenta.addMouseListener(this);
        this.menu.jMenuEliminarVentaProd.addActionListener(this);
        this.menu.generarVenta.addActionListener(this);
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

        menu.textEfectivoVenta.setEnabled(false);
        menu.pagarVenta.setEnabled(false);
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
        menu.textEfectivoVenta.setText("");
        menu.cambioVenta.setText("0.0");
    }

    public void resetDatosVenta() {
        menu.comboBoxProductosVenta.setSelectedIndex(-1);
        menu.comboBoxClientesVenta.setSelectedIndex(-1);
        menu.textCantidadProdVenta.setText("");
        menu.valorSubtotal.setText("0.0");
        menu.valorTotalPago.setText("0.0");
        menu.valorIva.setText("0.0");
        menu.textEfectivoVenta.setText("");
        menu.cambioVenta.setText("0.0");
        idDetalle = 1;
        menu.textEfectivoVenta.setEnabled(false);
        menu.pagarVenta.setEnabled(false);
    }

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
//                                iva = (double) Math.round(subtotal * 100) / 100;
                                subtotal = precioProducto * cantidadProducto;
//                                subtotal = (double) Math.round(subtotal * 100) / 100;
                                total = subtotal + iva;
//                                total = (double) Math.round(total * 100) / 100;
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

    public void generarVenta() throws FileNotFoundException, BadElementException, IOException {
        Date date = new Date();
        String fechaActual = new SimpleDateFormat("yyyy/MM/dd").format(date);
        int indexSeleccionado = menu.comboBoxClientesVenta.getSelectedIndex();
        if (indexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            if (listaProductosVenta.size() > 0) {
                Credenciales credenciales = Credenciales.getInstancia(); // Se obtiene la instancia de Credenciales
                String usuarioLogeado = credenciales.getUsuario(); // Se obtienen el usuario usando el metodo get de Credenciales
                int idUsuario = prodDao.buscarUsuario(usuarioLogeado);
                ComboBox clienteSeleccionado = (ComboBox) menu.comboBoxClientesVenta.getSelectedItem();
                encabezado.setIdCliente_fk(clienteSeleccionado.getId());
                encabezado.setIdEmpresa_fk(1);
                encabezado.setIdUsuario_fk(idUsuario);
                encabezado.setValorPagar(totalVenta);
                encabezado.setFechaVenta(fechaActual);
                if (prodDao.registroEncabezado(encabezado)) {
                    int lastIdEncabezadoVenta = prodDao.obtenerIdEncabezado();
                    for (detalleVenta producto : listaProductosVenta) {
                        detalle.setIdEncabezadoVenta_fk(lastIdEncabezadoVenta);
                        detalle.setIdProducto_fk(producto.getIdProducto_fk());
                        detalle.setCantidad(producto.getCantidad());
                        detalle.setPrecioUnitario(producto.getPrecioUnitario());
                        detalle.setSubtotal(producto.getSubtotal());
                        detalle.setIva(producto.getIva());
                        detalle.setTotalPagar(producto.getTotalPagar());
                        if (prodDao.registroDetalle(detalle)) {
//                            resetDatosVenta();
                            actualizarStock(producto.getIdProducto_fk(), producto.getCantidad());
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al registrar el detalle de la venta del producto" + producto.getNombreProducto(), "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    JOptionPane.showMessageDialog(null, "La venta ha sido facturada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    GeneradorPDFFactura factura = new GeneradorPDFFactura();
                    factura.setNombreCliente(clienteSeleccionado.getNombre());
                    factura.setNombreUsuario(usuarioLogeado);
                    factura.setIdEmpresa(1);
                    factura.setNumVenta(lastIdEncabezadoVenta);
                    factura.setFechaFactura(fechaActual);
                    factura.setListaProductos(menu.tableProductosVenta);
                    factura.setSubtotal(subtotalVenta);
                    factura.setIva(ivaVenta);
                    factura.setTotal(totalVenta);
                    factura.generarFactura();
                    resetDatosVenta();
                    listaProductosVenta.clear();
                    listarVenta();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el encabezado de la venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay productos agregados en la cola", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void GeneradorPDFFactura () { 
    }
    
    public void calcularCantidades() {
        subtotalVenta = 0.0;
        ivaVenta = 0.0;
        totalVenta = 0.0;
        for (detalleVenta valor : listaProductosVenta) {
            subtotalVenta += valor.getSubtotal();
            ivaVenta += valor.getIva();
            totalVenta += valor.getTotalPagar();
        }
        menu.valorSubtotal.setText(String.valueOf(subtotalVenta));
        menu.valorIva.setText(String.valueOf(ivaVenta));
        menu.valorTotalPago.setText(String.valueOf(totalVenta));
        menu.textEfectivoVenta.setEnabled(true);
        menu.pagarVenta.setEnabled(true);
    }

    public void calcularCambio() {
        String efectivo = menu.textEfectivoVenta.getText();
        if (!Validaciones.validarNoVacios("Debes ingresar un monto de dinero primero", efectivo)) {
            return;
        } else {
            if (!Validaciones.validarParseoADouble(efectivo, "Debes ingresar un valor numerico")) {
                return;
            } else {
                if (!Validaciones.validarNumeroEnteroNoNegativo(efectivo, "El monto debe ser mayor a cero")) {
                    return;
                } else {
                    double montoPago = Double.parseDouble(efectivo);
                    double totalPago = Double.parseDouble(menu.valorTotalPago.getText());
                    if (!Validaciones.valorMenor(totalPago, montoPago, "Parece que no te alcanza para pagar prueba con otra cantidad")) {
                        return;
                    } else {
                        double cambio = montoPago - totalPago;
                        menu.cambioVenta.setText(String.valueOf(cambio));
                    }
                }
            }
        }
    }

    public void actualizarStock (int idProducto, int cantidadVendida) {
        Producto producto = prodDao.buscarCantidadProd(idProducto);
        int cantidadActual = producto.getCantidad();
        int nuevaCantidad = cantidadActual - cantidadVendida;
        prodDao.actualizarCantidad(nuevaCantidad, idProducto);
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

    public void eliminarProducto() {
        if (filaEliminar > -1) {
            int prodEliminar = (int) menu.tableProductosVenta.getValueAt(filaEliminar, 0);
            if (prodEliminar > -1) {
                listaProductosVenta.remove(prodEliminar - 1);
                listarVenta();
                calcularCantidades();
            }
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
            agregarDetalleVenta();
        } else if (e.getSource() == menu.pagarVenta) {
            calcularCambio();
        } else if (e.getSource() == menu.jMenuEliminarVentaProd) {
            eliminarProducto();
            limpiarDatos();
            if (listaProductosVenta.size() == 0) {
                menu.textEfectivoVenta.setEnabled(false);
                menu.pagarVenta.setEnabled(false);
            }
        } else if (e.getSource() == menu.generarVenta) {
            try {
                generarVenta();
            } catch (BadElementException ex) {
                System.out.println("");
            } catch (IOException ex) {
                System.out.println("");
            }
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
        } else if (e.getSource() == menu.tableProductosVenta) {
            filaEliminar = menu.tableProductosVenta.rowAtPoint(e.getPoint());
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
