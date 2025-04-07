package geoanalytique.model;
 
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/**
 * Modele mathematique pour les droites
 * 
 */

public class Droite extends GeoObject {
    /** Point appartenant à la droite */
    protected Point point;
    /** Pente de la droite (coefficient directeur) */
    protected double pente;
    
    /**
     * Ce constructeur est interdit d’utilisation.
     * Il est laissé temporairement pour permettre la compilation.
     */
    public Droite() {
        throw new RuntimeException("INTERDICTION D'UTILISER CE CONSTRUCTEUR!!!!");
    }

    /**
     * Constructeur principal d’une droite.
     *
     * @param p Un point appartenant à la droite
     * @param pente La pente de la droite
     * @param controleur Le contrôleur associé
     */
    public Droite(Point p, double pente, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.point = p;
        this.pente = pente;
    }

    /**
     * Constructeur secondaire utile dans les sous-classes.
     *
     * @param controleur Le contrôleur associé
     */
    public Droite(GeoAnalytiqueControleur controleur) {
        super(controleur);
    }

    /**
     * Vérifie si un point appartient à la droite.
     * Cela revient à vérifier si la pente entre ce point et le point de référence est égale à celle de la droite.
     *
     * @param p Le point à tester
     * @return true si le point est sur la droite
     */
    @Override
    public boolean contient(Point p) {
        try {
            double penteAvecP = point.calculPente(p);
            return Math.abs(penteAvecP - this.pente) < Point.DELTA_PRECISION;
        } catch (ArithmeticException e) {
            // Cas d'une droite verticale : on compare les abscisses
            return Math.abs(p.getX() - point.getX()) < Point.DELTA_PRECISION;
        }
    }

    /**
     * Vérifie si deux droites sont équivalentes (même pente et point sur la même droite).
     *
     * @param o Objet à comparer
     * @return true si les deux droites sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Droite)) return false;
        Droite d = (Droite) o;
        boolean memePente = Math.abs(this.pente - d.pente) < Point.DELTA_PRECISION;
        boolean memePosition = d.contient(this.point); // le point de cette droite est sur l'autre
        return memePente && memePosition;
    }

    /**
     * Applique un visiteur à cette droite (pattern Visiteur).
     *
     * @param obj Le visiteur
     * @param <T> Type de retour
     * @return Résultat du visiteur
     * @throws VisiteurException En cas de problème pendant la visite
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitDroite(this);
    }
}