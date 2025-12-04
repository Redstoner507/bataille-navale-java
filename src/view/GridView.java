package view;

import controller.AttackListener;
import controller.ShipPlacementListener;
import model.AttackResult;
import model.Grid;
import model.cell.Cell;
import model.cell.CellState;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GridView extends JPanel {
    private final int _gridSize;        // Taille de la grille
    private final CellView[][] _cells;  // Cases de la grille
    private boolean _locked = false;    // Etat verrouillé
    private ShipPlacementListener _placementListener;
    private AttackListener _attackListener;

    public GridView(int gridSize, int cellSize, Grid grid) {
        this._gridSize = gridSize;
        this._cells = new CellView[gridSize][gridSize];

        setLayout(new BorderLayout());
        add(createGridPanel(cellSize, grid), BorderLayout.CENTER);
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
                CellView cell = getCellView(cellSize, row, col, grid);
                _cells[row][col] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    private CellView getCellView(int cellSize, int row, int col, Grid grid) {
        Cell cell = grid.getCell(row, col);
        CellView cellView = new CellView(new Point(row, col), cellSize, cell.getType());
        cellView.setPreferredSize(new Dimension(50, 50)); // taille carrée
        cellView.setBorder(new LineBorder(Color.BLACK)); // quadrillage
        cellView.setOpaque(true);
        cellView.setFocusable(false); // désactivée par défaut
        final int cellRow = row;
        final int cellCol = col;
        cellView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 1. Vérifier si la grille n'est pas verrouillée et si la case est cliquable (focus/enabled)
                if (!_locked && cellView.isFocusable() && e.getButton() == MouseEvent.BUTTON1) {
                    // 2. Appeler la méthode de la GridView pour informer le Contrôleur
                      notifyCellAttacked(cellRow, cellCol);
                }
            }
        });
        return cellView;
    }

    /**
     * Updates the appearance of a cell according to the result of an attack.
     * @param result The result of the attack, contains the status, and the coordinates of the cell affected by the attack.
     */
    public void updateCellDisplay(AttackResult result, int row, int col) {
        CellView cell = _cells[row][col];

        switch (result.getStatus()) {
            case HIT -> {
                if (cell.getCellState() != CellState.SUNK) cell.setCellIcon(CellState.HIT);
            }
            case SUNK -> cell.setCellIcon(CellState.SUNK);

            case MISS, ALREADY_VISITED -> cell.setCellIcon(CellState.MISS);

            case ITEM_FOUND, ISLAND_MISS -> cell.setCellIcon(CellState.ISLAND_HIT);

            case SONAR_RESULT -> {
                cell.displaySonarScan(result.getSonarCount());
                return;
            }
        }
        // La cellule n'est plus cliquable après avoir été attaquée.
        cell.setFocusable(false);
        for (MouseListener listener : cell.getMouseListeners()) {
            cell.removeMouseListener(listener);
        }
    }

    /**
     * Activates or deactivates the entire grid.
     */
    public void setLocked(boolean locked) {
        _locked = locked;
        for (int row = 0; row < _gridSize; row++) {
            for (int col = 0; col < _gridSize; col++) {
                _cells[row][col].setFocusable(!locked);
            }
        }
    }

    /**
     * Activates or deactivates a single cell.
     */
    public void setCellEnabled(int row, int col, boolean enabled) {
        if (row < 0 || row >= _gridSize || col < 0 || col >= _gridSize) {
            return;
        }
        _cells[row][col].setFocusable(enabled);
    }

    /**
     * Adds a click listener to every cell.
     */
    public void addCellClickListener(MouseListener listener) {
        for (int row = 0; row < _gridSize; row++) {
            for (int col = 0; col < _gridSize; col++) {
                _cells[row][col].addMouseListener(listener);
            }
        }
    }

    public void setShipPlacementListener(ShipPlacementListener listener) {
        this._placementListener = listener;
    }

    /**
     * Called by ShipView when a ship is dropped.
     */
    public boolean notifyShipDropped(ShipView shipView, int row, int col) {
        if (_placementListener != null) {
            return _placementListener.shipDropped(shipView, row, col);
        }
        return false;
    }

    public void setAttackListener(AttackListener listener) {
        this._attackListener = listener;
    }

    public void notifyCellAttacked(int row, int col) {
        if (_attackListener != null) {
            _attackListener.onCellAttacked(row, col);
        }
    }

    public CellView getCell(int row, int col) {
        return _cells[row][col];
    }

    public int getGridSize() {
        return _gridSize;
    }

    public boolean isLocked() {
        return _locked;
    }

    public void clearShips() {
        for (int r = 0; r < _gridSize; r++) {
            for (int c = 0; c < _gridSize; c++) {
                if (_cells[r][c].getCellState() == CellState.SHIP) {
                    _cells[r][c].setCellIcon(CellState.WATER);
                }
            }
        }
    }

    public void hideGrid() {
        for (int r = 0; r < _gridSize; r++) {
            for (int c = 0; c < _gridSize; c++) {
                switch (_cells[r][c].getCellType()) {
                    case WATER -> _cells[r][c].setCellIcon(CellState.WATER);
                    case ISLAND -> _cells[r][c].setCellIcon(CellState.ISLAND);
                }
            }
        }
    }
}
