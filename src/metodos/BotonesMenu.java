package metodos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import vista.Menu;

public class BotonesMenu{

    private Menu menu;
    private JTabbedPane tabbedPane;
    private JLabel tituloPanel;
    private final Color colorDefault = new Color(0, 161, 199);
    private final Color colorPressed = new Color(0, 149, 184);
    
//    public BotonesMenu(Menu menu) {
//        this.menu = menu;
//        this.tabbedPane = menu.jTabbedPanel;
//        tabbedPane.setSelectedIndex(1);
//        this.menu.btnInicio.addActionListener(this);
//        this.menu.btnUsuario.addActionListener(this);
//        this.menu.btnProducto.addActionListener(this);
//        this.menu.btnCliente.addActionListener(this);
//        this.menu.btnCategoria.addActionListener(this);
//        this.menu.btnProveedor.addActionListener(this);
//        this.menu.btnVenta.addActionListener(this);
//        this.menu.btnReporte.addActionListener(this);
//        this.menu.btn_actualizarStock.addActionListener(this);
//    }
    public BotonesMenu(Menu menu) {
        this.menu = menu;
        this.tabbedPane = menu.jTabbedPanel;
        this.tituloPanel = menu.tituloPanel;
//        tabbedPane.setSelectedIndex(1);
//        this.menu.btnInicio.addActionListener(this);
//        this.menu.btnUsuario.addActionListener(this);
//        this.menu.btnProducto.addActionListener(this);
//        this.menu.btnCliente.addActionListener(this);
//        this.menu.btnCategoria.addActionListener(this);
//        this.menu.btnProveedor.addActionListener(this);
//        this.menu.btnVenta.addActionListener(this);
//        this.menu.btnReporte.addActionListener(this);
//        this.menu.btn_actualizarStock.addActionListener(this);
    }

    public void cambiarColorDefault (){
        menu.btnInicio.setBackground(colorDefault);
        menu.btnUsuario.setBackground(colorDefault);
        menu.btnProducto.setBackground(colorDefault);
        menu.btnCliente.setBackground(colorDefault);
        menu.btnCategoria.setBackground(colorDefault);
        menu.btnProveedor.setBackground(colorDefault);
        menu.btnVenta.setBackground(colorDefault);
        menu.btnEmpresa.setBackground(colorDefault);
    }
    
    public void cambiarColor (JButton boton) {
        cambiarColorDefault ();
        boton.setBackground(colorPressed);
    }
    
    public void cambiarPanel (int num) {
        tabbedPane.setSelectedIndex(num);
    }
    
    public void cambiarTitulo (String texto) {
        tituloPanel.setText(texto);
    }
    
    
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        cambiarColorDefault ();
//        if (e.getSource() == menu.btnInicio) {
//            cambiarPanel (0);
//            cambiarColor (menu.btnInicio);
//        } else if (e.getSource() == menu.btnUsuario) {
//            cambiarPanel (1);
//            cambiarColor (menu.btnUsuario);
//        } else if (e.getSource() == menu.btnProducto) {
//            cambiarPanel(2);
//            cambiarColor (menu.btnProducto);
//            
//        } else if (e.getSource() == menu.btnCliente) {
//            cambiarPanel(3);
//            cambiarColor (menu.btnCliente);
//        } else if (e.getSource() == menu.btnCategoria) {
//            cambiarPanel(4);
//            cambiarColor (menu.btnCategoria);
//        } else if (e.getSource() == menu.btnProveedor) {
//            cambiarPanel(5);
//            cambiarColor (menu.btnProveedor);
//        } else if (e.getSource() == menu.btnVenta) {
//            cambiarPanel(6);
//            cambiarColor (menu.btnVenta);
//        } else if (e.getSource() == menu.btnReporte) {
//            cambiarPanel(7);
//            cambiarColor (menu.btnReporte);
//        } else if (e.getSource() == menu.btn_actualizarStock) {
//            cambiarPanel(8);
//        }
    }

//}
