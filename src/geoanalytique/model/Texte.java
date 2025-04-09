package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/**
 * Classe représentant un texte positionné dans un espace géographique
 */
public class Texte extends GeoObject {
    /** Coordonnée horizontale du texte (abscisse) */
    private double x;
    /** Coordonnée verticale du texte (ordonnée) */
    private double y;
    /** Contenu du texte */
    private String contenu;

    public static final double DELTA_PRECISION = 0.3;

    public Texte(double x, double y, String contenu, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.x = x;
        this.y = y;
        this.contenu = contenu;
    }

    public Texte(String name, double x, double y, String contenu, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.x = x;
        this.y = y;
        this.contenu = contenu;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getContenu() {
        return contenu;
    }

    public void setX(double x) {
        this.x = x;
        modifie();
    }

    public void setY(double y) {
        this.y = y;
        modifie();
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
        modifie();
    }

    /**
     * Déplace le texte de dx et dy
     */
    public void deplacer(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        modifie();
    }

    /**
     * Vérifie si deux objets Texte sont à la même position (x, y)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Texte)) return false;
        Texte t = (Texte) o;
        return Math.abs(this.x - t.x) < DELTA_PRECISION &&
               Math.abs(this.y - t.y) < DELTA_PRECISION &&
               this.contenu.equals(t.contenu);
    }

    /**
     * Méthode pour le visiteur
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitTexte(this);
    }

    /**
     * Un texte ne contient un point que s’il est à sa position exacte
     */
    @Override
    public boolean contient(Point p) {
        return Math.abs(p.getX() - x) < DELTA_PRECISION &&
               Math.abs(p.getY() - y) < DELTA_PRECISION;
    }
}
