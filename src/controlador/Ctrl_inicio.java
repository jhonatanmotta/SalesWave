package controlador;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import metodos.BotonesMenu;
import vista.Menu;

public class Ctrl_inicio implements MouseListener{ 
    
    private Menu menu;
    private BotonesMenu botones;
    
    public Ctrl_inicio(Menu menu) {
        this.menu = menu;
        this.menu.btnInicio.addMouseListener(this);
        this.botones = new BotonesMenu(this.menu); 
        botones.cambiarPanel(0);
        botones.cambiarColor(menu.btnInicio);
        botones.cambiarTitulo("Bienvenido a SalesWave");
    }

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
