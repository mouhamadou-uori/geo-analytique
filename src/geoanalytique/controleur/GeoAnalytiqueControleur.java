package geoanalytique.controleur;
 
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.exception.VisiteurException;
import geoanalytique.graphique.GTexte;
import geoanalytique.graphique.Graphique;
import geoanalytique.graphique.GLigne;
import geoanalytique.gui.GeoAnalytiqueGUI;
import geoanalytique.model.Cercle;
import geoanalytique.model.Ellipse;
import geoanalytique.model.GeoObject;
import geoanalytique.model.Point;
import geoanalytique.model.Texte;
import geoanalytique.model.Segment;
import geoanalytique.model.Rectangle;
import geoanalytique.model.Carre;
import geoanalytique.model.Triangle;
import geoanalytique.model.ViewPort;
import geoanalytique.model.Polygone;
import geoanalytique.util.Dessinateur;
import geoanalytique.util.Operation;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Cette classe est le controleur/presenteur principale. Tous les evenements importants
 * emanant de l'interface graphique doivent passer par cette classe.
 * 
 * 
 *
 */
public class GeoAnalytiqueControleur implements ActionListener, MouseListener, HierarchyBoundsListener, MouseMotionListener {

	private ArrayList<GeoObject> objs;
	private ViewPort viewport;
	private GeoAnalytiqueGUI view;
	
	private transient GeoObject select;

	private String currentTool = "POINT";
	private Point startPoint = null;
	private Point pointMaintenu = null;

	private Point centreEnCoursCreation = null;
	private Cercle cercleEnCoursCreation = null;
	private Ellipse ellipseEnCoursCreation = null;
	private Point pointDragInitial = null;
	private Triangle triangleEnCoursCreation = null;
	private Polygone polygoneEnCoursCreation = null;
	
        
		
	/**
	 * Constructeur de base afin de créer le lien entre la vue passee en
         * argument et le gestionnaire d'evenement.
         * @param view reference l'interface graphique dont le controleur sera le
         * gestionnaire d'evenement.
	 */
	public GeoAnalytiqueControleur(GeoAnalytiqueGUI view) {
		objs = new ArrayList<GeoObject>();
		this.view = view;
		viewport = new ViewPort(view.getCanvas().getWidth(),view.getCanvas().getHeight());
		// TODO: A completer avec vos modifications
	}

        /**
         * Cette fonction prend en charge l'ajout dans le système d'un nouvel
         * objet geometrique. On pourra en particulier, mettre a jour la vue
         * et la liste des operations realisable sur l'application. 
         * D'autre taches pourront etre realisees si besoin est.
         * @param obj objet geometrique a ajouter dans le systeme
         */
	public void addObjet(GeoObject obj) {
            objs.add(obj);
            recalculPoints();
            // TODO: a completer
	}
	
	
	/**
         * Cette fonction est appele par le modele pour prevenir le controleur
         * d'une mise a jour de l'objet geometrique passe en argument. Le 
         * controleur peut donc mettre a jour les informations de la vue, si 
         * c'est necessaire.
         * @param object objet ayant subit une modification
         */
	public void update(GeoObject object) {
		// TODO: a completer
	}

	public void actionPerformed(ActionEvent e) {
		// TODO: a completer
    	currentTool = ((JButton) e.getSource()).getText(); // contenu/texte du bouton
		System.out.println(currentTool);
	}

	public void mouseClicked(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}
	

	public void mouseReleased(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
			if (currentTool.equals("CIRCLE") && centreEnCoursCreation != null) {
				Point releasePoint = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), null);
				double rayonFinal = centreEnCoursCreation.calculerDistance(releasePoint);
				
				// Si le rayon est trop petit, supprimez le cercle temporaire
				if (rayonFinal < 0.1) {
					//this.removeObjet(cercleEnCoursCreation);
				} else {
					// Sinon, finalisez le cercle
					cercleEnCoursCreation.setRayon(rayonFinal);
					// Vous pouvez éventuellement ajouter un point visuel à la circonférence
					// this.addObjet(new Point("Rayon", releasePoint.getX(), releasePoint.getY(), null));
				}
				
