package view;

import javax.swing.*;

import model.cell.CellState;

import java.awt.*;
import java.awt.event.MouseListener;

public class CellView extends JLabel {  
    private static final ImageIcon IMG_SHIP =
        new ImageIcon(CellView.class.getResource("/images/ship.png")); 
    private static final ImageIcon IMG_HIT =
            new ImageIcon(CellView.class.getResource("/images/hit.png"));
    private static final ImageIcon IMG_MISS =
            new ImageIcon(CellView.class.getResource("/images/miss.png"));
    private static final ImageIcon IMG_SUNK =
            new ImageIcon(CellView.class.getResource("/images/sunk.png"));
    private static final ImageIcon IMG_WATER =
            new ImageIcon(CellView.class.getResource("/images/water.png"));

    private boolean _isClickable = true;
    private final Point _cellPosition;

    public CellView(Point cellPosition, int cellSize) {
        _cellPosition = cellPosition;
        setIcon(IMG_WATER);
        setPreferredSize(new Dimension(cellSize, cellSize));
    }

    /**
     * Sets the icon of the cell according to its state.
     */
    public void setCellIcon(CellState state) {
        switch (state) {
            case SHIP:
                setIcon(IMG_SHIP);
                break;
            case WATER:
                setIcon(IMG_WATER);
                break;
            case MISS:
                setIcon(IMG_MISS);
                break;
            case HIT:
                setIcon(IMG_HIT);
                break;
            case SUNK:
                setIcon(IMG_SUNK);
                break;
            default:
                setIcon(IMG_WATER);
                break;
        }
        revalidate();
        repaint();
    }

    /** 
     * Paints the border of the cell.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
