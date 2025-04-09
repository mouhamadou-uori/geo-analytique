package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/** 
 * Modele mathematique pour les ellipses
 * 
 */
public class Ellipse extends Surface {
    /** Centre de l'ellipse */
    private Point centre;
    /** Rayon horizontal  */
    private double rx;
    /** Rayon vertical */
    private double ry;


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

    public Ellipse(GeoAnalytiqueControleur controleur){
        super(controleur);
    }

    @Override
    public double calculerAire() {
        // TODO: a completer
        throw new UnsupportedOperationException("Not supported yet.");
        
    }

    
    @Override
    public boolean equals(Object o) {
        // TODO: a completer
        return false;
    }
    
	@Override
	public boolean contient(Point p) {
        if (p == null || centre == null) {
            return false;
        }
        
        // Calculer les coordonnées relatives du point par rapport au centre
        double dx = p.getX() - centre.getX();
        double dy = p.getY() - centre.getY();
        
        // Équation de l'ellipse : (x/a)² + (y/b)² <= 1
        // où a = rx et b = ry sont les demi-axes
        double resultat = (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry);
        
        // Ajout d'une petite marge (0.1) pour faciliter la sélection
        return resultat <= 1.1;
	}
    
    @Override
	public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitEllipse(this);
    }

    @Override
    public Point calculerCentreGravite() {
        // TODO: a completer
        return null;
    }
}