				// Réinitialiser les variables temporaires
				centreEnCoursCreation = null;
				cercleEnCoursCreation = null;
				view.repaint();
			}
			else if (currentTool.equals("ELLIPSE") && centreEnCoursCreation != null) {
				// Réinitialisation des variables pour la prochaine ellipse
				centreEnCoursCreation = null;
				ellipseEnCoursCreation = null;
				pointDragInitial = null;
			}
			// reste du code...
			view.getCanvas().repaint();
	}

	public void mouseEntered(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void mouseExited(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void mousePressed(MouseEvent e) {
            // TODO: a completer pour un clique souris dans le canevas
			Point pointClique = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), this);
			// if (currentTool.equals("CIRCLE")) {
			// 	centreEnCoursCreation = pointClique;
        	// 	cercleEnCoursCreation = new Cercle(centreEnCoursCreation, 0, null);
        	// 	this.addObjet(cercleEnCoursCreation); // Ajouter un cercle temporaire avec rayon 0
			// }
			switch (currentTool) {
				case "POINT":
					this.addObjet(new Point("Ori", pointClique.getX(),pointClique.getY(), this));
					break;
				case "LINE":
					handleLineCreation(pointClique);
					break;
				case "CIRCLE":
					if (centreEnCoursCreation == null/*  && cercleEnCoursCreation == null */) {
						centreEnCoursCreation = pointClique;
						this.addObjet(new Point("Ori", pointClique.getX(),pointClique.getY(), this));
						cercleEnCoursCreation = new Cercle(centreEnCoursCreation, 0, this);
						this.addObjet(cercleEnCoursCreation); // Ajouter un cercle temporaire avec rayon 0
					}
					break;
                case "ELLIPSE":
                    if (centreEnCoursCreation == null) {
                        centreEnCoursCreation = pointClique;
                        this.addObjet(new Point("Centre", pointClique.getX(), pointClique.getY(), this));
                        ellipseEnCoursCreation = new Ellipse(this);
                        ellipseEnCoursCreation.setCentre(centreEnCoursCreation);
                        ellipseEnCoursCreation.setRx(0);
                        ellipseEnCoursCreation.setRy(0);
                        this.addObjet(ellipseEnCoursCreation); // Ajouter une ellipse temporaire
                        pointDragInitial = null; // Sera initialisé lors du premier drag
                    }
                    break;
				case "RECTANGLE":
					handleRectangleCreation(pointClique);
					break;
				case "SQUARE":
					handleSquareCreation(pointClique);
					break;
				case "TRIANGLE":
					handleTriangleCreation(pointClique);
					break;
                case "TRIANGLE_RECTANGLE":
                    handleTriangleRectangleCreation(pointClique);
                    break;
                case "TRIANGLE_ISOCELE":
                    handleTriangleIsoceleCreation(pointClique);
                    break;
                case "TRIANGLE_EQUILATERAL":
                    handleTriangleEquilateralCreation(pointClique);
                    break;
				case "TEXT":
					String texteUtilisateur = demanderTexte("Veuillez entrer un texte:", "Saisie de texte");
					if (texteUtilisateur != null) {
						// Utiliser le texte saisi
						System.out.println("Texte saisi: " + texteUtilisateur);
						Texte objGTexte = new Texte(pointClique.getX(), pointClique.getY(), texteUtilisateur, this);
						this.addObjet(objGTexte);
					} else {
						// L'utilisateur a annulé
						System.out.println("Saisie annulée");
					}
					break;
				case "LENGTH":
				case "SLOPE":
				case "MIDPOINT":
					break;
			}
			view.getCanvas().repaint();
	}

	public String demanderTexte(String message, String titre) {
		return JOptionPane.showInputDialog(null, message, titre, JOptionPane.QUESTION_MESSAGE);
	}

    public void mouseDragged(MouseEvent e) {
		if (currentTool.equals("CIRCLE") && centreEnCoursCreation != null && cercleEnCoursCreation != null) {
			// Calculer la position actuelle de la souris
			Point pointCourant = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), this);
			
			// Calculer le rayon basé sur la distance entre le centre et la position actuelle
			double rayon = centreEnCoursCreation.calculerDistance(pointCourant);
			
			// Supprimer l'ancien cercle de la liste d'objets
			objs.remove(cercleEnCoursCreation);
			
			// Créer un nouveau cercle avec le rayon mis à jour et l'ajouter
			cercleEnCoursCreation = new Cercle(centreEnCoursCreation, rayon, this);
			this.addObjet(cercleEnCoursCreation);
			
			// Mettre à jour l'affichage
			recalculPoints();
		}
		else if (currentTool.equals("ELLIPSE") && centreEnCoursCreation != null && ellipseEnCoursCreation != null) {
			// Calculer la position actuelle de la souris
			Point pointCourant = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), this);
			
			// Initialiser le point initial de drag si c'est le premier déplacement
			if (pointDragInitial == null) {
				pointDragInitial = pointCourant;
			}
			
			// Calcul des rayons horizontal et vertical
			double rx = Math.abs(pointCourant.getX() - centreEnCoursCreation.getX());
			double ry = Math.abs(pointCourant.getY() - centreEnCoursCreation.getY());
			
			// Mise à jour de l'ellipse
			objs.remove(ellipseEnCoursCreation);
			ellipseEnCoursCreation.setRx(rx);
			ellipseEnCoursCreation.setRy(ry);
			this.addObjet(ellipseEnCoursCreation);
			
			// Mettre à jour l'affichage
			recalculPoints();
		}
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

	private void handleLineCreation(Point clickPoint) {
		if (startPoint == null) {
			startPoint = clickPoint;
			this.addObjet(new Point("Ori", clickPoint.getX(),clickPoint.getY(), null));
		} else {
			this.addObjet(new Point("Ori", clickPoint.getX(),clickPoint.getY(), null));
			addObjet(new Segment(startPoint, new Point("B", clickPoint.getX(), clickPoint.getY(), null), null));
			startPoint = null;
		}
	}

	private void handleCircleCreation(Point clickPoint) {
		if (startPoint == null) {
			startPoint = clickPoint;
			this.addObjet(new Point("Ori", clickPoint.getX(),clickPoint.getY(), null));
			
		} else {
			double rayon = startPoint.calculerDistance(clickPoint);
			this.addObjet(new Point("Ori", clickPoint.getX(),clickPoint.getY(), null));
			Cercle cercle = new Cercle(startPoint, rayon, null);
			this.addObjet(cercle);
			cercle.setRayon(rayon);
			startPoint = null;
		}
	}

	private void handleCircleModification(Point clickPoint) {
		if (centreEnCoursCreation == null && cercleEnCoursCreation == null) {
			centreEnCoursCreation = clickPoint;
			this.addObjet(new Point("Ori", clickPoint.getX(),clickPoint.getY(), null));
			cercleEnCoursCreation = new Cercle(centreEnCoursCreation, 0, null);
        	this.addObjet(cercleEnCoursCreation); // Ajouter un cercle temporaire avec rayon 0
		} else {
			double rayon = centreEnCoursCreation.calculerDistance(clickPoint);
			cercleEnCoursCreation.setRayon(rayon);
			// startPoint = null;
		}
	}

	private void handleRectangleCreation(Point clickPoint) {
		if (startPoint == null) {
			startPoint = clickPoint;
			this.addObjet(new Point("Ori", clickPoint.getX(), clickPoint.getY(), this));
		} else {
			// Le premier point est le coin inférieur gauche, le second définit la largeur et hauteur
			double largeur = Math.abs(clickPoint.getX() - startPoint.getX());
			double hauteur = Math.abs(clickPoint.getY() - startPoint.getY());
			
			// Déterminer le vrai coin inférieur gauche (peut différer de startPoint selon la direction du drag)
			double coinX = Math.min(startPoint.getX(), clickPoint.getX());
			double coinY = Math.min(startPoint.getY(), clickPoint.getY());
			Point coinInfGauche = new Point(coinX, coinY, this);
			
			// Créer le rectangle
			Rectangle rectangle = new Rectangle(coinInfGauche, largeur, hauteur, this);
			this.addObjet(rectangle);
			
			// Réinitialiser le point de départ
			startPoint = null;
		}
	}
	
	private void handleSquareCreation(Point clickPoint) {
		if (startPoint == null) {
			startPoint = clickPoint;
			this.addObjet(new Point("Ori", clickPoint.getX(), clickPoint.getY(), this));
		} else {
			// Le premier point est le coin inférieur gauche, le second définit la taille du côté
			// On prend le max des deux dimensions pour avoir un carré
			double cote = Math.max(
				Math.abs(clickPoint.getX() - startPoint.getX()),
				Math.abs(clickPoint.getY() - startPoint.getY())
			);
			
			// Déterminer le coin inférieur gauche
			double coinX = Math.min(startPoint.getX(), startPoint.getX() + cote);
			double coinY = Math.min(startPoint.getY(), startPoint.getY() + cote);
			Point coinInfGauche = new Point(coinX, coinY, this);
			
			// Créer le carré
			Carre carre = new Carre(coinInfGauche, cote, this);
			this.addObjet(carre);
			
			// Réinitialiser le point de départ
			startPoint = null;
		}
	}
	
	private void handleTriangleCreation(Point clickPoint) {
		if (pointMaintenu == null) {
			// Premier sommet
			pointMaintenu = clickPoint;
			this.addObjet(new Point("A", clickPoint.getX(), clickPoint.getY(), this));
		} else if (startPoint == null) {
			// Deuxième sommet
			startPoint = clickPoint;
			this.addObjet(new Point("B", clickPoint.getX(), clickPoint.getY(), this));
		} else {
			// Troisième sommet, on crée le triangle
			this.addObjet(new Point("C", clickPoint.getX(), clickPoint.getY(), this));
			Triangle triangle = new Triangle(pointMaintenu, startPoint, clickPoint, this);
			this.addObjet(triangle);
			
			// Réinitialiser les points
			pointMaintenu = null;
			startPoint = null;
		}
	}

        /**
         * Cette fonction permet de realiser toutes les taches inherante a la
         * selection d'un objet geometrique dans la vue. Cette fonction est tres
         * utile pour marquer l'objet selectionne de maniere plus significative.
         * 
         * @param o objet a selectionne
         */
	private void selectionner(GeoObject o) {
		// TODO: a completer
	}
	
	/**
         * Operation permettant de deselectionner le dernier objet selectionne
         * (si il existe). On pourra enlever tous marqueurs present sur l'interface
         * graphique a ce moment ainsi que les operations anciennement realisable.
         */	
	private void deselectionner() {
		// TODO: a completer
	}
	


        /**
         * Cette fonction est appele uniquement lorsque la liaison controleur et
         * interface graphique a ete realisee. Elle permet de realiser certaines
         * taches necessaires a ce moment. Comme par exemple ajouter un listener
         * aux boutons etc.
         */
	public void prepareTout(GeoAnalytiqueControleur controleur) {
            // Preparation des evenements du canevas
            view.getCanvas().addMouseListener(this);
            view.getCanvas().addHierarchyBoundsListener(this);
			view.getCanvas().addMouseMotionListener(this);
			for (JButton button : view.getAllButtons()) {
    			button.addActionListener(this);
			}
			// view.getSideBar().addActionListner(this);
            // TODO: a completer si necessaire
            
            
	}

	public void ancestorMoved(HierarchyEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void ancestorResized(HierarchyEvent e) {
	    // TODO: a completer si le canevas est redimentionnable
		viewport.resize(view.getCanvas().getWidth(), view.getCanvas().getHeight());
		recalculPoints();
		view.repaint();
	}

        /**
         * Cette fonction est la fonction permettant de caracteriser le presenteur.
         * Elle realise la presentation des donnees en indiquant a la vue les
         * element graphique devant etre affiche en fonction de la zone d'affichage
         * (Viewport).
         */
	private void recalculPoints() {
		
            // on nettoie les anciennes images
            view.getCanvas().clear();
            // redessine toutes les figures
            Dessinateur d = new Dessinateur(viewport);
            for (GeoObject o : objs) {
            	Graphique c;
                try {
                    c = o.visitor(d);
                    view.getCanvas().addGraphique(c);
                    
                    // Traitement spécial pour les polygones (Rectangle, Carré, Triangle)
                    if (o instanceof Rectangle) {
                        // Ajouter les segments 1, 2 et 3 (le segment 0 est déjà ajouté)
                        Rectangle r = (Rectangle) o;
                        for (int i = 1; i < 4; i++) {
                            Segment s = r.getSegment(i);
                            GLigne ligne = viewport.convert(s.getDebut().getX(), s.getDebut().getY(), 
                                                           s.getFin().getX(), s.getFin().getY());
                            view.getCanvas().addGraphique(ligne);
                        }
                    } else if (o instanceof Triangle) {
                        // Ajouter les segments 1 et 2 (le segment 0 est déjà ajouté)
                        Triangle t = (Triangle) o;
                        for (int i = 1; i < 3; i++) {
                            Segment s = t.getSegment(i);
                            GLigne ligne = viewport.convert(s.getDebut().getX(), s.getDebut().getY(), 
                                                           s.getFin().getX(), s.getFin().getY());
                            view.getCanvas().addGraphique(ligne);
                        }
                    }
                } catch (VisiteurException e) {
                    e.printStackTrace();
                }
            }
            // TODO: a completer
	}

	
        /**
         * Cette fonction permet de lancer une operation sur un objet. A priori
         * Elle n'a pas a etre modifiee dans un premier temps. Sauf si vous voulez
         * modifier le comportement de celle-ci en donnant un aspect plus jolie.
         * @param object objet sur lequel realise l'operation
         * @param ope operation devant etre realise sur l'objet <i>object</i>
         */
	public void lanceOperation(GeoObject object, Operation ope) {
            // TODO: a modifier si vous avez compris comment la fonction
            // procedais. Sinon laissez telle quel
		for(int i=0; i < ope.getArite();i++) {
			try {
				String res = JOptionPane.showInputDialog(view, ope.getDescriptionArgument(i), ope.getTitle(),JOptionPane.QUESTION_MESSAGE);
				if(res == null)
					return;
				if(ope.getClassArgument(i) == Double.class) {
					ope.setArgument(i, Double.valueOf(res));
				}
				else if(ope.getClassArgument(i) == Integer.class) {
					ope.setArgument(i, Integer.valueOf(res));
				}
				else if(ope.getClassArgument(i) == Character.class) {
					ope.setArgument(i, Character.valueOf(res.charAt(0)));
				}
				else if(ope.getClassArgument(i) == String.class) {
					ope.setArgument(i, res);
				}
				else if(GeoObject.class.isAssignableFrom(ope.getClassArgument(i))) {
					ope.setArgument(i, searchObject(res));
				}
				else {
                                    JOptionPane.showMessageDialog(view, "Classe de l'argument non supporte", "Erreur dans le lancement de l'operation", JOptionPane.ERROR_MESSAGE);
       				    return;
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (ArgumentOperationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IncorrectTypeOperationException e) {
				e.printStackTrace();
			}
		}
                // Dans notre application on autorise un resultat, que nous devons
                // interprété. Pas de resultat correspond au pointeur null
		Object o = ope.calculer();
		if(o != null) {
                       // on a bien trouve un resultat. Mais on ne connait pas
                       // sa nature on va donc le tester
			if(GeoObject.class.isAssignableFrom(o.getClass())) {
                            // c'est un objet analytique on l'ajoute dans notre systeme
				addObjet((GeoObject) o);
			}
			else {
                            // on ne connait pas le type, donc on l'avise a l'utilisateur
				JOptionPane.showConfirmDialog(view, o, ope.getTitle(),JOptionPane.OK_OPTION);
			}
                        // TODO BONUS: proposer et modifier le traitement du resultat
                        // pour pouvoir renvoyer plusieurs chose en meme temps
		}
		recalculPoints();
	}
	
        /**
         * Cette fonction permet de retrouver un objet dans la liste des objets
         * geometrique a partir de son nom (que l'on supposera unique). Si le nom
         * de l'objet est un introuvable on leve l'exception ArgumentOperationException.
         * Cette fonction est utilisee dans le calcul d'une operation.
         * @param x nom de l'objet a rechercher
         * @return Renvoie l'objet ayant pour nom x, sinon leve une exception
         * @throws geoanalytique.exception.ArgumentOperationException
         */
	private Object searchObject(String x) throws ArgumentOperationException {
		for (GeoObject o : objs) {
			if(o.getName().equals(x))
				return o;
		}
		throw new ArgumentOperationException("Nom de l'objet introuvable");
	}

    // Méthode pour gérer la création d'une ellipse
    private void handleEllipseCreation(Point clickPoint) {
        if (startPoint == null) {
            // Premier clic: centre de l'ellipse
            startPoint = clickPoint;
            this.addObjet(new Point("Centre", clickPoint.getX(), clickPoint.getY(), this));
        } else if (pointMaintenu == null) {
            // Deuxième clic: point définissant le rayon horizontal (rx)
            pointMaintenu = clickPoint;
            double rx = startPoint.calculerDistance(clickPoint);
            
            // Crée une ellipse temporaire (rx = ry pour l'instant)
            Ellipse ellipseTemp = new Ellipse(this);
            ellipseTemp.setCentre(startPoint);
            ellipseTemp.setRx(rx);
            ellipseTemp.setRy(rx);
            this.addObjet(ellipseTemp);
            
            // Marquer la position pour définir rx
            this.addObjet(new Point("RX", clickPoint.getX(), clickPoint.getY(), this));
        } else {
            // Troisième clic: point définissant le rayon vertical (ry)
            double rx = startPoint.calculerDistance(pointMaintenu);
            double ry = Math.abs(clickPoint.getY() - startPoint.getY());
            
            // Supprime l'ellipse temporaire (nous la recréerons avec les bonnes dimensions)
            // Notez que nous devrions conserver une référence à l'ellipse temporaire pour la supprimer
            // Comme ce n'est pas implémenté, nous allons simplement créer une nouvelle ellipse
            
            // Crée l'ellipse finale
            Ellipse ellipse = new Ellipse(this);
            ellipse.setCentre(startPoint);
            ellipse.setRx(rx);
            ellipse.setRy(ry);
            this.addObjet(ellipse);
            
            // Marquer la position pour définir ry
            this.addObjet(new Point("RY", clickPoint.getX(), clickPoint.getY(), this));
            
            // Réinitialiser les points
            startPoint = null;
            pointMaintenu = null;
        }
    }
    
    // Méthode pour gérer la création d'un triangle rectangle
    private void handleTriangleRectangleCreation(Point clickPoint) {
        if (pointMaintenu == null) {
            // Premier sommet (A)
            pointMaintenu = clickPoint;
            this.addObjet(new Point("A", clickPoint.getX(), clickPoint.getY(), this));
        } else if (startPoint == null) {
            // Deuxième sommet (B) - un côté de l'angle droit
            startPoint = clickPoint;
            this.addObjet(new Point("B", clickPoint.getX(), clickPoint.getY(), this));
        } else {
            // Calculer le troisième sommet pour former un angle droit en A
            // On projette le point cliqué sur l'axe perpendiculaire à AB passant par A
            
            // Vecteur AB
            double abx = startPoint.getX() - pointMaintenu.getX();
            double aby = startPoint.getY() - pointMaintenu.getY();
            
            // Vecteur perpendiculaire à AB (-by, bx)
            double perpX = -aby;
            double perpY = abx;
            
            // Normalisation du vecteur perpendiculaire
            double length = Math.sqrt(perpX * perpX + perpY * perpY);
            perpX /= length;
            perpY /= length;
            
            // Projection du vecteur AC sur le vecteur perpendiculaire
            double acx = clickPoint.getX() - pointMaintenu.getX();
            double acy = clickPoint.getY() - pointMaintenu.getY();
            double proj = acx * perpX + acy * perpY;
            
            // Coordonnées du point C pour avoir un angle droit
            double cX = pointMaintenu.getX() + proj * perpX;
            double cY = pointMaintenu.getY() + proj * perpY;
            
            // Créer le point C
            Point c = new Point("C", cX, cY, this);
            this.addObjet(c);
            
            // Créer le triangle
            Triangle triangle = new Triangle(pointMaintenu, startPoint, c, this);
            this.addObjet(triangle);
            
            // Réinitialiser les points
            pointMaintenu = null;
            startPoint = null;
        }
    }
    
    // Méthode pour gérer la création d'un triangle isocèle
    private void handleTriangleIsoceleCreation(Point clickPoint) {
        if (pointMaintenu == null) {
            // Premier sommet (A) - le sommet principal de l'isocèle
            pointMaintenu = clickPoint;
            this.addObjet(new Point("A", clickPoint.getX(), clickPoint.getY(), this));
        } else if (startPoint == null) {
            // Deuxième sommet (B) - définit la base et la hauteur
            startPoint = clickPoint;
            this.addObjet(new Point("B", clickPoint.getX(), clickPoint.getY(), this));
        } else {
            // Pour un triangle isocèle, on veut |AB| = |AC|
            // On utilise la direction indiquée par le troisième clic, mais on ajuste la distance
            
            // Distance AB (qui sera égale à AC)
            double distAB = pointMaintenu.calculerDistance(startPoint);
            
            // Direction souhaitée (de A vers le point cliqué)
            double dirX = clickPoint.getX() - pointMaintenu.getX();
            double dirY = clickPoint.getY() - pointMaintenu.getY();
            
            // Normaliser cette direction
            double dirLength = Math.sqrt(dirX * dirX + dirY * dirY);
            dirX /= dirLength;
            dirY /= dirLength;
            
            // Coordonnées du point C à distance |AB| de A dans la direction indiquée
            double cX = pointMaintenu.getX() + dirX * distAB;
            double cY = pointMaintenu.getY() + dirY * distAB;
            
            // Créer le point C
            Point c = new Point("C", cX, cY, this);
            this.addObjet(c);
            
            // Créer le triangle
            Triangle triangle = new Triangle(pointMaintenu, startPoint, c, this);
            this.addObjet(triangle);
            
            // Réinitialiser les points
            pointMaintenu = null;
            startPoint = null;
        }
    }
    
    // Méthode pour gérer la création d'un triangle équilatéral
    private void handleTriangleEquilateralCreation(Point clickPoint) {
        if (pointMaintenu == null) {
            // Premier sommet (A)
            pointMaintenu = clickPoint;
            this.addObjet(new Point("A", clickPoint.getX(), clickPoint.getY(), this));
        } else if (startPoint == null) {
            // Deuxième sommet (B)
            startPoint = clickPoint;
            this.addObjet(new Point("B", clickPoint.getX(), clickPoint.getY(), this));
        } else {
            // Pour un triangle équilatéral, on calcule la position du 3ème sommet
            // à partir des deux premiers et de la contrainte que tous les côtés ont la même longueur
            
            // Vecteur AB
            double abx = startPoint.getX() - pointMaintenu.getX();
            double aby = startPoint.getY() - pointMaintenu.getY();
            
            // Distance AB
            double distAB = Math.sqrt(abx * abx + aby * aby);
            
            // Rotation de 60 degrés
            double angle = 60 * Math.PI / 180;
            double rotX = abx * Math.cos(angle) - aby * Math.sin(angle);
            double rotY = abx * Math.sin(angle) + aby * Math.cos(angle);
            
            // Coordonnées du point C
            double cX = pointMaintenu.getX() + rotX;
            double cY = pointMaintenu.getY() + rotY;
            
            // Créer le point C
            Point c = new Point("C", cX, cY, this);
            this.addObjet(c);
            
            // Créer le triangle
            Triangle triangle = new Triangle(pointMaintenu, startPoint, c, this);
            this.addObjet(triangle);
            
            // Réinitialiser les points
            pointMaintenu = null;
            startPoint = null;
        }
    }
}
