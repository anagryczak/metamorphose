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
    private BufferedImage joueur, paysage;
    private BufferedImage[] frames;        // tableau de sprites d’animation
    private int frameIndex = 0;            // index du sprite courant
    private int frameCounter = 0;          // compteur pour le délai d’animation
    private int frameDelay = 4;            // nombre de mises à jour avant de changer de frame
    private boolean faceDroite = true;     // true = regarde à droite, false = à gauche

    // --- État de vie ---
    private boolean enVie = true;          // true tant que le personnage est vivant

    // --- Position et mouvement ---
    protected double x, y, dx, dy, nx, ny; // x,y = position; dx,dy = déplacement; nx,ny = position future
    private boolean gauche, droite, haut, bas; // drapeaux pour les touches
    private int vitx = 10, vity = 20;      // vitesses de base (horizontale/verticale, ici vity n’est plus utilisée)

    // --- Dimensions ---
    private int ha_j, la_j, ha_p, la_p;    // dimensions du joueur et du paysage

    // --- Gravité et saut ---
    private double gravite = 0;            // vitesse verticale actuelle
    private double dgrav;                  // accélération due à la gravité
    private double vitesseSaut;            // impulsion initiale du saut
    private double vitesseMaxChute;        // vitesse maximale de chute

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

        } catch (IOException ex) {
            Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
        }

        // --- Position initiale du joueur ---
        this.x = 100;
        this.y = 800;

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

        // paramètres physiques
        dgrav = 0.5;          // accélération de la gravité
        vitesseSaut = -20;    // impulsion initiale du saut (vers le haut)
        vitesseMaxChute = 30; // vitesse maximale de chute

        dx = 0;               // on ré-initialise le déplacement horizontal

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
        boolean hautActuel = this.haut;
        boolean hautJustPressed  =  hautActuel && !hautAvant;   // vient d’être pressée
        boolean hautJustReleased = !hautActuel &&  hautAvant;   // vient d’être relâchée
        hautAvant = hautActuel; // on mémorise l’état pour la prochaine frame

        // --- Gravité : augmentation progressive de la vitesse verticale ---
        if (gravite < vitesseMaxChute) {
            gravite = gravite + dgrav;
        }

        // --- Début du saut : uniquement si on est sur le sol ---
        if (hautJustPressed && surSol && !hautJustReleased) {
            gravite = vitesseSaut;   // impulsion vers le haut (valeur négative)
            sautEnCours = true;      // le saut est en cours et peut encore être “raccourci”
            surSol = false;
        }

        // --- Saut variable : si on relâche la touche pendant la montée, on réduit la hauteur ---
        if (hautJustReleased && sautEnCours && gravite < 0) {
            gravite *= 0.5;          // réduit la vitesse ascendante → saut plus petit
            sautEnCours = false;     // après ça, le saut n’est plus contrôlé
        }

        // --- Calcul des positions futures ---
        nx = x + dx;
        ny = y + gravite;

        Rectangle prox_x = new Rectangle((int) nx, (int) y, la_j, ha_j);
        Rectangle prox_y = new Rectangle((int) x, (int) ny, la_j, ha_j);

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
            gravite = 0;
            surSol = true;
        }

        // --- Condition de “mort” par sortie en bas du niveau ---
        if (y > ha_p - ha_j - 10) {
            enVie = false;
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

    // --- Rendu graphique du joueur ---
    public void rendu(Graphics2D contexte) {
        if (faceDroite) {
            // affichage normal (vers la droite)
            contexte.drawImage(frames[frameIndex], (int) x, (int) y, null);
        } else {
            // affichage miroir horizontal (vers la gauche)
            contexte.drawImage(frames[frameIndex],
                               (int) x + la_j,
                               (int) y,
                               -la_j,
                               ha_j,
                               null);
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

}
