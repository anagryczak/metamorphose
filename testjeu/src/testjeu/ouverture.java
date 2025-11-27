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

    // --- Entités du jeu ---
    public Avatar joueur;
    public List<Obstacle> blocs;      // liste de tous les blocs (obstacles solides)

    // --- Paramètre de la grille (taille d’une tuile) ---
    private final int TAILLE_TUILE = 64;

    // --- Carte du niveau : 0 = vide, 1 = bloc ---
    // Chaque ligne représente une rangée de tuiles.
    private int[][] carte = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public ouverture() {
        // --- Chargement et redimensionnement de l’image de fond ---
        try {
            BufferedImage imgOriginal = ImageIO.read(
                getClass().getClassLoader().getResource("testjeu/image/paysage.png")
            );

            int nouvelleLargeur = (int) (imgOriginal.getWidth() * SCALE_PAYSAGE);
            int nouvelleHauteur = (int) (imgOriginal.getHeight() * SCALE_PAYSAGE);

            this.paysage = redimensionner(imgOriginal, nouvelleLargeur, nouvelleHauteur);
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
    }
}
