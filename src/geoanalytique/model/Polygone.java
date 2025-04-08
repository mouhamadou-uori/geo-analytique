package geoanalytique.model;
 
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

import java.util.Collection;

/**
 * Classe de base pour les polygones.
 * 
 */
public abstract class Polygone extends Surface {
    /** Sommets du polygone  */
    protected Collection<Point> sommets;

   /**
     * Constructeur sans nom du polygone.
     *
     * @param controles Collection de points représentant les sommets
     * @param controleur Contrôleur de l'application
     */
    public Polygone(Collection<Point> controles, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.sommets = controles;
    }

    /**
     * Constructeur avec nom du polygone.
     *
     * @param name Nom du polygone
     * @param controles Collection de points représentant les sommets
     * @param controleur Contrôleur de l'application
     */
    public Polygone(String name, Collection<Point> controles, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.sommets = controles;
    }

    /**
     * Retourne le segment reliant deux sommets consécutifs du polygone.
     * Cette méthode est abstraite et doit être implémentée par les sous-classes.
     *
     * @param nb Numéro du segment
     * @return Le segment correspondant
     */
    public abstract Segment getSegment(int nb);

    /**
     * Vérifie si un point est à l'intérieur du polygone.
     * Cette version simple vérifie si le point est contenu dans l’un des segments.
     *
     * @param p Le point à tester
     * @return true si le point appartient à un des côtés (segments)
     */
    @Override
    public boolean contient(Point p) {
        for (int i = 0; i < sommets.size(); i++) {
            Segment s = getSegment(i);
            if (s.contient(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applique un visiteur à ce polygone (pattern Visiteur).
     *
     * @param obj Le visiteur
     * @param <T> Type de retour
     * @return Résultat du visiteur
     * @throws VisiteurException Si une erreur survient
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitPolygone(this);
    }

    /**
     * Retourne la collection des sommets du polygone.
     * @return Collection des points
     */
    public Collection<Point> getSommets() {
        return sommets;
    }
}