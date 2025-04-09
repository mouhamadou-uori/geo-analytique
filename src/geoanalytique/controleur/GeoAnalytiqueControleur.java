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
import geoanalytique.model.Droite;
import geoanalytique.model.Losange;
import geoanalytique.util.Dessinateur;
import geoanalytique.util.Operation;
import geoanalytique.model.geoobject.operation.ChangeNomOperation;
import geoanalytique.model.geoobject.operation.DeplacerPointOperation;
import geoanalytique.model.geoobject.operation.CalculAireTriangleOperation;
import geoanalytique.model.geoobject.operation.CalculCentreGraviteTriangleOperation;
import geoanalytique.model.geoobject.operation.CalculCercleCirconscritOperation;
import geoanalytique.model.geoobject.operation.CalculCercleInscritOperation;
import geoanalytique.model.geoobject.operation.CalculMediatriceDroiteOperation;
import geoanalytique.model.geoobject.operation.CalculeDistanceEntreDeuxPointsOperation;
import geoanalytique.model.geoobject.operation.CalculeMilieuEntreDeuxPointsOperation;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

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
	
	// Variables pour gérer la sélection du deuxième point
	private Operation operationEnCours = null;
	private GeoObject premierPoint = null;
	private int argumentEnCours = 0;
	private boolean enAttenteDeSelection = false;
	private JButton boutonOperationEnCours = null;
	private JButton boutonOutilActif = null; // Nouvelle variable pour stocker le bouton d'outil actif
        
		
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
		// Récupérer le bouton cliqué
		JButton boutonClique = (JButton) e.getSource();
		
		// Réinitialiser la couleur du bouton d'outil précédent s'il existe
		if (boutonOutilActif != null && boutonOutilActif != boutonClique) {
			boutonOutilActif.setBackground(new Color(0, 120, 160));
		}
		
		// Mettre en évidence le bouton cliqué
		boutonClique.setBackground(new Color(255, 165, 0)); // Orange pour le bouton actif
		boutonOutilActif = boutonClique;
		
		// Mettre à jour l'outil actuel
		currentTool = boutonClique.getText();
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
        Point pointClique = new Point((double)(e.getX()-viewport.getCentreX())/40, -((double)(e.getY()-viewport.getCentreY())/40), this);
        
        // Si on est en attente de sélection pour une opération
        if (enAttenteDeSelection && operationEnCours != null) {
            // Distance maximale de sélection (en unités du modèle)
            double maxDistance = 0.5;
            GeoObject closestObject = null;
            double closestDistance = Double.MAX_VALUE;
            
            // Parcourir tous les objets pour trouver le plus proche du clic
            for (GeoObject obj : objs) {
                if (obj instanceof Point) {
                    Point p = (Point) obj;
                    double distance = p.calculerDistance(pointClique);
                    if (distance < maxDistance && distance < closestDistance) {
                        closestDistance = distance;
                        closestObject = p;
                    }
                } else if (obj instanceof Segment) {
                    Segment s = (Segment) obj;
                    // Calculer la distance du point au segment
                    double distance = calculerDistancePointSegment(pointClique, s);
                    if (distance < maxDistance && distance < closestDistance) {
                        closestDistance = distance;
                        closestObject = s;
                    }
                } else if (obj instanceof Droite) {
                    Droite d = (Droite) obj;
                    // Calculer la distance du point à la droite
                    double distance = calculerDistancePointDroite(pointClique, d);
                    if (distance < maxDistance && distance < closestDistance) {
                        closestDistance = distance;
                        closestObject = d;
                    }
                } else if (obj.contient(pointClique)) {
                    // Pour les autres objets (cercles, ellipses, polygones, etc.)
                    // utiliser la méthode contient
                    closestObject = obj;
                    break;
                }
            }
            
            // Si un objet a été sélectionné
            if (closestObject != null) {
                try {
                    // Définir l'argument pour l'opération en cours
                    operationEnCours.setArgument(argumentEnCours, closestObject);
                    
                    // Passer à l'argument suivant ou terminer l'opération
                    argumentEnCours++;
                    if (argumentEnCours >= operationEnCours.getArite()) {
                        // Tous les arguments sont définis, exécuter l'opération
                        Object resultat = operationEnCours.calculer();
                        traiterResultatOperation(resultat, operationEnCours);
                        
                        // Réinitialiser les variables
                        enAttenteDeSelection = false;
                        operationEnCours = null;
                        premierPoint = null;
                        argumentEnCours = 0;
                        currentTool = "SELECT"; // Revenir en mode sélection
                    } else {
                        // Demander le prochain argument
                        JOptionPane.showMessageDialog(view, 
                            "Veuillez maintenant sélectionner " + operationEnCours.getDescriptionArgument(argumentEnCours), 
                            "Sélection", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Erreur lors de la sélection: " + ex.getMessage(), 
                                                 "Erreur", JOptionPane.ERROR_MESSAGE);
                    
                    // Réinitialiser les variables en cas d'erreur
                    enAttenteDeSelection = false;
                    operationEnCours = null;
                    premierPoint = null;
                    argumentEnCours = 0;
                    currentTool = "SELECT";
                }
                
                view.repaint();
                return;
            }
        }
        
        // Si on est en mode sélection ou opération, chercher un objet sous le curseur
        if (currentTool.equals("SELECT") || 
            currentTool.equals("LENGTH") || 
            currentTool.equals("SLOPE") || 
            currentTool.equals("MIDPOINT") ||
            currentTool.equals("DEPLACER POINT") ||
            currentTool.equals("SURFACE") ||
            currentTool.equals("CONTOUR") ||
            currentTool.equals("DISTANCE A -> B") ||
            currentTool.equals("MILIEU A -> B") ||
            currentTool.equals("MEDIATRICE") ||
            currentTool.equals("BISSECTRICE") ||
            currentTool.equals("MEDIANE") ||
            currentTool.equals("CENTRE GRAVITE") ||
            currentTool.equals("O CIRCONSCRIT") ||
            currentTool.equals("O INSCRIT")) {
            
            // Distance maximale de sélection (en unités du modèle)
            double maxDistance = 0.5;
            GeoObject closestObject = null;
            double closestDistance = Double.MAX_VALUE;
            
            // Parcourir tous les objets pour trouver le plus proche du clic
            for (GeoObject obj : objs) {
                if (obj instanceof Point) {
                    Point p = (Point) obj;
                    double distance = p.calculerDistance(pointClique);
                    if (distance < maxDistance && distance < closestDistance) {
                        closestDistance = distance;
                        closestObject = p;
                    }
                } else if (obj instanceof Segment) {
                    Segment s = (Segment) obj;
                    // Calculer la distance du point au segment
                    double distance = calculerDistancePointSegment(pointClique, s);
                    if (distance < maxDistance && distance < closestDistance) {
                        closestDistance = distance;
                        closestObject = s;
                    }
                } else if (obj instanceof Droite) {
                    Droite d = (Droite) obj;
                    // Calculer la distance du point à la droite
                    double distance = calculerDistancePointDroite(pointClique, d);
                    if (distance < maxDistance && distance < closestDistance) {
                        closestDistance = distance;
                        closestObject = d;
                    }
                } else if (obj.contient(pointClique)) {
                    // Pour les autres objets (cercles, ellipses, polygones, etc.)
                    // utiliser la méthode contient
                    closestObject = obj;
                    break;
                }
            }
            
            // Sélectionner l'objet le plus proche s'il y en a un
            if (closestObject != null) {
                selectionner(closestObject);
                return;
            } else {
                // Si aucun objet n'est proche, désélectionner
                deselectionner();
            }
        } else {
            // Comportement normal pour la création d'objets
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
                case "LOSANGE":
                    handleLosangeCreation(pointClique);
                    break;
                case "TRIANGLE":
                    handleTriangleCreation(pointClique);
                    break;
                case "TRIANGLE_RECTANGLE":
                case "TRIANGLE RECT":
                    handleTriangleRectangleCreation(pointClique);
                    break;
                case "TRIANGLE_ISOCELE":
                case "TRIANGLE ISOS":
                    handleTriangleIsoceleCreation(pointClique);
                    break;
                case "TRIANGLE_EQUILATERAL":
                case "TRIANGLE EQUI":
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
        // Si un objet est déjà sélectionné, le désélectionner d'abord
        if (select != null) {
            deselectionner();
        }
        
        // Sélectionner le nouvel objet
        select = o;
        
        // Vider le panneau d'opérations
        JPanel operationsPanel = (JPanel) view.getPanelOperations();
        operationsPanel.removeAll();
        
        // Créer un panneau pour les boutons d'opérations
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(new Color(40, 40, 40));
        
        // Déterminer quelles opérations sont disponibles pour cet objet
        List<Operation> availableOperations = getAvailableOperationsForObject(o);
        
        // Ajouter un bouton pour chaque opération disponible
        for (Operation op : availableOperations) {
            JButton opButton = new JButton(op.getTitle());
            opButton.setBackground(new Color(0, 120, 160));
            opButton.setForeground(Color.WHITE);
            opButton.setBorder(new EmptyBorder(10, 10, 10, 10));
            opButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, opButton.getPreferredSize().height));
            opButton.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            
            // Ajouter un gestionnaire d'événements pour cette opération
            opButton.addActionListener(new OperationControleur(o, op, this));
            
            buttonsPanel.add(opButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        operationsPanel.add(buttonsPanel);
        
        // Rafraîchir l'interface
        operationsPanel.revalidate();
        operationsPanel.repaint();
        
        // Mettre à jour l'affichage pour montrer l'objet sélectionné en évidence
        recalculPoints();
	}
	
	/**
         * Operation permettant de deselectionner le dernier objet selectionne
         * (si il existe). On pourra enlever tous marqueurs present sur l'interface
         * graphique a ce moment ainsi que les operations anciennement realisable.
         */	
	private void deselectionner() {
        // Réinitialiser l'objet sélectionné
        select = null;
        
        // Vider le panneau d'opérations
        JPanel operationsPanel = (JPanel) view.getPanelOperations();
        operationsPanel.removeAll();
        
        // Ajouter une étiquette indiquant qu'aucun objet n'est sélectionné
        JLabel noSelectionLabel = new JLabel("Aucun objet sélectionné");
        noSelectionLabel.setForeground(Color.WHITE);
        operationsPanel.add(noSelectionLabel);
        
        // Rafraîchir l'interface
        operationsPanel.revalidate();
        operationsPanel.repaint();
        
        // Mettre à jour l'affichage pour enlever l'évidence de sélection
        recalculPoints();
    }
    
    /**
     * Détermine les opérations disponibles pour un objet spécifique en fonction de son type.
     * 
     * @param obj L'objet pour lequel déterminer les opérations disponibles
     * @return Liste des opérations disponibles
     */
    private List<Operation> getAvailableOperationsForObject(GeoObject obj) {
        List<Operation> operations = new ArrayList<>();
        
        // Ajouter l'opération de changement de nom pour tous les objets
        operations.add(new ChangeNomOperation(obj));
        
        // Opérations spécifiques selon le type d'objet
        if (obj instanceof Point) {
            // Pour le déplacement de point, on crée une opération avec le point déjà défini
            DeplacerPointOperation deplacementOp = new DeplacerPointOperation();
            try {
                // On définit le point comme premier argument
                deplacementOp.setArgument(0, obj);
                operations.add(deplacementOp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (obj instanceof Triangle) {
            operations.add(new CalculAireTriangleOperation());
            operations.add(new CalculCentreGraviteTriangleOperation());
            operations.add(new CalculCercleCirconscritOperation());
            operations.add(new CalculCercleInscritOperation());
        }
        
        if (obj instanceof Segment) {
            operations.add(new CalculMediatriceDroiteOperation());
        }
        
        // Pour tous les points, permettre de calculer la distance à un autre point
        if (obj instanceof Point) {
            operations.add(new CalculeDistanceEntreDeuxPointsOperation());
            operations.add(new CalculeMilieuEntreDeuxPointsOperation());
        }
        
        return operations;
    }

    /**
     * Modifie la méthode recalculPoints pour mettre en évidence l'objet sélectionné
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
                
                // Vérifier que le graphique n'est pas null avant de l'utiliser
                if (c != null) {
                    // Si l'objet est sélectionné, modifie son apparence
                    if (o == select) {
                        c.setCouleur(new Color(255, 69, 0)); // Orange-rouge vif pour la sélection
                    }
                    
                    view.getCanvas().addGraphique(c);
                }
                
                // Traitement spécial pour les polygones (Rectangle, Carré, Triangle, Losange)
                if (o instanceof Rectangle) {
                    // Ajouter les segments 1, 2 et 3 (le segment 0 est déjà ajouté)
                    Rectangle r = (Rectangle) o;
                    for (int i = 1; i < 4; i++) {
                        Segment s = r.getSegment(i);
                        if (s != null) {
                            GLigne ligne = viewport.convert(s.getDebut().getX(), s.getDebut().getY(), 
                                                           s.getFin().getX(), s.getFin().getY());
                            
                            // Vérifier que la ligne n'est pas null
                            if (ligne != null) {
                                // Si l'objet est sélectionné, modifie l'apparence des segments
                                if (o == select) {
                                    ligne.setColor(new Color(255, 69, 0)); // Orange-rouge vif
                                }
                                
                                view.getCanvas().addGraphique(ligne);
                            }
                        }
                    }
                } else if (o instanceof Triangle) {
                    // Ajouter les segments 1 et 2 (le segment 0 est déjà ajouté)
                    Triangle t = (Triangle) o;
                    for (int i = 1; i < 3; i++) {
                        Segment s = t.getSegment(i);
                        if (s != null) {
                            GLigne ligne = viewport.convert(s.getDebut().getX(), s.getDebut().getY(), 
                                                           s.getFin().getX(), s.getFin().getY());
                            
                            // Vérifier que la ligne n'est pas null
                            if (ligne != null) {
                                // Si l'objet est sélectionné, modifie l'apparence des segments
                                if (o == select) {
                                    ligne.setColor(new Color(255, 69, 0)); // Orange-rouge vif
                                }
                                
                                view.getCanvas().addGraphique(ligne);
                            }
                        }
                    }
                } else if (o instanceof Losange) {
                    // Ajouter les segments 0, 1, 2 et 3 du losange
                    Losange l = (Losange) o;
                    for (int i = 0; i < 4; i++) {
                        Segment s = l.getSegment(i);
                        if (s != null) {
                            GLigne ligne = viewport.convert(s.getDebut().getX(), s.getDebut().getY(), 
                                                           s.getFin().getX(), s.getFin().getY());
                            
                            // Vérifier que la ligne n'est pas null
                            if (ligne != null) {
                                // Si l'objet est sélectionné, modifie l'apparence des segments
                                if (o == select) {
                                    ligne.setColor(new Color(255, 69, 0)); // Orange-rouge vif
                                }
                                
                                view.getCanvas().addGraphique(ligne);
                            }
                        }
                    }
                }
            } catch (VisiteurException e) {
                e.printStackTrace();
            }
        }
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

    private void handleLosangeCreation(Point clickPoint) {
        if (pointMaintenu == null) {
            // Premier clic: Centre du losange
            pointMaintenu = clickPoint;
            this.addObjet(new Point("Centre", clickPoint.getX(), clickPoint.getY(), this));
        } else if (startPoint == null) {
            // Deuxième clic: Définit la première demi-diagonale (direction et longueur)
            startPoint = clickPoint;
            double demi1 = pointMaintenu.calculerDistance(clickPoint);
            
            // Calculer l'angle de la première diagonale
            double dx = clickPoint.getX() - pointMaintenu.getX();
            double dy = clickPoint.getY() - pointMaintenu.getY();
            double angle = Math.atan2(dy, dx);
            
            // Ajouter un point temporaire pour marquer l'extrémité de la diagonale
            this.addObjet(new Point("D1", clickPoint.getX(), clickPoint.getY(), this));
            
            // Demander à l'utilisateur de cliquer pour la deuxième diagonale
            JOptionPane.showMessageDialog(view, 
                "Cliquez maintenant pour définir la deuxième diagonale", 
                "Création d'un losange", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Troisième clic: Définit la deuxième demi-diagonale (direction perpendiculaire, longueur)
            double demi1 = pointMaintenu.calculerDistance(startPoint);
            
            // Calculer l'angle de la première diagonale
            double dx1 = startPoint.getX() - pointMaintenu.getX();
            double dy1 = startPoint.getY() - pointMaintenu.getY();
            double angle = Math.atan2(dy1, dx1);
            
            // Calculer le vecteur perpendiculaire normalisé pour la deuxième direction
            double dx2 = clickPoint.getX() - pointMaintenu.getX();
            double dy2 = clickPoint.getY() - pointMaintenu.getY();
            
            // Projection du vecteur sur la direction perpendiculaire à la première diagonale
            // (rotation de 90 degrés du vecteur de la première diagonale)
            double perpX = -dy1 / demi1;
            double perpY = dx1 / demi1;
            
            // Longueur de la projection (demi-diagonale 2)
            double demi2 = Math.abs(dx2 * perpX + dy2 * perpY);
            
            // Ajouter un point pour marquer la projection
            double projX = pointMaintenu.getX() + perpX * demi2;
            double projY = pointMaintenu.getY() + perpY * demi2;
            this.addObjet(new Point("D2", projX, projY, this));
            
            // Créer et ajouter le losange
            Losange losange = new Losange(pointMaintenu, demi1, demi2, angle, "Losange", this);
            this.addObjet(losange);
            
            // Réinitialiser les points
            pointMaintenu = null;
            startPoint = null;
        }
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
			
			// Ajouter les listeners à tous les boutons
			for (JButton button : view.getAllButtons()) {
    			button.addActionListener(this);
    			
    			// Si c'est le bouton SELECT, on le met en évidence par défaut
    			if (button.getText().equals("SELECT")) {
    			    button.setBackground(new Color(255, 165, 0)); // Orange pour le bouton actif
    			    boutonOutilActif = button;
    			    currentTool = "SELECT"; // S'assurer que l'outil actuel est SELECT
    			}
			}
        
        // Initialiser le panneau d'opérations
        JPanel operationsPanel = (JPanel) view.getPanelOperations();
        operationsPanel.removeAll();
        JLabel noSelectionLabel = new JLabel("Aucun objet sélectionné");
        noSelectionLabel.setForeground(Color.WHITE);
        operationsPanel.add(noSelectionLabel);
        operationsPanel.revalidate();
        operationsPanel.repaint();
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
     * Cette fonction permet de lancer une operation sur un objet. A priori
     * Elle n'a pas a etre modifiee dans un premier temps. Sauf si vous voulez
     * modifier le comportement de celle-ci en donnant un aspect plus jolie.
     * @param object objet sur lequel realise l'operation
     * @param ope operation devant etre realise sur l'objet <i>object</i>
     */
    public void lanceOperation(GeoObject object, Operation ope) {
        // Réinitialiser la couleur du bouton précédent s'il existe
        if (boutonOperationEnCours != null) {
            boutonOperationEnCours.setBackground(new Color(0, 120, 160));
        }

        // Trouver le bouton correspondant à l'opération
        JPanel operationsPanel = (JPanel) view.getPanelOperations();
        for (Component comp : operationsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel buttonsPanel = (JPanel) comp;
                for (Component buttonComp : buttonsPanel.getComponents()) {
                    if (buttonComp instanceof JButton) {
                        JButton button = (JButton) buttonComp;
                        if (button.getText().equals(ope.getTitle())) {
                            button.setBackground(new Color(255, 165, 0)); // Orange pour le bouton actif
                            boutonOperationEnCours = button;
                            break;
                        }
                    }
                }
            }
        }

        // Si l'opération a une arité de 0, on peut la lancer directement
        if (ope.getArite() == 0) {
            Object resultat = ope.calculer();
            traiterResultatOperation(resultat, ope);
            return;
        }
        
        // Cas spécial pour CalculMediatriceDroiteOperation avec un segment
        if (ope instanceof CalculMediatriceDroiteOperation && object instanceof Segment) {
            Segment segment = (Segment) object;
            try {
                // Définir les deux points du segment comme arguments
                ope.setArgument(0, segment.getDebut());
                ope.setArgument(1, segment.getFin());
                
                // Exécuter l'opération et traiter le résultat
                Object resultat = ope.calculer();
                traiterResultatOperation(resultat, ope);
                view.repaint();
                return;
            } catch (Exception e) {
                    e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur lors du calcul de la médiatrice: " + e.getMessage(), 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Cas spécial pour CalculCercleCirconscritOperation avec un triangle
        if (ope instanceof CalculCercleCirconscritOperation && object instanceof Triangle) {
            try {
                // Définir le triangle comme argument
                ope.setArgument(0, object);
                
                // Exécuter l'opération et traiter le résultat
                Object resultat = ope.calculer();
                if (resultat instanceof Cercle) {
                    Cercle cercle = (Cercle) resultat;
                    cercle.setName("Cercle circonscrit");
                    traiterResultatOperation(cercle, ope);
                    JOptionPane.showMessageDialog(view, "Le cercle circonscrit a été tracé", 
                                               "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
                view.repaint();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur lors du calcul du cercle circonscrit: " + e.getMessage(), 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Cas spécial pour CalculCercleInscritOperation avec un triangle
        if (ope instanceof CalculCercleInscritOperation && object instanceof Triangle) {
            try {
                // Définir le triangle comme argument
                ope.setArgument(0, object);
                
                // Exécuter l'opération et traiter le résultat
                Object resultat = ope.calculer();
                if (resultat instanceof Cercle) {
                    Cercle cercle = (Cercle) resultat;
                    cercle.setName("Cercle inscrit");
                    traiterResultatOperation(cercle, ope);
                    JOptionPane.showMessageDialog(view, "Le cercle inscrit a été tracé", 
                                               "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
                view.repaint();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur lors du calcul du cercle inscrit: " + e.getMessage(), 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Cas spécial pour CalculeDistanceEntreDeuxPointsOperation avec un point
        if (ope instanceof CalculeDistanceEntreDeuxPointsOperation && object instanceof Point) {
            try {
                // Définir le premier point comme argument
                ope.setArgument(0, object);
                
                // Afficher un message à l'utilisateur pour sélectionner le deuxième point
                JOptionPane.showMessageDialog(view, "Veuillez maintenant sélectionner le deuxième point", 
                                             "Sélection", JOptionPane.INFORMATION_MESSAGE);
                
                // Configurer l'état pour la sélection du deuxième point
                operationEnCours = ope;
                premierPoint = object;
                argumentEnCours = 1; // On passe au deuxième argument
                enAttenteDeSelection = true;
                
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur lors du calcul de la distance: " + e.getMessage(), 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Cas spécial pour CalculeMilieuEntreDeuxPointsOperation avec un point
        if (ope instanceof CalculeMilieuEntreDeuxPointsOperation && object instanceof Point) {
            try {
                // Définir le premier point comme argument
                ope.setArgument(0, object);
                
                // Afficher un message à l'utilisateur pour sélectionner le deuxième point
                JOptionPane.showMessageDialog(view, "Veuillez maintenant sélectionner le deuxième point", 
                                             "Sélection", JOptionPane.INFORMATION_MESSAGE);
                
                // Configurer l'état pour la sélection du deuxième point
                operationEnCours = ope;
                premierPoint = object;
                argumentEnCours = 1; // On passe au deuxième argument
                enAttenteDeSelection = true;
                
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur lors du calcul du milieu: " + e.getMessage(), 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Pour les opérations avec arguments, on demande les arguments nécessaires
        for(int argIndex=0; argIndex < ope.getArite(); argIndex++) {
            final int i = argIndex; // Variable finale pour le lambda
            try {
                // Vérifier si l'argument est déjà défini (cas du point pour DeplacerPointOperation)
                if (i == 0 && ope instanceof DeplacerPointOperation) {
                    // L'argument 0 est déjà défini dans getAvailableOperationsForObject
                    continue;
                }
                
                // Pour les opérations qui nécessitent deux points ou un segment, on peut proposer de les sélectionner
                if (GeoObject.class.isAssignableFrom(ope.getClassArgument(i))) {
                    // Afficher un message à l'utilisateur pour sélectionner l'objet
                    JOptionPane.showMessageDialog(view, "Veuillez sélectionner " + ope.getDescriptionArgument(i), 
                                                 "Sélection", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Configurer l'état pour la sélection
                    operationEnCours = ope;
                    premierPoint = object;
                    argumentEnCours = i;
                    enAttenteDeSelection = true;
                    
                    return;
                }
                
                // Sinon, on demande à l'utilisateur de saisir l'argument
                String res = JOptionPane.showInputDialog(view, ope.getDescriptionArgument(i), ope.getTitle(), JOptionPane.QUESTION_MESSAGE);
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
                    // Si l'argument est un GeoObject, on cherche l'objet par son nom
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
        
        // Exécuter l'opération et traiter le résultat
        Object resultat = ope.calculer();
        traiterResultatOperation(resultat, ope);
        view.repaint();
    }
    
    /**
     * Traite le résultat d'une opération
     * @param resultat Le résultat de l'opération
     * @param ope L'opération qui a été exécutée
     */
    private void traiterResultatOperation(Object resultat, Operation ope) {
        if(resultat != null) {
            // Si le résultat est un GeoObject, on l'ajoute au système
            if(GeoObject.class.isAssignableFrom(resultat.getClass())) {
                addObjet((GeoObject) resultat);
				System.out.println("resultat: " + resultat);
			}
			else {
                // Sinon, on affiche le résultat à l'utilisateur
                JOptionPane.showConfirmDialog(view, resultat, ope.getTitle(), JOptionPane.OK_OPTION);
			}
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

    /**
     * Calcule la distance d'un point à un segment
     * @param p Le point
     * @param s Le segment
     * @return La distance minimale du point au segment
     */
    private double calculerDistancePointSegment(Point p, Segment s) {
        Point a = s.getDebut();
        Point b = s.getFin();
        
        // Vecteur AB
        double abx = b.getX() - a.getX();
        double aby = b.getY() - a.getY();
        
        // Vecteur AP
        double apx = p.getX() - a.getX();
        double apy = p.getY() - a.getY();
        
        // Longueur du segment AB
        double abLength = Math.sqrt(abx * abx + aby * aby);
        
        // Si le segment est un point, retourner la distance au point
        if (abLength == 0) {
            return p.calculerDistance(a);
        }
        
        // Normaliser le vecteur AB
        abx /= abLength;
        aby /= abLength;
        
        // Projection de AP sur AB
        double t = apx * abx + apy * aby;
        
        // Si la projection est en dehors du segment
        if (t < 0) {
            return p.calculerDistance(a);
        } else if (t > abLength) {
            return p.calculerDistance(b);
        }
        
        // Point de projection sur le segment
        double projX = a.getX() + t * abx;
        double projY = a.getY() + t * aby;
        
        // Distance du point à sa projection
        return Math.sqrt((p.getX() - projX) * (p.getX() - projX) + (p.getY() - projY) * (p.getY() - projY));
    }
    
    /**
     * Calcule la distance d'un point à une droite
     * @param p Le point dont on veut calculer la distance
     * @param d La droite
     * @return La distance minimale du point à la droite
     */
    private double calculerDistancePointDroite(Point p, Droite d) {
        // On utilise la formule de la distance d'un point à une droite
        // en utilisant la représentation point-pente
        Point p1 = d.getPoint();
        double m = d.getPente();
        
        // Si la pente est infinie (droite verticale)
        if (Double.isInfinite(m)) {
            return Math.abs(p.getX() - p1.getX());
        }
        
        // Pour une droite y = mx + b
        // b = y1 - mx1 où (x1,y1) est un point de la droite
        double b = p1.getY() - m * p1.getX();
        
        // La distance est donnée par |mx - y + b| / sqrt(1 + m²)
        return Math.abs(m * p.getX() - p.getY() + b) / Math.sqrt(1 + m * m);
    }

    public void hierarchyBoundsChanged(HierarchyEvent e) {
        // Mettre à jour les dimensions du viewport en utilisant la méthode resize
        viewport.resize(view.getCanvas().getWidth(), view.getCanvas().getHeight());
        
        // Mettre à jour le centre du viewport
        viewport.setCentreX(view.getCanvas().getWidth() / 2);
        viewport.setCentreY(view.getCanvas().getHeight() / 2);
        
        // Redessiner les objets
        recalculPoints();
    }
}
