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

    private BufferedImage joueur, paysage;
    protected double x, y, dx, dy, nx, ny;
    private boolean gauche, droite, haut, bas;
    private int vitx = 10, vity = 10;
    private int ha_j, la_j, ha_p, la_p; 
    private double gravite, dgrav;
    private static final double scalep = 2, scalej = 2;

    public Avatar() {
        try {
            BufferedImage imgOriginalJ = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/joueur.png"));

            int novaLarguraJ = (int) (imgOriginalJ.getWidth()  * scalej);
            int novaAlturaJ  = (int) (imgOriginalJ.getHeight() * scalej);

            this.joueur = redimensionar(imgOriginalJ, novaLarguraJ, novaAlturaJ);
            BufferedImage imgOriginalp = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/paysage.png"));

            int novaLargurap = (int) (imgOriginalp.getWidth()  * scalep);
            int novaAlturap  = (int) (imgOriginalp.getHeight() * scalep);

            this.paysage = redimensionar(imgOriginalp, novaLargurap, novaAlturap);
            
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = 100;
        this.y = 100;
        this.gauche = false;
        this.droite = false;
        this.haut   = false;
        this.bas    = false;
        this.ha_p = paysage.getHeight();
        this.la_p = paysage.getWidth();
        this.ha_j = joueur.getHeight();
        this.la_j = joueur.getWidth();
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, la_j, ha_j);
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
    
    

    public void miseAJour(List<Obstacle> blocs) {
        
        dgrav = 0.5;
        dx = 0;
        dy= 0;
        
        if (this.gauche) { dx -= vitx;}
        if (this.droite) { dx += vitx;}
        if (this.haut)   { dy -= vity;}
        if (this.bas)    { dy += vity;}
        
        nx = x + dx; 
        ny = y + dy + gravite;
        Rectangle prox_x = new Rectangle((int)nx, (int)y, la_j, ha_j);
        Rectangle prox_y = new Rectangle((int)x, (int)ny, la_j, ha_j);
        
        if ((nx <= la_p-la_j) && (nx >= 0) && (!colision(prox_x, blocs))) {
            x = nx;
        }
        if ((ny <= ha_p-ha_j) && (ny >= 0) && (!colision(prox_y, blocs))) {
            gravite = gravite + dgrav;
            y = ny;
        } else {
            gravite = 0;
        }
        
    }
    
    private boolean colision(Rectangle r, List<Obstacle> blocs) {
    for (Obstacle b : blocs) {
        if (r.intersects(b.getBounds())) {
            return true;
        }
    }
    return false;
}


    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.joueur, (int) x, (int) y, null);
    }
    
    private BufferedImage redimensionar(BufferedImage img, int novaLargura, int novaAltura) {
        BufferedImage nova = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nova.createGraphics();
        g2.drawImage(img, 0, 0, novaLargura, novaAltura, null);
        g2.dispose();
        return nova;
    }

}
