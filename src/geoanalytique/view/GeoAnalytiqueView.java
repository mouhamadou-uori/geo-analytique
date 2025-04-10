/*
 * GeoAnalytiqueView.java
 *
 * Created on 09 juin 2012, 03:11
 */

package geoanalytique.view;

import geoanalytique.graphique.Graphique; 

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Cette classe represente la zone a dessin pour afficher les objets geometriques
 * 
 */
// TODO (optionnel): modifier la classe de base, pour une classe plus adapte
public class GeoAnalytiqueView extends javax.swing.JPanel {
    private static final long serialVersionUID = -5516505527325580028L;
	private ArrayList<Graphique> graphiques;
    private static final int GRID_SIZE = 40;
        private int originX, originY;
    
    /** Creates new form GeoAnalytiqueView */
    public GeoAnalytiqueView() {
        initComponents();
        graphiques = new ArrayList<Graphique>();
        setOpaque(true);
        setBackground(new java.awt.Color(10, 15, 30));
        setBorder(null); // S'assurer qu'il n'y a pas de bordure
        
        // Ajouter un écouteur de redimensionnement
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Mettre à jour les coordonnées des axes lorsque le composant est redimensionné
                updateOrigin();
                repaint();
            }
        });
    }
    
    public void addGraphique(Graphique g) {
    	graphiques.add(g);
    }
    public void deleteLastGraphique(){
        graphiques.remove(graphiques.size()-1);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 255, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 349, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    public int getOriginX() {
        return originX;
    }
    
    public void setOriginX(int originX) {
        this.originX = originX;
    }
    
    public int getOriginY() {
        return originY;
    }
    
    public void setOriginY(int originY) {
        this.originY = originY;
    }
    public int getGridSize(){
        return GRID_SIZE;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Appeler la méthode parente pour dessiner le fond
        
        // Dessiner le fond sombre
        g.setColor(new java.awt.Color(10, 15, 30));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Dessiner la grille et les axes
        drawGrid(g);
        drawAxes(g);
        
        // Dessiner tous les graphiques ici, après le fond
        for (Graphique graphique : graphiques) {
            graphique.paint(g);
        }
    }
    
    public void clear() {
		graphiques.clear();
	}

	public void removeGraphique(Graphique c) {
		graphiques.remove(c);		
	}

    private void drawGrid(Graphics g) {
        g.setColor(new java.awt.Color(30, 40, 60)); // Couleur de la grille
        
        // Lignes horizontales
        for (int y = originY % GRID_SIZE; y < getHeight(); y += GRID_SIZE) {
            g.drawLine(0, y, getWidth(), y);
        }
        
        // Lignes verticales
        for (int x = originX % GRID_SIZE; x < getWidth(); x += GRID_SIZE) {
            g.drawLine(x, 0, x, getHeight());
        }
    }

    private void drawAxes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new java.awt.Color(0, 230, 230)); // Couleur des axes
        g2d.setStroke(new java.awt.BasicStroke(2)); // Épaisseur des axes
        
        // Axe X
        g.drawLine(0, originY, getWidth(), originY);
        
        // Axe Y
        g.drawLine(originX, 0, originX, getHeight());
        
        // Flèches des axes
        int arrowSize = 10;
        
        // Flèche de l'axe X
        g.drawLine(getWidth() - arrowSize, originY - arrowSize, getWidth(), originY);
        g.drawLine(getWidth() - arrowSize, originY + arrowSize, getWidth(), originY);
        
        // Flèche de l'axe Y
        g.drawLine(originX - arrowSize, arrowSize, originX, 0);
        g.drawLine(originX + arrowSize, arrowSize, originX, 0);
        
        // Ticks et labels des axes
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        
        // Ticks axe X
        for (int i = -10; i <= 10; i++) {
            if (i == 0) continue; // Sauter l'origine
            
            int x = originX + i * GRID_SIZE;
            if (x >= 0 && x <= getWidth()) {
                g.drawLine(x, originY - 5, x, originY + 5);
                String label = String.valueOf(i);
                java.awt.FontMetrics fm = g.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g.drawString(label, x - labelWidth / 2, originY + 20);
            }
        }
        
        // Ticks axe Y
        for (int i = -10; i <= 10; i++) {
            if (i == 0) continue; // Sauter l'origine
            
            int y = originY - i * GRID_SIZE;
            if (y >= 0 && y <= getHeight()) {
                g.drawLine(originX - 5, y, originX + 5, y);
                String label = String.valueOf(i);
                java.awt.FontMetrics fm = g.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g.drawString(label, originX - labelWidth - 10, y + 5);
            }
        }
        
        // Label origine
        g.drawString("0", originX - 15, originY + 15);
    }
    
    /**
     * Mettre à jour la position de l'origine du repère au centre du composant
     */
    private void updateOrigin() {
        originX = getWidth() / 2;
        originY = getHeight() / 2;
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        // Assurer que l'origine est définie après que le composant soit ajouté à la hiérarchie
        updateOrigin();
    }
}
