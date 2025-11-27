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
    private BufferedImage[] frames;  
    private int frameIndex = 0;     
    private int frameCounter = 0;   
    private int frameDelay = 4;
    private boolean faceDroite = true;
    protected double x, y, dx, dy, nx, ny;
    private boolean gauche, droite, haut, bas;
    private int vitx = 10, vity = 20;
    private int ha_j, la_j, ha_p, la_p; 
    private double gravite = 0, dgrav, vit_saut, vit_max;
    private static final double scalep = 2, scalej = 1.5;
    private boolean surSol = false;
    private boolean sautEnCours = false;
    private boolean hautAvant = false;

    public Avatar() {
        try {
            String[] nomes = {
                "testjeu/image/joueur_0.png",
                "testjeu/image/joueur_1.png"
            };

            frames = new BufferedImage[nomes.length];

            for (int i = 0; i < nomes.length; i++) {
                BufferedImage original = ImageIO.read(getClass().getClassLoader().getResource(nomes[i]));

                int novaLargura = (int)(original.getWidth() * scalej);
                int novaAltura = (int)(original.getHeight() * scalej);

                frames[i] = redimensionar(original, novaLargura, novaAltura);
            }
            
            this.joueur = frames[0];
            
            BufferedImage imgOriginalp = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/paysage.png"));

            int novaLargurap = (int) (imgOriginalp.getWidth()  * scalep);
            int novaAlturap  = (int) (imgOriginalp.getHeight() * scalep);

            this.paysage = redimensionar(imgOriginalp, novaLargurap, novaAlturap);
            
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = 100;
        this.y = 800;
        this.gauche = false;
        this.droite = false;
        this.haut   = false;
        this.bas    = false;
        this.ha_p = paysage.getHeight();
        this.la_p = paysage.getWidth();
        this.ha_j = frames[0].getHeight();
        this.la_j = frames[0].getWidth();
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
        vit_saut = -20;
        dx = 0;
        vit_max = 30;
        //dy= 0;
        
        if (this.gauche) { dx -= vitx; faceDroite = false;}
        if (this.droite) { dx += vitx; faceDroite = true;}
        
        
        boolean hautActuel = this.haut;
        boolean hautJustPressed  =  hautActuel && !hautAvant;
        boolean hautJustReleased = !hautActuel &&  hautAvant;
        hautAvant = hautActuel;

        if (gravite < vit_max){
                gravite = gravite + dgrav;
        }
        if (hautJustPressed && surSol && !hautJustReleased) {
            gravite = vit_saut;   // impulso pra cima (negativo)
            sautEnCours = true;      // estamos num pulo que pode ser controlado
            surSol = false;
        }
        if (hautJustReleased && sautEnCours && gravite < 0) {
            gravite *= 0.5;      // corta parte da velocidade pra cima → pulo mais baixo
            sautEnCours = false; // depois disso não controlamos mais esse pulo
        }
        
        nx = x + dx; 
        ny = y + gravite;
        Rectangle prox_x = new Rectangle((int)nx, (int)y, la_j, ha_j);
        Rectangle prox_y = new Rectangle((int)x, (int)ny, la_j, ha_j);
        
        if ((nx <= la_p-la_j) && (nx >= 0) && (!colision(prox_x, blocs))) {
            x = nx;
        }
        if ((ny <= ha_p-ha_j) && (ny >= 0) && (!colision(prox_y, blocs))) {
            surSol = false;
            y = ny;
        } else {
            gravite = 0;
            surSol = true;
        }
        
        if (dx != 0 && surSol) {
            frameCounter++;
            if (frameCounter >= frameDelay) {
                frameCounter = 0;
                frameIndex++;
            if (frameIndex >= frames.length) {
                frameIndex = 0;  // volta pro primeiro frame
            }
        }
        } else {
    
        frameIndex = 0;
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
        if (faceDroite) {
            contexte.drawImage(frames[frameIndex], (int)x, (int)y, null);
        } else {
            contexte.drawImage(frames[frameIndex],(int)x + la_j,(int)y, -la_j, ha_j, null);
        }
    }
    
    private BufferedImage redimensionar(BufferedImage img, int novaLargura, int novaAltura) {
        BufferedImage nova = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nova.createGraphics();
        g2.drawImage(img, 0, 0, novaLargura, novaAltura, null);
        g2.dispose();
        return nova;
    }

}
