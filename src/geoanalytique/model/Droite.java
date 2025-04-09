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
    
    // Ce constructeur EST INTERDIT d'utilisation
    // PAR CONSEQUENT IL NE FAUT PAS LE MODIFIER
    // OU MIEUX IL FAUT LE SUPPRIMER.
    // On laisse ce constructeur au debut, pour pouvoir compiler le programme
    // simplement
    public Droite() {
       throw new RuntimeException("INTERDICTION D'UTILISER CE CONSTRUCTEUR!!!!") ;
    }
    
    public Droite(Point p, double pente, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.point = p;
        this.pente = pente;
    }

    public Droite(GeoAnalytiqueControleur controleur) {
        super(controleur);
    }
    
    /**
     * Retourne un point appartenant à la droite
     * @return Le point de référence de la droite
     */
    public Point getPoint() {
        return point;
    }
    
    /**
     * Retourne la pente de la droite
     * @return Le coefficient directeur de la droite
     */
    public double getPente() {
        return pente;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Droite)) {
            return false;
        }
        Droite other = (Droite) o;
        return point.equals(other.point) && pente == other.pente;
    }
    
   	@Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitDroite(this);
	}

	@Override
	public boolean contient(Point p) {
        // Une droite contient un point si y = mx + b
        // où b = y1 - mx1 (y1,x1 sont les coordonnées du point de référence)
        double b = point.getY() - pente * point.getX();
        // On compare y avec mx + b
        return Math.abs(p.getY() - (pente * p.getX() + b)) < 0.0001; // Tolérance pour les erreurs de calcul
	}
    
}