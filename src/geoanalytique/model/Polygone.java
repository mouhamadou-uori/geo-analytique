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

    public Polygone (Collection<Point> controles, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.sommets = controles;
    }
    
    public Polygone (String name, Collection<Point> controles, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.sommets = controles;
    }

    public abstract Segment getSegment (int nb);
    
	@Override
	public boolean contient(Point p) {
            // TODO: a completer
            return false;
	}
    
    @Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
            return obj.visitPolygone(this);
	}
}

