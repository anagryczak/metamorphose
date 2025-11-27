package testjeu;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Testjeu extends JFrame implements ActionListener {

    // --- Framebuffer pour dessiner manuellement l’image ---
    private BufferedImage framebuffer;
    private Graphics2D contexte;

    // --- Élément Swing qui affichera l’image du jeu ---
    private JLabel affichage;

    // --- Boucle de jeu (timer) ---
    private Timer timer;

    // --- Classe principale du jeu (gère le fond, obstacles, avatar, etc.) ---
    private ouverture ouvertureJeu;

    // --- Gestion du clavier ---
    private clavier gestionClavier;

    // --- Dimensions de la fenêtre (égales à la taille du paysage) ---
    private int largeur, hauteur;


    public Testjeu() {

        // --- Initialisation de la scène (contient le fond + le joueur + obstacles) ---
        this.ouvertureJeu = new ouverture();

        // Récupération de la taille de l’image de fond
        largeur = this.ouvertureJeu.getLargeurImage();
        hauteur = this.ouvertureJeu.getHauteurImage();

        // --- Configuration de la fenêtre ---
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label qui affichera l’image générée par le framebuffer
        this.affichage = new JLabel();
        this.affichage.setPreferredSize(new java.awt.Dimension(largeur, hauteur));
        this.setContentPane(this.affichage);
        this.pack();

        // --- Gestion du clavier (après l’instanciation de ouvertureJeu) ---
        this.gestionClavier = new clavier(ouvertureJeu);
        this.addKeyListener(this.gestionClavier);

        // --- Création du framebuffer (image sur laquelle on dessine tout) ---
        this.framebuffer = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        this.affichage.setIcon(new ImageIcon(framebuffer));
        this.contexte = this.framebuffer.createGraphics();

        // --- Timer qui servira de boucle principale du jeu (40 ms ≈ 25 fps) ---
        this.timer = new Timer(40, this);
        this.timer.start();
    }


    // --- Fonction appelée automatiquement par le Timer (boucle du jeu) ---
    @Override
    public void actionPerformed(ActionEvent e) {

        // 1. Mise à jour de toute la logique du jeu
        this.ouvertureJeu.miseAJour();

        // 2. Rendu graphique
        this.ouvertureJeu.rendu(contexte);

        // 3. On redessine le JLabel contenant le framebuffer
        this.affichage.repaint();
    }


    public static void main(String[] args) {
        Testjeu fenetre = new Testjeu();
        fenetre.setVisible(true); // lancement de la fenêtre principale
    }
}
