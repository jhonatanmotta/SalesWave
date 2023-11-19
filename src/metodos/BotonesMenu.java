package metodos;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import vista.Menu;

public class BotonesMenu{
    
    // Vista del menu
    private Menu menu;
    // Tap Panel
    private JTabbedPane tabbedPane;
    // Titulo del panel
    private JLabel tituloPanel;
    // color por default de los botones
    private final Color colorDefault = new Color(0, 161, 199);
    // color pressed de los botones
    private final Color colorPressed = new Color(0, 149, 184);

    // Metodo constructor que obtenie la vista del menu
    public BotonesMenu(Menu menu) {
        this.menu = menu; // Asigna la vista del menu a la variable local menu
        this.tabbedPane = menu.jTabbedPanel; // Asigna el elemento de la vista jTabbedPanel a la variable local tabbedPane
        this.tituloPanel = menu.tituloPanel; // Asigna el elemento de la vista tituloPanel a la variable local tituloPanel
    }

    //metodo para cambiar todos los botones del menu a su color por default
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
    
    //metodo para cambiar de color el boton que se presiono
    public void cambiarColor (JButton boton) {
        cambiarColorDefault ();
        boton.setBackground(colorPressed);
    }
    
    // metodo para cambiar la vista del menu
    public void cambiarPanel (int num) {
        tabbedPane.setSelectedIndex(num);
    }
    
    // metodo para cambiar el titulo de cada vista del menu
    public void cambiarTitulo (String texto) {
        tituloPanel.setText(texto);
    }
}