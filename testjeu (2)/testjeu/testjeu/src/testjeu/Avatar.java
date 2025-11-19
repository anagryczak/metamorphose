package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Avatar {

    private BufferedImage joueur;
    protected double x, y;
    private boolean gauche, droite, haut, bas;

    public Avatar() {
        try {
            this.joueur = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/joueur.png"));
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = 1000;
        this.y = 500;
        this.gauche = false;
        this.droite = false;
        this.haut   = false;
        this.bas    = false;
    }
    
    public void setGauche(boolean gauche) {
        this.gauche = gauche;
    }

    public void setDroite(boolean droite) {
        this.droite = droite;
    }
    
    public void setHaut(boolean haut) {
        this.haut = haut;
    }
    
    public void setBas(boolean bas) {
        this.bas = bas;
    }

    public void miseAJour() {
        if (this.gauche) {
            x -= 10;
        }
        if (this.droite) {
            x += 10;
        }
        if (this.haut) {
            y -= 5;
        }
        if (this.bas) {
            y += 5;
        }
        if (x > 1536-216) {
            x = 1536-216;
        }
        if (y > 1024-143) {
            y = 1024-143;
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
    }

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.joueur, (int) x, (int) y, null);
    }

}
