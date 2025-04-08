package geoanalytique.controleur;
 
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.exception.VisiteurException;
import geoanalytique.graphique.Graphique;
import geoanalytique.gui.GeoAnalytiqueGUI;
import geoanalytique.model.Cercle;
import geoanalytique.model.GeoObject;
import geoanalytique.model.Point;
import geoanalytique.model.Segment;
import geoanalytique.model.ViewPort;
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
			Point pointClique = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), null);
			// if (currentTool.equals("CIRCLE")) {
			// 	centreEnCoursCreation = pointClique;
        	// 	cercleEnCoursCreation = new Cercle(centreEnCoursCreation, 0, null);
        	// 	this.addObjet(cercleEnCoursCreation); // Ajouter un cercle temporaire avec rayon 0
			// }
			switch (currentTool) {
				case "POINT":
					this.addObjet(new Point("Ori", pointClique.getX(),pointClique.getY(), null));
					break;
				case "LINE":
					handleLineCreation(pointClique);
					break;
				case "CIRCLE":
					if (centreEnCoursCreation == null/*  && cercleEnCoursCreation == null */) {
						centreEnCoursCreation = pointClique;
						this.addObjet(new Point("Ori", pointClique.getX(),pointClique.getY(), null));
						cercleEnCoursCreation = new Cercle(centreEnCoursCreation, 0, null);
						this.addObjet(cercleEnCoursCreation); // Ajouter un cercle temporaire avec rayon 0
					}
					break;
				case "TEXT":
					break;
				case "LENGTH":
				case "SLOPE":
				case "MIDPOINT":
					break;
			}
			view.getCanvas().repaint();
	}

    public void mouseDragged(MouseEvent e) {
		if (currentTool.equals("CIRCLE") && centreEnCoursCreation != null && cercleEnCoursCreation != null) {
			// Calculer la position actuelle de la souris
			Point pointCourant = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), null);
			
			// Calculer le rayon basé sur la distance entre le centre et la position actuelle
			double rayon = centreEnCoursCreation.calculerDistance(pointCourant);
			
			// Supprimer l'ancien cercle de la liste d'objets
			objs.remove(cercleEnCoursCreation);
			
			// Créer un nouveau cercle avec le rayon mis à jour et l'ajouter
			cercleEnCoursCreation = new Cercle(centreEnCoursCreation, rayon, null);
			this.addObjet(cercleEnCoursCreation);
			
			// Mettre à jour l'affichage
			// view.getCanvas().clear();
			recalculPoints();
			// view.getCanvas().repaint();
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
					ope.setArgument(i, new Double(res));
				}
				else if(ope.getClassArgument(i) == Integer.class) {
					ope.setArgument(i, new Integer(res));
				}
				else if(ope.getClassArgument(i) == Character.class) {
					ope.setArgument(i, new Character(res.charAt(0)));
				}
				else if(ope.getClassArgument(i) == String.class) {
					ope.setArgument(i, new String(res));
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
}
