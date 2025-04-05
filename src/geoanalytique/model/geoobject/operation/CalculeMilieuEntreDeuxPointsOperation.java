package geoanalytique.model.geoobject.operation;
import geoanalytique.util.Operation;
import geoanalytique.model.Point;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;

/**
 * Opération qui calcule le milieu de deux points.
 */
public class CalculeMilieuEntreDeuxPointsOperation implements Operation {

    private Point p1;
    private Point p2;

    /**
     * @return Le titre de l'opération
     */
    @Override
    public String getTitle() {
        return "Milieu de deux points";
    }

    /**
     * @return Le nombre d'arguments nécessaires (2)
     */
    @Override
    public int getArite() {
        return 2;
    }

    /**
     * Définit les deux points pour l'opération.
     * @param num Numéro de l'argument (0 ou 1)
     * @param o L'objet (doit être un Point)
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
     * @return Classe attendue
     */
    @Override
    public Class getClassArgument(int num) {
        return Point.class;
    }

    /**
     * Calcule et renvoie le point milieu.
     * @return Le point milieu (Point)
     */
    @Override
    public Object calculer() {
        double mx = (p1.getX() + p2.getX()) / 2;
        double my = (p1.getY() + p2.getY()) / 2;
        return new Point(mx, my, null);
    }

    /**
     * @param num Position de l'argument
     * @return Description de l'argument
     */
    @Override
    public String getDescriptionArgument(int num) {
        return num == 0 ? "Premier point" : "Deuxième point";
    }
}
