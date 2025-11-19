package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.util.List;

public class Avatar {

    private BufferedImage joueur;
    protected double x, y;
    private boolean gauche, droite, haut, bas;
    private int largeur, hauteur;
    private int speed_x = 10, speed_y = 10;
    private int dx = 0, dy = 0;
    

    public Avatar() {
        try {
            this.joueur = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/joueur.png"));
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = 50;
        this.y = 50;
        this.largeur = joueur.getWidth();
        this.hauteur  = joueur.getHeight();
        this.gauche  = false;
        this.droite  = false;
        this.haut    = false;
        this.bas     = false;
    }
    
    public java.awt.Rectangle getBounds() {
    return new java.awt.Rectangle((int)x, (int)y, largeur, hauteur);
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

    public void miseAJour(List<Obstacle> obstacles, int worldW, int worldH) {

        if (this.gauche) { dx -= speed_x; }
        if (this.droite) { dx += speed_x; }
        if (this.haut)   { dy -= speed_y; }
        if (this.bas)    { dy += speed_y; }
        
        double nx = x + dx; 
        double ny = y + dy;
        
        Rectangle futurX = new Rectangle((int)nx, (int)y, largeur, hauteur);
        Rectangle futurY = new Rectangle((int)x, (int)ny, largeur, hauteur);
        
        if (!colision(futurX, obstacles) && nx >= 0 && nx + largeur <= worldW)  { x = nx; }
        if (!colision(futurY, obstacles) && ny >= 0 && ny + hauteur <= worldH)  { y = ny; }

    }
    
    private boolean colision(Rectangle r, List<Obstacle> obstacles) {
    for (Obstacle o : obstacles) {
        if (r.intersects(o.getBounds())) {
            return true;
        }
    }
    return false;
}

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.joueur, (int) x, (int) y, null);
    }

}
