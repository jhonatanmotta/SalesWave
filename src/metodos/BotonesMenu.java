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

    public BotonesMenu(Menu menu) {
        this.menu = menu;
        this.tabbedPane = menu.jTabbedPanel;
        this.tituloPanel = menu.tituloPanel;
    }

    public void cambiarColorDefault (){
        menu.btnInicio.setBackground(colorDefault);
        menu.btnUsuario.setBackground(colorDefault);
        menu.btnProducto.setBackground(colorDefault);
        menu.btnCliente.setBackground(colorDefault);
        menu.btnCategoria.setBackground(colorDefault);
        menu.btnProveedor.setBackground(colorDefault);
        menu.btnVenta.setBackground(colorDefault);
        menu.btnHistorial.setBackground(colorDefault);
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
}