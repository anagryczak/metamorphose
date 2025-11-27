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

    // --- Sprites et images ---
    private BufferedImage joueur, paysage, game_over;
    private BufferedImage[] frames;        // tableau de sprites d’animation
    private int frameIndex = 0;            // index du sprite courant
    private int frameCounter = 0;          // compteur pour le délai d’animation
    private int frameDelay = 4;            // nombre de mises à jour avant de changer de frame
    private boolean faceDroite = true;     // true = regarde à droite, false = à gauche


    // --- Vies du joueur ---
    private int vies = 3;                  // nombre de vies au départ
    private boolean enVie = true;          // true tant que le personnage est vivant
    private double xinit, yinit; // position de réapparition (respawn)

    // --- Position et mouvement ---
    protected double x, y, dx, dy, nx, ny; // x,y = position; dx,dy = déplacement; nx,ny = position future
    private boolean gauche, droite, haut, bas; // drapeaux pour les touches
    private int vitx = 10, vity = 20;      // vity não é mais usada

    // --- Dimensions ---
    private int ha_j, la_j, ha_p, la_p;    // dimensions du joueur et du paysage

    // --- Gravité et saut (corrigido) ---
    private double vy = 0.0;               // vitesse verticale (px/s)

    // Tratamos "gravite" como ACELERAÇÃO base (G), não como velocidade
    private double gravite = 4400.0;       // aceleração da gravidade (px/s²) – ajuste o feeling aqui
    private double dt = 1.0 / 60.0;        // pas de temps approximatif (60 FPS)
    private double jump_vy = -1400.0;       // impulsion initiale du saut (px/s, négatif = vers le haut)
    private double vy_max = 3000.0;        // vitesse maximale de chute

    // pulo variável + fast-fall
    private static final double SHORT_JUMP_FACTOR = 0.5; // quanto cortar se soltar ↑ cedo
    private static final double FAST_FALL_MULT    = 3.0; // multipl. da gravidade com seta pra baixo

    // --- Facteurs d’échelle pour les images ---
    private static final double scalePaysage = 2;
    private static final double scaleJoueur = 1.2;

    // --- Gestion du saut ---
    private boolean surSol = false;        // true si le joueur est en contact avec le sol
    private boolean sautEnCours = false;   // true si le saut est encore “contrôlable”
    private boolean hautAvant = false;     // état précédent de la touche haut (pour détecter press/release)

    public Avatar() {
        try {
            // --- Chargement et redimensionnement des sprites du joueur ---
            String[] nomsFrames = {
                "testjeu/image/joueur_0.png",
                "testjeu/image/joueur_1.png"
            };

            frames = new BufferedImage[nomsFrames.length];

            for (int i = 0; i < nomsFrames.length; i++) {
                BufferedImage original = ImageIO.read(
                    getClass().getClassLoader().getResource(nomsFrames[i])
                );

                int nouvelleLargeur = (int) (original.getWidth() * scaleJoueur);
                int nouvelleHauteur = (int) (original.getHeight() * scaleJoueur);

                frames[i] = redimensionar(original, nouvelleLargeur, nouvelleHauteur);
            }

            // sprite par défaut
            this.joueur = frames[0];

            // --- Chargement et redimensionnement du paysage ---
            BufferedImage imgOriginalPaysage = ImageIO.read(
                getClass().getClassLoader().getResource("testjeu/image/paysage.png")
            );

            int nouvelleLargeurPaysage = (int) (imgOriginalPaysage.getWidth() * scalePaysage);
            int nouvelleHauteurPaysage = (int) (imgOriginalPaysage.getHeight() * scalePaysage);

            this.paysage = redimensionar(imgOriginalPaysage, nouvelleLargeurPaysage, nouvelleHauteurPaysage);
            
            // --- Chargement ame over paysage ---
            this.game_over = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/game_over.png"));
            
        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }

        // --- Position initiale du joueur ---
        
        this.x = 100;
        this.y = 900;
        
        // mémoriser la position de départ pour le respawn
        this.xinit = this.x;
        this.yinit = this.y;

        // --- Initialisation des drapeaux de mouvement ---
        this.gauche = false;
        this.droite = false;
        this.haut   = false;
        this.bas    = false;

        // --- Dimensions du paysage et du joueur ---
        this.ha_p = paysage.getHeight();
        this.la_p = paysage.getWidth();
        this.ha_j = frames[0].getHeight();
        this.la_j = frames[0].getWidth();
    }

    // Rectangle de collision du joueur (hitbox)
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, la_j, ha_j);
    }

    // --- Setters pour les entrées clavier ---
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

    // --- Mise à jour de la logique du joueur ---
    public void miseAJour(List<Obstacle> blocs) {
        
        // si le joueur n’est plus en vie et n’a plus de vies, on ne met plus à jour
        if (!enVie && vies <= 0) {
            return;
        }

        dx = 0;  // déplacement horizontal à recalcular a cada frame

        // --- Lecture des entrées de déplacement horizontal ---
        if (this.gauche) {
            dx -= vitx;
            faceDroite = false;
        }
        if (this.droite) {
            dx += vitx;
            faceDroite = true;
        }

        // --- Gestion des événements de la touche haut (saut) ---
        boolean hautActuel      = this.haut;
        boolean hautJustPressed =  hautActuel && !hautAvant;   // vient d’être pressée
        boolean hautJustReleased= !hautActuel &&  hautAvant;   // vient d’être relâchée
        hautAvant = hautActuel; // mémorise l’état pour la prochaine frame

        // --- Début du saut : uniquement si on est sur le sol ---
        if (hautJustPressed && surSol) {
            vy = jump_vy;       // impulso pra cima (negativo)
            surSol = false;
            sautEnCours = true; // o pulo ainda pode ser “cortado”
        }

        // --- Gravité (com fast-fall) ---
        // usamos um "g" local para não explodir gravite acumulando FAST_FALL_MULT
        double g = gravite; // aceleração base
        if (bas && !surSol && vy > 0) {
            // seta para baixo → cai mais rápido
            g *= FAST_FALL_MULT;
        }

        // integra velocidade
        vy += g * dt;
        if (vy > vy_max) vy = vy_max;

        // --- Saut variable : si on relâche la touche pendant la montée, on réduit la hauteur ---
        if (hautJustReleased && sautEnCours && vy < 0) {
            vy *= SHORT_JUMP_FACTOR;      // corta parte da subida → pulo menor
            sautEnCours = false;
        }

        // --- Calcul des positions futures ---
        nx = x + dx;
        // AQUI estava o bug antes: era y + vy + dt (errado)
        ny = y + vy * dt;  // usa velocidade (px/s) * dt (s) → deslocamento em pixels

        Rectangle prox_x = new Rectangle((int) nx, (int) y,  la_j, ha_j);
        Rectangle prox_y = new Rectangle((int) x,  (int) ny, la_j, ha_j);

        // --- Collision et mise à jour en X ---
        if ((nx <= la_p - la_j) && (nx >= 0) && (!colision(prox_x, blocs))) {
            x = nx;
        }

        // --- Collision et mise à jour en Y (gravité + saut) ---
        if ((ny <= ha_p - ha_j) && (ny >= 0) && (!colision(prox_y, blocs))) {
            surSol = false;
            y = ny;
        } else {
            // on a touché un bloc ou la limite verticale
            if (vy > 0) {
                // só considera “no chão” se estava caindo
                surSol = true;
            }
            vy = 0;
        }

        // --- Condition de “mort” par sortie en bas du niveau ---
        if (y > ha_p - ha_j - 10) {
            gererMort();
        }

        // --- Animation des frames (uniquement si on se déplace horizontalement et qu’on est au sol) ---
        if (dx != 0 && surSol) {
            frameCounter++;
            if (frameCounter >= frameDelay) {
                frameCounter = 0;
                frameIndex++;
                if (frameIndex >= frames.length) {
                    frameIndex = 0;  // on revient à la première frame
                }
            }
        } else {
            // personnage immobile → frame de base
            frameIndex = 0;
        }
    }

    // Détection de collision avec n’importe quel obstacle de la liste
    private boolean colision(Rectangle r, List<Obstacle> blocs) {
        for (Obstacle b : blocs) {
            if (r.intersects(b.getBounds())) {
                return true;
            }
        }
        return false;
    }
    
    private void gererMort() {
        // le joueur vient de mourir (chute ou autre)
        vies--;

        if (vies > 0) {
            // il reste des vies → on réapparaît
            enVie = true;

            // remettre le joueur à la position de départ
            x = xinit;
            y = yinit;

            // réinitialiser la physique verticale
            gravite = 0;
            surSol = false;
            sautEnCours = false;

        } else {
            // plus de vies → game over (le joueur reste mort)
            enVie = false;
        }
    }

    // --- Rendu graphique du joueur ---
    public void rendu(Graphics2D contexte) {
        
        // si le joueur n’a plus de vies, on ne l’affiche plus
        if (!enVie && vies <= 0) {
            return;
        }
        
        if (faceDroite) {
            // affichage normal (vers la droite)
            contexte.drawImage(frames[frameIndex], (int) x, (int) y, null);
        } else {
            // affichage miroir horizontal (vers la gauche)
            contexte.drawImage(frames[frameIndex],(int) x + la_j,(int) y,-la_j, ha_j,null);
        }
    }

    // Redimensionnement d’une image (utilisé pour le joueur et le paysage)
    private BufferedImage redimensionar(BufferedImage img, int nouvelleLargeur, int nouvelleHauteur) {
        BufferedImage nouvelle = new BufferedImage(nouvelleLargeur, nouvelleHauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nouvelle.createGraphics();
        g2.drawImage(img, 0, 0, nouvelleLargeur, nouvelleHauteur, null);
        g2.dispose();
        return nouvelle;
    }
    
    // --- Accesseurs pour le nombre de vies et l’état de vie ---
    public int getVies() {
        return vies;
    }

    public boolean estEnVie() {
        return enVie;
    }

}
