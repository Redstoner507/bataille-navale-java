package view;

import controller.ConfigController;
import controller.PlacementController;
import model.Grid;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final JLabel _statusLabel; // Status du jeu

    public GameView(LabeledGridView playerGrid, int gridSize, int cellSize, PlacementController placementController, Grid computerGrid, ConfigController configController) {
        setLayout(new BorderLayout(10, 10));

        // Titre principal
        JLabel titleLabel = new JLabel("Bataille Navale", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // Panneau central contenant les 2 grilles
        JPanel gridsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        gridsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        playerGrid.setTitle("Your ships");
        GridView playerGridView = playerGrid.getGridView();
        playerGridView.setLocked(true);
        LabeledGridView computerLabeledGridView = new LabeledGridView("Enemy ships", gridSize, cellSize);
        GridView computerGridView = computerLabeledGridView.getGridView();
        placementController.randomShipsPlacement(computerGrid, computerGridView, configController.getShipConfig());
        computerGridView.clearGrid();

        gridsPanel.add(playerGrid);
        gridsPanel.add(computerLabeledGridView);
        add(gridsPanel, BorderLayout.CENTER);

        // Label d'état
        _statusLabel = new JLabel("Attack the other enemy's grid.", SwingConstants.CENTER);
        _statusLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        _statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(_statusLabel, BorderLayout.SOUTH);
    }

    public void setStatus(String text) {
        _statusLabel.setText(text);
    }
}
