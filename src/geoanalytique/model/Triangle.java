package geoanalytique.model;

import java.util.List;

/**
 * Classe représentant un triangle dans un repère orthonormé.
 * Un triangle est un polygone à trois sommets.
 */
public class Triangle extends Polygone {
    
    /**
     * Constructeur par défaut.
     * Crée un triangle équilatéral centré à l'origine.
     */
    public Triangle() {
        super(List.of(
            new Point(0, 2),
            new Point(-Math.sqrt(3), -1),
            new Point(Math.sqrt(3), -1)
        ), "Triangle");
    }
    
    /**
     * Constructeur avec trois sommets.
     * @param p1 Premier sommet
     * @param p2 Deuxième sommet
     * @param p3 Troisième sommet
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(List.of(p1, p2, p3), "Triangle");
    }
    
    /**
     * Constructeur avec trois sommets et nom.
     * @param p1 Premier sommet
     * @param p2 Deuxième sommet
     * @param p3 Troisième sommet
     * @param nom Le nom du triangle
     */
    public Triangle(Point p1, Point p2, Point p3, String nom) {
        super(List.of(p1, p2, p3), nom);
    }
    
    /**
     * Calcule le centre de gravité du triangle.
     * @return Le centre de gravité
     */
    public Point centreGravite() {
        List<Point> sommets = getSommets();
        double x = (sommets.get(0).getX() + sommets.get(1).getX() + sommets.get(2).getX()) / 3;
        double y = (sommets.get(0).getY() + sommets.get(1).getY() + sommets.get(2).getY()) / 3;
        return new Point(x, y, "Centre de gravité de " + getNom());
    }
    
    /**
     * Vérifie si le triangle est équilatéral.
     * @return true si le triangle est équilatéral, false sinon
     */
    public boolean estEquilateral() {
        List<Point> sommets = getSommets();
        double d1 = sommets.get(0).distance(sommets.get(1));
        double d2 = sommets.get(1).distance(sommets.get(2));
        double d3 = sommets.get(2).distance(sommets.get(0));
        
        return Math.abs(d1 - d2) < 1e-10 && Math.abs(d2 - d3) < 1e-10;
    }
    
    /**
     * Vérifie si le triangle est isocèle.
     * @return true si le triangle est isocèle, false sinon
     */
    public boolean estIsocele() {
        List<Point> sommets = getSommets();
        double d1 = sommets.get(0).distance(sommets.get(1));
        double d2 = sommets.get(1).distance(sommets.get(2));
        double d3 = sommets.get(2).distance(sommets.get(0));
        
        return Math.abs(d1 - d2) < 1e-10 || Math.abs(d2 - d3) < 1e-10 || Math.abs(d3 - d1) < 1e-10;
    }
    
    /**
     * Vérifie si le triangle est rectangle.
     * @return true si le triangle est rectangle, false sinon
     */
    public boolean estRectangle() {
        List<Point> sommets = getSommets();
        double d1 = sommets.get(0).distance(sommets.get(1));
        double d2 = sommets.get(1).distance(sommets.get(2));
        double d3 = sommets.get(2).distance(sommets.get(0));
        
        // Théorème de Pythagore
        double d1Carre = d1 * d1;
        double d2Carre = d2 * d2;
        double d3Carre = d3 * d3;
        
        return Math.abs(d1Carre + d2Carre - d3Carre) < 1e-10 ||
               Math.abs(d2Carre + d3Carre - d1Carre) < 1e-10 ||
               Math.abs(d3Carre + d1Carre - d2Carre) < 1e-10;
    }
}

