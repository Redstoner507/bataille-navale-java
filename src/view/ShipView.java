package view;

import model.Orientation;
import model.ship.Ship;
import utils.ShipImageProvider;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShipView extends JPanel {
    private final Ship _ship;
    private final LabeledGridView _grid;
    private final JLayeredPane _topLayer;
    private final JPanel _originalPanel;
    private final int _cellSize;
    private Orientation _orientation;
    private boolean _dragging = false;
    private Point _dragOffset;
    private final JPanel _segmentPanel = new JPanel();

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
        JLabel title = new JLabel(ship.getName(), SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // panel des segments
        _segmentPanel.setOpaque(false);
        _segmentPanel.setLayout(new BoxLayout(_segmentPanel, BoxLayout.X_AXIS));
        add(_segmentPanel);

        loadSegments();
        updateSize();
        initDragAndDrop();
    }

    private void loadSegments() {
        _segmentPanel.removeAll();
        _segmentPanel.setLayout(new BoxLayout(_segmentPanel,
                _orientation == Orientation.HORIZONTAL ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));

        for (int i = 0; i < _ship.getSize(); i++) {
            Image img = ShipImageProvider.getShipSegment(_cellSize);
            JLabel seg = new JLabel(new ImageIcon(img));
            seg.setPreferredSize(new Dimension(_cellSize, _cellSize));
            seg.setMinimumSize(new Dimension(_cellSize, _cellSize));
            seg.setMaximumSize(new Dimension(_cellSize, _cellSize));
            seg.setBorder(new LineBorder(Color.BLACK));
            seg.setAlignmentX(Component.CENTER_ALIGNMENT);
            seg.setAlignmentY(Component.CENTER_ALIGNMENT);
            _segmentPanel.add(seg);
        }

        _segmentPanel.revalidate();
        _segmentPanel.repaint();
    }

    private void updateSize() {
        revalidate();
        repaint();
    }

    public void setOrientation(Orientation orientation) {
        this._orientation = orientation;
        _ship.setOrientation(orientation);
        loadSegments(); // reconstruit les segments correctement
        updateSize();
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
                        } catch (IllegalComponentStateException ignored) {
                        }
                    }
                    if (foundRow != -1) break;
                }

                _topLayer.remove(ShipView.this);

                boolean placed = false;
                if (foundRow != -1) {
                    placed = gridView.notifyShipDropped(ShipView.this, foundRow, foundCol);
                }

                if (!placed) {
                    _originalPanel.add(ShipView.this);
                    _originalPanel.revalidate();
                    _originalPanel.repaint();
                }
                _topLayer.revalidate();
                _topLayer.repaint();
                gridView.revalidate();
                gridView.repaint();
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    @Override public Dimension getPreferredSize() {
        int segW = _orientation == Orientation.HORIZONTAL ? _cellSize * _ship.getSize() : _cellSize;
        int segH = _orientation == Orientation.HORIZONTAL ? _cellSize : _cellSize * _ship.getSize();

        // Taille du titre
        Component title = getComponent(0); // le JLabel
        Dimension titleSize = title.getPreferredSize();

        // Largeur totale : max entre segment et titre
        int totalW = Math.max(segW, titleSize.width); //85

        // Hauteur totale : titre + segments + marge
        int totalH = titleSize.height + segH + 5;

        return new Dimension(totalW, totalH);
    }

    public Ship getShip() {
        return _ship;
    }
}
