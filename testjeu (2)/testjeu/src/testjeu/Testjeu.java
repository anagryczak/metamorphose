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

    private BufferedImage framebuffer;
    private Graphics2D contexte;
    private JLabel jLabel1;
    private Timer timer;
    private abertura abertura;
    private clavier keyL;

    
    public Testjeu() {
        
        
        // initialisation de la fenetre
        this.setSize(1536, 1024);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.jLabel1 = new JLabel();
        this.jLabel1.setPreferredSize(new java.awt.Dimension(1536, 1024));
        this.setContentPane(this.jLabel1);
        this.pack();

        
        // Cria a abertura
        this.abertura = new abertura();
        
        this.keyL = new clavier(abertura); //Of course on le met APRES avoir déclaré Jeu()....
        this.addKeyListener(this.keyL);
        
        this.framebuffer = new BufferedImage(this.jLabel1.getWidth(), this.jLabel1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.jLabel1.setIcon(new ImageIcon(framebuffer));
        this.contexte = this.framebuffer.createGraphics();


        // Creation du Timer qui appelle this.actionPerformed() tous les 40 ms
        this.timer = new Timer(40, this);
        this.timer.start();
    }
    

    public void actionPerformed(ActionEvent e) {
        this.abertura.miseAJour();
        this.abertura.rendu(contexte);
        this.jLabel1.repaint();
    }
    
    public static void main(String[] args) {
        Testjeu fenetre = new Testjeu();
        fenetre.setVisible(true);
    }
    
}