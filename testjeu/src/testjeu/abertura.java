package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class abertura {
    
    private BufferedImage paysage;
    public Avatar joueur;
    
    public abertura() {
        //abre a imagem de fundo escolhida e colocada na pasta
        try {
            this.paysage = ImageIO.read(getClass().getResource("/testjeu/image/paysage.png"));
        } catch (IOException ex) {
            Logger.getLogger(abertura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.joueur = new Avatar();
    }
    
    public void miseAJour() {
        this.joueur.miseAJour();
    }

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.paysage, 0, 0, null);
        this.joueur.rendu(contexte);
    }
    
}
