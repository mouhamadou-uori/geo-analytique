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
    
    @Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitSegment(this);
	}
}

