package view;

import javax.swing.*;

import model.cell.CellState;
import model.cell.CellType;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Objects;

public class CellView extends JLabel {  
    private static final Image IMG_SHIP =
        new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/ship.png"))).getImage();
    private static final Image IMG_HIT =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/hit.png"))).getImage();
    private static final Image IMG_MISS =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/miss.png"))).getImage();
    private static final Image IMG_SUNK =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/sunk.png"))).getImage();
    private static final Image IMG_WATER =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/water.png"))).getImage();
    private static final Image IMG_ISLAND =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/island.png"))).getImage();
    private static final Image IMG_ISLAND_HIT =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/island_hit.png"))).getImage();

    private Image _currentImage;
    private boolean _isClickable = true;
    private final Point _cellPosition;
    private CellState _cellState;
    private final CellType _cellType;
    private int _sonarCountToDisplay = -1;

    public CellView(Point cellPosition, int cellSize, CellType cellType) {
        _cellPosition = cellPosition;
        _cellType = cellType;
        setCellIcon(CellState.EMPTY);
        setPreferredSize(new Dimension(cellSize, cellSize));
    }

    /**
     * Sets the icon of the cell according to its state.
     */
    public void setCellIcon(CellState state) {
        switch (state) {
            case EMPTY -> {
                if (_cellType == CellType.ISLAND) _currentImage = IMG_ISLAND;
                else _currentImage = IMG_WATER;
            }
            case SHIP -> _currentImage = IMG_SHIP;
            case MISS -> _currentImage = IMG_MISS;
            case HIT -> _currentImage = IMG_HIT;
            case SUNK -> _currentImage = IMG_SUNK;
            case ISLAND_HIT -> _currentImage = IMG_ISLAND_HIT;
        }
        _cellState = state;
        revalidate();
        repaint();
    }

    public void displaySonarScan(int sonarCount) {
        _sonarCountToDisplay = sonarCount;
        revalidate();
        repaint();
    }

    /** 
     * Paints the cell and scales the image to fill the label.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Dessiner l'image de fond
        g2d.drawImage(_currentImage, 0, 0, getWidth(), getHeight(), this);
        switch (_cellType) {
            case WATER -> setBackground(Color.CYAN);
            case ISLAND -> setBackground(Color.ORANGE);
        }

        // --- Dessin du Compteur Sonar ---
        // Afficher le chiffre si un scan a été effectué (count >= 0)
        if (_sonarCountToDisplay >= 0) {
            String text = String.valueOf(_sonarCountToDisplay);
            // Configuration du texte
            g2d.setFont(new Font("Arial", Font.BOLD, getHeight() / 2));
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // Calcul du centrage
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            // Dessin du nombre au centre
            g2d.drawString(text, x, y);
        }
        // bordure
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /**
     * Overrides addMouseListener to only detect clicks when clickable.
     */
    @Override
    public void addMouseListener(MouseListener l) {
        if (_isClickable)
            super.addMouseListener(l);
    }

    public CellState getCellState() {
        return _cellState;
    }

    public CellType getCellType() {
        return _cellType;
    }

    public void setClickable(boolean clickable) {
        this._isClickable = clickable;
    }

    public boolean isClickable() {
        return _isClickable;
    }

    public Point getCellPosition() {
        return _cellPosition;
    }
}
