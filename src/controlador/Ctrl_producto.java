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
import modelo.Categoria;
import metodos.ComboBox;
import metodos.Validaciones;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.Proveedor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import vista.Menu;

public class Ctrl_producto implements ActionListener, MouseListener, KeyListener {

    // atributos del controlador producto
    private Producto prod; // atributo de tipo Producto para asignar una instancia de este
    private ProductoDAO prodDao; // atributo de tipo ProductoDAO para asignar una instancia de este
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    // instancia de DefaultTableModel para modificar el contenido de la tabla y darle estilos
    DefaultTableModel modeloTabla = new DefaultTableModel();

    // Constructor con parámetros
    public Ctrl_producto(Producto prod, ProductoDAO prodDao, Menu menu) {
        this.prod = prod; // Se le asigna al atributo prod la instancia que llega por parametro
        this.prodDao = prodDao; // Se le asigna al atributo prodDao la instancia que llega por parametro
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btn_registrarProd.addActionListener(this); // se le añade el ActionListener al boton btn_registrarProd
        this.menu.btn_modificarProd.addActionListener(this); // se le añade el ActionListener al boton btn_modificarProd
        this.menu.btn_limpiarProd.addActionListener(this); // se le añade el ActionListener al boton btn_limpiarProd
        this.menu.jMenuEliminarProd.addActionListener(this); // se le añade el ActionListener al boton jMenuEliminarProd
        this.menu.jMenuHabilitarProd.addActionListener(this); // se le añade el ActionListener al boton jMenuHabilitarProd
        this.menu.tableProducto.addMouseListener(this); // se le añade el MouseListener a la tabla tableProducto
        this.menu.tableStock.addMouseListener(this); // se le añade el MouseListener a la tabla tableStock
        this.menu.btnProducto.addMouseListener(this); // se le añade el MouseListener al boton btnProducto
        this.menu.btn_irStock.addMouseListener(this); // se le añade el MouseListener al boton btn_irStock
        this.menu.btn_volverProd.addMouseListener(this); // se le añade el MouseListener al boton btn_volverProd
        this.menu.textBuscarProd.addKeyListener(this); // se le añade el KeyListener al textField textBuscarProd
        this.menu.comboBoxProductos.addActionListener(this); // se le añade el ActionListener al combobox comboBoxProductos
        this.menu.btn_actualizarStock.addActionListener(this); // se le añade el ActionListener al boton btn_actualizarStock
        styleProducto(); // metodo para inicializar los estilos del JPanel
    }

    // metodo de estilos
    public void styleProducto() {
        this.menu.textIdProd.setVisible(false); // el textField de idProducto estara oculto
        this.menu.textIdProdStock.setVisible(false); // el textField de idStock estara oculto
        this.menu.textStockAct.setEnabled(false); // el textField de textStockAct estara inactivo
        // se le asigna un placeholder al textField textBuscarProd
        menu.textBuscarProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar producto");
        // se le asigna un placeholder a los comboBox
        menu.comboBoxCategoriaProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona una categoria");
        menu.comboBoxProveedorProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un proveedor");
        menu.comboBoxProductos.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un producto");
        // se le añade el buscador a los comboBox
        AutoCompleteDecorator.decorate(menu.comboBoxCategoriaProd);
        AutoCompleteDecorator.decorate(menu.comboBoxProveedorProd);
        AutoCompleteDecorator.decorate(menu.comboBoxProductos);
    }

    // se estable estado de los comboBox para que se muestre el placeholder
    public void estadoCombo() {
        menu.comboBoxCategoriaProd.setSelectedIndex(-1);
        menu.comboBoxProveedorProd.setSelectedIndex(-1);
        menu.comboBoxProductos.setSelectedIndex(-1);
    }

