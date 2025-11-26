package view;

import javax.swing.*;

import model.cell.CellState;

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

    private Image _currentImage = IMG_WATER;
    private boolean _isClickable = true;
    private final Point _cellPosition;

    public CellView(Point cellPosition, int cellSize) {
        _cellPosition = cellPosition;
        setPreferredSize(new Dimension(cellSize, cellSize));
    }

    /**
     * Sets the icon of the cell according to its state.
     */
    public void setCellIcon(CellState state) {
        switch (state) {
            case SHIP -> _currentImage = IMG_SHIP;
            case WATER -> _currentImage = IMG_WATER;
            case MISS -> _currentImage = IMG_MISS;
            case HIT -> _currentImage = IMG_HIT;
            case SUNK -> _currentImage = IMG_SUNK;
            case ISLAND -> _currentImage = IMG_ISLAND;
            case ISLAND_HIT -> _currentImage = IMG_ISLAND_HIT;
            default -> _currentImage = IMG_WATER;
        }
        revalidate();
        repaint();
    }

    /** 
     * Paints the cell and scales the image to fill the label.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner l'image redimensionnée pour remplir le JLabel
        g.drawImage(_currentImage, 0, 0, getWidth(), getHeight(), this);

        // bordure
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }

    /**
     * Overrides addMouseListener to only detect clicks when clickable.
     */
    @Override
    public void addMouseListener(MouseListener l) {
        if (_isClickable)
            super.addMouseListener(l);
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
