package geoanalytique.model.geoobject.operation;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.modele.Point;
import geoanalytique.modele.Cercle;

/**
 * Opération qui calcule le cercle inscrit dans un triangle
 */
public class CalculCercleInscritOperation implements Operation {
    private Triangle triangle;
    
    @Override
    public String getTitle() {
        return "Calcul du cercle inscrit dans un triangle";
    }
    
    @Override
    public int getArite() {
        return 1;
    }
    
    @Override
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        if (num != 0) {
            throw new ArgumentOperationException("L'argument doit être 0");
        }
        if (!(o instanceof Triangle)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Triangle");
        }
        this.triangle = (Triangle) o;
    }
    
    @Override
    public Class getClassArgument(int num) {
        if (num == 0) {
            return Triangle.class;
        }
        return null;
    }
    
    @Override
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
        
        Point centre = new Point(x, y);
        
        // Calcul du rayon (aire / demi-périmètre)
        double s = perimetre / 2;
        double aire = Math.sqrt(s * (s - ab) * (s - bc) * (s - ca)); // Formule de Héron
        double rayon = aire / s;
        
        return new Cercle(centre, rayon);
    }
    
    @Override
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Le triangle dont on veut calculer le cercle inscrit";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
}