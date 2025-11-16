package view;

import model.CellState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class CellView extends JPanel {
    private Image _segmentImage;
    private boolean _isClickable = true;
    private CellState _cellState = CellState.EMPTY;
    private Point _cellPosition;

    public CellView(Point cellPosition) {
        _cellPosition = cellPosition;
        setPreferredSize(new Dimension(50, 50));
        setBackground(Color.CYAN);
    }

    /**
     * Defines the segment of the ship to display.
     * @param segmentImage path to the image.
     */
    public void setSegmentImage(Image segmentImage) {
        this._segmentImage = segmentImage;
        repaint();
    }

    public void setShipSegment(Image image) {
        // Charge l'image correspondant au segment index selon le type et l'orientation
        setSegmentImage(image);
        _cellState = CellState.SHIP_SEGMENT;
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

    public Point getCellPosition() {
        return _cellPosition;
    }
}
