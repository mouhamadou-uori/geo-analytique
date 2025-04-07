package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/**
 * Classe représentant les points 
 */
public class Point extends GeoObject {

    /** Coordonnée horizontale du point (abscisse) */
    private double x;
    /** Coordonnée verticale du point (ordonnée) */
    private double y;

    /**
     * Marge de tolérance pour comparer deux valeurs flottantes.
     */
    public static final double DELTA_PRECISION = 0.3;

    public Point(double x, double y, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.x = x;
        this.y = y;
    }

    public Point(String name, double x, double y, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
        modifie();
    }

    public void setY(double y) {
        this.y = y;
        modifie();
    }

    /**
     * Calcule la pente (coefficient directeur) entre ce point et un autre.
     * La pente est définie par la formule : (y2 - y1) / (x2 - x1).
     * Si les deux points ont une abscisse très proche, une exception est levée pour éviter une division par zéro.
     *
     * @param a Le second point utilisé pour calculer la pente
     * @return La pente entre ce point et le point a
     * @throws ArithmeticException Si les deux points ont des abscisses trop proches
     */
    public double calculPente(Point a) {
        if (Math.abs(a.x - this.x) < DELTA_PRECISION) {
            throw new ArithmeticException("Division par zéro : pente verticale");
        }
        return (a.y - this.y) / (a.x - this.x);
    }

    /**
     * Vérifie si ce point est égal à un autre objet.
     * Deux points sont considérés égaux si leurs coordonnées sont très proches, en tenant compte d'une marge d'erreur DELTA_PRECISION.
     *
     * @param o L’objet à comparer avec ce point
     * @return true si les deux objets sont des points presque identiques
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return Math.abs(this.x - p.x) < DELTA_PRECISION &&
               Math.abs(this.y - p.y) < DELTA_PRECISION;
    }

    /**
     * Calcule la distance euclidienne entre ce point et un autre point.
     *
     * @param b Le point de destination
     * @return La distance entre les deux points
     */
    public double calculerDistance(Point b) {
        double dx = this.x - b.x;
        double dy = this.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Déplace le point selon les décalages donnés sur les axes X et Y.
     *
     * @param dx Décalage horizontal (abscisse)
     * @param dy Décalage vertical (ordonnée)
     */
    public void deplacer(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        modifie();
    }

    /**
     * Applique un visiteur à ce point (pattern Visiteur).
     * @param obj Le visiteur
     * @return Le résultat retourné par le visiteur
     * @param <T> Type de retour attendu
     * @throws VisiteurException si une erreur survient dans le traitement
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitPoint(this);
    }

    /**
     * Vérifie si un point est contenu dans ce point (égalité).
     * @param p Le point à vérifier
     * @return true si le point est égal à ce point
     */
    @Override
    public boolean contient(Point p) {
        return equals(p);
    }
}
