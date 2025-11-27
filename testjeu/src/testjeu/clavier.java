package testjeu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Classe responsable de l'écoute du clavier.
 * Lorsqu'une touche est appuyée ou relâchée, 
 * des événements (events) sont générés et interceptés ici.
 */
public class clavier implements KeyListener {

    // Référence vers la scène du jeu (où se trouve l’avatar)
    private ouverture scene;

    // Constructeur : on reçoit la scène pour pouvoir modifier l’état du joueur
    public clavier(ouverture scene) {
        this.scene = scene;
    }

    // --- Quand une touche est pressée ---
    @Override
    public void keyPressed(KeyEvent evt) {

        // Flèche droite → déplacement vers la droite
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.scene.joueur.setDroite(true);
        }

        // Flèche gauche → déplacement vers la gauche
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.scene.joueur.setGauche(true);
        }

        // Flèche haut → saut
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            this.scene.joueur.setHaut(true);
        }

        // Flèche bas (actuellement non utilisée dans la physique)
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            this.scene.joueur.setBas(true);
        }
    }

    // --- Quand une touche est relâchée ---
    @Override
    public void keyReleased(KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.scene.joueur.setDroite(false);
        }

        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.scene.joueur.setGauche(false);
        }

        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            this.scene.joueur.setHaut(false);
        }

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            this.scene.joueur.setBas(false);
        }
    }

    // --- Méthode non utilisée (nécessaire car imposée par l’interface KeyListener) ---
    @Override
    public void keyTyped(KeyEvent event) {
        // Rien à faire ici !
    }
}
