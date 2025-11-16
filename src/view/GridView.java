package view;

import controller.GridActionListener;
import model.Grid;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseListener;

public class GridView extends JPanel {
    private final int _gridSize;        // Taille de la grille
    private final CellView[][] _cells;  // Cases de la grille
    private boolean _locked = false;    // Etat verrouillé
    private final Grid _grid;           // Grille concrète
    private GridActionListener _listener;

    public GridView(int gridSize) {
        this._gridSize = gridSize;
        this._cells = new CellView[gridSize][gridSize];
        this._grid = new Grid(gridSize);

        setLayout(new BorderLayout());
        add(createGridPanel(), BorderLayout.CENTER);
    }

    /**
     * Creates the main grid panel with column and row headers.
     * @return JPanel
     */
    private JPanel createGridPanel() {
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
                CellView cell = new CellView(new Point(row, col));
                cell.setPreferredSize(new Dimension(50, 50)); // taille carrée
                cell.setBorder(new LineBorder(Color.BLACK)); // quadrillage
                cell.setEnabled(false); // désactivée par défaut
                _cells[row][col] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    /**
     * Activates or deactivates the entire grid.
     */
    public void setLocked(boolean locked) {
        _locked = locked;
        for (int r = 0; r < _gridSize; r++) {
            for (int c = 0; c < _gridSize; c++) {
                _cells[r][c].setEnabled(!locked);
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
        _cells[row][col].setEnabled(enabled);
    }

    /**
     * Adds a click listener to every cell.
     */
    public void addCellClickListener(MouseListener listener) {
        for (int r = 0; r < _gridSize; r++) {
            for (int c = 0; c < _gridSize; c++) {
                _cells[r][c].addMouseListener(listener);
            }
        }
    }

    public void setGridActionListener(GridActionListener listener) {
        this._listener = listener;
    }

    /**
     * Called by ShipView when a ship is dropped.
     */
    public boolean notifyShipDropped(ShipView shipView, int row, int col) {
        if (_listener != null) {
            return _listener.shipDropped(shipView, row, col);
        }
        return false;
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

    public Grid getGrid() {
        return _grid;
    }

}
