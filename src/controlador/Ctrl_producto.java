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

    private Producto prod;
    private ProductoDAO prodDao;
    private Menu menu;
    DefaultTableModel modeloTabla = new DefaultTableModel();

    public Ctrl_producto(Producto prod, ProductoDAO prodDao, Menu menu) {
        this.prod = prod;
        this.prodDao = prodDao;
        this.menu = menu;
        this.menu.btn_registrarProd.addActionListener(this);
        this.menu.btn_modificarProd.addActionListener(this);
        this.menu.btn_limpiarProd.addActionListener(this);
        this.menu.jMenuEliminarProd.addActionListener(this);
        this.menu.jMenuHabilitarProd.addActionListener(this);
        this.menu.tableProducto.addMouseListener(this);
        this.menu.tableStock.addMouseListener(this);
        this.menu.btnProducto.addMouseListener(this);
        this.menu.btn_irStock.addMouseListener(this);
        this.menu.btn_volverProd.addMouseListener(this);
        this.menu.textBuscarProd.addKeyListener(this);
        this.menu.comboBoxProductos.addActionListener(this);
        this.menu.btn_actualizarStock.addActionListener(this);
        styleProducto();
    }

    public void styleProducto() {
        this.menu.textIdProd.setVisible(false);
        this.menu.textIdProdStock.setVisible(false);
        this.menu.textStockAct.setEnabled(false);
        menu.textBuscarProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar producto");
        menu.comboBoxCategoriaProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona una categoria");
        menu.comboBoxProveedorProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un proveedor");
        menu.comboBoxProductos.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selecciona un producto");
        AutoCompleteDecorator.decorate(menu.comboBoxCategoriaProd);
        AutoCompleteDecorator.decorate(menu.comboBoxProveedorProd);
        AutoCompleteDecorator.decorate(menu.comboBoxProductos);
    }

    public void estadoCombo() {
        menu.comboBoxCategoriaProd.setSelectedIndex(-1);
        menu.comboBoxProveedorProd.setSelectedIndex(-1);
        menu.comboBoxProductos.setSelectedIndex(-1);
    }

    public void registrarProducto() {
        ComboBox categoriaProd = (ComboBox) menu.comboBoxCategoriaProd.getSelectedItem();
        ComboBox proveedorProd = (ComboBox) menu.comboBoxProveedorProd.getSelectedItem();
        String nombreProd = menu.textNombreProd.getText().trim();
        String descripcionProd = menu.textDescripcionProd.getText().trim();
        String precioCompra = menu.textPrecioCProd.getText().trim();
        String precioVenta = menu.textPrecioVProd.getText().trim();
        String cantidad = menu.textCantidadProd.getText().trim();
        String ivaProd = (String) menu.comboBoxIvaProd.getSelectedItem();
        if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombreProd, descripcionProd, precioCompra, precioVenta, cantidad)) {
            return;
        } else {
            if (categoriaProd == null) {
                JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando una categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                if (proveedorProd == null) {
                    JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando un proveedor", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (ivaProd.equalsIgnoreCase("Seleccione iva")) {
                        JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando un iva para este producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (!Validaciones.validarParseoAEntero(cantidad, "La cantidad de un producto debe ser un valor numerico")
                                || !Validaciones.validarParseoADouble(precioCompra, "El precio de compra de un producto debe ser un valor numerico")
                                || !Validaciones.validarParseoADouble(precioVenta, "El precio de venta de un producto debe ser un valor numerico")) {
                            return;
                        } else {
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
                            prod.setNombre(nombreProd);
                            prod.setDescripcion(descripcionProd);
                            prod.setCantidad(Integer.parseInt(cantidad));
                            prod.setPrecioCompra(Double.parseDouble(precioCompra));
                            prod.setPrecioVenta(Double.parseDouble(precioVenta));
                            prod.setIdCategoria_fk(categoriaProd.getId());
                            prod.setIdProveedor_fk(proveedorProd.getId());
                            if (prodDao.registroProducto(prod)) {
                                limpiarTabla();
                                listarProducto();
                                limpiarContenidoInput();
                                JOptionPane.showMessageDialog(null, "Producto registrado con exito");
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al registrar el producto");
                            }
                        }
                    }
                }
            }
        }
    }

    public void modificarProducto() {
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para modificar los datos del producto", menu.textIdProd.getText())) {
            return;
        } else {
            int id = Integer.parseInt(menu.textIdProd.getText());
            String nombre = menu.textNombreProd.getText().trim();
            String descripcion = menu.textDescripcionProd.getText().trim();
            String precioCompra = menu.textPrecioCProd.getText().trim();
            String precioVenta = menu.textPrecioVProd.getText().trim();
            String cantidad = menu.textCantidadProd.getText().trim();
            ComboBox categoriaProd = (ComboBox) menu.comboBoxCategoriaProd.getSelectedItem();
            ComboBox proveedorProd = (ComboBox) menu.comboBoxProveedorProd.getSelectedItem();
            String ivaProd = (String) menu.comboBoxIvaProd.getSelectedItem();
            if (!Validaciones.validarNoVacios("Recuerda que todos los campos son obligatorios", nombre, descripcion, precioCompra, precioVenta, cantidad)) {
                return;
            } else {
                if (categoriaProd == null) {
                    JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando una categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (proveedorProd == null) {
                        JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando una proveedor", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (ivaProd.equalsIgnoreCase("Seleccione iva")) {
                            JOptionPane.showMessageDialog(null, "Parece que no estas seleccionando un iva", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        } else {
                            if (!Validaciones.validarParseoAEntero(cantidad, "La cantidad de un producto debe ser un valor numerico")
                                    || !Validaciones.validarParseoADouble(precioCompra, "El precio de compra de un producto debe ser un valor numerico")
                                    || !Validaciones.validarParseoADouble(precioVenta, "El precio de venta de un producto debe ser un valor numerico")) {
                                return;
                            } else {
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
                                prod.setIdProducto(id);
                                prod.setNombre(nombre);
                                prod.setDescripcion(descripcion);
                                prod.setPrecioCompra(Double.parseDouble(precioCompra));
                                prod.setPrecioVenta(Double.parseDouble(precioVenta));
                                prod.setCantidad(Integer.parseInt(cantidad));
                                prod.setIdCategoria_fk(categoriaProd.getId());
                                prod.setIdProveedor_fk(proveedorProd.getId());
                                if (prodDao.modificarProducto(prod)) {
                                    limpiarTabla();
                                    listarProducto();
                                    limpiarContenidoInput();
                                    JOptionPane.showMessageDialog(null, "Producto modificado con exito");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error al modificar el producto");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void eliminarProducto() {
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para eliminar un producto", menu.textIdProd.getText())) {
            return;
        } else {
            int id = Integer.parseInt(menu.textIdProd.getText());
            if (prodDao.estadoProducto(0, id)) {
                limpiarTabla();
                listarProducto();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Producto eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar producto");
            }
        }
    }

    public void habilitarProducto() {
        if (!Validaciones.validarNoVacios("Debes seleccionar una fila para habilitar un producto", menu.textIdProd.getText())) {
            return;
        } else {
            int id = Integer.parseInt(menu.textIdProd.getText());
            if (prodDao.estadoProducto(1, id)) {
                limpiarTabla();
                listarProducto();
                limpiarContenidoInput();
                JOptionPane.showMessageDialog(null, "Producto habilitado");
            } else {
                JOptionPane.showMessageDialog(null, "Error al habilitar producto");
            }
        }
    }

    public void listarProducto() {
        List<Producto> lista = prodDao.listaProductos(menu.textBuscarProd.getText().trim());
        modeloTabla = (DefaultTableModel) menu.tableProducto.getModel();
        Object[] ob = new Object[5];
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
        menu.tableProducto.setModel(modeloTabla);
        menu.tableStock.setModel(modeloTabla);

        menu.tableProducto.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableProducto.getColumnModel().getColumn(0).setMaxWidth(0);

        menu.tableStock.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tableStock.getColumnModel().getColumn(0).setMaxWidth(0);

        JTableHeader headerProd = menu.tableProducto.getTableHeader();
        JTableHeader headerStock = menu.tableStock.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        headerProd.setOpaque(false);
        headerStock.setOpaque(false);
        headerProd.setBackground(headerColor);
        headerStock.setBackground(headerColor);
        headerProd.setFont(headerFont);
        headerStock.setFont(headerFont);
        headerProd.setForeground(textColor);
        headerStock.setForeground(textColor);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i -= 1;
        }
    }

    public void agregarContenidoInput(int fila) {
        String estado = menu.tableProducto.getValueAt(fila, 4).toString();
        if (estado == "Disponible") {
            menu.jMenuEliminarProd.setVisible(true);
            menu.jMenuHabilitarProd.setVisible(false);
        } else {
            menu.jMenuHabilitarProd.setVisible(true);
            menu.jMenuEliminarProd.setVisible(false);
        }
        menu.textCantidadProd.setEnabled(false);
        menu.btn_registrarProd.setEnabled(false);
        menu.textIdProd.setText(menu.tableProducto.getValueAt(fila, 0).toString());
        int idProducto = Integer.parseInt(menu.textIdProd.getText());
        Producto producto = prodDao.buscarProducto(idProducto);
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
        menu.textNombreProd.setText(producto.getNombre());
        menu.textDescripcionProd.setText(producto.getDescripcion());
        menu.textPrecioCProd.setText(String.valueOf(producto.getPrecioCompra()));
        menu.textPrecioVProd.setText(String.valueOf(producto.getPrecioVenta()));
        menu.textCantidadProd.setText(String.valueOf(producto.getCantidad()));
        menu.comboBoxCategoriaProd.setSelectedItem(new ComboBox(producto.getIdCategoria_fk(), producto.getCategoria()));
        menu.comboBoxProveedorProd.setSelectedItem(new ComboBox(producto.getIdProveedor_fk(), producto.getProveedor()));
    }

    public void agregarContenidoStock(int fila) {
        menu.textIdProdStock.setText(menu.tableStock.getValueAt(fila, 0).toString());
        int idProducto = Integer.parseInt(menu.textIdProdStock.getText());
        Producto producto = prodDao.buscarCantidadProd(idProducto);
        menu.textStockAct.setText(String.valueOf(producto.getCantidad()));
        int idProd = producto.getIdProducto();
        String nombreProd = producto.getNombre();
        menu.comboBoxProductos.setSelectedItem(new ComboBox(idProd, nombreProd));
    }

    public void limpiarContenidoInput() {
        menu.textNombreProd.setText("");
        menu.textDescripcionProd.setText("");
        menu.textPrecioCProd.setText("");
        menu.textPrecioVProd.setText("");
        menu.textCantidadProd.setText("");
        menu.textIdProd.setText("");
        menu.comboBoxCategoriaProd.setSelectedIndex(-1);
        menu.comboBoxProveedorProd.setSelectedIndex(-1);
        menu.comboBoxProductos.setSelectedIndex(-1);
        menu.comboBoxIvaProd.setSelectedItem("Seleccione iva");
        menu.btn_registrarProd.setEnabled(true);
        menu.textCantidadProd.setEnabled(true);
    }

    public void llenarComboBox() {
        List<Categoria> listaCategoria = prodDao.listarComboCategoria();
        List<Proveedor> listaProveedor = prodDao.listarComboProveedor();
        List<Producto> listaProductos = prodDao.listarComboProducto();
        for (int i = 0; i < listaCategoria.size(); i++) {
            int idCat = listaCategoria.get(i).getIdCategoria();
            String nombreCat = listaCategoria.get(i).getNombre();
            menu.comboBoxCategoriaProd.addItem(new ComboBox(idCat, nombreCat));
        }
        for (int i = 0; i < listaProveedor.size(); i++) {
            int idProv = listaProveedor.get(i).getIdProveedor();
            String nombreProv = listaProveedor.get(i).getNombre();
            String apellidoProv = listaProveedor.get(i).getApellido();
            menu.comboBoxProveedorProd.addItem(new ComboBox(idProv, nombreProv + " " + apellidoProv));
        }
        for (int i = 0; i < listaProductos.size(); i++) {
            int idProd = listaProductos.get(i).getIdProducto();
            String nombreProd = listaProductos.get(i).getNombre();
            menu.comboBoxProductos.addItem(new ComboBox(idProd, nombreProd));
        }
    }

    public void eliminarItemsCombox() {
        menu.comboBoxCategoriaProd.removeAllItems();
        menu.comboBoxProveedorProd.removeAllItems();
        menu.comboBoxProductos.removeAllItems();
    }

    public void llenarCantProd() {
        ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductos.getSelectedItem();
        if (productoSeleccionado != null) {
            menu.btn_actualizarStock.setEnabled(true);
            int id = productoSeleccionado.getId();
            Producto producto = prodDao.buscarCantidadProd(id);
            menu.textStockAct.setText(String.valueOf(producto.getCantidad()));
        } else {
            menu.textStockAct.setText("");
        }
    }

    public void actualizarStock() {
        String cantidad = menu.textStockNew.getText().trim();
        int productoIndexSeleccionado = menu.comboBoxProductos.getSelectedIndex();
        if (productoIndexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else if (!Validaciones.validarNoVacios("Debes agregar una cantidad para actualizar el stock", cantidad)) {
            return;
        } else {
            if (!Validaciones.validarParseoAEntero(cantidad, "La cantidad de un producto debe ser un valor numerico")) {
//            if (Integer.parseInt(cantidad) <= 0) {
                return;
            } else {
                if (Integer.parseInt(cantidad) <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad no puede ser negativa o igual a cero", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    System.out.println("holaaa");
                    ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductos.getSelectedItem();
                    int stockActual = Integer.parseInt(menu.textStockAct.getText());
                    int id = productoSeleccionado.getId();
                    int stockNuevo = Integer.parseInt(cantidad) + stockActual;
                    if (prodDao.actualizarCantidad(stockNuevo, id)) {
                        JOptionPane.showMessageDialog(null, "El stock disponible de " + String.valueOf(productoSeleccionado.getNombre()) + " ha sido actualizado");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el stock de " + String.valueOf(productoSeleccionado.getNombre()));
                    }
                }
            }
        }
    }

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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tableProducto) {
            int fila = menu.tableProducto.rowAtPoint(e.getPoint());
            agregarContenidoInput(fila);
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
