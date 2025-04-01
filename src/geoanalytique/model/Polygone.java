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

    public Polygone (Collection<Point> controles,GeoAnalytiqueControleur controleur) {
    	// TODO: a completer
    }
    
    public Polygone (String name,Collection<Point> controles,GeoAnalytiqueControleur controleur) {
        // TODO: a completer
    }

    public abstract Segment getSegment (int nb);
    
	@Override
	public boolean contient(Point p) {
            // TODO: a completer
            return false;
	}
    
    @Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
            // TODO: a completer
        return null;
	}
}

