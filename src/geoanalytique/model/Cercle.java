package geoanalytique.model;
 
import geoanalytique.exception.VisiteurException;
import geoanalytique.util.GeoObjectVisitor;

/**
 * Modele mathematique pour les cercles.
 * 
 */
public class Cercle extends Ellipse {
        /**
         * Un cercle est une ellipse avec rx = ry = rayon.
         * Le centre et le rayon sont hérités de la classe Ellipse.
         */

     /**
     * Constructeur d’un cercle.
     * @param centre Centre du cercle
     * @param rayon Rayon du cercle
     * @param controleur Contrôleur associé
     */
    public Cercle(Point centre, double rayon, GeoAnalytiqueControleur controleur) {
        super(centre, rayon, rayon, controleur); // rx = ry = rayon
    }

    /**
     * Applique un visiteur à ce cercle (pattern Visiteur).
     *
     * @param obj Le visiteur
     * @param <T> Type de retour
     * @return Résultat du visiteur
     * @throws VisiteurException En cas d’erreur
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitCercle(this);
    }

    /**
     * Vérifie si un point est à l’intérieur du cercle.
     * C’est équivalent au test de l’ellipse avec rx = ry = rayon.
     *
     * @param p Le point à tester
     * @return true si le point est dans le cercle
     */
    @Override
    public boolean contient(Point p) {
        double dx = p.getX() - getCentre().getX();
        double dy = p.getY() - getCentre().getY();
        double distanceCarree = dx * dx + dy * dy;
        double rayon = getRx(); // ou getRy(), c’est la même chose ici
        return distanceCarree <= rayon * rayon;
    }
}