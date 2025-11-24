package view;

import javax.swing.*;
import java.awt.*;

public class LabeledGridView extends JPanel {
    private final JLabel _titleLabel;   // Label du titre
    private final GridView _gridView;   // Grille

    public LabeledGridView(String title, int gridSize, int cellSize) {
        _titleLabel = new JLabel(title, SwingConstants.CENTER);
        _gridView = new GridView(gridSize, cellSize);
        setupComponents();
    }

    public LabeledGridView(String title, int gridSize, int cellSize, boolean locked) {
        this(title, gridSize, cellSize);
        _gridView.setLocked(locked);
    }

    /**
     * Creates the title of the grid and adds the title and the grid to the panel.
     */
    private void setupComponents() {
        setLayout(new BorderLayout());

        // Panneau supérieur pour le titre
        JPanel titlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        // Espace vide à gauche pour aligner le titre avec la grille
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        titlePanel.add(Box.createHorizontalStrut(30), gbc); // largeur ajustable selon taille des cases

        // Label du titre centré sur les colonnes
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        _titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(_titleLabel, gbc);

        add(titlePanel, BorderLayout.NORTH);

        // Grille
        add(_gridView, BorderLayout.CENTER);
    }

    public GridView getGridView() {
        return _gridView;
    }

    public void setTitle(String title) {
        _titleLabel.setText(title);
    }
}
