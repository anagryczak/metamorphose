package testjeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

// <<< ALTERAÇÃO >>>
import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class Testjeu extends JFrame implements ActionListener {
    
    private BufferedImage framebuffer;
    private Graphics2D contexte;
    private JLabel jLabel1;
    private Timer timer;
    private abertura abertura;

    // --- REMOVIDO: Obstacle bloco;
    // <<< ALTERAÇÃO >>>
    private List<Obstacle> blocos;
    private final int TILE = 64;

    // <<< ALTERAÇÃO >>> mapa baseado em matriz
    // 16 linhas (0–15), 24 colunas (0–23)
    // 0 = vazio, 1 = bloco
    private int[][] mapa = {
        // linha 0 (topo)
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };

    public Testjeu() {
        super("Jogo da Lagarta");

        this.setSize(1536, 1024);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        framebuffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        contexte = framebuffer.createGraphics();

        jLabel1 = new JLabel(new ImageIcon(framebuffer));
        this.add(jLabel1);

        abertura = new abertura();
        this.addKeyListener(new clavier(abertura));

        // --- REMOVIDO: bloco = new Obstacle(300, 700, 64, 64);

        // <<< ALTERAÇÃO >>> AGORA OS BLOCOS SÃO GERADOS A PARTIR DA MATRIZ
        blocos = new ArrayList<>();
        gerarBlocosDoMapa();  // <<< AQUI

        timer = new Timer(1000 / 60, this);
        timer.start();

        this.setLocationRelativeTo(null);
    }

    // <<< NOVO >>> cria blocos lendo o mapa[][]
    private void gerarBlocosDoMapa() {
        blocos.clear();
        for (int lin = 0; lin < mapa.length; lin++) {
            for (int col = 0; col < mapa[0].length; col++) {
                if (mapa[lin][col] == 1) {
                    int x = col * TILE;
                    int y = lin * TILE;
                    blocos.add(new Obstacle(x, y, TILE, TILE));
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double x0 = this.abertura.joueur.getX();
        double y0 = this.abertura.joueur.getY();

        this.abertura.miseAJour();

        // <<< ALTERAÇÃO >>> movimento separado em X e depois Y com resolução de colisão
        double dx = this.abertura.joueur.getX() - x0;
        double dy = this.abertura.joueur.getY() - y0;

        // mover no X
        this.abertura.joueur.setX(x0 + dx);
        this.abertura.joueur.setY(y0);
        resolverColisaoX(this.abertura.joueur);

        // mover no Y
        this.abertura.joueur.setY(y0 + dy);
        resolverColisaoY(this.abertura.joueur);
        
        int H = framebuffer.getHeight();
        Avatar a = this.abertura.joueur;
        if (a.getY() + a.getHeight() > H) {
            a.setY(H - a.getHeight());
            a.setVy(0);
            a.setOnGround(true);
        }

        // render
        contexte.setColor(new Color(135, 206, 235));
        contexte.fillRect(0, 0, framebuffer.getWidth(), framebuffer.getHeight());
        this.abertura.rendu(contexte);

        // <<< ALTERAÇÃO >>> desenhar todos os blocos
        for (Obstacle b : blocos) {
            b.draw(contexte);
        }

        this.jLabel1.repaint();
    }

    public static void main(String[] args) {
        Testjeu fenetre = new Testjeu();
        fenetre.setVisible(true);
    }

    // <<< ALTERAÇÃO >>> helper para adicionar plataformas horizontais
    private void adicionarPlataforma(int xInicio, int y, int blocosQtde) {
        for (int i = 0; i < blocosQtde; i++) {
            blocos.add(new Obstacle(xInicio + i * TILE, y, TILE, TILE));
        }
    }

    // <<< ALTERAÇÃO >>> colisão só no eixo X contra todos os blocos
    private void resolverColisaoX(Avatar a) {
        Rectangle ra = a.getBounds();
        for (Obstacle b : blocos) {
            Rectangle rb = b.getBounds();
            if (!ra.intersects(rb)) continue;
            Rectangle inter = ra.intersection(rb);
            if (inter.width <= 0) continue;

            int axCenter = ra.x + ra.width / 2;
            int bxCenter = rb.x + rb.width / 2;

            if (axCenter < bxCenter) {
                a.setX(a.getX() - inter.width);
            } else {
                a.setX(a.getX() + inter.width);
            }
            ra = a.getBounds();
        }
    }

    // <<< ALTERAÇÃO >>> colisão só no eixo Y contra todos os blocos
    private void resolverColisaoY(Avatar a) {
        Rectangle ra = a.getBounds();
        a.setOnGround(false);

        for (Obstacle b : blocos) {
            Rectangle rb = b.getBounds();
            if (!ra.intersects(rb)) continue;

            Rectangle inter = ra.intersection(rb);
            if (inter.height <= 0) continue;

            int ayCenter = ra.y + ra.height / 2;
            int byCenter = rb.y + rb.height / 2;

            if (ayCenter < byCenter) {
                a.setY(a.getY() - inter.height); // pousou no topo
                if (a.getVy() > 0) a.setVy(0);
                a.setOnGround(true);
            } else {
                a.setY(a.getY() + inter.height); // bateu por baixo
                if (a.getVy() < 0) a.setVy(0);
            }
            ra = a.getBounds();
        }
    }
}
