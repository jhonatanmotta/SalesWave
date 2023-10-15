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
import metodos.PlaceholderTextfield;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.Proveedor;
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
        this.menu.btnProducto.addMouseListener(this);
        this.menu.textBuscarProd.addKeyListener(this);
        styleProducto();
    }

    public void styleProducto() {
        this.menu.textIdProd.setVisible(false);
        menu.textBuscarProd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar producto");
    }

    public void registrarProducto() {
        ComboBox categoriaProd = (ComboBox) menu.comboBoxCategoriaProd.getSelectedItem();
        ComboBox proveedorProd = (ComboBox) menu.comboBoxProveedorProd.getSelectedItem();
        String nombreProd = menu.textNombreProd.getText().trim();
        String descripcionProd = menu.textDescripcionProd.getText().trim();
        String precioCompra = menu.textPrecioCProd.getText().trim();
        String precioVenta = menu.textPrecioCProd.getText().trim();
        String cantidad = menu.textCantidadProd.getText().trim();
        String ivaProd = (String) menu.comboBoxIvaProd.getSelectedItem();
        if (nombreProd.isEmpty() || descripcionProd.isEmpty() || precioCompra.isEmpty() || precioVenta.isEmpty() || cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            if (ivaProd.equalsIgnoreCase("Seleccione iva")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un iva", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                switch (ivaProd) {
                    case "No agrava iva": {
                        prod.setIva(0);
                        break;
                    }
                    case "5%": {
                        prod.setIva(5);
                        break;
                    }
                    case "19%": {
                        prod.setIva(19);
                        break;
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

    public void modificarProducto() {
        if (menu.textIdProd.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila");
        } else {
            int id = Integer.parseInt(menu.textIdProd.getText());
            String nombre = menu.textNombreProd.getText();
            String descripcion = menu.textDescripcionProd.getText();
            String precioCompra = menu.textPrecioCProd.getText();
            String precioVenta = menu.textPrecioVProd.getText();
            String cantidad = menu.textCantidadProd.getText();
            ComboBox categoriaProd = (ComboBox) menu.comboBoxCategoriaProd.getSelectedItem();
            ComboBox proveedorProd = (ComboBox) menu.comboBoxProveedorProd.getSelectedItem();
            String ivaProd = (String) menu.comboBoxIvaProd.getSelectedItem();
            if (nombre.isEmpty() || descripcion.isEmpty() || precioCompra.isEmpty() || precioVenta.isEmpty() || cantidad.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                if (ivaProd.equalsIgnoreCase("Seleccione iva")) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un iva", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    switch (ivaProd) {
                        case "No agrava iva": {
                            prod.setIva(0);
                            break;
                        }
                        case "5%": {
                            prod.setIva(5);
                            break;
                        }
                        case "19%": {
                            prod.setIva(19);
                            break;
                        }
                    }
                    prod.setIdProducto(id);
                    prod.setNombre(nombre);
                    prod.setDescripcion(descripcion);
                    prod.setPrecioCompra(Double.valueOf(precioCompra));
                    prod.setPrecioVenta(Double.valueOf(precioVenta));
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

    public void eliminarProducto() {
        if (menu.textIdProd.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para eliminar un producto");
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
        if (menu.textIdProd.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para habilitar un producto");
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
        List<Producto> lista = prodDao.listaProductos(menu.textBuscarProd.getText());
        modeloTabla = (DefaultTableModel) menu.tableProducto.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProducto();
            ob[1] = lista.get(i).getNombre();
            ob[2] = lista.get(i).getDescripcion();
            ob[3] = lista.get(i).getCantidad();
            ob[4] = lista.get(i).getEstado();
            modeloTabla.addRow(ob);
        }
        menu.tableProducto.setModel(modeloTabla);
        JTableHeader header = menu.tableProducto.getTableHeader();
        Color headerColor = new Color(232, 158, 67);
        Color textColor = Color.WHITE;
        Font headerFont = new Font("Tahoma", Font.PLAIN, 14);
        header.setOpaque(false);
        header.setBackground(headerColor);
        header.setFont(headerFont);
        header.setForeground(textColor);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i -= 1;
        }
    }

    public void agregarContenidoInput(int fila) {
        int estado = Integer.parseInt(menu.tableProducto.getValueAt(fila, 4).toString());
        if (estado == 1) {
            menu.jMenuEliminarProd.setVisible(true);
            menu.jMenuHabilitarProd.setVisible(false);
        } else {
            menu.jMenuHabilitarProd.setVisible(true);
            menu.jMenuEliminarProd.setVisible(false);
        }
        menu.btn_modificarProd.setEnabled(true);
        menu.textCantidadProd.setEnabled(false);
        menu.btn_registrarProd.setEnabled(false);
        menu.textIdProd.setText(menu.tableProducto.getValueAt(fila, 0).toString());
        int idProducto = Integer.parseInt(menu.textIdProd.getText());
        Producto producto = prodDao.buscarProducto(idProducto);
        switch (producto.getIva()) {
            case 0: {
                menu.comboBoxIvaProd.setSelectedItem("No agrava iva");
                break;
            }
            case 5: {
                menu.comboBoxIvaProd.setSelectedItem("5%");
                break;
            }
            case 19: {
                menu.comboBoxIvaProd.setSelectedItem("19%");
                break;
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

    public void limpiarContenidoInput() {
        menu.textNombreProd.setText("");
        menu.textDescripcionProd.setText("");
        menu.textPrecioCProd.setText("");
        menu.textPrecioVProd.setText("");
        menu.textCantidadProd.setText("");
        menu.textIdProd.setText("");
        menu.comboBoxIvaProd.setSelectedItem("Seleccione iva");
        menu.btn_registrarProd.setEnabled(true);
        menu.textCantidadProd.setEnabled(true);
    }

    public void llenarComboBox() {
        List<Categoria> listaCategoria = prodDao.listarComboCategoria();
        List<Proveedor> listaProveedor = prodDao.listarComboProveedor();
        for (int i = 0; i < listaCategoria.size(); i++) {
            int idCat = listaCategoria.get(i).getIdCategoria();
            String nombreCat = listaCategoria.get(i).getNombre();
            menu.comboBoxCategoriaProd.addItem(new ComboBox(idCat, nombreCat));
        }
        for (int i = 0; i < listaProveedor.size(); i++) {
            int idProv = listaProveedor.get(i).getIdProveedor();
            String nombreProv = listaProveedor.get(i).getNombre();
            menu.comboBoxProveedorProd.addItem(new ComboBox(idProv, nombreProv));
        }
    }
    
    public void eliminarItemsCombox() {
        menu.comboBoxCategoriaProd.removeAllItems();
        menu.comboBoxProveedorProd.removeAllItems();
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
            botones.cambiarTitulo("Registro de Productos");
            limpiarTabla();
            listarProducto();
            eliminarItemsCombox();
            llenarComboBox();
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
