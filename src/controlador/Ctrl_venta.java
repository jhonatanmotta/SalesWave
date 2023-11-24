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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    // atributos del controlador venta
    private ProductoDAO prodDao; // atributo de tipo ProductoDAO para asignar una instancia de este
    private encabezadoVenta encabezado; // atributo de tipo encabezadoVenta para asignar una instancia de este
    private detalleVenta detalle; // atributo de tipo detalleVenta para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTabla = new DefaultTableModel();
    // lista de los productos en la cola de venta
    List<detalleVenta> listaProductosVenta = new ArrayList<>();

    private detalleVenta producto; // instancia detalleVenta
    private int idProducto = 0; //id del Producto por defecto
    private int idDetalle = 1; //id del detalleVenta por defecto
    private String nombreProducto = ""; //nombreProducto por defecto
    private int cantidadProducto = 0; //cantidadProducto por defecto
    private int cantidadProductoDataBase = 0;  //cantidadProductoDataBase por defecto
    private double precioProducto = 0.0;  //precioProducto por defecto
    private double subtotal = 0.0;  //subtotal por defecto
    private int porcentajeIva = 0;  //porcentajeIva por defecto
    private double iva = 0.0;  //iva por defecto
    private double total = 0.0;  //total por defecto
    
    //calculos generales 
    BigDecimal subtotalVenta = BigDecimal.ZERO; //subtotalVenta por defecto
    BigDecimal ivaVenta = BigDecimal.ZERO; //ivaVenta por defecto
    BigDecimal totalVenta = BigDecimal.ZERO; //totalVenta por defecto
    
    private int filaEliminar = -1;  //filaEliminar por defecto

    // Constructor con parámetros
    public Ctrl_venta(ProductoDAO prodDao, encabezadoVenta encabezado, detalleVenta detalle, Menu menu) {
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.prodDao = prodDao; // Se le asigna al atributo prodDao la instancia que llega por parametro
        this.encabezado = encabezado; // Se le asigna al atributo encabezado la instancia que llega por parametro
        this.detalle = detalle; // Se le asigna al atributo detalle la instancia que llega por parametro
        this.menu.btnVenta.addMouseListener(this); // se le añade el MouseListener al boton btnVenta
        this.menu.btn_InicioVender.addMouseListener(this); // se le añade el MouseListener al boton btn_InicioVender
        this.menu.aniadirProdVenta.addActionListener(this); // se le añade el ActionListener al boton aniadirProdVenta
        this.menu.pagarVenta.addActionListener(this); // se le añade el ActionListener al boton pagarVenta
        this.menu.tableProductosVenta.addMouseListener(this); // se le añade el MouseListener a la tabla tableProductosVenta
        this.menu.jMenuEliminarVentaProd.addActionListener(this); // se le añade el ActionListener al boton jMenuEliminarVentaProd
        this.menu.generarVenta.addActionListener(this); // se le añade el ActionListener al boton generarVenta
        styleVenta(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleVenta() {
        // se le da el estilo a la tabla de las ventas
        JTableHeader headerVenta = menu.tableProductosVenta.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        headerVenta.setOpaque(false);
        headerVenta.setBackground(headerColor);
        headerVenta.setFont(headerFont);
        headerVenta.setForeground(textColor);

        // se le asigna un placeholder a los comboBox
        menu.comboBoxProductosVenta.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un producto");
        menu.comboBoxClientesVenta.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un cliente");
        // se le añade el buscador a los comboBox
        AutoCompleteDecorator.decorate(menu.comboBoxProductosVenta);
        AutoCompleteDecorator.decorate(menu.comboBoxClientesVenta);

        menu.textEfectivoVenta.setEnabled(false); // el textField de textEfectivoVenta estara inactivo
        menu.pagarVenta.setEnabled(false); // el textField de pagarVenta estara inactivo
    }

    // se estable estado de los comboBox para que se muestre el placeholder
    public void estadoCombo() {
        menu.comboBoxClientesVenta.setSelectedIndex(-1);
        menu.comboBoxProductosVenta.setSelectedIndex(-1);
    }

    // metodo para calcular el iva de un producto
    public double calcularIva(double precio, int cantidad, int porcentajeIva) {
        // iva del producto
        double totalIva;
        // si el porcentaje de iva es 0 el valor del iva sera 0
        if (porcentajeIva == 0) {
            totalIva = 0.0;
            // de lo contrario hace el cálculo y redondea el resultado a 2 decimales
        } else {
            totalIva = Math.round((precio * cantidad) * (porcentajeIva / 100.0) * 100.0) / 100.0;
        }
        // devuelve el iva
        return totalIva;
    }

    //metodo para limpiar el contenido de los inputs y selecciona index -1 del comboBox comboBoxProductosVenta
    public void limpiarDatos() {
        menu.comboBoxProductosVenta.setSelectedIndex(-1);
        menu.textCantidadProdVenta.setText("");
        menu.textEfectivoVenta.setText("");
        menu.cambioVenta.setText("0.0");
    }

    // metodo que vuelve todos los elementos de la venta a su estado por defecto 
    // ya se vacio o un indice selecccionado por defecto
    // algunos botones los vuelve deshabilitados
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

    // metodo para agregar productos a la cola de la venta
    public void agregarDetalleVenta() {
        // obtiene el indice selecciona del comboBox del producto
        int indexSeleccionado = menu.comboBoxProductosVenta.getSelectedIndex();
        // obtiene la cantidad del producto ingresada
        String cantidadProd = menu.textCantidadProdVenta.getText();
        // valida el indice seleccionado en el combobox del producto
        if (indexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            //obtiene el itemSeleccionado del ComboBox
            ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductosVenta.getSelectedItem();
            // valida que el campo de cantidad no este vacio 
            if (!Validaciones.validarNoVacios("Debes ingresar una cantidad", cantidadProd)) {
                return;
            } else {
                // valida que la cantidad sea un valor numerico
                if (!Validaciones.validarParseoAEntero(cantidadProd, "La cantidad debe ser un valor numerico")) {
                    return;
                } else {
                    // valida que el numero sea mayor a cero
                    if (!Validaciones.validarNumeroEnteroNoNegativo(cantidadProd, "La cantidad debe ser un valor mayor a cero")) {
                        return;
                    } else {
                        // obtiene la cantidad ingresada en textField
                        cantidadProducto = Integer.parseInt(cantidadProd);
                        // obtiene el id del itemSeleccionado del comboBox
                        idProducto = productoSeleccionado.getId();
                        // busca la informacion relaciona al producto
                        Producto datosProducto = prodDao.buscarProducto(idProducto);
                        // llena las variables con la informacion del producto
                        nombreProducto = datosProducto.getNombre();
                        cantidadProductoDataBase = datosProducto.getCantidad();
                        precioProducto = datosProducto.getPrecioVenta();
                        porcentajeIva = datosProducto.getIva();
                        iva = calcularIva(precioProducto, cantidadProducto, porcentajeIva);
                        // busca que el prodcuot a agregar a la cola de venta ya no este ahi
                        if (!buscarValorTabla(nombreProducto)) {
                            // si la cantidad que se desea comprar difiere de la cantidad en stock arrona error
                            if (cantidadProducto <= cantidadProductoDataBase) {
                                // hace el calculo del subtotal del producto
                                subtotal = precioProducto * cantidadProducto;
                                // hace el calculo del total del producto
                                total = subtotal + iva;
                                // usa la instacia de la clase detalleVenta para pasar la informacion del detalle
                                producto = new detalleVenta(idDetalle, 1, idProducto, nombreProducto, cantidadProducto, precioProducto, subtotal, iva, total, 1);
                                // añade la informacion a la lista de los detalles de venta
                                listaProductosVenta.add(producto);
                                // incrementa el id local del detalle venta
                                idDetalle++;
                                // limpia la seleccion e inputs
                                limpiarDatos();
                                // lista el producto en la tabla de la cola
                                listarVenta();
                                // se hacen los calculos de las cantidades globales
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

    // metodo para generar una nueva venta
    public void generarVenta() throws FileNotFoundException, BadElementException, IOException {
        // instancia de Date para obtener la fecha del momento
        Date date = new Date();
        // se le da formato a la fecha
        String fechaActual = new SimpleDateFormat("yyyy/MM/dd").format(date);
        // se obtiene el index seleccionado del comboBox cliente
        int indexSeleccionado = menu.comboBoxClientesVenta.getSelectedIndex();
        // valida el index seleccionado del comboBox cliente
        if (indexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            // valida que el tamaño de la lista de la cola sea mayor a cero
            if (listaProductosVenta.size() > 0) {
                Credenciales credenciales = Credenciales.getInstancia(); // Se obtiene la instancia de Credenciales
                String usuarioLogeado = credenciales.getUsuario(); // Se obtienen el usuario logeado el metodo get de Credenciales
                // se obtiene el id de dicho usuario mediante una consulta en la base de datos
                int idUsuario = prodDao.buscarUsuario(usuarioLogeado);
                // se obtiene el itemSelecciona del comboBox cliente
                ComboBox clienteSeleccionado = (ComboBox) menu.comboBoxClientesVenta.getSelectedItem();
                // se establece al informacion del encabezado de la venta en el objeto encabezado
                encabezado.setIdCliente_fk(clienteSeleccionado.getId());
                encabezado.setIdEmpresa_fk(1);
                encabezado.setIdUsuario_fk(idUsuario);
                encabezado.setValorPagar(totalVenta.doubleValue());
                encabezado.setFechaVenta(fechaActual);
                // valida si el registro del encabezado de la venta fue existoso
                if (prodDao.registroEncabezado(encabezado)) {
                    // obtiene el ultimo id agregado a la tabla encabezadoVenta
                    int lastIdEncabezadoVenta = prodDao.obtenerIdEncabezado();
                    // for que recorre todos los elementos de la lista de la cola de venta
                    for (detalleVenta producto : listaProductosVenta) {
                        // se establece la informacion del detalle de venta en el objeto detalle 
                        detalle.setIdEncabezadoVenta_fk(lastIdEncabezadoVenta);
                        detalle.setIdProducto_fk(producto.getIdProducto_fk());
                        detalle.setCantidad(producto.getCantidad());
                        detalle.setPrecioUnitario(producto.getPrecioUnitario());
                        detalle.setSubtotal(producto.getSubtotal());
                        detalle.setIva(producto.getIva());
                        detalle.setTotalPagar(producto.getTotalPagar());
                        // si el registro del detalle de la venta fue existoso, resta la cantidad en Stock del producto vendido
                        if (prodDao.registroDetalle(detalle)) {
                            actualizarStock(producto.getIdProducto_fk(), producto.getCantidad());
                        } else {
                            // mensaje de error
                            JOptionPane.showMessageDialog(null, "Error al registrar el detalle de la venta del producto" + producto.getNombreProducto(), "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    // mensaje informativo
                    JOptionPane.showMessageDialog(null, "La venta ha sido facturada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // se instancia la clase GeneradorPDFFactura
                    GeneradorPDFFactura factura = new GeneradorPDFFactura();
                    // se establecen los valores usando el objeto factura
                    factura.setNombreCliente(clienteSeleccionado.getNombre());
                    factura.setNombreUsuario(usuarioLogeado);
                    factura.setIdEmpresa(1);
                    factura.setNumVenta(lastIdEncabezadoVenta);
                    factura.setFechaFactura(fechaActual);
                    factura.setListaProductos(menu.tableProductosVenta);
                    factura.setSubtotal(subtotalVenta.doubleValue());
                    factura.setIva(ivaVenta.doubleValue());
                    factura.setTotal(totalVenta.doubleValue());
                    factura.generarFactura();
                    // se ponen los valores por defecto
                    resetDatosVenta();
                    // se limpia la lista de la cola de venta
                    listaProductosVenta.clear();
                    // se lista la cola que al momento estara vacia
                    listarVenta();
                } else {
                    // mensaje de error
                    JOptionPane.showMessageDialog(null, "Error al registrar el encabezado de la venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "No hay productos agregados en la cola", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // método para calcular las cantidades globales de la venta
    public void calcularCantidades() {
        // se ponen las cantidades en 0 de nuevo
        subtotalVenta = BigDecimal.ZERO;
        ivaVenta = BigDecimal.ZERO;
        totalVenta = BigDecimal.ZERO;
        
        // for que recorre la lista de productos en especifico los valores 
        // subtotal, total e iva del detalle de la venta
        for (detalleVenta valor : listaProductosVenta) {
            subtotalVenta = subtotalVenta.add(BigDecimal.valueOf(valor.getSubtotal()));
            ivaVenta = ivaVenta.add(BigDecimal.valueOf(valor.getIva()));
            totalVenta = totalVenta.add(BigDecimal.valueOf(valor.getTotalPagar()));
        }
        
        // Redondeo a dos decimales con BigDecimal
        subtotalVenta = subtotalVenta.setScale(2, BigDecimal.ROUND_HALF_UP);
        ivaVenta = ivaVenta.setScale(2, BigDecimal.ROUND_HALF_UP);
        totalVenta = totalVenta.setScale(2, BigDecimal.ROUND_HALF_UP);
        
        // Establecer valores formateados en el JLabel
        menu.valorSubtotal.setText(String.valueOf(subtotalVenta));
        menu.valorIva.setText(String.valueOf(ivaVenta));
        menu.valorTotalPago.setText(String.valueOf(totalVenta));

        // habilita los campos de efectivo y pago
        menu.textEfectivoVenta.setEnabled(true);
        menu.pagarVenta.setEnabled(true);
    }

    // metodo para calcular el cambio de una venta
    public void calcularCambio() {
        // obtiene el valor del efectivo ingresado y del total a pagar
        String efectivo = menu.textEfectivoVenta.getText();
        String totalPagar = menu.valorTotalPago.getText();
        // valida que el valor no sea vacio
        if (!Validaciones.validarNoVacios("Debes ingresar un monto de dinero primero", efectivo)) {
            return;
        } else {
            // valida que el valor sea numerico
            if (!Validaciones.validarParseoADouble(efectivo, "Debes ingresar un valor numerico")) {
                return;
            } else {
                // valida que el valor sea mayor a cero
                if (!Validaciones.validarNumeroEnteroNoNegativo(efectivo, "El monto debe ser mayor a cero")) {
                    return;
                } else {
                    // parsea el valor con el que se va pagar 
                    double montoPago = Double.parseDouble(efectivo);
                    // obtiene el valor total de la venta
                    double totalPago = Double.parseDouble(totalPagar);
                    // valida que el valor con el que se va pagar no sea menor al del pago
                    if (!Validaciones.valorMenor(totalPago, montoPago, "Parece que no te alcanza para pagar prueba con otra cantidad")) {
                        return;
                    } else {
                        // hace la operacion para obtener el cambio
                        double cambio = montoPago - totalPago;
                        // Formatear los valores a dos decimales
                        DecimalFormat df = new DecimalFormat("#.##");
                        df.setMaximumFractionDigits(2);
                        // setea el JLabel con el cambio a dar
                        menu.cambioVenta.setText(String.valueOf(df.format(cambio)));
                    }
                }
            }
        }
    }

    // metodo para actualizar el stock una vez realizada una venta
    public void actualizarStock(int idProducto, int cantidadVendida) {
        // busca la cantidad en stock del producto en ese momento
        Producto producto = prodDao.buscarCantidadProd(idProducto);
        // obtiene la cantidad del stock actual
        int cantidadActual = producto.getCantidad();
        // calcula la nueva cantidad restando la cantidad actual por la vendida
        int nuevaCantidad = cantidadActual - cantidadVendida;
        // ejecuta el metodo para actualizar la cantidad en la base de datos
        prodDao.actualizarCantidad(nuevaCantidad, idProducto);
    }

    // busca que el producto a ingresar a la cola no haya sido agregado antes
    public boolean buscarValorTabla(String productoBusqueda) {
        // variable de retorno
        boolean encontrado = false;
        // for que recorre la tabla para buscar coincidencias con el valor que se esta buscando
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            Object valor = modeloTabla.getValueAt(fila, 1);
            // si el producto es encontrado en la cola de venta, cambia la variable de retorno a true y
            //rompe el bucle for
            if (valor != null && valor.toString().equals(productoBusqueda)) {
                encontrado = true;
                break;
            }
        }
        // devuelve la varible de retorno
        return encontrado;
    }

    // metodo para listar los prodcutios en la cola de venta
    public void listarVenta() {
        // obtiene el modelo de la tabla de venta
        modeloTabla = (DefaultTableModel) menu.tableProductosVenta.getModel();
        // cuenta la cantidad de elementos de la lista y la establece como cantidad de filas
        modeloTabla.setRowCount(listaProductosVenta.size());
        // for que recorre la lista y va estableciendo los valores en cada fila y columna correspondiente de la tabla
        for (int i = 0; i < listaProductosVenta.size(); i++) {
            modeloTabla.setValueAt(i + 1, i, 0);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getNombreProducto(), i, 1);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getCantidad(), i, 2);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getPrecioUnitario(), i, 3);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getSubtotal(), i, 4);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getIva(), i, 5);
            modeloTabla.setValueAt(listaProductosVenta.get(i).getTotalPagar(), i, 6);
        }
        // se le pasa el nuevo modelo a la tabla
        menu.tableProductosVenta.setModel(modeloTabla);
    }

    // metodo para eliminar productos de la cola
    public void eliminarProducto() {
        // valida si la fila a eliminar es mayor a -1
        if (filaEliminar > -1) {
            // obtiene el numero en el orden de la cola a eliminar
            int prodEliminar = (int) menu.tableProductosVenta.getValueAt(filaEliminar, 0);
            // valida si el numero es mayor a -1
            if (prodEliminar > -1) {
                // eliminar el elemento de la lista de la cola de la venta
                listaProductosVenta.remove(prodEliminar - 1);
                // lista de nuevo la tabla para que se actualice los datos
                listarVenta();
                // calcula de nuevo las cantidades globales
                calcularCantidades();
            }
        }
    }

    // metodo para llenar los comboBox de las vista de la venta
    public void llenarComboBox() {
        // Lista de productos
        List<Producto> listaProductos = prodDao.listarComboProducto();
        // for que recorrre la listaProductos y pone su contenido en el comboBox mediante el objeto ComboBox
        for (int i = 0; i < listaProductos.size(); i++) {
            int idProd = listaProductos.get(i).getIdProducto();
            String nombreProd = listaProductos.get(i).getNombre();
            menu.comboBoxProductosVenta.addItem(new ComboBox(idProd, nombreProd));
        }
        // Lista de clientes
        List<Cliente> listaClientes = prodDao.listarComboCliente();
        // for que recorrre la listaClientes y pone su contenido en el comboBox mediante el objeto ComboBox
        for (int i = 0; i < listaClientes.size(); i++) {
            int idCliente = listaClientes.get(i).getIdCliente();
            String nombreCliente = listaClientes.get(i).getNombre();
            String apellidoCliente = listaClientes.get(i).getApellido();
            menu.comboBoxClientesVenta.addItem(new ComboBox(idCliente, nombreCliente + " " + apellidoCliente));
        }
    }

    // Metodo que elimina todos los items de los comboBox
    public void eliminarItemsCombox() {
        menu.comboBoxProductosVenta.removeAllItems();
        menu.comboBoxClientesVenta.removeAllItems();
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
    @Override
    public void actionPerformed(ActionEvent e) {
        // accion para añadir productos a la cola
        if (e.getSource() == menu.aniadirProdVenta) {
            agregarDetalleVenta();
            // accion de pagar la venta
        } else if (e.getSource() == menu.pagarVenta) {
            calcularCambio();
            // accion para eliminar un producto de la cola de venta
        } else if (e.getSource() == menu.jMenuEliminarVentaProd) {
            eliminarProducto();
            limpiarDatos();
            if (listaProductosVenta.size() == 0) {
                menu.textEfectivoVenta.setEnabled(false);
                menu.pagarVenta.setEnabled(false);
            }
            // accion de generar una venta que ejecuta metodos dentro de la clase
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

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
    @Override
    public void mouseClicked(MouseEvent e) {
        // accion de venta en el menu del sistema
        if (e.getSource() == menu.btnVenta) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(6);
            botones.cambiarColor(menu.btnVenta);
            botones.cambiarTitulo("Venta");
            eliminarItemsCombox();
            llenarComboBox();
            estadoCombo();
            // accion de click en la tabla para luego eliminar la fila
        } else if (e.getSource() == menu.tableProductosVenta) {
            filaEliminar = menu.tableProductosVenta.rowAtPoint(e.getPoint());
        } // accion de venta en el inicio del sistema
        else if (e.getSource() == menu.btn_InicioVender) {
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
