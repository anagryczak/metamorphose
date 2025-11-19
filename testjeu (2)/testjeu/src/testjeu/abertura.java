package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class abertura {
    
    private BufferedImage paysage;
    public Avatar joueur;
    private final List<Obstacle> obstacles = new ArrayList<>();
    
    public abertura() {
        //abre a imagem de fundo escolhida e colocada na pasta
        try {
            this.paysage = ImageIO.read(getClass().getResource("/testjeu/image/paysage.png"));
        } catch (IOException ex) {
            Logger.getLogger(abertura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.joueur = new Avatar();
        
        obstacles.add(new Obstacle(200, 100, 120, 40));
        //obstacles.add(new Obstacle(100, 220, 60, 120));
        //obstacles.add(new Obstacle(350, 180, 160, 40));
    }
    
    public void miseAJour() {
        int wW = paysage.getWidth();
        int wH = paysage.getHeight();
        this.joueur.miseAJour(obstacles, wW, wH);
    }

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.paysage, 0, 0, null);
        for (Obstacle o : obstacles) {
            o.rendu(contexte);
        }
        this.joueur.rendu(contexte);
    }
    
}
