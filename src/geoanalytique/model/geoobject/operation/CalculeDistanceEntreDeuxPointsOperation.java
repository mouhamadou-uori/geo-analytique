package geoanalytique.model.geoobject.operation;
import geoanalytique.util.Operation;
import geoanalytique.model.Point;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;

/**
 * Opération de calcul de la distance entre deux points.
 */
public class CalculeDistanceEntreDeuxPointsOperation implements Operation {

    private Point p1;
    private Point p2;

    /**
     * @return Le titre de l'opération
     */
    @Override
    public String getTitle() {
        return "Distance entre deux points";
    }

    /**
     * @return Le nombre d'arguments nécessaires (2)
     */
    @Override
    public int getArite() {
        return 2;
    }

    /**
     * Définit les deux points nécessaires à l'opération.
     * @param num Numéro de l'argument (0 ou 1)
     * @param o L'objet à utiliser (doit être un Point)
     */
    @Override
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        if (!(o instanceof Point)) throw new IncorrectTypeOperationException();
        if (num == 0) p1 = (Point) o;
        else if (num == 1) p2 = (Point) o;
        else throw new ArgumentOperationException();
    }

    /**
     * @param num Position de l'argument
     * @return La classe attendue pour cet argument
     */
    @Override
    public Class getClassArgument(int num) {
        return Point.class;
    }

    /**
     * Calcule la distance euclidienne entre les deux points.
     * @return La distance (Double)
     */
    @Override
    public Object calculer() {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * @param num Position de l'argument
     * @return Description de l'argument pour l'utilisateur
     */
    @Override
    public String getDescriptionArgument(int num) {
        return num == 0 ? "Premier point" : "Deuxième point";
    }
}
