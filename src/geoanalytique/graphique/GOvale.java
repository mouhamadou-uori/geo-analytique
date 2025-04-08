package geoanalytique.graphique;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Objet graphique permettant de dessiner un ovale quelconque à la Java
 *
 * @see java.awt.Graphics#drawOval(int, int, int, int)
 */
public class GOvale extends Graphique {
    private int x, y, width, height;

    /**
     * Constructeur de GOvale
     * 
     * @param x      Coordonnée X du coin supérieur gauche de l'ovale
     * @param y      Coordonnée Y du coin supérieur gauche de l'ovale
     * @param w      Largeur de l'ovale
     * @param h      Hauteur de l'ovale
     * @param color  Couleur de l'ovale
     */
    public GOvale(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = Color.BLUE;
    }

    /**
     * Méthode paint pour dessiner l'ovale
     *
     * @param g Le contexte graphique
     */
    @Override
    public void paint(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.drawOval(x, y, width, height);
        g.setColor(oldColor);
    }
}
