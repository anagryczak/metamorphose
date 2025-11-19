package testjeu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class clavier implements KeyListener {
    
    private abertura abertura;

    public clavier(abertura abertura) {
        this.abertura = abertura;
    }
    
    @Override
    public void keyPressed(KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.abertura.joueur.setDroite(true);
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.abertura.joueur.setGauche(true);
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            this.abertura.joueur.setHaut(true);   // agora tecla ↑ ativa o pulo
        }
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            this.abertura.joueur.setBas(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.abertura.joueur.setDroite(false);
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.abertura.joueur.setGauche(false);
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            this.abertura.joueur.setHaut(false);  // seta pra cima solta
        }
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            this.abertura.joueur.setBas(false);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent event) {
        // nada aqui
    }
}
