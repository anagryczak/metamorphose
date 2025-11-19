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
    private static final double scale = 2;
    
    public Obstacle(int x, int y) {
        try {
            BufferedImage imgOriginal = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/bloco.png"));

            int novaLargura = (int) (imgOriginal.getWidth()  * scale);
            int novaAltura  = (int) (imgOriginal.getHeight() * scale);

            this.bloc = redimensionar(imgOriginal, novaLargura, novaAltura);
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.x = x;
        this.y = y;
        this.l = bloc.getWidth();
        this.h = bloc.getHeight();
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, l, h);
    }
    
    private BufferedImage redimensionar(BufferedImage img, int novaLargura, int novaAltura) {
        BufferedImage nova = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nova.createGraphics();
        g2.drawImage(img, 0, 0, novaLargura, novaAltura, null);
        g2.dispose();
        return nova;
    }
    
    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.bloc, (int) x, (int) y, null);
    }
    
    
}
