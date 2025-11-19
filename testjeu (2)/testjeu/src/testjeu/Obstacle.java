package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Rectangle;


public class Obstacle {
    
    public int x, y, w, h;
    private BufferedImage interf;
    
    public Obstacle (int x, int y, int w, int h){
        
        try {
            this.interf = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/terra.png"));
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = x;
        this.y = y; 
        this.w = w;
        this.h = h;
    }
    
    public Rectangle getBounds(){
        return new Rectangle(x, y, w, h);
    }
    
    public void rendu( Graphics2D contexte){
       //contexte.drawImage(this.interf, (int) x, (int) y, null);
       contexte.setColor(new Color(0, 0, 0, 120));
       contexte.fillRect(x, y, w, h);
       contexte.setColor (Color.BLACK);
       contexte.drawRect (x, y, w, h);
    }
    
}
