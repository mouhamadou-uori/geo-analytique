package geoanalytique.model.geoobject.operation;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.model.Cercle;
import geoanalytique.util.Operation;
import geoanalytique.model.Triangle;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.model.GeoObject;

/**
 * Opération qui calcule le cercle inscrit dans un triangle
 */
public class CalculCercleInscritOperation implements Operation {
    private Triangle triangle;
    
    public String getTitle() {
        return "Calcul du cercle inscrit dans un triangle";
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
        
        // Calcul des longueurs des côtés
        double ab = Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
        double bc = Math.sqrt(Math.pow(c.getX() - b.getX(), 2) + Math.pow(c.getY() - b.getY(), 2));
        double ca = Math.sqrt(Math.pow(a.getX() - c.getX(), 2) + Math.pow(a.getY() - c.getY(), 2));
        
        // Calcul du centre du cercle inscrit (formule barycentrique)
        double perimetre = ab + bc + ca;
        double x = (ab * c.getX() + bc * a.getX() + ca * b.getX()) / perimetre;
        double y = (ab * c.getY() + bc * a.getY() + ca * b.getY()) / perimetre;
        
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
        
        // Calcul du rayon (aire / demi-périmètre)
        double s = perimetre / 2;
        double aire = Math.sqrt(s * (s - ab) * (s - bc) * (s - ca)); // Formule de Héron
        double rayon = aire / s;
        
        return new Cercle(centre, rayon, controleur);
    }
    
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Le triangle dont on veut calculer le cercle inscrit";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
}