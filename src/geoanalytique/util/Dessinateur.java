package geoanalytique.util;
 
import geoanalytique.exception.VisiteurException;
import geoanalytique.graphique.GCoordonnee;
import geoanalytique.graphique.GLigne;
import geoanalytique.graphique.GOvale;
import geoanalytique.graphique.GTexte;
import geoanalytique.graphique.Graphique;
import geoanalytique.model.Cercle;
import geoanalytique.model.Droite;
import geoanalytique.model.Ellipse;
import geoanalytique.model.Point;
import geoanalytique.model.Polygone;
import geoanalytique.model.Segment;
import geoanalytique.model.Texte;
import geoanalytique.model.ViewPort;
import geoanalytique.model.Rectangle;
import geoanalytique.model.Carre;
import geoanalytique.model.Triangle;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * Cette objet est utilise par le presenteur, pour 'convertir' les modeles
 * en objet graphique utilisable par la vue. Le dessinateur repose sur le design pattern
 * Visitor. 
 * 
 * 
 *
 */
public class Dessinateur implements GeoObjectVisitor<Graphique> {

    private ViewPort viewport;
    
	/**
	 * 
	 */
	public Dessinateur(ViewPort viewport) {
            this.viewport = viewport;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitDroite(geoanalytique.model.Droite)
	 */
	public Graphique visitDroite(Droite d) throws VisiteurException {
            // TODO: a completer
            return null;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitEllipse(geoanalytique.model.Ellipse)
	 */
	public Graphique visitEllipse(Ellipse e) throws VisiteurException {
    	// Conversion du centre de l'ellipse depuis le modèle vers les coordonnées écran
    	GCoordonnee centreConverti = viewport.convert(e.getCentre().getX(), e.getCentre().getY());

    	// Rayons de l'ellipse
    	double rx = e.getRx();
    	double ry = e.getRy();

    	// Calcul du coin supérieur gauche de l'ovale à dessiner
    	int x = (int) (centreConverti.getX() - 40 * rx);
    	int y = (int) (centreConverti.getY() - 40 * ry);
    	int width = (int) (2 * 40 * rx);
    	int height = (int) (2 * 40 * ry);

    	// Création de l'objet graphique GOvale
    	return new GOvale(x, y, width, height);
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitCercle(geoanalytique.model.Cercle)
	 */
	public Graphique visitCercle(Cercle c) throws VisiteurException {
    	// Conversion du centre du cercle en coordonnées écran
    	GCoordonnee centreConverti = viewport.convert(((Cercle)c).getCentre().getX(), ((Cercle)c).getCentre().getY());

    	// Rayon du cercle
    	double r = c.getRayon();

    	// Calcul du coin supérieur gauche de l'ovale à dessiner
    	int x = (int) (centreConverti.getX() - r);
    	int y = (int) (centreConverti.getY() - r);
    	int size = (int) (40 * r);

    	// Création du GOvale avec largeur = hauteur = diamètre
    	return new GOvale(x, y, size, size);
	}


	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitPoint(geoanalytique.model.Point)
	 */
	public Graphique visitPoint(Point o) throws VisiteurException {
            GCoordonnee c = viewport.convert(((Point)o).getX(),((Point)o).getY());
            return c;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitTexte(geoanalytique.model.Texte)
	 */
	public Graphique visitTexte(Texte t) throws VisiteurException {
	    GCoordonnee coord = viewport.convert(t.getX(), t.getY());
	    return new GTexte(coord.getX(), coord.getY(), t.getContenu());
	}


	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitPolygone(geoanalytique.model.Polygone)
	 */
	public Graphique visitPolygone(Polygone p) throws VisiteurException {
            // TODO: a completer
            return null;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitSegment(geoanalytique.model.Segment)
	 */
	public Graphique visitSegment(Segment s) throws VisiteurException {
            GLigne l = viewport.convert(((Segment)s).getDebut().getX(), ((Segment)s).getDebut().getY(), ((Segment)s).getFin().getX(), ((Segment)s).getFin().getY());
            return l;
	}
	
	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitRectangle(geoanalytique.model.Rectangle)
	 */
	public Graphique visitRectangle(Rectangle r) throws VisiteurException {
        // Pour le rectangle, on dessine 4 segments (les côtés du rectangle)
        Segment s1 = r.getSegment(0);
        Segment s2 = r.getSegment(1);
        Segment s3 = r.getSegment(2);
        Segment s4 = r.getSegment(3);
        
        // On retourne le premier segment et on ajoute les autres segments individuellement dans le contrôleur
        // La méthode recalculPoints du contrôleur va ajouter chaque segment à la vue
        return visitSegment(s1);
	}
	
	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitCarre(geoanalytique.model.Carre)
	 */
	public Graphique visitCarre(Carre c) throws VisiteurException {
        // Comme un carré est un rectangle, on utilise la méthode de dessin du rectangle
        return visitRectangle(c);
	}
	
	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitTriangle(geoanalytique.model.Triangle)
	 */
	public Graphique visitTriangle(Triangle t) throws VisiteurException {
        // Pour le triangle, on dessine 3 segments (les côtés du triangle)
        Segment s1 = t.getSegment(0);
        Segment s2 = t.getSegment(1);
        Segment s3 = t.getSegment(2);
        
        // On retourne le premier segment et on ajoute les autres segments individuellement dans le contrôleur
        // La méthode recalculPoints du contrôleur va ajouter chaque segment à la vue
        return visitSegment(s1);
	}
}
