/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testjeu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author manal.benaissa
 */
//Ok ! Ici c'est pour écouter votre clavier ! Quand une touche du clavier est appuyé, un "event" est émis. Vous pouvez traiter l'event en question comme suit :
public class clavier implements KeyListener {
    
    private abertura abertura;

    public clavier(abertura abertura) {
        this.abertura = abertura;
    }
    
    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_RIGHT) {
            this.abertura.joueur.setDroite(true);
        }
        if (evt.getKeyCode() == evt.VK_LEFT) {
            this.abertura.joueur.setGauche(true);
        }
        if (evt.getKeyCode() == evt.VK_UP) {
            this.abertura.joueur.setHaut(true);
        }
        if (evt.getKeyCode() == evt.VK_DOWN) {
            this.abertura.joueur.setBas(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_RIGHT) {
            this.abertura.joueur.setDroite(false);
        }
        if (evt.getKeyCode() == evt.VK_LEFT) {
            this.abertura.joueur.setGauche(false);
        }
        if (evt.getKeyCode() == evt.VK_UP) {
            this.abertura.joueur.setHaut(false);
        }
        if (evt.getKeyCode() == evt.VK_DOWN) {
            this.abertura.joueur.setBas(false);
        }
    }
    

    @Override
    public void keyTyped(KeyEvent event) {
        //Nothing to do here !
    }


}
