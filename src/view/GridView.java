package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GridView extends JPanel {
    private final int _gridSize;        // Taille de la grille
    private final JButton[][] _cells;   // Cases de la grille
    private boolean _locked = false;    // Etat verrouillé

    public GridView(int gridSize) {
        this._gridSize = gridSize;
        this._cells = new JButton[gridSize][gridSize];

        setLayout(new BorderLayout());
        add(createGridPanel(), BorderLayout.CENTER);
    }

    /**
     * Crée le panneau principal de la grille avec en-têtes de colonnes et lignes.
     * @return JPanel
     */
    private JPanel createGridPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(_gridSize + 1, _gridSize + 1)); // +1 pour les labels

        // Première cellule vide (coin supérieur gauche)
        panel.add(new JLabel(" "));

        // En-têtes de colonnes : A, B, C, ...
        for (int col = 0; col < _gridSize; col++) {
            char letter = (char) ('A' + col);
            JLabel label = new JLabel(String.valueOf(letter), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label);
        }

        // Lignes avec numéros + boutons
        for (int row = 0; row < _gridSize; row++) {
            // Numéro de ligne
            JLabel rowLabel = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(rowLabel);

            // Cases de la grille
            for (int col = 0; col < _gridSize; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(50, 50)); // taille carrée
                cell.setFocusPainted(false);
                cell.setEnabled(false); // désactivée par défaut
                _cells[row][col] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    /**
     * Active ou désactive la grille entière.
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
     * Active ou désactive une seule case.
     */
    public void setCellEnabled(int row, int col, boolean enabled) {
        if (row < 0 || row >= _gridSize || col < 0 || col >= _gridSize) {
            return;
        }
        _cells[row][col].setEnabled(enabled);
    }

    /**
     * Ajoute un écouteur de clics sur toutes les cases.
     */
    public void addCellClickListener(ActionListener listener) {
        for (int r = 0; r < _gridSize; r++) {
            for (int c = 0; c < _gridSize; c++) {
                _cells[r][c].addActionListener(listener);
            }
        }
    }

    /**
     * Renvoie la cellule à une position donnée.
     */
    public JButton getCell(int row, int col) {
        return _cells[row][col];
    }

    public int getGridSize() {
        return _gridSize;
    }

    public boolean isLocked() {
        return _locked;
    }
}
