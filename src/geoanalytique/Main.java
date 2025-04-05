package geoanalytique;
 
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.gui.GeoAnalytiqueGUI;

import geoanalytique.model.Point;
import geoanalytique.model.Segment;

import javax.swing.JFrame;

/**
 * Classe de lancement principale de l'application.
 * Vous pouvez modifier comme vous voulez ce lanceur minimale.
 * 
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	GeoAnalytiqueGUI mainPanel = new GeoAnalytiqueGUI();
    	
    	JFrame frame = new JFrame("GeoAnalytique - version 0.01");
    	frame.getContentPane().add(mainPanel);
    	frame.setSize(800, 600);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
    	frame.pack();
    	
    	GeoAnalytiqueControleur controleur = new GeoAnalytiqueControleur(mainPanel);
    	controleur.prepareTout();
        
        // Petit exemple
        controleur.addObjet(new Point("Ori", 0,1, controleur));
        controleur.addObjet(new Segment(new Point("A", 0, 2, null), new Point("B", 4, 4, null), controleur));
    	
    	frame.setVisible(true);
    	
    }

}
