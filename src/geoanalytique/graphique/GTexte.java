package geoanalytique.graphique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

/**
 * Graphique permettant d'afficher un texte dans une zone de dessin
 * 
 * @see Graphics#drawString(java.lang.String, int, int) 
 */
public class GTexte extends Graphique {
    private int x;
    private int y;
    private String txt;
    private Font police = new Font("Arial", Font.PLAIN, 20);
    
    /**
     * Constructeur avec couleur spécifiée
     * 
     * @param x coordonnée horizontale du texte
     * @param y coordonnée verticale du texte
     * @param txt texte à afficher
     * @param color couleur du texte
     */
    public GTexte(int x, int y, String txt, Color color) {
        this.x = x;
        this.y = y;
        this.txt = txt;
        this.color = color;
    }
    
    /**
     * Constructeur avec couleur par défaut (noir)
     * 
     * @param x coordonnée horizontale du texte
     * @param y coordonnée verticale du texte
     * @param txt texte à afficher
     */
    public GTexte(int x, int y, String txt) {
        this(x, y, txt, Color.BLUE);
    }
     
    /**
     * Dessine le texte dans la zone graphique
     * 
     * @param g contexte graphique
     * @see Graphics#drawString(java.lang.String, int, int) 
     */
    @Override
    public void paint(Graphics g) {
        Color oldColor = g.getColor();
        g.setFont(police);
        g.setColor(color);
        g.drawString(txt, x, y);
        g.setColor(oldColor);
    }
}