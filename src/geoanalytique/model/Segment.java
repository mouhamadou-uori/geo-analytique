package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
 

/**
 *        Un segment est considerer comme une droite car nous passons par au moins 
 *        1 point et que la pente est aussi definit dans le Segment. En revanche 
 *        on pourra lancer une exception si le traitement ne peut s'appliquer sur 
 *        le segment en cours.
 * 
 * 
 */
public class Segment extends Droite {
    /** Point de début du segment */
    private Point debut;
    /** Point d’arrivée du segment */
    private Point fin;

    public Point getDebut(){
        return debut;
    }
    public Point getFin(){
        return fin;
    }
    public Segment (Point a, Point b,GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.debut = a;
        this.fin = b;
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
        return obj.visitSegment(this);
	}
}

