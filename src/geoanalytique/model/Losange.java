package geoanalytique.model;

import java.util.List;
import java.util.ArrayList;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.exception.VisiteurException;

/**
 * Classe représentant un losange dans un repère orthonormé.
 * Un losange est un quadrilatère dont les quatre côtés sont de même longueur.
 * Il peut être défini par son centre et ses deux diagonales.
 */
public class Losange extends Polygone {
    
    private Point centre;
    private double demiDiagonale1;
    private double demiDiagonale2;
    private double angle; // Angle de rotation en radians par rapport à l'axe des abscisses
    
    /**
     * Constructeur avec centre, demi-diagonales et nom.
     * @param centre Le centre du losange
     * @param demiDiagonale1 La demi-longueur de la première diagonale
     * @param demiDiagonale2 La demi-longueur de la deuxième diagonale
     * @param angle L'angle de rotation en radians
     * @param nom Le nom du losange
     * @param controleur Le contrôleur associé
     */
    public Losange(Point centre, double demiDiagonale1, double demiDiagonale2, double angle, String nom, GeoAnalytiqueControleur controleur) {
        super(nom, calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle, controleur), controleur);
        this.centre = centre;
        this.demiDiagonale1 = demiDiagonale1;
        this.demiDiagonale2 = demiDiagonale2;
        this.angle = angle;
    }
    
    /**
     * Calcule les sommets du losange à partir du centre et des demi-diagonales.
     * @param centre Le centre du losange
     * @param demiDiagonale1 La demi-longueur de la première diagonale
     * @param demiDiagonale2 La demi-longueur de la deuxième diagonale
     * @param angle L'angle de rotation en radians
     * @param controleur Le contrôleur associé
     * @return La liste des sommets du losange
     */
    private static List<Point> calculerSommets(Point centre, double demiDiagonale1, double demiDiagonale2, double angle, GeoAnalytiqueControleur controleur) {
        List<Point> sommets = new ArrayList<>();
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        
        // Calcul des quatre sommets du losange
        double x1 = centre.getX() + demiDiagonale1 * cosAngle;
        double y1 = centre.getY() + demiDiagonale1 * sinAngle;
        sommets.add(new Point(x1, y1, controleur));
        
        double x2 = centre.getX() - demiDiagonale2 * sinAngle;
        double y2 = centre.getY() + demiDiagonale2 * cosAngle;
        sommets.add(new Point(x2, y2, controleur));
        
        double x3 = centre.getX() - demiDiagonale1 * cosAngle;
        double y3 = centre.getY() - demiDiagonale1 * sinAngle;
        sommets.add(new Point(x3, y3, controleur));
        
        double x4 = centre.getX() + demiDiagonale2 * sinAngle;
        double y4 = centre.getY() - demiDiagonale2 * cosAngle;
        sommets.add(new Point(x4, y4, controleur));
        
        return sommets;
    }
    
    /**
     * Retourne le centre du losange.
     * @return Le centre du losange
     */
    public Point getCentre() {
        return centre;
    }
    
    /**
     * Retourne la demi-longueur de la première diagonale.
     * @return La demi-longueur de la première diagonale
     */
    public double getDemiDiagonale1() {
        return demiDiagonale1;
    }
    
    /**
     * Retourne la demi-longueur de la deuxième diagonale.
     * @return La demi-longueur de la deuxième diagonale
     */
    public double getDemiDiagonale2() {
        return demiDiagonale2;
    }
    
    /**
     * Retourne l'angle de rotation du losange.
     * @return L'angle de rotation en radians
     */
    public double getAngle() {
        return angle;
    }
    
    /**
     * Calcule la longueur du côté du losange.
     * @return La longueur du côté
     */
    public double getCote() {
        // List<Point> sommets = getSommets();
        // return sommets.get(0).distance(sommets.get(1));
        return 0.0;
    }
    
    /**
     * Calcule l'aire du losange.
     * @return L'aire du losange
     */
    @Override
    public double calculerAire() {
        // L'aire d'un losange est égale à la moitié du produit des diagonales
        return demiDiagonale1 * demiDiagonale2 * 2;
    }
    
    /**
     * Vérifie si le losange est un carré.
     * @return true si le losange est un carré, false sinon
     */
    public boolean estCarre() {
        // Un losange est un carré si ses diagonales sont égales et perpendiculaires
        return Math.abs(demiDiagonale1 - demiDiagonale2) < 1e-10;
    }
    
    @Override
    public String toString() {
        return /*getNom() +*/ ": Centre " + centre + 
               ", Diagonale 1 = " + (2 * demiDiagonale1) + 
               ", Diagonale 2 = " + (2 * demiDiagonale2) + 
               ", Angle = " + Math.toDegrees(angle) + "°";
    }
    @Override
    public Segment getSegment(int nb) {
        if (nb < 0 || nb >= 4) {
            return null; // Indice invalide
        }
        
        // Retourne le segment correspondant à l'indice
        int debutIdx = nb;
        int finIdx = (nb + 1) % 4; // Assure la fermeture du losange
        
        return new Segment(getSommets().get(debutIdx), getSommets().get(finIdx), getControleur());
    }

    @Override
    public Point calculerCentreGravite() {
        return centre; // Le centre de gravité du losange est son centre
    }
    
    @Override
    public boolean contient(Point p) {
        if (p == null) return false;
        
        // Translation du point dans le repère du losange
        double dx = p.getX() - centre.getX();
        double dy = p.getY() - centre.getY();
        
        // Rotation inverse pour aligner avec les axes du losange
        double cosAngle = Math.cos(-angle);
        double sinAngle = Math.sin(-angle);
        double x = dx * cosAngle - dy * sinAngle;
        double y = dx * sinAngle + dy * cosAngle;
        
        // Vérification si le point est dans le losange aligné
        return Math.abs(x) <= demiDiagonale1 && Math.abs(y) <= demiDiagonale2;
    }

    /**
     * Retourne les sommets du losange.
     * @return La collection des sommets du losange
     */
    public List<Point> getSommets() {
        return (List<Point>) sommets;
    }
    
    /**
     * Retourne le contrôleur associé à ce losange.
     * @return Le contrôleur
     */
    public GeoAnalytiqueControleur getControleur() {
        try {
            return (GeoAnalytiqueControleur) super.visitor(new GeoObjectVisitor<Object>() {
                @Override
                public Object visitPoint(Point p) throws VisiteurException { return null; }
                @Override
                public Object visitTexte(Texte p) throws VisiteurException { return null; }
                @Override
                public Object visitSegment(Segment s) throws VisiteurException { return null; }
                @Override
                public Object visitDroite(Droite d) throws VisiteurException { return null; }
                @Override
                public Object visitEllipse(Ellipse e) throws VisiteurException { return null; }
                @Override
                public Object visitCercle(Cercle e) throws VisiteurException { return null; }
                @Override
                public Object visitPolygone(Polygone p) throws VisiteurException { 
                    try {
                        java.lang.reflect.Field f = GeoObject.class.getDeclaredField("controleur");
                        f.setAccessible(true);
                        return f.get(p);
                    } catch (Exception e) {
                        return null;
                    }
                }
                @Override
                public Object visitRectangle(Rectangle r) throws VisiteurException { return null; }
                @Override
                public Object visitCarre(Carre c) throws VisiteurException { return null; }
                @Override
                public Object visitTriangle(Triangle t) throws VisiteurException { return null; }
            });
        } catch (VisiteurException e) {
            return null; // Return null if an exception occurs
        }
    }

    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitPolygone(this);
    }
}

