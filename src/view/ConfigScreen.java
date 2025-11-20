package view;

import javax.swing.*;

import controller.ConfigController;
import controller.GameController;

import java.awt.*;

public class ConfigScreen extends JPanel {
    private static final int MAX_OCCUPIABLE_CELLS = 35;
    private final JSpinner _gridSizeSpinner;
    private final JSpinner _carrierSpinner, _battleshipSpinner, _cruiserSpinner, _submarineSpinner, _destroyerSpinner;
    private final JCheckBox _islandCheckBox;
    private final JButton _backButton;
    private final JButton _startPlacementButton;
    private final JLabel _occupiedCellsLabel;
    private final GameController _gameController;

    public ConfigScreen(GameController gameController, ConfigController configController) {
        _gameController = gameController;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Game configuration", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);
        add(Box.createVerticalStrut(20));

        // Taille de la grille
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gridPanel.add(new JLabel("Grid size :"));
        _gridSizeSpinner = new JSpinner(new SpinnerNumberModel(10, 6, 10, 1));
        gridPanel.add(_gridSizeSpinner);
        add(gridPanel);

        _gridSizeSpinner.addChangeListener(e -> {
            int gridSize = (int) _gridSizeSpinner.getValue();
            configController.setGridSize(gridSize);
        });

        // Nombre de bateaux
        _occupiedCellsLabel = new JLabel();
        _occupiedCellsLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(_occupiedCellsLabel);

        _carrierSpinner = createShipSpinner("Carrier (5 cells) : ");
        _battleshipSpinner = createShipSpinner("Battleship (4 cells) : ");
        _cruiserSpinner = createShipSpinner("Cruiser (3 cells) : ");
        _submarineSpinner = createShipSpinner("Submarine (3 cells) : ");
        _destroyerSpinner = createShipSpinner("Destroyer (2 cells) : ");

        updateOccupiedCellsLabel(getOccupiedCellsCount());

        // Mode île
        JPanel islandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        _islandCheckBox = new JCheckBox("Island mode");
        _islandCheckBox.setSelected(false);
        islandPanel.add(_islandCheckBox);
        add(islandPanel);

        add(Box.createVerticalStrut(20));

        // Boutons
        _backButton = new JButton("Back");
        _backButton.addActionListener(e -> _gameController.showStartScreen());

        _startPlacementButton = new JButton("Confirm");
        _startPlacementButton.addActionListener(e -> _gameController.startPlacementScreen());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(_backButton);
        buttonPanel.add(_startPlacementButton);
        
        add(buttonPanel);
    }

    private JSpinner createShipSpinner(String label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        spinner.addChangeListener(e -> {
            int occupiedCells = getOccupiedCellsCount();
            updateOccupiedCellsLabel(occupiedCells);
            if (occupiedCells > MAX_OCCUPIABLE_CELLS) {
                spinner.setValue(spinner.getPreviousValue());
            }
        });
        panel.add(spinner);
        add(panel);
        return spinner;
    }

    private int getOccupiedCellsCount() {
        return (int) _carrierSpinner.getValue() * 5
                + (int) _battleshipSpinner.getValue() * 4
                + (int) _cruiserSpinner.getValue() * 3
                + (int) _submarineSpinner.getValue() * 3
                + (int) _destroyerSpinner.getValue() * 2;
    }

    private void updateOccupiedCellsLabel(int occupiedCells) {
        _occupiedCellsLabel.setText("Occupied cells : " + occupiedCells + " / " + MAX_OCCUPIABLE_CELLS);
    }
}
