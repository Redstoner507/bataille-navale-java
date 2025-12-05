package view;

import javax.swing.*;

import controller.ConfigController;
import controller.GameController;
import model.ship.ShipType;

import java.awt.*;

public class ConfigScreen extends JPanel {
    private static final int MAX_OCCUPIABLE_CELLS = 35;
    private final JSpinner _gridSizeSpinner;
    private final JSpinner _carrierSpinner, _battleshipSpinner, _cruiserSpinner, _submarineSpinner, _destroyerSpinner;
    private final JLabel _occupiedCellsLabel;

    public ConfigScreen(GameController gameController, ConfigController configController) {
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

        // Nombre de bateaux
        _occupiedCellsLabel = new JLabel();
        _occupiedCellsLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(_occupiedCellsLabel);

        _carrierSpinner = createShipSpinner(configController, ShipType.CARRIER, "Carrier (5 cells) : ");
        _battleshipSpinner = createShipSpinner(configController, ShipType.BATTLESHIP, "Battleship (4 cells) : ");
        _cruiserSpinner = createShipSpinner(configController, ShipType.CRUISER, "Cruiser (3 cells) : ");
        _submarineSpinner = createShipSpinner(configController, ShipType.SUBMARINE, "Submarine (3 cells) : ");
        _destroyerSpinner = createShipSpinner(configController, ShipType.DESTROYER, "Destroyer (2 cells) : ");

        updateOccupiedCellsLabel(getOccupiedCellsCount());

        // Mode île
        JPanel islandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox islandCheckBox = new JCheckBox("Island mode");
        islandCheckBox.setSelected(false);
        islandPanel.add(islandCheckBox);
        add(islandPanel);

        // Niveau d'IA
        String[] options = {"Very Easy", "Easy", "Medium", "Hard", "Very Hard"};
        JComboBox<String> aiLevelBox = new JComboBox<>(options);
        JPanel aiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        aiPanel.add(new JLabel("Computer AI Level: "));
        aiPanel.add(aiLevelBox);
        add(aiPanel);

        add(Box.createVerticalStrut(20));

        // Boutons
        JButton backButton = new JButton("Back");

        JButton startPlacementButton = new JButton("Confirm");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(startPlacementButton);
        
        add(buttonPanel);

        // Listeners

        _gridSizeSpinner.addChangeListener(e -> {
            int gridSize = (int) _gridSizeSpinner.getValue();
            configController.setGridSize(gridSize);
            if (gridSize < 10) {
                islandCheckBox.setSelected(false);
            }
        });

        islandCheckBox.addActionListener(e -> {
            configController.setIslandModeEnabled(islandCheckBox.isSelected());
            if (islandCheckBox.isSelected()) {
                configController.setGridSize(10);
                _gridSizeSpinner.setValue(10);
            }
        });

        aiLevelBox.addActionListener(e -> {
            String selectedLevel = (String) aiLevelBox.getSelectedItem();
            configController.setPlayerStrategy(selectedLevel);
        });

        backButton.addActionListener(e -> gameController.showStartScreen());
        startPlacementButton.addActionListener(e -> gameController.showPlacementScreen());
    }

    private JSpinner createShipSpinner(ConfigController configController, ShipType shipType, String label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        spinner.addChangeListener(e -> {
            int occupiedCells = getOccupiedCellsCount();
            updateOccupiedCellsLabel(occupiedCells);
            if (occupiedCells > MAX_OCCUPIABLE_CELLS) {
                spinner.setValue(spinner.getPreviousValue());
            }
            else {
                configController.setShipNumber(shipType, (int) spinner.getValue());
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
