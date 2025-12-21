package view;

import controller.AttackListener;
import controller.ObjectPlacementListener;
import model.Grid;
import model.GridListener;
import model.Placeable;
import model.cell.Cell;
import model.cell.CellState;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class GridView extends JPanel implements GridListener {
    private final int _gridSize;        // Taille de la grille
    private final CellView[][] _cells;  // Cases de la grille
    private boolean _locked = false;    // Etat verrouillé

    // Callbacks pour le contrôleur
    private ObjectPlacementListener _placementListener;
    private AttackListener _attackListener;
    private Consumer<Placeable> _removePlaceableListener;

    public GridView(int gridSize, int cellSize, Grid grid) {
        this._gridSize = gridSize;
        this._cells = new CellView[gridSize][gridSize];

        setLayout(new BorderLayout());
        add(createGridPanel(cellSize, grid), BorderLayout.CENTER);

        // abonnement au modèle
        grid.addListener(this);
    }

    @Override
    public void onCellUpdated(int row, int col, Cell cell) {
        // Mise à jour automatique quand le modèle change
        CellView view = _cells[row][col];

        // Vérification de sécurité avant de charger des segments de bateau
        if (cell.getObject() != null && cell.getObject().getPlaceableType() == model.PlaceableType.SHIP) {
            String shipName = cell.getObject().getName();

            if (shipName != null && !shipName.isEmpty()) {
                Placeable p = cell.getObject();
                int segmentIndex = (p.getOrientation() == model.Orientation.HORIZONTAL)
                        ? col - p.getStartCol()
                        : row - p.getStartRow();

                view.setShipInfo(shipName.toLowerCase(), segmentIndex,
                        p.getOrientation() == model.Orientation.HORIZONTAL ? 1 : 0);
            }
        }

        view.setCellIcon(cell.getState());
        view.repaint();
    }

    @Override
    public void onGridReset() {
        clearGrid();
    }

    /**
     * Creates the main grid panel with column and row headers.
     * @return JPanel
     */
    private JPanel createGridPanel(int cellSize, Grid grid) {
        JPanel panel = new JPanel(new GridLayout(_gridSize + 1, _gridSize + 1)); // +1 pour les labels

        // Première cellule vide (coin supérieur gauche)
        panel.add(new JLabel(" "));

        // En-têtes de colonnes : A, B, C, ...
        for (int col = 0; col < _gridSize; col++) {
            char letter = (char) ('A' + col);
            JLabel columnLabel = new JLabel(String.valueOf(letter), SwingConstants.CENTER);
            columnLabel.setFont(new Font("Arial", Font.BOLD, 14));
            columnLabel.setBorder(new LineBorder(Color.BLACK)); // bordure en haut
            panel.add(columnLabel);
        }

        // Lignes avec numéros + boutons
        for (int row = 0; row < _gridSize; row++) {
            // Numéro de ligne
            JLabel rowLabel = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rowLabel.setBorder(new LineBorder(Color.BLACK)); // bordure à gauche
            panel.add(rowLabel);

            // Cases de la grille
            for (int col = 0; col < _gridSize; col++) {
                CellView cell = new CellView(cellSize, grid.getCell(row, col).getType());
                cell.setBorder(new LineBorder(Color.BLACK));
                int r = row;
                int c = col;

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!_locked && e.getButton() == MouseEvent.BUTTON1) {
                            if (_attackListener != null) _attackListener.onCellAttacked(r, c);
                        }
                    }
                });

                _cells[row][col] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    public void displaySonarScan(int row, int col, int count) {
        _cells[row][col].displaySonarScan(count);
    }

    /**
     * Activates or deactivates the entire grid.
     */
    public void setLocked(boolean locked) {
        _locked = locked;
    }

    public void setObjectPlacementListener(ObjectPlacementListener listener) {
        this._placementListener = listener;
    }

    public void setAttackListener(AttackListener listener) {
        this._attackListener = listener;
    }

    public void setOnPlaceableRemoveListener(Consumer<Placeable> l) { this._removePlaceableListener = l; }

    /**
     * Called by ShipView when a ship is dropped.
     */
    public boolean notifyPlaceableDropped(PlaceableView placeableView, int row, int col) {
        if (_placementListener != null) {
            return _placementListener.objectDropped(placeableView, row, col);
        }
        return false;
    }

    public void removePlaceableVisuals(Placeable placeable) {
        if (_removePlaceableListener != null) _removePlaceableListener.accept(placeable);
    }

    public CellView getCell(int row, int col) {
        return _cells[row][col];
    }

    public int getGridSize() {
        return _gridSize;
    }

    public void clearGrid() {
        for (int r = 0; r < _gridSize; r++) {
            for (int c = 0; c < _gridSize; c++) {
                _cells[r][c].setCellIcon(CellState.EMPTY);
            }
        }
    }
}
