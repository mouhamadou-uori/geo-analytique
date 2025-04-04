package geoanalytique.model;

/**
 * Classe représentant un carré dans un repère orthonormé.
 * Un carré est un rectangle avec des côtés de même longueur.
 */
public class Carre extends Rectangle {
    
    /**
     * Constructeur par défaut.
     * Crée un carré centré à l'origine avec un côté de 2.
     */
    public Carre() {
        this(new Point(-1, -1), 2);
    }
    
    /**
     * Constructeur avec coin inférieur gauche et côté.
     * @param coinInfGauche Le coin inférieur gauche du carré
     * @param cote La longueur du côté du carré
     */
    public Carre(Point coinInfGauche, double cote) {
        super(coinInfGauche, cote, cote, "Carré");
    }
    
    /**
     * Constructeur avec coin inférieur gauche, côté et nom.
     * @param coinInfGauche Le coin inférieur gauche du carré
     * @param cote La longueur du côté du carré
     * @param nom Le nom du carré
     */
    public Carre(Point coinInfGauche, double cote, String nom) {
        super(coinInfGauche, cote, cote, nom);
    }
    
    /**
     * Retourne la longueur du côté du carré.
     * @return La longueur du côté
     */
    public double getCote() {
        return getLargeur();
    }
}