    // metodo para registrar un nuevo producto
    public void registrarProducto() {
         // se obtiene la informacion del producto de los textField y los items de los ComboBox
        ComboBox categoriaProd = (ComboBox) menu.comboBoxCategoriaProd.getSelectedItem();
        ComboBox proveedorProd = (ComboBox) menu.comboBoxProveedorProd.getSelectedItem();
        String nombreProd = menu.textNombreProd.getText().trim();
        String descripcionProd = menu.textDescripcionProd.getText().trim();
        String precioCompra = menu.textPrecioCProd.getText().trim();
        String precioVenta = menu.textPrecioVProd.getText().trim();
        String cantidad = menu.textCantidadProd.getText().trim();
        String ivaProd = (String) menu.comboBoxIvaProd.getSelectedItem();
        // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
        if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombreProd, descripcionProd, precioCompra, precioVenta, cantidad)) {
            return;
        } else {
            // valida si se ha seleccionado un item en el comboBox de categoria
            if (categoriaProd == null) {
                JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando una categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // valida si se ha seleccionado un item en el comboBox de proveedor
                if (proveedorProd == null) {
                    JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando un proveedor", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // valida si se ha seleccionado un item en el comboBox de iva
                    if (ivaProd.equalsIgnoreCase("Seleccione iva")) {
                        JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando un iva para este producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // valida que cantidad ingresada del producto se pueda parsear a un entero
                        // y el precio de compra y venta se pueda parsear a un double
                        if (!Validaciones.validarParseoAEntero(cantidad, "La cantidad de un producto debe ser un valor numerico")
                                || !Validaciones.validarParseoADouble(precioCompra, "El precio de compra de un producto debe ser un valor numerico")
                                || !Validaciones.validarParseoADouble(precioVenta, "El precio de venta de un producto debe ser un valor numerico")) {
                            return;
                        } else {
                            // segun la seleccion del comboBox de iva se estable el valor del iva en el objeto prod
                            switch (ivaProd) {
                                case "No agrava iva" -> {
                                    prod.setIva(0);
                                }
                                case "5%" -> {
                                    prod.setIva(5);
                                }
                                case "19%" -> {
                                    prod.setIva(19);
                                }
                            }
                            // se establece la informacion del producto en el objeto prod
                            prod.setNombre(nombreProd);
                            prod.setDescripcion(descripcionProd);
                            prod.setCantidad(Integer.parseInt(cantidad));
                            prod.setPrecioCompra(Double.parseDouble(precioCompra));
                            prod.setPrecioVenta(Double.parseDouble(precioVenta));
                            prod.setIdCategoria_fk(categoriaProd.getId());
                            prod.setIdProveedor_fk(proveedorProd.getId());
                            // metodo de insercion de datos
                            if (prodDao.registroProducto(prod)) {
                                // limpia el contenido de la tabla
                                limpiarTabla();
                                // añade contenido a la tabla
                                listarProducto();
                                // limpia los campos
                                limpiarContenidoInput();
                                // mensaje informativo
                                JOptionPane.showMessageDialog(null, "Producto registrado con exito");
                            } else {
                                // mensaje de error
                                JOptionPane.showMessageDialog(null, "Error al registrar el producto");
                            }
                        }
                    }
                }
            }
        }
    }
    
    //metodo para modicar los datos del producto  en la base de datos
    public void modificarProducto() {
        // valida si se ha seleccionado una fila para modificar
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para modificar los datos del producto", menu.textIdProd.getText())) {
            return;
        } else {
            // se obtiene la informacion del producto de los textField
            int id = Integer.parseInt(menu.textIdProd.getText());
            String nombre = menu.textNombreProd.getText().trim();
            String descripcion = menu.textDescripcionProd.getText().trim();
            String precioCompra = menu.textPrecioCProd.getText().trim();
            String precioVenta = menu.textPrecioVProd.getText().trim();
            String cantidad = menu.textCantidadProd.getText().trim();
            ComboBox categoriaProd = (ComboBox) menu.comboBoxCategoriaProd.getSelectedItem();
            ComboBox proveedorProd = (ComboBox) menu.comboBoxProveedorProd.getSelectedItem();
            String ivaProd = (String) menu.comboBoxIvaProd.getSelectedItem();
            // valida los campos vacios y arroja un cuadro de dialogo con una advertencia
            if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombre, descripcion, precioCompra, precioVenta, cantidad)) {
                return;
            } else {
                // valida si se ha seleccionado un item en el comboBox de categoria
                if (categoriaProd == null) {
                    JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando una categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // valida si se ha seleccionado un item en el comboBox de proveedor
                    if (proveedorProd == null) {
                        JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando una proveedor", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // valida si se ha seleccionado un item en el comboBox de iva
                        if (ivaProd.equalsIgnoreCase("Seleccione iva")) {
                            JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando un iva", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        } else {
                            // valida que cantidad ingresada del producto se pueda parsear a un entero
                            // y el precio de compra y venta se pueda parsear a un double
                            if (!Validaciones.validarParseoAEntero(cantidad, "La cantidad de un producto debe ser un valor numerico")
                                    || !Validaciones.validarParseoADouble(precioCompra, "El precio de compra de un producto debe ser un valor numerico")
                                    || !Validaciones.validarParseoADouble(precioVenta, "El precio de venta de un producto debe ser un valor numerico")) {
                                return;
                            } else {
                                // segun la seleccion del comboBox de iva se estable el valor del iva en el objeto prod
                                switch (ivaProd) {
                                    case "No agrava iva" -> {
                                        prod.setIva(0);
                                    }
                                    case "5%" -> {
                                        prod.setIva(5);
                                    }
                                    case "19%" -> {
                                        prod.setIva(19);
                                    }
                                }
                                // se establece la informacion del producto en el objeto prod
                                prod.setIdProducto(id);
                                prod.setNombre(nombre);
                                prod.setDescripcion(descripcion);
                                prod.setPrecioCompra(Double.parseDouble(precioCompra));
                                prod.setPrecioVenta(Double.parseDouble(precioVenta));
                                prod.setCantidad(Integer.parseInt(cantidad));
                                prod.setIdCategoria_fk(categoriaProd.getId());
                                prod.setIdProveedor_fk(proveedorProd.getId());
                                // metodo para modificar el producto en la base de datos
                                if (prodDao.modificarProducto(prod)) {
                                    // limpia el contenido de la tabla
                                    limpiarTabla();
                                    // añade contenido a la tabla
                                    listarProducto();
                                    // limpia los campos
                                    limpiarContenidoInput();
                                    // mensaje informativo
                                    JOptionPane.showMessageDialog(null, "Producto modificado con exito");
                                } else {
                                    // mensaje de error
                                    JOptionPane.showMessageDialog(null, "Error al modificar el producto");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // metodo para eliminar un producto, es decir cambiarlo de estado
    public void eliminarProducto() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar un producto", menu.textIdProd.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdProd.getText());
            // metodo para modificar el estado del producto
            if (prodDao.estadoProducto(0, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarProducto();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Producto eliminado");
            } else {
                // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al eliminar producto");
            }
        }
    }
    
    
    // metodo para habilitar un producto, es decir cambiarlo de estado
    public void habilitarProducto() {
        // valida que el usuario haya selecionado una fila
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar un producto", menu.textIdProd.getText())) {
            return;
        } else {
            // obtiene el id de textField
            int id = Integer.parseInt(menu.textIdProd.getText());
            // metodo para modificar el estado del producto
            if (prodDao.estadoProducto(1, id)) {
                // limpia el contenido de la tabla
                limpiarTabla();
                // añade contenido a la tabla
                listarProducto();
                // limpia los campos
                limpiarContenidoInput();
                // mensaje informativo
                JOptionPane.showMessageDialog(null, "Producto habilitado");
            } else {
                 // mensaje de error
                JOptionPane.showMessageDialog(null, "Error al habilitar producto");
            }
        }
    }

    // metodo para mostrar los datos en la tabla de prodcuto
    public void listarProducto() {
        // lista con la informacion del producto
        List<Producto> lista = prodDao.listaProductos(menu.textBuscarProd.getText().trim());
        // obtiene el modelo de la tabla
        modeloTabla = (DefaultTableModel) menu.tableProducto.getModel();
        // arreglo con la cantidad de columnas
        Object[] ob = new Object[5];
        // for que recorre la lista y va agregando la informacion a las filas de la tabla
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProducto();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getDescripcion();
            ob[3] = lista.get(i).getCantidad();
            if (lista.get(i).getEstado() == 1) {
                ob[4] = "Disponible";
            } else {
                ob[4] = "Baja";
            }
            modeloTabla.addRow(ob);
        }
        // se le pone el nuevo modelo a las tablas
        menu.tableProducto.setModel(modeloTabla);
        menu.tableStock.setModel(modeloTabla);
        
        // se oculta la columna del id
        menu.tableProducto.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableProducto.getColumnModel().getColumn(0).setMaxWidth(0);
        // se oculta la columna del id
        menu.tableStock.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableStock.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // se obtiene los headers de las tablas
        JTableHeader headerProd = menu.tableProducto.getTableHeader();
        JTableHeader headerStock = menu.tableStock.getTableHeader();
        // color del header
        Color headerColor = new Color(232, 158, 67);
        // color del texto del header
        Color textColor = Color.WHITE;
        // tipo de letra del header
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        // se quita lo opaco del header, se estable el color de fondo, letra y color
        headerProd.setOpaque(false);
        headerStock.setOpaque(false);
        headerProd.setBackground(headerColor);
        headerStock.setBackground(headerColor);
        headerProd.setFont(headerFont);
        headerStock.setFont(headerFont);
        headerProd.setForeground(textColor);
        headerStock.setForeground(textColor);
    }

    // metodo para limpiar la tabla
    public void limpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i -= 1;
        }
    }
    
    // metodo para agregar contenido a los inputs luego de dar click en un campo de la tabla
    public void agregarContenidoInput(int fila) {
        String estado = menu.tableProducto.getValueAt(fila, 4).toString();
        if (estado == "Disponible") {
            // pone visible el boton de eliminar producto
            menu.jMenuEliminarProd.setVisible(true);
            menu.jMenuHabilitarProd.setVisible(false);
        } else {
            // pone visible el boton de habilitar producto
            menu.jMenuHabilitarProd.setVisible(true);
            menu.jMenuEliminarProd.setVisible(false);
        }
        // inhabilita el boton de registrar e inhabilita el textField de cantidad del producto 
        menu.textCantidadProd.setEnabled(false);
        menu.btn_registrarProd.setEnabled(false);
        // pone el id de la tabla en el textField
        menu.textIdProd.setText(menu.tableProducto.getValueAt(fila, 0).toString());
        //obtiene el id de la tabla en la fila especificada
        int idProducto = Integer.parseInt(menu.textIdProd.getText());
        Producto producto = prodDao.buscarProducto(idProducto);
        // Selecciona un item del comboBox del iva
        switch (producto.getIva()) {
            case 0 -> {
                menu.comboBoxIvaProd.setSelectedItem("No agrava iva");
            }
            case 5 -> {
                menu.comboBoxIvaProd.setSelectedItem("5%");
            }
            case 19 -> {
                menu.comboBoxIvaProd.setSelectedItem("19%");
            }
        }
        // agrega contenido a los inputs, y selecciona los comboBox con sus itemSeleccionado
        menu.textNombreProd.setText(producto.getNombre());
        menu.textDescripcionProd.setText(producto.getDescripcion());
        menu.textPrecioCProd.setText(String.valueOf(producto.getPrecioCompra()));
        menu.textPrecioVProd.setText(String.valueOf(producto.getPrecioVenta()));
        menu.textCantidadProd.setText(String.valueOf(producto.getCantidad()));
        menu.comboBoxCategoriaProd.setSelectedItem(new ComboBox(producto.getIdCategoria_fk(), producto.getCategoria()));
        menu.comboBoxProveedorProd.setSelectedItem(new ComboBox(producto.getIdProveedor_fk(), producto.getProveedor()));
    }

    // metedo que añade informacion a los campos de la vista de actuliazar al dar un click en algun campo de la tabla
    public void agregarContenidoStock(int fila) {
        // se obtiene el id de la tabla
        menu.textIdProdStock.setText(menu.tableStock.getValueAt(fila, 0).toString());
        // se obtiene el id del textField y se parsea
        int idProducto = Integer.parseInt(menu.textIdProdStock.getText());
        // Se busca la cantidad del producto mediante el id 
        Producto producto = prodDao.buscarCantidadProd(idProducto);
        // se pone la cantidad del producto en el text field
        menu.textStockAct.setText(String.valueOf(producto.getCantidad()));
        // se obtiene el id y el nombre y se le pasan al comboBox mediante el objeto ComboBox
        int idProd = producto.getIdProducto();
        String nombreProd = producto.getNombre();
        menu.comboBoxProductos.setSelectedItem(new ComboBox(idProd, nombreProd));
    }

    //metodo para limpiar el contenido de los inputs
    public void limpiarContenidoInput() {
        menu.textNombreProd.setText("");
        menu.textDescripcionProd.setText("");
        menu.textPrecioCProd.setText("");
        menu.textPrecioVProd.setText("");
        menu.textCantidadProd.setText("");
        menu.textIdProd.setText("");
        menu.comboBoxCategoriaProd.setSelectedIndex(-1); // estable el indexSeleccionado por Default
        menu.comboBoxProveedorProd.setSelectedIndex(-1); // estable el indexSeleccionado por Default
        menu.comboBoxProductos.setSelectedIndex(-1); // estable el indexSeleccionado por Default
        menu.comboBoxIvaProd.setSelectedItem("Seleccione iva"); // estable el itemSeleccionado por Default
        menu.btn_registrarProd.setEnabled(true); // habilita el boton de registrar
        menu.textCantidadProd.setEnabled(true); // habilita el textField de la cantidad del producto
    }

    // metodo para llenar los comboBox de las vista con la informacion
    public void llenarComboBox() {
        // Listas de categorias, proveedores, productos
        List<Categoria> listaCategoria = prodDao.listarComboCategoria();
        List<Proveedor> listaProveedor = prodDao.listarComboProveedor();
        List<Producto> listaProductos = prodDao.listarComboProducto();
        // for que recorrre la listaCategoria y pone su contenido en el comboBox mediante el objeto ComboBox
        for (int i = 0; i < listaCategoria.size(); i++) {
            int idCat = listaCategoria.get(i).getIdCategoria();
            String nombreCat = listaCategoria.get(i).getNombre();
            menu.comboBoxCategoriaProd.addItem(new ComboBox(idCat, nombreCat));
        }
        // for que recorrre la listaProveedor y pone su contenido en el comboBox mediante el objeto ComboBox
        for (int i = 0; i < listaProveedor.size(); i++) {
            int idProv = listaProveedor.get(i).getIdProveedor();
            String nombreProv = listaProveedor.get(i).getNombre();
            String apellidoProv = listaProveedor.get(i).getApellido();
            menu.comboBoxProveedorProd.addItem(new ComboBox(idProv, nombreProv + " " + apellidoProv));
        }
        // for que recorrre la listaProductos y pone su contenido en el comboBox mediante el objeto ComboBox
        for (int i = 0; i < listaProductos.size(); i++) {
            int idProd = listaProductos.get(i).getIdProducto();
            String nombreProd = listaProductos.get(i).getNombre();
            menu.comboBoxProductos.addItem(new ComboBox(idProd, nombreProd));
        }
    }
    
    // Metodo que elimina todos los items de los comboBox
    public void eliminarItemsCombox() {
        menu.comboBoxCategoriaProd.removeAllItems();
        menu.comboBoxProveedorProd.removeAllItems();
        menu.comboBoxProductos.removeAllItems();
    }

    // Metodo para llenar la cantidad disponible de un objeto
    public void llenarCantProd() {
        // obtiene el item seleccionado del ComboBox
        ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductos.getSelectedItem();
        // valida si el item es diferente a null
        if (productoSeleccionado != null) {
            // valita el boton de actualizar stock
            menu.btn_actualizarStock.setEnabled(true);
            // obtiene el id del item seleccionado
            int id = productoSeleccionado.getId();
            // busca la cantidad de dicho producto mediante una consulta en la base de datos
            Producto producto = prodDao.buscarCantidadProd(id);
            // Pone la cantidad del producto en el textField
            menu.textStockAct.setText(String.valueOf(producto.getCantidad()));
        } else {
            // si el item es null pone en el textField un String vacio
            menu.textStockAct.setText("");
        }
    }

    // metodo para actulizar el stock de un producto 
    public void actualizarStock() {
        // obtiene la cantidad nueva
        String cantidad = menu.textStockNew.getText().trim();
        // obtiene el indice seleecionado del ComboBox
        int productoIndexSeleccionado = menu.comboBoxProductos.getSelectedIndex();
        // valida que el indice selecciona no sea -1
        if (productoIndexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            // valida que nueva cantidad no sea un valor vacio        
        } else if (!Validaciones.validarNoVacios("Debes agregar una cantidad para actualizar el stock", cantidad)) {
            return;
        } else {
            // valida que la nueva cantidad se pueda parsear a un entero 
            if (!Validaciones.validarParseoAEntero(cantidad, "La cantidad de un producto debe ser un valor numerico")) {
                return;
            } else {
                // valida que la cantidad sea mayor de cero
                if (Integer.parseInt(cantidad) <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad no puede ser negativa o igual a cero", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    // obtiene el item seleccionado del comboBox
                    ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductos.getSelectedItem();
                    // obtiene la cantidad actual del Stock del textField
                    int stockActual = Integer.parseInt(menu.textStockAct.getText());
                    // obtiene el id producto seleccionado en el comboBox
                    int id = productoSeleccionado.getId();
                    // suma ambas cantidades
                    int stockNuevo = Integer.parseInt(cantidad) + stockActual;
                    // metodo para actualizar la cantidad del stock en la base de datos 
                    if (prodDao.actualizarCantidad(stockNuevo, id)) {
                        // mensaje informativo
                        JOptionPane.showMessageDialog(null, "El stock disponible de " + String.valueOf(productoSeleccionado.getNombre()) + " ha sido actualizado");
                    } else {
                        // mensaje de error
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el stock de " + String.valueOf(productoSeleccionado.getNombre()));
                    }
                }
            }
        }
    }

    // actionPerformed que recibe todos los eventos de la vista y ejecuta metodos
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btn_registrarProd) {
            registrarProducto();
        } else if (e.getSource() == menu.btn_modificarProd) {
            modificarProducto();
        } else if (e.getSource() == menu.jMenuEliminarProd) {
            eliminarProducto();
        } else if (e.getSource() == menu.jMenuHabilitarProd) {
            habilitarProducto();
        } else if (e.getSource() == menu.btn_limpiarProd) {
            limpiarContenidoInput();
        } else if (e.getSource() == menu.comboBoxProductos) {
            llenarCantProd();
        } else if (e.getSource() == menu.btn_actualizarStock) {
            actualizarStock();
            limpiarTabla();
            listarProducto();
            estadoCombo();
            menu.textStockAct.setText("");
            menu.textStockNew.setText("");
        }
    }

    // mouseClicked que recibe todos los clicks de la vista y ejecuta metodos
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableProducto) {
            int fila = menu.tableProducto.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
            // accion de cambiar el panel, color del boton, y cargar la tabla y los comboBox
        } else if (e.getSource() == menu.btnProducto) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(2);
            botones.cambiarColor(menu.btnProducto);
            botones.cambiarTitulo("Gestion de Productos");
            limpiarTabla();
            listarProducto();
            eliminarItemsCombox();
            llenarComboBox();
            estadoCombo();
            // accion de cambiar el panel y cargar la tabla y los comboBox
        } else if (e.getSource() == menu.btn_irStock) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(8);
            botones.cambiarTitulo("Stock de Productos");
            limpiarTabla();
            listarProducto();
            eliminarItemsCombox();
            llenarComboBox();
            estadoCombo();
            menu.btn_actualizarStock.setEnabled(false);
            menu.textStockNew.setText("");
            // accion de cambiar el panel y cargar la tabla y los comboBox
        } else if (e.getSource() == menu.btn_volverProd) {
            BotonesMenu botones = new BotonesMenu(menu);
            botones.cambiarPanel(2);
            botones.cambiarTitulo("Gestion de Productos");
            limpiarTabla();
            listarProducto();
        } else if (e.getSource() == menu.tableStock) {
            int fila = menu.tableStock.rowAtPoint(e.getPoint());
            agregarContenidoStock(fila);
        }
    }

    // keyReleased que recibe los eventos al soltar una letra del teclado y ejecuta metodos
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == menu.textBuscarProd) {
            limpiarTabla();
            listarProducto();
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