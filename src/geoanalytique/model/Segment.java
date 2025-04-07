package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/**
 * Représente un segment géométrique entre deux points.
 * Un segment hérite de la classe Droite mais possède deux extrémités bien définies (début et fin).
 */
public class Segment extends Droite {

    /** Point de début du segment */
    private Point debut;
    /** Point d’arrivée du segment */
    private Point fin;

    public Point getDebut() {
        return debut;
    }

    public Point getFin() {
        return fin;
    }

    public Segment(Point a, Point b, GeoAnalytiqueControleur controleur) {
        super(a, a.calculPente(b), controleur); // appel au constructeur de Droite avec point + pente
        this.debut = a;
        this.fin = b;
    }

    /**
     * Vérifie si deux segments sont égaux (mêmes extrémités, peu importe l’ordre).
     *
     * @param o Objet à comparer
     * @return true si les segments ont les mêmes extrémités 
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Segment)) return false;
        Segment s = (Segment) o;

        return (this.debut.equals(s.debut) && this.fin.equals(s.fin)) ||
               (this.debut.equals(s.fin) && this.fin.equals(s.debut));
    }

    /**
     * Vérifie si un point appartient au segment.
     * Un point appartient au segment s’il est aligné avec les extrémités et se situe entre elles.
     *
     * @param p Le point à tester
     * @return true si le point appartient au segment
     */
    @Override
    public boolean contient(Point p) {
        // Vérifie si p est aligné avec le segment (même pente avec les extrémités)
        try {
            double pente1 = debut.calculPente(p);
            double pente2 = p.calculPente(fin);

            boolean aligné = Math.abs(pente1 - pente2) < Point.DELTA_PRECISION;

            // Vérifie si p est entre debut et fin 
            boolean entreX = (p.getX() >= Math.min(debut.getX(), fin.getX()) &&
                              p.getX() <= Math.max(debut.getX(), fin.getX()));
            boolean entreY = (p.getY() >= Math.min(debut.getY(), fin.getY()) &&
                              p.getY() <= Math.max(debut.getY(), fin.getY()));

            return aligné && entreX && entreY;

        } catch (ArithmeticException e) {
            // Cas où le segment est vertical : on compare x et on vérifie que y est entre les deux
            boolean memeX = Math.abs(p.getX() - debut.getX()) < Point.DELTA_PRECISION;
            boolean entreY = (p.getY() >= Math.min(debut.getY(), fin.getY()) &&
                              p.getY() <= Math.max(debut.getY(), fin.getY()));
            return memeX && entreY;
        }
    }

    /**
     * Applique un visiteur à ce segment (pattern Visiteur).
     *
     * @param obj Le visiteur
     * @param <T> Type de retour
     * @return Résultat du visiteur
     * @throws VisiteurException En cas d’erreur lors du traitement
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitSegment(this);
    }
}
