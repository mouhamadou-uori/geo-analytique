package geoanalytique.model;

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
        this(new Point(-1, -0.5), 2, 1);
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
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY()),
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY() + hauteur),
            new Point(coinInfGauche.getX(), coinInfGauche.getY() + hauteur)
        ), "Rectangle");
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
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY()),
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY() + hauteur),
            new Point(coinInfGauche.getX(), coinInfGauche.getY() + hauteur)
        ), nom);
    }
    
    /**
     * Retourne la largeur du rectangle.
     * @return La largeur du rectangle
     */
    public double getLargeur() {
        List<Point> sommets = getSommets();
        return sommets.get(0).distance(sommets.get(1));
    }
    
    /**
     * Retourne la hauteur du rectangle.
     * @return La hauteur du rectangle
     */
    public double getHauteur() {
        List<Point> sommets = getSommets();
        return sommets.get(0).distance(sommets.get(3));
    }
    
    @Override
    public double aire() {
        return getLargeur() * getHauteur();
    }
}
