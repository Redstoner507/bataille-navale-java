package view;

import javax.swing.*;
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
        //_cellSize = cellSize;
        setPreferredSize(new Dimension(cellSize, cellSize));
    }

    /**
     * Defines the segment of the ship to display.
     */
    public void setSegmentImage() {
        //this._segmentImage = ShipImageProvider.getShipSegment(_cellSize);
        setIcon(IMG_SHIP);
        revalidate();
        repaint();
    }

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
