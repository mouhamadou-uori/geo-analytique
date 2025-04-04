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
    
    public Droite(Point p, double pente,GeoAnalytiqueControleur controleur) {
        // TODO: a completer
    }

    public Droite(GeoAnalytiqueControleur controleur) {
        super(controleur);
    }
    
    @Override
    public boolean equals(Object o) {
        // TODO: a completer
        return false;
    }
    
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