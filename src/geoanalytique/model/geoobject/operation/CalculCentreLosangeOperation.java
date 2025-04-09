package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.util.Operation;
import geoanalytique.model.Losange;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.model.GeoObject;

/**
 * Opération qui calcule le centre d'un losange
 */
public class CalculCentreLosangeOperation implements Operation {
    private Losange losange;
    
    public String getTitle() {
        return "Calcul du centre du losange";
    }
    
    public int getArite() {
        return 1;
    }
    
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        if (num != 0) {
            throw new ArgumentOperationException("L'argument doit être 0");
        }
        if (!(o instanceof Losange)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Losange");
        }
        this.losange = (Losange) o;
    }
    
    public Class getClassArgument(int num) {
        if (num == 0) {
            return Losange.class;
        }
        return null;
    }
    
    public Object calculer() {
        if (losange == null) {
            return null;
        }
        
        // Récupérer le centre du losange
        Point centre = losange.getCentre();
        
        // Récupérer le contrôleur du losange
        GeoAnalytiqueControleur controleur = null;
        try {
            java.lang.reflect.Field field = GeoObject.class.getDeclaredField("controleur");
            field.setAccessible(true);
            controleur = (GeoAnalytiqueControleur) field.get(losange);
        } catch (Exception e) {
            // Ignorer les erreurs, controleur restera null
        }
        
        // Créer un nouveau point avec les mêmes coordonnées mais avec le contrôleur
        return new Point(centre.getX(), centre.getY(), controleur);
    }
    
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Le losange dont on veut calculer le centre";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
} 