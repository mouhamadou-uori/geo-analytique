package geoanalytique.model;
import geoanalytique.model.Point;
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.exception.VisiteurException;
import geoanalytique.controleur.GeoAnalytiqueControleur;

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
    public Rectangle(GeoAnalytiqueControleur controleur) {
        this(new Point(-1.5, -0.5, controleur), 2, 1, controleur);
    }
    
    /**
     * Constructeur avec coin inférieur gauche, largeur et hauteur.
     * @param coinInfGauche Le coin inférieur gauche du rectangle
     * @param largeur La largeur du rectangle
     * @param hauteur La hauteur du rectangle
     */
    public Rectangle(Point coinInfGauche, double largeur, double hauteur, GeoAnalytiqueControleur controleur) {
        super(List.of(
            coinInfGauche,
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY(), controleur),
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY() + hauteur, controleur),
            new Point(coinInfGauche.getX(), coinInfGauche.getY() + hauteur, controleur)
        ), controleur);
    }
    
    /**
     * Constructeur avec coin inférieur gauche, largeur, hauteur et nom.
     * @param coinInfGauche Le coin inférieur gauche du rectangle
     * @param largeur La largeur du rectangle
     * @param hauteur La hauteur du rectangle
     * @param nom Le nom du rectangle
     */
    public Rectangle(Point coinInfGauche, double largeur, double hauteur, String nom, GeoAnalytiqueControleur controleur) {
        super(nom, List.of(
            coinInfGauche,
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY(), controleur),
            new Point(coinInfGauche.getX() + largeur, coinInfGauche.getY() + hauteur, controleur),
            new Point(coinInfGauche.getX(), coinInfGauche.getY() + hauteur, controleur)
        ), controleur);
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
    public Segment getSegment(int nb) {
        Point[] points = sommets.toArray(new Point[0]);
        GeoAnalytiqueControleur controleur = null;
        if (points.length > 0 && points[0] != null) {
            // Récupérer le contrôleur à partir d'un des points (si disponible)
            try {
                java.lang.reflect.Field field = GeoObject.class.getDeclaredField("controleur");
                field.setAccessible(true);
                controleur = (GeoAnalytiqueControleur) field.get(points[0]);
            } catch (Exception e) {
                // Ignorer les erreurs, controleur restera null
            }
        }
        
        return switch (nb) {
            case 0 -> new Segment(points[0], points[1], controleur);
            case 1 -> new Segment(points[1], points[2], controleur);
            case 2 -> new Segment(points[2], points[3], controleur);
            case 3 -> new Segment(points[3], points[0], controleur);
            default -> throw new IllegalArgumentException("Index de segment invalide : " + nb);
        };
    }

    @Override
    public double calculerAire() {
        return getLargeur() * getHauteur();
    }

    @Override
    public Point calculerCentreGravite() {
        Point[] points = sommets.toArray(new Point[0]);
        double x = (points[0].getX() + points[2].getX()) / 2;
        double y = (points[0].getY() + points[2].getY()) / 2;
        return new Point(x, y, null);
    }
    
    /**
     * Implémentation du patron visiteur
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitRectangle(this);
    }
}
