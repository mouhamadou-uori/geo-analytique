package geoanalytique.gui;

import geoanalytique.view.GeoAnalytiqueView;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Représente l'interface graphique principale de l'application GeoAnalytique.
 * <p>
 * Cette interface contient une zone centrale de dessin (canvas) et une barre latérale
 * contenant trois sections : Éléments, Identifiants et Opérations.
 * </p>
 * 
 * @author 
 */
public class GeoAnalytiqueGUI extends JPanel {

    /** Composant principal de dessin ou d'affichage graphique. */
    private GeoAnalytiqueView grille;

    /** Panneau latéral contenant les sections de boutons ou d'actions. */
    private JPanel sideBar;

    /** Section dédiée aux éléments (formes, objets, etc.). */
    private JPanel panelElements;

    /** Section dédiée aux identifiants ou objets sélectionnés. */
    private JPanel panelIDs;

    /** Section dédiée aux opérations (actions sur les éléments, calculs, etc.). */
    private JPanel panelOperations;

    /**
     * Constructeur de l'interface graphique principale.
     * Initialise les composants et organise l'agencement.
     */
    public GeoAnalytiqueGUI() {
        grille = new GeoAnalytiqueView();
        sideBar = new JPanel();

        panelElements = new JPanel();
        panelIDs = new JPanel();
        panelOperations = new JPanel();

        // Ajout de bordures avec titres pour chaque section
        panelElements.setBorder(new TitledBorder("Éléments"));
        panelIDs.setBorder(new TitledBorder("Identifiants"));
        panelOperations.setBorder(new TitledBorder("Opérations"));

        // Organisation verticale des sections dans la sidebar
        sideBar.setLayout(new GridLayout(3, 1));
        sideBar.add(panelElements);
        sideBar.add(panelIDs);
        sideBar.add(panelOperations);
        sideBar.setPreferredSize(new Dimension(200, 0)); // largeur fixe

        // Layout principal
        this.setLayout(new BorderLayout());
        this.add(grille, BorderLayout.CENTER);
        this.add(sideBar, BorderLayout.EAST);
    }

    /**
     * Retourne la vue graphique centrale (canvas).
     *
     * @return la grille principale de type {@link GeoAnalytiqueView}
     */
    public GeoAnalytiqueView getCanvas() {
        return grille;
    }

    /**
     * Retourne le panneau contenant les boutons ou options liés aux éléments.
     *
     * @return le conteneur de la section "Éléments"
     */
    public Container getPanelElements() {
        return panelElements;
    }

    /**
     * Retourne le panneau contenant les informations ou options liées aux identifiants.
     *
     * @return le conteneur de la section "Identifiants"
     */
    public Container getPanelIDs() {
        return panelIDs;
    }

    /**
     * Retourne le panneau contenant les opérations applicables sur les éléments.
     *
     * @return le conteneur de la section "Opérations"
     */
    public Container getPanelOperations() {
        return panelOperations;
    }
}
