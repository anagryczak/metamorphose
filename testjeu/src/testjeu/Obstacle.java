package testjeu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Obstacle {

    private int x, y, w, h;
    private BufferedImage texture;
    private boolean spike;   // <<< NOVO: indica se é espinho

    // construtor original 
    // cria um bloco NORMAL
    public Obstacle(int x, int y, int w, int h) {
        this(x, y, w, h, false); // chama o novo construtor e marca spike = false
    }

    // <<< NOVO >>> construtor que permite criar bloco COM espinho
    public Obstacle(int x, int y, int w, int h, boolean spike) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.spike = spike;

        try {
            if (spike) {
                // carrega sprite do espinho
                texture = ImageIO.read(
                    getClass().getResourceAsStream("/testjeu/image/Spike-Sprite.png")
                );
            } else {
                // carrega sprite normal
                texture = ImageIO.read(
                    getClass().getResourceAsStream("/testjeu/image/bloc.png")
                );
            }

        } catch (Exception e) {
            texture = null; // fallback para evitar travamento
            System.out.println("Erro ao carregar textura do Obstacle: " + e.getMessage());
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public void draw(Graphics2D g) {
        if (texture != null) {
            g.drawImage(texture, x, y, w, h, null);
        } else {
            // fallback
            g.setColor(spike ? java.awt.Color.RED : java.awt.Color.GRAY);
            g.fillRect(x, y, w, h);
        }
    }

    // <<< NOVO >>> útil no futuro (dano)
    public boolean isSpike() {
        return spike;
    }

    // getters opcionais (mantidos iguais)
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return w; }
    public int getHeight() { return h; }
}

