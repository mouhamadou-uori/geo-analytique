package geoanalytique.model;
import geoanalytique.model.Point;

import java.util.List;

/**
 * Classe représentant un rectangle dans un repère orthonormé.
 * Un rectangle est un polygone à quatre sommets avec des angles droits.
 */
public class Rectangle extends Polygone {
    
    /**
     * Constructeur par défaut.
     * Crée un rectangle centré à l'origine avec une largeur de 2 et une hauteur de 1.
     */
    public Rectangle() {
        this(new Point(-1.5, -0.5, null), 2, 1);
    }
    
    /**
     * Constructeur avec coin inférieur gauche, largeur et hauteur.
     * @param coinInfGauche Le coin inférieur gauche du rectangle
     * @param largeur La largeur du rectangle
     * @param hauteur La hauteur du rectangle
     */
    public Rectangle(Point coinInfGauche, double largeur, double hauteur) {
        super(List.of(
            coinInfGauche,
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY(), null),
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY() + hauteur, null),
            new Point(coinInfGauche.getX(), coinInfGauche.getY() + hauteur, null)
        ), null);
    }
    
    /**
     * Constructeur avec coin inférieur gauche, largeur, hauteur et nom.
     * @param coinInfGauche Le coin inférieur gauche du rectangle
     * @param largeur La largeur du rectangle
     * @param hauteur La hauteur du rectangle
     * @param nom Le nom du rectangle
     */
    public Rectangle(Point coinInfGauche, double largeur, double hauteur, String nom) {
        super(List.of(
            coinInfGauche,
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY(), null),
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY() + hauteur, null),
            new Point(coinInfGauche.getX(), coinInfGauche.getY() + hauteur, null)
        ), null);
    }
    
    /**
     * Retourne la largeur du rectangle.
     * @return La largeur du rectangle
     */
    public double getLargeur() {
        // List<Point> sommets = getSommets();
        // return sommets.get(0).distance(sommets.get(1));
        return 0.0;
    }
    
    /**
     * Retourne la hauteur du rectangle.
     * @return La hauteur du rectangle
     */
    public double getHauteur() {
        // List<Point> sommets = getSommets();
        // return sommets.get(0).distance(sommets.get(3));
        return 0.0;
    }

    @Override
    public Segment getSegment (int nb){
        // TODO : a completer
        return null;
    }

    @Override
    public double calculerAire (){
        // TODO : a completer
        return 0.0;
    }

    @Override
    public Point calculerCentreGravite (){
        // TODO : a completer
        return null;
    }
}
