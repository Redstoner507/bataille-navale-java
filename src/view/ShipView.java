package view;

import model.Orientation;
import model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ShipView extends JPanel {
    private final Ship _ship;
    private final LabeledGridView _grid;
    private final JLayeredPane _topLayer;
    private final JPanel _originalPanel;
    private final int _cellSize;
    private Orientation _orientation;
    private boolean _dragging = false;
    private Point _dragOffset;
    private final JPanel segmentPanel = new JPanel();

    public ShipView(Ship ship, LabeledGridView grid, JLayeredPane topLayer, JPanel originalPanel, int cellSize) {
        this._ship = ship;
        this._grid = grid;
        this._topLayer = topLayer;
        this._originalPanel = originalPanel;
        this._cellSize = cellSize;
        this._orientation = ship.getOrientation();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // titre du bateau
        JLabel title = new JLabel(ship.getType().name(), SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // panel des segments
        segmentPanel.setOpaque(false);
        segmentPanel.setLayout(new BoxLayout(segmentPanel, BoxLayout.X_AXIS));
        add(segmentPanel);

        loadSegments();
        adjustSize();
        initDragAndDrop();
    }

    private void adjustSize() {
        Component title = getComponent(0);
        int panelWidth = _orientation == Orientation.HORIZONTAL ? _cellSize * _ship.getSize() : _cellSize;
        int panelHeight = _orientation == Orientation.HORIZONTAL ? _cellSize : _cellSize * _ship.getSize();

        segmentPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        segmentPanel.setMaximumSize(new Dimension(panelWidth, panelHeight));
        segmentPanel.setMinimumSize(new Dimension(panelWidth, panelHeight));

        int shipWidth = Math.max(panelWidth, title.getPreferredSize().width);
        int shipHeight = panelHeight + title.getPreferredSize().height;
        Dimension shipDim = new Dimension(shipWidth, shipHeight);
        setPreferredSize(shipDim);
        setMaximumSize(shipDim);
        setMinimumSize(shipDim);

        revalidate();
        repaint();
    }

    private void loadSegments() {
        segmentPanel.removeAll();

        // Layout selon orientation
        segmentPanel.setLayout(new BoxLayout(segmentPanel,
                _orientation == Orientation.HORIZONTAL ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));

        for (int i = 0; i < _ship.getSize(); i++) {
            Image img = getSegmentImage();
            JLabel seg = new JLabel(new ImageIcon(img));
            seg.setPreferredSize(new Dimension(_cellSize, _cellSize));
            seg.setMinimumSize(new Dimension(_cellSize, _cellSize));
            seg.setMaximumSize(new Dimension(_cellSize, _cellSize));
            seg.setAlignmentX(Component.CENTER_ALIGNMENT);
            seg.setAlignmentY(Component.CENTER_ALIGNMENT);
            segmentPanel.add(seg);
        }

        // Ajuster la taille du ShipView selon l'orientation
        adjustSize();
    }

    private Image rotateImage90(Image img) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage rotated = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.translate(h / 2.0, w / 2.0);
        g2d.rotate(Math.toRadians(90));
        g2d.translate(-w / 2.0, -h / 2.0);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return rotated;
    }

    private void initDragAndDrop() {
        MouseAdapter ma = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                _dragging = true;

                // Calculer le centre du segment 0 en coordonnées locales
                _dragOffset = new Point(_cellSize / 2, _cellSize / 2);

                // Retirer du panel original et ajouter au topLayer
                Point loc = SwingUtilities.convertPoint(ShipView.this, new Point(0, 0), _topLayer);
                _originalPanel.remove(ShipView.this);
                _topLayer.add(ShipView.this, JLayeredPane.PALETTE_LAYER);
                setBounds(loc.x, loc.y, getWidth(), getHeight());
                _topLayer.moveToFront(ShipView.this);
                _topLayer.revalidate();
                _topLayer.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!_dragging) return;

                // Convertit la position du curseur dans le repère du topLayer
                Point newLoc = SwingUtilities.convertPoint(ShipView.this, e.getPoint(), _topLayer);

                // Applique l'offset centré sur le segment 0
                setLocation(newLoc.x - _dragOffset.x, newLoc.y - _dragOffset.y);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!_dragging) return;
                _dragging = false;

                Point screenPt = e.getLocationOnScreen();

                GridView gridView = _grid.getGridView();
                int foundRow = -1;
                int foundCol = -1;

                for (int row = 0; row < gridView.getGridSize(); row++) {
                    for (int col = 0; col < gridView.getGridSize(); col++) {
                        CellView cv = gridView.getCell(row, col);
                        try {
                            Point cellLoc = cv.getLocationOnScreen();
                            Rectangle rect = new Rectangle(cellLoc.x, cellLoc.y, cv.getWidth(), cv.getHeight());
                            if (rect.contains(screenPt)) {
                                foundRow = row;
                                foundCol = col;
                                break;
                            }
                        } catch (IllegalComponentStateException ignored) {}
                    }
                    if (foundRow != -1) break;
                }

                boolean placed = false;
                if (foundRow != -1) {
                    placed = gridView.notifyShipDropped(ShipView.this, foundRow, foundCol);
                }

                if (!placed) {
                    _originalPanel.add(ShipView.this);
                    _originalPanel.revalidate();
                    _originalPanel.repaint();
                    _topLayer.remove(ShipView.this);
                } else {
                    _topLayer.remove(ShipView.this);
                }

                _topLayer.revalidate();
                _topLayer.repaint();
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public Ship getShip() {
        return _ship;
    }

    public void setOrientation(Orientation orientation) {
        this._orientation = orientation;
        _ship.setOrientation(orientation);
        loadSegments(); // reconstruit les segments correctement
    }

    public Image getSegmentImage() {
        String path = "img/ship.png";
        Image img = new ImageIcon(path).getImage();
        if (_orientation == Orientation.VERTICAL) img = rotateImage90(img);
        return img;
    }
}
