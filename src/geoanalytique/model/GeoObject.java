package geoanalytique.model;
 
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
import geoanalytique.model.geoobject.operation.ChangeNomOperation;
import geoanalytique.util.Operation;
import java.util.ArrayList;

/**
 * Classe de base a tous objets geometriques
 * 
 */
public abstract class GeoObject {
    private static int count = 0;
    private ArrayList<Operation> operations;
    
    private String name;
    private GeoAnalytiqueControleur controleur;

    // Ce constructeur EST INTERDIT d'utilisation
    // PAR CONSEQUENT IL NE FAUT PAS LE MODIFIER
    // OU MIEUX IL FAUT LE SUPPRIMER.
    // On laisse ce constructeur au debut, pour pouvoir compiler le programme
    // simplement
    public GeoObject() {
       throw new RuntimeException("INTERDICTION D'UTILISER CE CONSTRUCTEUR!!!!") ;
    }
    
    public GeoObject (String name,GeoAnalytiqueControleur controleur) {
        operations = new ArrayList<Operation>();
        operations.add(new ChangeNomOperation(this));
        // TODO: a completer
    }
    
    public GeoObject (GeoAnalytiqueControleur controleur) {
        this.name = this.getClass().getSimpleName()+(count++);
        // TODO: a completer
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void modifie() {
        // Notify the controller that this object has been modified
        if (controleur != null) {
            controleur.update(this);
        }
    }

    public abstract boolean contient(Point p);
    
    public abstract <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException;
}

