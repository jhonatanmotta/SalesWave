package controlador;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import metodos.BotonesMenu;
import metodos.ComboBox;
import metodos.Validaciones;
import modelo.Cliente;
import modelo.Producto;
import modelo.ProductoDAO;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import vista.Menu;

public class Ctrl_venta implements ActionListener, MouseListener, KeyListener {

    private ProductoDAO prodDao;
    private Menu menu;
    DefaultTableModel modeloTabla = new DefaultTableModel();

    public Ctrl_venta(ProductoDAO prodDao, Menu menu) {
        this.menu = menu;
        this.prodDao = prodDao;
        this.menu.btnVenta.addMouseListener(this);
        this.menu.aniadirProdVenta.addActionListener(this);

        styleVenta();
    }

    public void styleVenta() {
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

    public void agregarDetalleVenta() {
//        int selectedIndex = menu.comboBoxProductosVenta.getSelectedIndex();
//        if (selectedIndex == -1) {
////            ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductosVenta.getSelectedItem();
//            JOptionPane.showMessageDialog(null, "Debes seleccionar un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
////            System.out.println("es una instancia");
//        } else {
//            JOptionPane.showMessageDialog(null, "Todo good", "Advertencia", JOptionPane.WARNING_MESSAGE);
////            System.out.println("no es una instancia");
//        }
        int indexSeleccionado = menu.comboBoxProductosVenta.getSelectedIndex();
        String cantidadProd = menu.textCantidadProdVenta.getText();
        if (indexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            ComboBox productoSeleccionado = (ComboBox) menu.comboBoxProductosVenta.getSelectedItem();
            if (!Validaciones.validarNoVacios("Debes ingresar una cantidad", cantidadProd)) {
                if(!Validaciones.validarParseoAEntero(cantidadProd, "La cantidad debe ser un valor numerico")) {
                    return;
                }
            }
        }
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
