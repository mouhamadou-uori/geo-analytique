package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.exception.VisiteurException;
import geoanalytique.controleur.GeoAnalytiqueControleur;

/**
 * Classe représentant un carré dans un repère orthonormé.
 * Un carré est un rectangle avec des côtés de même longueur.
 */
public class Carre extends Rectangle {
    
    /**
     * Constructeur par défaut.
     * Crée un carré centré à l'origine avec un côté de 2.
     */
    public Carre(GeoAnalytiqueControleur controleur) {
        this(new Point(-1, -1, controleur), 2, controleur);
    }
    
    /**
     * Constructeur avec coin inférieur gauche et côté.
     * @param coinInfGauche Le coin inférieur gauche du carré
     * @param cote La longueur du côté du carré
     */
    public Carre(Point coinInfGauche, double cote, GeoAnalytiqueControleur controleur) {
        super(coinInfGauche, cote, cote, "Carré", controleur);
    }
    
    /**
     * Constructeur avec coin inférieur gauche, côté et nom.
     * @param coinInfGauche Le coin inférieur gauche du carré
     * @param cote La longueur du côté du carré
     * @param nom Le nom du carré
     */
    public Carre(Point coinInfGauche, double cote, String nom, GeoAnalytiqueControleur controleur) {
        super(coinInfGauche, cote, cote, nom, controleur);
    }
    
    /**
     * Retourne la longueur du côté du carré.
     * @return La longueur du côté
     */
    public double getCote() {
        return getLargeur();
    }
    
    /**
     * Implémentation du patron visiteur
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitCarre(this);
    }
}