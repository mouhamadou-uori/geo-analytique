package geoanalytique.model;

import geoanalytique.model.Point;
import java.util.List;
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.exception.VisiteurException;
import geoanalytique.controleur.GeoAnalytiqueControleur;

/**
 * Classe représentant un triangle dans un repère orthonormé.
 * Un triangle est un polygone à trois sommets.
 */
public class Triangle extends Polygone {

    /**
     * Constructeur par défaut.
     * Crée un triangle équilatéral centré à l'origine.
     */
    public Triangle(GeoAnalytiqueControleur controleur) {
        super(List.of(
            new Point(0, 2, controleur),
            new Point(-Math.sqrt(3), -1, controleur),
            new Point(Math.sqrt(3), -1, controleur)
        ), controleur);
    }

    public Point getPointA() {
        return (Point) sommets.toArray()[0];
    }
    
    public Point getPointB() {
        return (Point) sommets.toArray()[1];
    }
    
    public Point getPointC() {
        return (Point) sommets.toArray()[2];
    }
    

    /**
     * Constructeur avec trois sommets.
     */
    public Triangle(Point p1, Point p2, Point p3, GeoAnalytiqueControleur controleur) {
        super(List.of(p1, p2, p3), controleur);
    }

    /**
     * Constructeur avec trois sommets et un nom.
     */
    public Triangle(Point p1, Point p2, Point p3, String nom, GeoAnalytiqueControleur controleur) {
        super(nom, List.of(p1, p2, p3), controleur);
    }

    /**
     * Calcule le centre de gravité du triangle.
     */
    @Override
    public Point calculerCentreGravite() {
        // List<Point> sommets = getSommets();
        // double x = (sommets.get(0).getX() + sommets.get(1).getX() + sommets.get(2).getX()) / 3;
        // double y = (sommets.get(0).getY() + sommets.get(1).getY() + sommets.get(2).getY()) / 3;
        // return new Point((float)x, (float)y, null);
        return null;
    }

    /**
     * Calcule l'aire du triangle.
     */
    @Override
    public double calculerAire() {
        // List<Point> sommets = getSommets();
        // double a = sommets.get(0).distance(sommets.get(1));
        // double b = sommets.get(1).distance(sommets.get(2));
        // double c = sommets.get(2).distance(sommets.get(0));
        // double s = (a + b + c) / 2;
        // return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        return 0;
    }

    /**
     * Alias de calculerAire(), comme demandé par la classe Polygone.
     */
    
    public double aire() {
        return calculerAire();
    }

    /**
     * Renvoie le segment i (0 à 2) du triangle.
     */
    @Override
    public Segment getSegment(int i) {
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
        
        return switch (i) {
            case 0 -> new Segment(points[0], points[1], controleur);
            case 1 -> new Segment(points[1], points[2], controleur);
            case 2 -> new Segment(points[2], points[0], controleur);
            default -> throw new IllegalArgumentException("Index de segment invalide : " + i);
        };
    }

    public boolean estEquilateral() {
        // List<Point> s = getSommets();
        // double d1 = s.get(0).distance(s.get(1));
        // double d2 = s.get(1).distance(s.get(2));
        // double d3 = s.get(2).distance(s.get(0));
        // return Math.abs(d1 - d2) < 1e-10 && Math.abs(d2 - d3) < 1e-10;
        return false;
    }

    public boolean estIsocele() {
        // List<Point> s = getSommets();
        // double d1 = s.get(0).distance(s.get(1));
        // double d2 = s.get(1).distance(s.get(2));
        // double d3 = s.get(2).distance(s.get(0));
        // return Math.abs(d1 - d2) < 1e-10 || Math.abs(d2 - d3) < 1e-10 || Math.abs(d3 - d1) < 1e-10;
        return false;
    }

    public boolean estRectangle() {
        // List<Point> s = getSommets();
        // double d1 = s.get(0).distance(s.get(1));
        // double d2 = s.get(1).distance(s.get(2));
        // double d3 = s.get(2).distance(s.get(0));
        // double d1Carre = d1 * d1;
        // double d2Carre = d2 * d2;
        // double d3Carre = d3 * d3;
        // return Math.abs(d1Carre + d2Carre - d3Carre) < 1e-10 ||
        //        Math.abs(d2Carre + d3Carre - d1Carre) < 1e-10 ||
        //        Math.abs(d3Carre + d1Carre - d2Carre) < 1e-10;
        return false;
    }
    
    /**
     * Implémentation du patron visiteur
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitTriangle(this);
    }
}
