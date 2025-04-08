package geoanalytique.model.geoobject.operation;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.util.Operation;
import geoanalytique.model.Triangle;

/**
 * Opération qui calcule l'aire d'un triangle
 */
public class CalculAireTriangleOperation implements Operation {
    private Triangle triangle;
    
    public String getTitle() {
        return "Calcul de l'aire d'un triangle";
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
        
        // Calcul de l'aire par la formule des déterminants
        double aire = 0.5 * Math.abs(
            (a.getX() * (b.getY() - c.getY()) + 
             b.getX() * (c.getY() - a.getY()) + 
             c.getX() * (a.getY() - b.getY()))
        );
        
        return Double.valueOf(aire);
    }
    
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Le triangle dont on veut calculer l'aire";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
}