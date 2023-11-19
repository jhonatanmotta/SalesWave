package controlador;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import metodos.BotonesMenu;
import vista.Menu;

public class Ctrl_inicio implements MouseListener{ 
    
    // atributos del controlador inicio
    private Menu menu; // atributo de tipo Menu para asignar una instancia de este
    private BotonesMenu botones; // atributo de tipo BotonesMenu para asignar una instancia de este
    
    // constructor con parametros
    public Ctrl_inicio(Menu menu) {
        this.menu = menu; // Se le asigna al atributo menu la instancia que llega por parametro
        this.menu.btnInicio.addMouseListener(this); // se le añade el ActionListener al boton btnInicio
        this.botones = new BotonesMenu(this.menu); // Se le asigna al atributo botones la instancia de BotonesMenu
        botones.cambiarPanel(0); // se elige predeterminado (el de inicio)
        botones.cambiarColor(menu.btnInicio); // se cambia el boton de inicio a su estado en pressed)
        botones.cambiarTitulo("Bienvenido a SalesWave"); // se cambia el titulo de la vista de inicio
    }

    // mouseClicked que recibe todos los clicks de la vista y ejecuta los metodos de BotonesMenu
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btnInicio) {
            botones.cambiarPanel(0);
            botones.cambiarColor(menu.btnInicio);
            botones.cambiarTitulo("Bienvenido a SalesWave");
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
    
}
