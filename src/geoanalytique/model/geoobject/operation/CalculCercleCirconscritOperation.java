package geoanalytique.model.geoobject.operation;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.util.Operation;
import geoanalytique.model.Triangle;
import geoanalytique.model.Cercle;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.model.GeoObject;

/**
 * Opération qui calcule le cercle circonscrit à un triangle
 */
public class CalculCercleCirconscritOperation implements Operation {
    private Triangle triangle;
    
    public String getTitle() {
        return "Calcul du cercle circonscrit à un triangle";
    }
    
    public int getArite() {
        return 1;
    }
    
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        if (num != 0) {
            throw new ArgumentOperationException("L'argument doit être 0");
        }
        if (!(o instanceof Triangle)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Triangle");
        }
        this.triangle = (Triangle) o;
    }
    
    public Class getClassArgument(int num) {
        if (num == 0) {
            return Triangle.class;
        }
        return null;
    }
    
    public Object calculer() {
        if (triangle == null) {
            return null;
        }
        
        Point a = triangle.getPointA();
        Point b = triangle.getPointB();
        Point c = triangle.getPointC();
        
        // Calcul des médiatrices
        double xAB = (a.getX() + b.getX()) / 2;
        double yAB = (a.getY() + b.getY()) / 2;
        double pentePerpAB = -1 / ((b.getY() - a.getY()) / (b.getX() - a.getX()));
        
        double xBC = (b.getX() + c.getX()) / 2;
        double yBC = (b.getY() + c.getY()) / 2;
        double pentePerpBC = -1 / ((c.getY() - b.getY()) / (c.getX() - b.getX()));
        
        // Intersection des médiatrices = centre du cercle circonscrit
        double x = (yBC - yAB + pentePerpAB * xAB - pentePerpBC * xBC) / (pentePerpAB - pentePerpBC);
        double y = pentePerpAB * (x - xAB) + yAB;
        
        // Récupérer le contrôleur du triangle
        GeoAnalytiqueControleur controleur = null;
        try {
            java.lang.reflect.Field field = GeoObject.class.getDeclaredField("controleur");
            field.setAccessible(true);
            controleur = (GeoAnalytiqueControleur) field.get(triangle);
        } catch (Exception e) {
            // Ignorer les erreurs, controleur restera null
        }
        
        Point centre = new Point(x, y, controleur);
        // Rayon = distance du centre à n'importe quel sommet
        double rayon = Math.sqrt(Math.pow(centre.getX() - a.getX(), 2) + Math.pow(centre.getY() - a.getY(), 2));
        
        return new Cercle(centre, rayon, controleur);
    }
    
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Le triangle dont on veut calculer le cercle circonscrit";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
}