package geoanalytique.model;

import java.util.List;

/**
 * Classe représentant un losange dans un repère orthonormé.
 * Un losange est un quadrilatère dont les quatre côtés sont de même longueur.
 * Il peut être défini par son centre et ses deux diagonales.
 */
public class Losange extends Polygone {
    
    private Point centre;
    private double demiDiagonale1;
    private double demiDiagonale2;
    private double angle; // Angle de rotation en radians par rapport à l'axe des abscisses
    
    /**
     * Constructeur par défaut.
     * Crée un losange centré à l'origine avec des diagonales de 2 et 1.
     */
    public Losange() {
        this(new Point(0, 0, null), 1, 0.5, 0);
    }
    
    /**
     * Constructeur avec centre et demi-diagonales.
     * @param centre Le centre du losange
     * @param demiDiagonale1 La demi-longueur de la première diagonale
     * @param demiDiagonale2 La demi-longueur de la deuxième diagonale
     * @param angle L'angle de rotation en radians
     */
    public Losange(Point centre, double demiDiagonale1, double demiDiagonale2, double angle) {
        super(calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle), null);
        this.centre = centre;
        this.demiDiagonale1 = demiDiagonale1;
        this.demiDiagonale2 = demiDiagonale2;
        this.angle = angle;
    }
    
    /**
     * Constructeur avec centre, demi-diagonales et nom.
     * @param centre Le centre du losange
     * @param demiDiagonale1 La demi-longueur de la première diagonale
     * @param demiDiagonale2 La demi-longueur de la deuxième diagonale
     * @param angle L'angle de rotation en radians
     * @param nom Le nom du losange
     */
    public Losange(Point centre, double demiDiagonale1, double demiDiagonale2, double angle, String nom) {
        super(calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle), null);
        this.centre = centre;
        this.demiDiagonale1 = demiDiagonale1;
        this.demiDiagonale2 = demiDiagonale2;
        this.angle = angle;
    }
    
    /**
     * Calcule les sommets du losange à partir du centre et des demi-diagonales.
     * @param centre Le centre du losange
     * @param demiDiagonale1 La demi-longueur de la première diagonale
     * @param demiDiagonale2 La demi-longueur de la deuxième diagonale
     * @param angle L'angle de rotation en radians
     * @return La liste des sommets du losange
     */
    private static List<Point> calculerSommets(Point centre, double demiDiagonale1, double demiDiagonale2, double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        
        // Calcul des sommets dans le repère tourné
        double x1 = centre.getX() + demiDiagonale1 * cosAngle;
        double y1 = centre.getY() + demiDiagonale1 * sinAngle;
        
        double x2 = centre.getX() + demiDiagonale2 * Math.cos(angle + Math.PI/2);
        double y2 = centre.getY() + demiDiagonale2 * Math.sin(angle + Math.PI/2);
        
        double x3 = centre.getX() - demiDiagonale1 * cosAngle;
        double y3 = centre.getY() - demiDiagonale1 * sinAngle;
        
        double x4 = centre.getX() - demiDiagonale2 * Math.cos(angle + Math.PI/2);
        double y4 = centre.getY() - demiDiagonale2 * Math.sin(angle + Math.PI/2);
        
        return List.of(
            new Point(x1, y1, null),
            new Point(x2, y2, null),
            new Point(x3, y3, null),
            new Point(x4, y4, null)
        );
    }
    
    /**
     * Retourne le centre du losange.
     * @return Le centre du losange
     */
    public Point getCentre() {
        return centre;
    }
    
    /**
     * Modifie le centre du losange.
     * @param centre Le nouveau centre du losange
     */
    // public void setCentre(Point centre) {
    //     this.centre = centre;
    //     setSommets(calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle));
    // }
    
    /**
     * Retourne la demi-longueur de la première diagonale.
     * @return La demi-longueur de la première diagonale
     */
    public double getDemiDiagonale1() {
        return demiDiagonale1;
    }
    
    /**
     * Modifie la demi-longueur de la première diagonale.
     * @param demiDiagonale1 La nouvelle demi-longueur de la première diagonale
     */
    // public void setDemiDiagonale1(double demiDiagonale1) {
    //     this.demiDiagonale1 = demiDiagonale1;
    //     setSommets(calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle));
    // }
    
    /**
     * Retourne la demi-longueur de la deuxième diagonale.
     * @return La demi-longueur de la deuxième diagonale
     */
    public double getDemiDiagonale2() {
        return demiDiagonale2;
    }
    
    /**
     * Modifie la demi-longueur de la deuxième diagonale.
     * @param demiDiagonale2 La nouvelle demi-longueur de la deuxième diagonale
     */
    // public void setDemiDiagonale2(double demiDiagonale2) {
    //     this.demiDiagonale2 = demiDiagonale2;
    //     setSommets(calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle));
    // }
    
    /**
     * Retourne l'angle de rotation du losange.
     * @return L'angle de rotation en radians
     */
    public double getAngle() {
        return angle;
    }
    
    /**
     * Modifie l'angle de rotation du losange.
     * @param angle Le nouvel angle de rotation en radians
     */
    // public void setAngle(double angle) {
    //     this.angle = angle;
    //     setSommets(calculerSommets(centre, demiDiagonale1, demiDiagonale2, angle));
    // }
    
    /**
     * Calcule la longueur du côté du losange.
     * @return La longueur du côté
     */
    public double getCote() {
        // List<Point> sommets = getSommets();
        // return sommets.get(0).distance(sommets.get(1));
        return 0.0;
    }
    
    /**
     * Calcule l'aire du losange.
     * @return L'aire du losange
     */
    public double aire() {
        // L'aire d'un losange est égale à la moitié du produit des diagonales
        return demiDiagonale1 * demiDiagonale2 * 2;
    }
    
    /**
     * Vérifie si le losange est un carré.
     * @return true si le losange est un carré, false sinon
     */
    public boolean estCarre() {
        // Un losange est un carré si ses diagonales sont égales et perpendiculaires
        return Math.abs(demiDiagonale1 - demiDiagonale2) < 1e-10;
    }
    
    @Override
    public String toString() {
        return /*getNom() +*/ ": Centre " + centre + 
               ", Diagonale 1 = " + (2 * demiDiagonale1) + 
               ", Diagonale 2 = " + (2 * demiDiagonale2) + 
               ", Angle = " + Math.toDegrees(angle) + "°";
    }
    @Override
    public Segment getSegment (int nb){
        // TODO : a completer
        return null;
    }

    @Override
    public Point calculerCentreGravite (){
        // TODO : a completer
        return null;
    }

    @Override
    public double calculerAire (){
        // TODO : a completer
        return 0.0;
    }
}

