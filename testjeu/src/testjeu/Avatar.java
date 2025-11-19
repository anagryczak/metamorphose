package testjeu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Avatar {

    private BufferedImage joueur;
    protected double x, y;
    
    private boolean gauche, droite, haut, bas;
    
    // física vertical
    private double vy = 0.0;
    private boolean onGround = false;

    // constantes da gravidade (ajuste se quiser)
    private static final double G  = 2000.0;      // gravidade (px/s²)
    private static final double DT = 1.0 / 60.0;  // passo de tempo aproximado (60 FPS)
    private static final double VY_MAX = 2500.0;  // limite de velocidade para baixo
    private static final double JUMP_VY = -800.0;  // força do pulo (ajuste no feeling)

    
    

    // ---- NOVO: controle de escala e hitbox ----
    private double scale = 0.25;       // ajuste como quiser (0.5 = metade do tamanho original)
    private int drawW = 32, drawH = 20;
   
    

    public Avatar() {
        try {
            // ajuste o caminho se seu sprite tiver outro nome
            this.joueur = ImageIO.read(getClass().getResourceAsStream("/testjeu/image/joueur.png"));
            if (this.joueur != null) {
                updateDrawSize();
            }
        } catch (Exception e) {
            this.joueur = null;
            // usa tamanhos default (drawW/drawH)
        }
        // posição inicial (ajuste se quiser)
        this.x = 150;
        this.y = 700;
    }

    private void updateDrawSize() {
        if (joueur != null) {
            this.drawW = (int) Math.max(1, Math.round(joueur.getWidth()  * scale));
            this.drawH = (int) Math.max(1, Math.round(joueur.getHeight() * scale));
        }
    }

    // ---- NOVO: trocar escala em runtime ----
    public void setScale(double s) {
        this.scale = s;
        updateDrawSize();
    }

    // sua lógica de atualização
    public void miseAJour() {
        double speed = 6.0; // velocidade horizontal

        // movimento HORIZONTAL pelo teclado
        if (gauche) x -= speed;
        if (droite) x += speed;

       if (haut && onGround) {
            vy = JUMP_VY;
            onGround = false;
        }

        // GRAVIDADE: acelera para baixo
        vy += G * DT;
        if (vy > VY_MAX) vy = VY_MAX;

        // aplica a velocidade vertical na posição
        y += vy * DT;
     
        
    }

    public void rendu(Graphics2D g) {
        if (joueur != null) {
            g.drawImage(joueur, (int) x, (int) y, drawW, drawH, null);
        } else {
            g.setColor(new java.awt.Color(30, 160, 60));
            g.fillRect((int) x, (int) y, drawW, drawH);
        }
    }
    
    
    
    public void clampTo(int maxW, int maxH) {
    if (x < 0) x = 0;
    if (y < 0) y = 0;

    if (x + drawW > maxW) x = maxW - drawW;
    if (y + drawH > maxH) y = maxH - drawH;

    // caso raro: se a sprite for maior que a tela
    if (drawW > maxW) x = 0;
    if (drawH > maxH) y = 0;
  }  
    
    
    

    // ---- NOVO: retângulo para colisão (AABB) ----
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, drawW, drawH);
    }

    // getters úteis
    public int getWidth()  { return drawW; }
    public int getHeight() { return drawH; }
    public double getX()   { return x; }
    public double getY()   { return y; }
    public void setX(double v){ x = v; }
    public void setY(double v){ y = v; }
    
    public double getVy() { return vy; }
    public void setVy(double v) { vy = v; }

    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean v) { onGround = v; }


    // setters/flags de movimento (mantém seu teclado)
    public void setGauche(boolean v) { this.gauche = v; }
    public void setDroite(boolean v) { this.droite = v; }
    public void setHaut(boolean v)   { this.haut = v; }
    public void setBas(boolean v)    { this.bas = v; }
    
   
    
}
