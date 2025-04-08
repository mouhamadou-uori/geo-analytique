package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/** 
 * Modèle mathématique pour les ellipses.
 * Une ellipse est définie par un centre, un rayon horizontal et un rayon vertical.
 */
public class Ellipse extends Surface {

    /** Centre de l'ellipse */
    private Point centre;

    /** Rayon horizontal */
    private double rx;

    /** Rayon vertical */
    private double ry;

    /**
     * Constructeur principal de l'ellipse.
     *
     * @param centre Centre de l’ellipse
     * @param rx Rayon horizontal (axe X)
     * @param ry Rayon vertical (axe Y)
     * @param controleur Référence vers le contrôleur
     */
    public Ellipse(Point centre, double rx, double ry, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.centre = centre;
        this.rx = rx;
        this.ry = ry;
    }

    /**
     * Constructeur secondaire sans initialisation complète.
     *
     * @param controleur Référence vers le contrôleur
     */
    public Ellipse(GeoAnalytiqueControleur controleur) {
        super(controleur);
    }

    /**
     * Calcule l’aire de l’ellipse selon la formule π * rx * ry.
     * @return Aire de l’ellipse
     */
    @Override
    public double calculerAire() {
        return Math.PI * rx * ry;
    }

    /**
     * Retourne le centre de gravité de l’ellipse (même que son centre géométrique).
     * @return Le centre de gravité
     */
    @Override
    public Point calculerCentreGravite() {
        return centre;
    }

    /**
     * Vérifie si deux ellipses sont égales (même centre, même rayons).
     * @param o Objet à comparer
     * @return true si les ellipses sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ellipse)) return false;
        Ellipse e = (Ellipse) o;

        return centre.equals(e.centre)
                && Math.abs(this.rx - e.rx) < Point.DELTA_PRECISION
                && Math.abs(this.ry - e.ry) < Point.DELTA_PRECISION;
    }

    /**
     * Vérifie si un point est à l’intérieur de l’ellipse.
     * Utilise l'équation réduite de l’ellipse centrée en (cx, cy) :
     * (x - cx)² / rx² + (y - cy)² / ry² ≤ 1
     *
     * @param p Le point à tester
     * @return true si le point est dans l’ellipse
     */
    @Override
    public boolean contient(Point p) {
        double dx = p.getX() - centre.getX();
        double dy = p.getY() - centre.getY();
        double val = (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry);
        return val <= 1.0;
    }

    /**
     * Applique un visiteur à cette ellipse (pattern Visiteur).
     *
     * @param obj Le visiteur
     * @param <T> Type de retour
     * @return Résultat du visiteur
     * @throws VisiteurException En cas d’erreur de visite
     */
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitEllipse(this);
    }

    //  GETTERS & SETTERS 

    public Point getCentre() {
        return centre;
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }
}
