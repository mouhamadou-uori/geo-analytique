package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.exception.VisiteurException;

/** 
 * Modele mathematique pour les ellipses
 * 
 */
public class Ellipse extends Surface {
   

    @Override
    public double calculerAire() {
        // TODO: a completer
        throw new UnsupportedOperationException("Not supported yet.");
        
    }

    
    @Override
    public boolean equals(Object o) {
        // TODO: a completer
        return false;
    }
    
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

    @Override
    public Point calculerCentreGravite() {
        // TODO: a completer
        return null;
    }
}