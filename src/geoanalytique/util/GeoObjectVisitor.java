/**
 * 
 */
package geoanalytique.util;
 
import geoanalytique.model.*;
import geoanalytique.exception.VisiteurException;

/**
 * Interface de base pour les visiteurs du modele des objets geometrique
 * (voir le cours sur les <i>design pattern</i>)
 *
 *
 */
public interface GeoObjectVisitor<T> {
	public T visitPoint(Point p) throws VisiteurException;
	public T visitTexte(Texte p) throws VisiteurException;
	public T visitSegment(Segment s) throws VisiteurException;
	public T visitDroite(Droite d) throws VisiteurException;
	public T visitEllipse(Ellipse e) throws VisiteurException;
	public T visitCercle(Cercle e) throws VisiteurException;
	public T visitPolygone(Polygone p) throws VisiteurException;
	public T visitRectangle(Rectangle r) throws VisiteurException;
	public T visitCarre(Carre c) throws VisiteurException;
	public T visitTriangle(Triangle t) throws VisiteurException;
}
