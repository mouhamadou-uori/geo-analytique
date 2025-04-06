package geoanalytique.gui;

import geoanalytique.view.GeoAnalytiqueView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.FontMetrics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import geoanalytique.model.Point;

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
    private static final Color BACKGROUND_COLOR = new Color(10, 15, 30);
    private static final Color GRID_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR = new Color(0, 230, 230);
    private static final Color PANEL_COLOR = new Color(15, 25, 40);
    private static final int POINT_SIZE = 8;

    private String currentTool = "POINT";
    private Point selectedPoint1 = null;
    private Point selectedPoint2 = null;

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

        panelElements = createShapesPanel();
        panelIDs = createIdsPanel();
        panelOperations = createOperationsPanel();

        // Ajout de bordures avec titres pour chaque section
        panelElements.setBorder(new TitledBorder("Éléments"));
        panelIDs.setBorder(new TitledBorder("Identifiants"));
        panelOperations.setBorder(new TitledBorder("Opérations"));

        // Organisation verticale des sections dans la sidebar
        sideBar.setLayout(new GridLayout(3, 1, 0, 8));
        sideBar.add(panelElements);
        sideBar.add(panelIDs);
        sideBar.add(panelOperations);
        sideBar.setBackground(PANEL_COLOR);
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

    private JPanel createShapesPanel() {
        // Panneau pour les boutons avec BoxLayout vertical
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(PANEL_COLOR);
        
        String[] tools = {"POINT", "LINE", "CIRCLE", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};
        
        for (String tool : tools) {
            JButton button = createToolButton(tool);
            
            // Configuration pour que le bouton prenne toute la largeur
            button.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espace entre les boutons
        }
        
        // Ajouter un élément invisible à la fin
        buttonsPanel.add(Box.createVerticalGlue());
        
        // Créer un JScrollPane personnalisé
        JScrollPane scrollPane = new JScrollPane(buttonsPanel) {
            @Override
            public boolean isWheelScrollingEnabled() {
                return true;
            }
        };
        
        // Configurer le scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        // Gestion du défilement à la molette
        scrollPane.addMouseWheelListener(e -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getValue() + e.getUnitsToScroll() * verticalBar.getUnitIncrement());
        });
        
        // Panneau conteneur final
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(PANEL_COLOR);
        containerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }
    private JPanel createOperationsPanel() {
        // Panneau pour les boutons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(PANEL_COLOR);
        
        String[] operations = {"LENGTH", "SLOPE", "MIDPOINT", "MIDPOINT", "MIDPOINT", "MIDPOINT", "MIDPOINT"};
        
        for (String operation : operations) {
            JButton button = createToolButton(operation);
            
            // Configuration pour que le bouton prenne toute la largeur
            button.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espace entre les boutons
        }
        
        // Ajouter un élément invisible à la fin pour garantir que tout le contenu est accessible
        buttonsPanel.add(Box.createVerticalGlue());
        
        // Créer un JScrollPane sans barre visible mais qui défile
        JScrollPane scrollPane = new JScrollPane(buttonsPanel) {
            @Override
            public boolean isWheelScrollingEnabled() {
                return true; // Assure que le défilement par molette est activé
            }
        };
        
        // Configurer le JScrollPane pour cacher les barres de défilement
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        // Ajouter un écouteur de molette pour gérer le défilement sans barre visible
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                int currentValue = verticalBar.getValue();
                int scrollAmount = e.getUnitsToScroll() * verticalBar.getUnitIncrement();
                verticalBar.setValue(currentValue + scrollAmount);
            }
        });
        
        // Panneau conteneur avec bordure
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(PANEL_COLOR);
        containerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }
    private JPanel createIdsPanel() {
        // Panneau pour les boutons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(PANEL_COLOR);
        
        String[] operations = {"id1", "id2", "id3", "id4", "id5", "id6", "id7"};
        
        for (String operation : operations) {
            JButton button = createToolButton(operation);
            
            // Configuration pour que le bouton prenne toute la largeur
            button.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espace entre les boutons
        }
        
        // Ajouter un élément invisible à la fin pour garantir que tout le contenu est accessible
        buttonsPanel.add(Box.createVerticalGlue());
        
        // Créer un JScrollPane sans barre visible mais qui défile
        JScrollPane scrollPane = new JScrollPane(buttonsPanel) {
            @Override
            public boolean isWheelScrollingEnabled() {
                return true; // Assure que le défilement par molette est activé
            }
        };
        
        // Configurer le JScrollPane pour cacher les barres de défilement
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        // Ajouter un écouteur de molette pour gérer le défilement sans barre visible
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                int currentValue = verticalBar.getValue();
                int scrollAmount = e.getUnitsToScroll() * verticalBar.getUnitIncrement();
                verticalBar.setValue(currentValue + scrollAmount);
            }
        });
        
        // Panneau conteneur avec bordure
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(PANEL_COLOR);
        containerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }
    private JButton createToolButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(ACCENT_COLOR);
        button.setBackground(PANEL_COLOR);
        button.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            currentTool = text;
            selectedPoint1 = null;
            selectedPoint2 = null;
            
            if (text.equals("LENGTH") || text.equals("SLOPE") || text.equals("MIDPOINT")) {
                JOptionPane.showMessageDialog(this, 
                    "Select two points to calculate " + text.toLowerCase(), 
                    text, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        return button;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Calculate origin
        grille.setOriginX(grille.getWidth() / 2);
        grille.setOriginY(grille.getHeight() / 2);
        
        // Draw grid
        drawGrid(g2d);
        
        // Draw axes
        drawAxes(g2d);
        grille.paint(g2d);
    }
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(GRID_COLOR);
        
        // Draw horizontal grid lines
        for (int y = grille.getOriginY() % grille.getGridSize(); y < getHeight(); y += grille.getGridSize()) {
            g2d.drawLine(0, y, grille.getWidth(), y);
        }
        
        // Draw vertical grid lines
        for (int x = grille.getOriginX() % grille.getGridSize(); x < grille.getWidth(); x += grille.getGridSize()) {
            g2d.drawLine(x, 0, x, getHeight());
        }
    }
    
    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(ACCENT_COLOR);
        g2d.setStroke(new BasicStroke(2));
        
        // Draw x-axis
        g2d.drawLine(0, grille.getOriginY(), grille.getWidth(), grille.getOriginY());
        
        // Draw y-axis
        g2d.drawLine(grille.getOriginX(), 0, grille.getOriginX(), getHeight());
        
        // Draw x-axis arrow
        int arrowSize = 10;
        g2d.drawLine(grille.getWidth() - arrowSize, grille.getOriginY() - arrowSize, grille.getWidth(), grille.getOriginY());
        g2d.drawLine(grille.getWidth() - arrowSize, grille.getOriginY() + arrowSize, grille.getWidth(), grille.getOriginY());
        
        // Draw y-axis arrow
        g2d.drawLine(grille.getOriginX() - arrowSize, arrowSize, grille.getOriginX(), 0);
        g2d.drawLine(grille.getOriginX() + arrowSize, arrowSize, grille.getOriginX(), 0);
        
        // Draw axis labels and ticks
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // X-axis ticks and labels
        for (int i = -10; i <= 10; i++) {
            if (i == 0) continue; // Skip origin
            
            int x = grille.getOriginX() + i * grille.getGridSize();
            if (x >= 0 && x <= grille.getWidth()) {
                g2d.drawLine(x, grille.getOriginY() - 5, x, grille.getOriginY() + 5);
                String label = String.valueOf(i);
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x - labelWidth / 2, grille.getOriginY() + 20);
            }
        }
        
        // Y-axis ticks and labels
        for (int i = -10; i <= 10; i++) {
            if (i == 0) continue; // Skip origin
            
            int y = grille.getOriginY() - i * grille.getGridSize();
            if (y >= 0 && y <= getHeight()) {
                g2d.drawLine(grille.getOriginX() - 5, y, grille.getOriginX() + 5, y);
                String label = String.valueOf(i);
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, grille.getOriginX() - labelWidth - 10, y + 5);
            }
        }
        
        // Draw origin label
        g2d.drawString("0", grille.getOriginX() - 15, grille.getOriginY() + 15);
    }
}
