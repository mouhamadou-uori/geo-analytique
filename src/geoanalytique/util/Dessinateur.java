package geoanalytique.util;
 
import geoanalytique.exception.VisiteurException;
import geoanalytique.graphique.GCoordonnee;
import geoanalytique.graphique.GLigne;
import geoanalytique.graphique.GOvale;
import geoanalytique.graphique.Graphique;
import geoanalytique.model.Cercle;
import geoanalytique.model.Droite;
import geoanalytique.model.Ellipse;
import geoanalytique.model.Point;
import geoanalytique.model.Polygone;
import geoanalytique.model.Segment;
import geoanalytique.model.ViewPort;

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

    	// Calcul de la position du coin supérieur gauche de l'ovale à dessiner
    	int x = (int) (centreConverti.getX() - e.getRx());
    	int y = (int) (centreConverti.getY() - e.getRy());
    	int width = (int) (2 * e.getRx());
    	int height = (int) (2 * e.getRy());

    	// Création de l'objet graphique GOvale avec une couleur (par exemple noire)
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
    	int size = (int) (2 * r);

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
}
