package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Rectangle;


public class Obstacle {
    private BufferedImage bloc;
    private int x, y, l, h; 
    
    public Obstacle() {
        try {
            this.bloc = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/bloco.png"));
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.x = 400;
        this.y = 400;
        this.l = bloc.getWidth();
        this.h = bloc.getHeight();
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, l, h);
    }
    
    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.bloc, (int) x, (int) y, null);
    }
    
    
}
