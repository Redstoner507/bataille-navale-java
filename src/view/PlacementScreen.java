package view;

import javax.swing.*;
import java.awt.*;

public class PlacementScreen extends JPanel {
    public JButton _fixedPlacementButton;
    public JButton _randomPlacementButton;
    public JButton _startGameButton;
    public JButton _backButton;
    public LabeledGridView _playerGrid;

    public PlacementScreen(int gridSize) {
        setLayout(new BorderLayout(10, 10));

        // Grille joueur
        _playerGrid = new LabeledGridView("Place your ships inside the grid", gridSize, false);
        add(_playerGrid, BorderLayout.CENTER);

        // Panneau boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        _fixedPlacementButton = new JButton("Fixed placement");
        _randomPlacementButton = new JButton("Random placement");
        _startGameButton = new JButton("Start game");
        _backButton = new JButton("Cancel");

        buttonPanel.add(_fixedPlacementButton);
        buttonPanel.add(_randomPlacementButton);
        buttonPanel.add(_startGameButton);
        buttonPanel.add(_backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getBackButton() {
        return _backButton;
    }
}
