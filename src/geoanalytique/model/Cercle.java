package geoanalytique.model;
 
import geoanalytique.exception.VisiteurException;
import geoanalytique.util.GeoObjectVisitor;

/**
 * Modele mathematique pour les cercles.
 * 
 */
public class Cercle extends Ellipse {
        @Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
            // TODO: a completer
            return null;
	}

	@Override
	public boolean contient(Point p) {
            // TODO: a completer
            return false;
	}
}
