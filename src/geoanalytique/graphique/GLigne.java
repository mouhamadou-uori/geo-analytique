package geoanalytique.graphique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
 
/**
 * Objet permettant de tracer des lignes sur le canevas.
 * 
 */
public class GLigne extends Graphique {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    
    public static final int EPAISSEUR = 6;

    /**
     * Constructeur d'une ligne avec coordonnées et couleur par défaut (noir)
     * @param x1 abscisse du premier point
     * @param y1 ordonnée du premier point
     * @param x2 abscisse du deuxième point
     * @param y2 ordonnée du deuxième point
     */
    public GLigne(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = Color.BLACK;
    }
    
    /**
     * Constructeur d'une ligne avec coordonnées et couleur spécifiée
     * @param x1 abscisse du premier point
     * @param y1 ordonnée du premier point
     * @param x2 abscisse du deuxième point
     * @param y2 ordonnée du deuxième point
     * @param color couleur de la ligne
     */
    public GLigne(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }
    
    public int getX1() {
        return x1;
    }
    
    public int getY1() {
        return y1;
    }
    
    public int getX2() {
        return x2;
    }
    
    public int getY2() {
        return y2;
    }

    /**
     * Mettre une couleur sur la ligne
     * @param c l'objet de la couleur
     */
    @Override
    public void setCouleur(Color c) {
        this.color = c;
    }
    
    /**
     * Vérifie si deux lignes sont égales
     * @param o l'objet à comparer
     * @return true si les deux lignes ont les mêmes coordonnées, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof GLigne) {
            GLigne ligne = (GLigne)o;
            return ligne.getX1() == x1 && ligne.getY1() == y1 
                && ligne.getX2() == x2 && ligne.getY2() == y2;
        }
        return false;
    }

    /**
     * Dessine la ligne sur le contexte graphique
     * @param g contexte graphique
     */
    @Override
    public void paint(Graphics g) {
        // Color save = g.getColor();
        // g.setColor(color);
        // if (Math.abs(x2 - x1) < Math.abs(y2 - y1)) {
            
        // }
        // for (int i = 0; i < EPAISSEUR; i++) {
        //     g.drawLine(x1, y1 + i, x2, y2 + i); // Décalage vertical
        // }
        // g.setColor(save);
        Graphics2D g2d = (Graphics2D) g;
        Color saveColor = g2d.getColor();
        Stroke saveStroke = g2d.getStroke();
        
        g2d.setColor(color);
        
        // Utiliser un cap d'extrémité plat au lieu de l'arrondi par défaut
        g2d.setStroke(new BasicStroke(
            EPAISSEUR,                  // Épaisseur
            BasicStroke.CAP_BUTT,       // Extrémités plates
            BasicStroke.JOIN_MITER      // Jonctions en pointe
        ));
        
        g2d.drawLine(x1, y1, x2, y2);
        
        g2d.setColor(saveColor);
        g2d.setStroke(saveStroke);
        }
    
    /**
     * Renvoie une représentation textuelle de la ligne
     * @return chaîne de caractères représentant la ligne
     */
    @Override
    public String toString() {
        return "Ligne: de (x1=" + x1 + ", y1=" + y1 + ") à (x2=" + x2 + ", y2=" + y2 + ")";
    }
}
