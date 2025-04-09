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
        // Vérifier si le point est à l'intérieur du polygone en utilisant l'algorithme du ray casting
        if (p == null || sommets == null || sommets.isEmpty()) {
            return false;
        }
        
        // Convertir la collection en tableau pour un accès plus facile
        Point[] points = sommets.toArray(new Point[0]);
        if (points.length < 3) {
            return false; // Un polygone doit avoir au moins 3 sommets
        }
        
        boolean inside = false;
        int j = points.length - 1;
        
        for (int i = 0; i < points.length; i++) {
            if ((points[i].getY() > p.getY()) != (points[j].getY() > p.getY()) &&
                (p.getX() < (points[j].getX() - points[i].getX()) * (p.getY() - points[i].getY()) / 
                (points[j].getY() - points[i].getY()) + points[i].getX())) {
                inside = !inside;
            }
            j = i;
        }
        
        return inside;
	}
    
    @Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
            return obj.visitPolygone(this);
	}
}

