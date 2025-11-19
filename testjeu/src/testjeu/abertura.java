package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.ArrayList;


public class abertura {
    
    private BufferedImage paysage;
    private static final double scale = 2;
    public Avatar joueur;
    public List<Obstacle> blocs;
    private final int TILE = 128;
    private int[][] map = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    
    public abertura() {
        //abre a imagem de fundo escolhida e colocada na pasta
        try {
            BufferedImage imgOriginal = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/paysage.png"));

            int novaLargura = (int) (imgOriginal.getWidth()  * scale);
            int novaAltura  = (int) (imgOriginal.getHeight() * scale);

            this.paysage = redimensionar(imgOriginal, novaLargura, novaAltura);
        } catch (IOException ex) {
            Logger.getLogger(abertura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.joueur = new Avatar();
        this.blocs = new ArrayList<>();
        
        for (int linha = 0; linha < map.length; linha++) {
            for (int coluna = 0; coluna < map[linha].length; coluna++) {

                if (map[linha][coluna] == 1) {
                    int x = coluna * TILE;
                    int y = linha * TILE;

                    blocs.add(new Obstacle(x, y));
                }
            }
        }
    }
    
    private BufferedImage redimensionar(BufferedImage img, int novaLargura, int novaAltura) {
        BufferedImage nova = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nova.createGraphics();
        g2.drawImage(img, 0, 0, novaLargura, novaAltura, null);
        g2.dispose();
        return nova;
    }
    
    public int getLargeurImage() {
        return this.paysage.getWidth();
    }

    public int getHauteurImage() {
        return this.paysage.getHeight();
    }
    
    public void miseAJour() {
        this.joueur.miseAJour(this.blocs);
    }

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.paysage, 0, 0, null);
        this.joueur.rendu(contexte);
        for (Obstacle b : blocs) {
            b.rendu(contexte);
        }
    }
    
}
