package geoanalytique.model.geoobject.operation;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.util.Operation;
import geoanalytique.model.Droite;
import geoanalytique.model.Segment;


/**
 * Opération qui calcule la médiatrice d'un segment
 */
public class CalculMediatriceDroiteOperation implements Operation {
    private Point p1;
    private Point p2;
    
    public String getTitle() {
        return "Calcul de la médiatrice d'un segment";
    }
    
    public int getArite() {
        return 2;
    }
    
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        if (num < 0 || num > 1) {
            throw new ArgumentOperationException("L'argument doit être 0 ou 1");
        }
        if (!(o instanceof Point)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Point");
        }
        if (num == 0) {
            this.p1 = (Point) o;
        } else {
            this.p2 = (Point) o;
        }
    }
    
    public Class getClassArgument(int num) {
        if (num == 0 || num == 1) {
            return Point.class;
        }
        return null;
    }
    
    public Object calculer() {
        if (p1 == null || p2 == null) {
            return null;
        }
        
        // Calculer le milieu du segment
        double xMilieu = (p1.getX() + p2.getX()) / 2;
        double yMilieu = (p1.getY() + p2.getY()) / 2;
        Point milieu = new Point(xMilieu, yMilieu, null);
        
        // Calculer la pente du segment
        double penteSegment = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        
        // Calculer la pente perpendiculaire
        double pentePerpendiculaire = -1 / penteSegment;
        
        // Calculer un deuxième point sur la médiatrice
        double x2 = xMilieu + 1;
        double y2 = yMilieu + pentePerpendiculaire;
        Point p2Mediatrice = new Point(x2, y2, null);
        
        return new Segment(milieu, p2Mediatrice, null);
    }
    
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Premier point du segment";
        } else if (num == 1) {
            return "Deuxième point du segment";
        }
        throw new ArgumentOperationException("L'argument demandé n'existe pas");
    }
}