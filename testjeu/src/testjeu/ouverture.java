package testjeu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.ArrayList;

public class ouverture {

    // --- Image de fond (paysage du niveau) ---
    private BufferedImage paysage;
    private static final double SCALE_PAYSAGE = 2;
    
    // --- HUD : icônes de vies ---
    private BufferedImage coeurPlein;
    private BufferedImage coeurVide;

    // --- Entités du jeu ---
    public Avatar joueur;
    public List<Obstacle> blocs;      // liste de tous les blocs (obstacles solides)

    // --- Paramètre de la grille (taille d’une tuile) ---
    private final int TAILLE_TUILE = 64;

    // --- Carte du niveau : 0 = vide, 1 = bloc ---
    // Chaque ligne représente une rangée de tuiles.
    // taille de la paysage en pixelart: 960x512 (30x16)
    private int[][] carte = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public ouverture() {
        // --- Chargement et redimensionnement de l’image de fond ---
        try {
            BufferedImage imgOriginal = ImageIO.read(getClass().getClassLoader().getResource("testjeu/image/paysage.png"));

            int nouvelleLargeur = (int) (imgOriginal.getWidth() * SCALE_PAYSAGE);
            int nouvelleHauteur = (int) (imgOriginal.getHeight() * SCALE_PAYSAGE);

            this.paysage = redimensionner(imgOriginal, nouvelleLargeur, nouvelleHauteur);
            
            coeurPlein = ImageIO.read(
                    getClass().getClassLoader().getResource("testjeu/image/coeurPlein.png")
            );

            coeurVide = ImageIO.read(
                    getClass().getClassLoader().getResource("testjeu/image/coeurVide.png")
            );


            
        } catch (IOException ex) {
            Logger.getLogger(ouverture.class.getName()).log(Level.SEVERE, null, ex);
        }

        // --- Création du joueur ---
        this.joueur = new Avatar();

        // --- Création de la liste de blocs à partir de la carte ---
        this.blocs = new ArrayList<>();

        for (int ligne = 0; ligne < carte.length; ligne++) {
            for (int colonne = 0; colonne < carte[ligne].length; colonne++) {

                if (carte[ligne][colonne] == 1) {
                    int x = colonne * TAILLE_TUILE;
                    int y = ligne * TAILLE_TUILE;

                    blocs.add(new Obstacle(x, y));
                }
            }
        }
    }
    
    // Réinitialise complètement le jeu (nouveau joueur, vies remises à zéro)
    public void reinitialiserJeu() {
        this.joueur = new Avatar();
        // Si un jour tu as des ennemis ou autres états, tu pourras aussi les réinitialiser ici.
    }
    
    private void dessinerVies(Graphics2D contexte) {
        int viesActuelles = joueur.getVies();
        int viesMax = joueur.getViesMax();

        int margeX = 20;
        int margeY = 20;
        int espacement = 10;

        int largeurCoeur = coeurPlein.getWidth();
        int hauteurCoeur = coeurPlein.getHeight();

        for (int i = 0; i < viesMax; i++) {

            int x = margeX + i * (largeurCoeur + espacement);
            int y = margeY;

            if (i < viesActuelles) {
                contexte.drawImage(coeurPlein, x, y, null);
            } else {
                contexte.drawImage(coeurVide, x, y, null);
            }
        }
    }

    // Redimensionner une image (utilisé pour le paysage)
    private BufferedImage redimensionner(BufferedImage img, int nouvelleLargeur, int nouvelleHauteur) {
        BufferedImage nouvelle = new BufferedImage(nouvelleLargeur, nouvelleHauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = nouvelle.createGraphics();
        g2.drawImage(img, 0, 0, nouvelleLargeur, nouvelleHauteur, null);
        g2.dispose();
        return nouvelle;
    }

    // --- Accesseurs pour la taille de l’image de fond (utilisée par la fenêtre) ---
    public int getLargeurImage() {
        return this.paysage.getWidth();
    }

    public int getHauteurImage() {
        return this.paysage.getHeight();
    }

    // --- Mise à jour de la logique de la scène (joueur + collisions, etc.) ---
    public void miseAJour() {
        this.joueur.miseAJour(this.blocs);
    }

    // --- Rendu de la scène complète ---
    public void rendu(Graphics2D contexte) {
        // 1. Dessiner le fond
        contexte.drawImage(this.paysage, 0, 0, null);

        // 2. Dessiner le joueur
        this.joueur.rendu(contexte);

        // 3. Dessiner tous les blocs de la carte
        for (Obstacle b : blocs) {
            b.rendu(contexte);
        }
        // 4. Dessiner tous les coeurs de ve
        dessinerVies(contexte);
        
        // 5. Si c’est game over…
        if (joueur.getVies() <= 0) {

            BufferedImage imgGO = joueur.getGameOverImage();

            // dessine l’image au centre de l’écran
            int x = (paysage.getWidth() - imgGO.getWidth()) / 2;
            int y = (paysage.getHeight() - imgGO.getHeight()) / 2;

            contexte.drawImage(imgGO, x, y, null);

            contexte.setColor(new java.awt.Color(0,0,0,100));
            contexte.fillRect(0,0,paysage.getWidth(),paysage.getHeight());
            contexte.drawImage(imgGO, x, y, null);

        }
    }
}
