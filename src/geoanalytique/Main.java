package geoanalytique;
 
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.gui.GeoAnalytiqueGUI;

import geoanalytique.model.Point;
import geoanalytique.model.Segment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Classe de lancement principale de l'application.
 * Vous pouvez modifier comme vous voulez ce lanceur minimale.
 * 
 */
public class Main {
	private static final Color BACKGROUND_COLOR = new Color(10, 15, 30);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	GeoAnalytiqueGUI mainPanel = new GeoAnalytiqueGUI();
    	
    	JFrame frame = new JFrame("GeoAnalytique - version 0.01");
    	// frame.setSize(1000, 600);
		frame.setMinimumSize(new Dimension(1200, 900));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(10, 10));
		mainPanel.setBackground(BACKGROUND_COLOR);
    	frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.getContentPane().setBackground(BACKGROUND_COLOR);
		//frame.setResizable(false);
    	frame.pack();
		frame.setVisible(true);
    	
    	GeoAnalytiqueControleur controleur = new GeoAnalytiqueControleur(mainPanel);
    	controleur.prepareTout();
        
        // Petit exemple
        controleur.addObjet(new Point("Ori", 0,0, controleur));
        try {
			Thread.sleep(3000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// controleur.addObjet(new Point("Ori", 0,1, controleur));
        controleur.addObjet(new Segment(new Point("A", 0, 2, null), new Point("B", 4, 4, null), controleur));
    	
    	
    	
    }

}
