package view;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final LabeledGridView _playerGrid;      // Grille du joueur
    private final LabeledGridView _computerGrid;    // Grille de l'ordinateur
    private final JLabel _statusLabel;              // Status du jeu

    public GameView(int gridSize) {
        setLayout(new BorderLayout(10, 10));

        // Titre principal
        JLabel titleLabel = new JLabel("Bataille Navale", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // Panneau central contenant les 2 grilles
        JPanel gridsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        gridsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        _playerGrid = new LabeledGridView("Your ships", gridSize, false);
        _computerGrid = new LabeledGridView("Enemy ships", gridSize);

        gridsPanel.add(_playerGrid);
        gridsPanel.add(_computerGrid);
        add(gridsPanel, BorderLayout.CENTER);

        // Label d'état
        _statusLabel = new JLabel("Place your ships inside the grid.", SwingConstants.CENTER);
        _statusLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        _statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(_statusLabel, BorderLayout.SOUTH);
    }

    public LabeledGridView getPlayerGridView() {
        return _playerGrid;
    }

    public LabeledGridView getComputerGridView() {
        return _computerGrid;
    }

    public void setStatus(String text) {
        _statusLabel.setText(text);
    }
}
