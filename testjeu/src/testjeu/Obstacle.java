package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Rectangle;

public class Obstacle {

    // --- Sprite du bloc (obstacle) ---
    private BufferedImage bloc;

    // --- Position et dimensions du bloc ---
    private int x, y;   // position dans la fenêtre
    private int largeur, hauteur;

    // --- Facteur d’échelle pour le sprite du bloc ---
    private static final double scale = 1;

    public Obstacle(int x, int y) {
        try {
            // --- Chargement de l’image originale du bloc ---
            BufferedImage imgOriginal = ImageIO.read(
                getClass().getClassLoader().getResource("testjeu/image/bloco.png")
            );

            // --- Redimensionnement selon le facteur d’échelle ---
            int nouvelleLargeur = (int) (imgOriginal.getWidth()  * scale);
            int nouvelleHauteur = (int) (imgOriginal.getHeight() * scale);

            this.bloc = redimensionner(imgOriginal, nouvelleLargeur, nouvelleHauteur);

        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }

        // --- Position du bloc dans le monde ---
        this.x = x;
        this.y = y;

        // --- Dimensions finales du sprite ---
        this.largeur = bloc.getWidth();
        this.hauteur = bloc.getHeight();
    }

    // Retourne le rectangle de collision du bloc
    public Rectangle getBounds() {
        return new Rectangle(x, y, largeur, hauteur);
    }

    // Méthode utilitaire pour redimensionner une image
    private BufferedImage redimensionner(BufferedImage img, int nouvelleLargeur, int nouvelleHauteur) {
        BufferedImage nouvelle = new BufferedImage(nouvelleLargeur, nouvelleHauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nouvelle.createGraphics();
        g2.drawImage(img, 0, 0, nouvelleLargeur, nouvelleHauteur, null);
        g2.dispose();
        return nouvelle;
    }

    // Dessine l'obstacle à l'écran
    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.bloc, (int) x, (int) y, null);
    }
}
