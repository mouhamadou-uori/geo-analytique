package geoanalytique.model.geoobject.operation;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.modele.Point;
import geoanalytique.modele.Triangle;

/**
 * Opération qui calcule les coordonnées du centre de gravité d'un triangle
 */
public class CalculCentreGraviteTriangleOperation implements Operation {
    private Triangle triangle;
    
    @Override
    public String getTitle() {
        return "Calcul du centre de gravité d'un triangle";
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
        
        // Calcul du centre de gravité (moyenne des coordonnées)
        double x = (a.getX() + b.getX() + c.getX()) / 3;
        double y = (a.getY() + b.getY() + c.getY()) / 3;
        
        return new Point(x, y);
    }
    
    @Override
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Le triangle dont on veut calculer le centre de gravité";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
}