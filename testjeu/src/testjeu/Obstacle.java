package testjeu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Obstacle {
    private int x, y, w, h;
    private BufferedImage texture;
    
    public Obstacle(int x, int y, int w, int h) {
       this.x = x;
       this.y = y;
       this.w = w;
       this.h = h;

        try {
            // carrega a imagem do package testjeu.image
            texture = ImageIO.read(getClass().getResourceAsStream("/testjeu/image/bloc.png"));
        } catch (Exception e) {
            texture = null; // se der erro, evita travar
        }
    }
    
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    } 
    
    
    public void draw(Graphics2D g) {
        if (texture != null) {
            // desenha a imagem ajustando ao tamanho w x h
            g.drawImage(texture, x, y, w, h, null);
        } else {
            // fallback: se não conseguir carregar a imagem, desenha um retângulo cinza
            g.setColor(java.awt.Color.GRAY);
            g.fillRect(x, y, w, h);
        }
    }
    
    
    
    
    
    
    
}
