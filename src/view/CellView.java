package view;

import model.CellState;
import utils.ShipImageProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class CellView extends JPanel {
    private Image _segmentImage;
    private boolean _isClickable = true;
    private CellState _cellState = CellState.EMPTY;
    private final Point _cellPosition;
    private final int _cellSize;

    public CellView(Point cellPosition, int cellSize) {
        _cellPosition = cellPosition;
        _cellSize = cellSize;
        setPreferredSize(new Dimension(cellSize, cellSize));
        setBackground(Color.CYAN);
    }

    /**
     * Defines the segment of the ship to display.
     */
    public void setSegmentImage() {
        this._segmentImage = ShipImageProvider.getShipSegment(_cellSize);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_segmentImage != null) {
            g.drawImage(_segmentImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
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

    public CellState getState() {
        return _cellState;
    }

    public void setState(CellState state) {
        _cellState = state;
    }

    public Point getCellPosition() {
        return _cellPosition;
    }
}
