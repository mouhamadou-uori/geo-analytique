package geoanalytique.model.geoobject.operation;
import geoanalytique.util.Operation;
import geoanalytique.model.Point;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;

/**
 * Représente une opération de déplacement d'un point dans le plan.
 * Prend un point à déplacer et deux valeurs (dx, dy) de translation.
 */
public class DeplacerPointOperation implements Operation {

    private Point point;
    private double dx;
    private double dy;

    /**
     * @return Le titre de l'opération
     */
    public String getTitle() {
        return "Déplacement du point";
    }

    /**
     * @return Le nombre d'arguments nécessaires (3)
     */
    public int getArite() {
        return 3;
    }

    /**
     * Définit les arguments : un point et deux valeurs de déplacement.
     * @param num Position de l'argument (0 à 2)
     * @param o Valeur à affecter
     * @throws ArgumentOperationException Si l'index est invalide
     * @throws IncorrectTypeOperationException Si le type est incorrect
     */
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        switch (num) {
            case 0 -> {
                if (!(o instanceof Point)) throw new IncorrectTypeOperationException();
                point = (Point) o;
            }
            case 1 -> {
                if (!(o instanceof Double)) throw new IncorrectTypeOperationException();
                dx = (Double) o;
            }
            case 2 -> {
                if (!(o instanceof Double)) throw new IncorrectTypeOperationException();
                dy = (Double) o;
            }
            default -> throw new ArgumentOperationException();
        }
    }

    /**
     * @param num Position de l'argument
     * @return La classe attendue pour cet argument
     */
    public Class getClassArgument(int num) {
        return switch (num) {
            case 0 -> Point.class;
            case 1, 2 -> Double.class;
            default -> null;
        };
    }

    /**
     * Réalise le déplacement du point.
     * @return Le point modifié
     */
    public Object calculer() {
        point.setX(point.getX() + dx);
        point.setY(point.getY() + dy);
        return point;
    }

    /**
     * @param num Position de l'argument
     * @return Une description pour l'utilisateur
     */
    public String getDescriptionArgument(int num) {
        return switch (num) {
            case 0 -> "Point à déplacer";
            case 1 -> "Déplacement en X";
            case 2 -> "Déplacement en Y";
            default -> "Inconnu";
        };
    }
}
