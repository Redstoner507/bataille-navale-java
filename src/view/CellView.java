package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class CellView extends JPanel {
    private Image _segmentImage;
    private boolean _isClickable = true;

    public CellView() {
        setPreferredSize(new Dimension(40, 40));
        setBackground(Color.CYAN);
    }

    /**
     * Defines the segment of the ship to display.
     * @param imagePath path to the image.
     */
    public void setSegmentImage(String imagePath) {
        if (imagePath != null) {
            this._segmentImage = new ImageIcon(imagePath).getImage();
        } else {
            this._segmentImage = null;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_segmentImage != null)
            g.drawImage(_segmentImage, 0, 0, getWidth(), getHeight(), null);
        else
            g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());
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
}
