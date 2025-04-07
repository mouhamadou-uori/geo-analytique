package geoanalytique.model;
 
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
import geoanalytique.util.GeoObjectVisitor;

/**
 * Modele mathematique pour les cercles.
 * 
 */
public class Cercle extends Ellipse {
    /**
     * Un cercle est une ellipse avec rx = ry = rayon.
     * Le centre et le rayon sont hérités de la classe Ellipse.
     */
    
    /**
     * Constructeur par défaut
     */
//     public Cercle() {
//         super();
//     }
    
    /**
     * Constructeur avec centre et rayon
     * @param centre Centre du cercle
     * @param rayon Rayon du cercle
     */
    public Cercle(Point centre, double rayon,GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.setCentre(centre);
        this.setRayon(rayon);
    }
    
    /**
     * Obtient le rayon du cercle
     * @return Le rayon du cercle
     */
    public double getRayon() {
        return super.getRx(); // rx = ry = rayon pour un cercle
    }
    
    /**
     * Définit le rayon du cercle
     * @param rayon Le nouveau rayon du cercle
     */
    public void setRayon(double rayon) {
        super.setRx(rayon);
        super.setRy(rayon); // Pour un cercle, rx = ry
    }
    
    /**
     * Obtient le centre du cercle
     * @return Le centre du cercle
     */
    public Point getCentre() {
        return super.getCentre();
    }
    
    /**
     * Définit le centre du cercle
     * @param centre Le nouveau centre du cercle
     */
    public void setCentre(Point centre) {
        super.setCentre(centre);
    }
    
    /**
     * Redéfinition de setRx pour maintenir rx = ry
     * @param rx Le nouveau rayon horizontal (qui sera aussi vertical)
     */
    @Override
    public void setRx(double rx) {
        super.setRx(rx);
        super.setRy(rx); // Pour un cercle, rx = ry
    }
    
    /**
     * Redéfinition de setRy pour maintenir rx = ry
     * @param ry Le nouveau rayon vertical (qui sera aussi horizontal)
     */
    @Override
    public void setRy(double ry) {
        super.setRy(ry);
        super.setRx(ry); // Pour un cercle, rx = ry
    }
    
    /**
     * Calcule la circonférence du cercle
     * @return La circonférence du cercle
     */
    public double calculerCirconference() {
        return 2 * Math.PI * getRayon();
    }

    /**
     * Vérifie si un point est contenu dans le cercle
     * @param p Le point à vérifier
     * @return true si le point est dans le cercle, false sinon
     */
    @Override
    public boolean contient(Point p) {
        if (p == null || getCentre() == null) {
            return false;
        }
        // Calcul de la distance au carré entre le point et le centre
        double distanceCarree = Math.pow(p.getX() - getCentre().getX(), 2) + 
                              Math.pow(p.getY() - getCentre().getY(), 2);
        // Le point est dans le cercle si sa distance au centre est <= au rayon
        return distanceCarree <= Math.pow(getRayon(), 2);
    }

    /**
     * Applique le visitor pattern
     * @param obj Le visiteur à appliquer
     * @return Le résultat du visiteur
     * @throws VisiteurException Si une erreur survient lors de la visite
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitCercle(this);
    }
    
    /**
     * Redéfinition de la méthode equals
     * @param o L'objet à comparer
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cercle)) return false;
        
        Cercle cercle = (Cercle) o;
        
        if (Double.compare(cercle.getRayon(), getRayon()) != 0) return false;
        return getCentre() != null ? getCentre().equals(cercle.getCentre()) : cercle.getCentre() == null;
    }
    
    /**
     * Redéfinition de la méthode hashCode
     * @return Le code de hachage de l'objet
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getCentre() != null ? getCentre().hashCode() : 0;
        temp = Double.doubleToLongBits(getRayon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    /**
     * Redéfinition de la méthode toString
     * @return Une représentation textuelle du cercle
     */
    @Override
    public String toString() {
        return "Cercle{" +
                "centre=" + getCentre() +
                ", rayon=" + getRayon() +
                '}';
    }
}