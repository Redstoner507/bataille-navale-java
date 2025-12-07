package view;

import javax.swing.*;

import controller.ConfigController;
import controller.GameController;
import model.ship.ShipType;

import java.awt.*;

public class ConfigScreen extends JPanel {
    private static final int MAX_OCCUPIABLE_CELLS = 35;
    private final JSlider _gridSizeSlider;
    private final JSpinner _carrierSpinner, _battleshipSpinner, _cruiserSpinner, _submarineSpinner, _destroyerSpinner;
    private final JLabel _occupiedCellsLabel;
    private final JCheckBox _islandCheckBox;
    private final JComboBox<String> _computerLevelBox;

    public ConfigScreen(GameController gameController, ConfigController configController) {
        setLayout(new BorderLayout());

        // Conteneur pour le centrage
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        add(centerWrapper, BorderLayout.CENTER);

        // Conteneur principal pour tous les éléments de configuration
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Ajouter le contentPanel au GridBagLayout
        centerWrapper.add(contentPanel, new GridBagConstraints());

        // Titre
        JLabel title = new JLabel("Game Configuration", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(40));

        // Taille de la grille
        JPanel gridPanel = new JPanel();
        gridPanel.add(new JLabel("Grid Size :"));

        // JSlider pour la taille (6 à 10)
        _gridSizeSlider = new JSlider(SwingConstants.HORIZONTAL, 6, 10, 10);
        _gridSizeSlider.setMajorTickSpacing(1);
        _gridSizeSlider.setPaintTicks(true);
        _gridSizeSlider.setPaintLabels(true);
        _gridSizeSlider.setPreferredSize(new Dimension(200, 50));

        gridPanel.add(_gridSizeSlider);
        gridPanel.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(gridPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Nombre de bateaux
        // Étiquette des cellules occupées
        _occupiedCellsLabel = new JLabel();
        _occupiedCellsLabel.setAlignmentX(CENTER_ALIGNMENT);
        _occupiedCellsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Panneau pour les spinners de bateaux
        JPanel shipConfigPanel = new JPanel();
        shipConfigPanel.setLayout(new BoxLayout(shipConfigPanel, BoxLayout.Y_AXIS));

        // Panneau enveloppe pour centrer le BoxLayout horizontalement
        JPanel shipPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        shipPanelWrapper.add(shipConfigPanel);
        shipPanelWrapper.setAlignmentX(CENTER_ALIGNMENT);

        _occupiedCellsLabel.setAlignmentX(CENTER_ALIGNMENT);
        shipConfigPanel.add(_occupiedCellsLabel); // Ajout du label dans le panneau
        shipConfigPanel.add(Box.createVerticalStrut(10));

        _carrierSpinner = createShipSpinner(configController, ShipType.CARRIER, "Carrier (5 cells) : ", shipConfigPanel);
        _battleshipSpinner = createShipSpinner(configController, ShipType.BATTLESHIP, "Battleship (4 cells) : ", shipConfigPanel);
        _cruiserSpinner = createShipSpinner(configController, ShipType.CRUISER, "Cruiser (3 cells) : ", shipConfigPanel);
        _submarineSpinner = createShipSpinner(configController, ShipType.SUBMARINE, "Submarine (3 cells) : ", shipConfigPanel);
        _destroyerSpinner = createShipSpinner(configController, ShipType.DESTROYER, "Destroyer (2 cells) : ", shipConfigPanel);

        contentPanel.add(shipPanelWrapper);
        contentPanel.add(Box.createVerticalStrut(20));

        updateOccupiedCellsLabel(getOccupiedCellsCount());

        // Mode île et difficulté de l'ordinateur
        JPanel optionsPanel = new JPanel(new BorderLayout());
        JPanel verticalOptionsPanel = new JPanel();
        verticalOptionsPanel.setLayout(new BoxLayout(verticalOptionsPanel, BoxLayout.Y_AXIS));
        verticalOptionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Marge interne

        // Mode île
        _islandCheckBox = new JCheckBox("Island mode");
        _islandCheckBox.setAlignmentX(CENTER_ALIGNMENT);
        verticalOptionsPanel.add(_islandCheckBox);
        verticalOptionsPanel.add(Box.createVerticalStrut(10)); // Espacement

        // Difficulté de l'ordinateur
        String[] options = {"Very Easy", "Easy", "Medium", "Hard", "Very Hard"};
        _computerLevelBox = new JComboBox<>(options);

        JPanel computerDifficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        computerDifficultyPanel.add(new JLabel("Computer difficulty : "));
        computerDifficultyPanel.add(_computerLevelBox);
        computerDifficultyPanel.setAlignmentX(CENTER_ALIGNMENT);
        verticalOptionsPanel.add(computerDifficultyPanel);
        verticalOptionsPanel.add(Box.createVerticalStrut(10)); // Espacement

        optionsPanel.add(verticalOptionsPanel, BorderLayout.CENTER);
        optionsPanel.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(optionsPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Boutons
        JButton backButton = new JButton("Back");
        JButton startPlacementButton = new JButton("Confirm");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(startPlacementButton);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(buttonPanel);

        // Listeners
        _gridSizeSlider.addChangeListener(e -> {
            int gridSize = _gridSizeSlider.getValue();
            configController.setGridSize(gridSize);

            // Si la grille est < 10, désactiver le mode Île
            if (gridSize < 10 && _islandCheckBox.isSelected()) {
                _islandCheckBox.setSelected(false);
                configController.setIslandModeEnabled(false);
            }
        });

        _islandCheckBox.addActionListener(e -> {
            configController.setIslandModeEnabled(_islandCheckBox.isSelected());
            if (_islandCheckBox.isSelected()) {
                // Forcer la taille à 10 si le mode Île est activé
                configController.setGridSize(10);
                _gridSizeSlider.setValue(10);
            }
        });

        _computerLevelBox.addActionListener(e -> {
            String selectedLevel = (String) _computerLevelBox.getSelectedItem();
            configController.setPlayerStrategy(selectedLevel);
        });

        backButton.addActionListener(e -> gameController.showStartScreen());
        startPlacementButton.addActionListener(e -> gameController.showPlacementScreen());
    }

    private JSpinner createShipSpinner(ConfigController configController, ShipType shipType, String label, JPanel container) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(new JLabel(label));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        spinner.setPreferredSize(new Dimension(50, 25)); // Taille fixe pour l'alignement

        spinner.addChangeListener(e -> {
            int occupiedCells = getOccupiedCellsCount();
            updateOccupiedCellsLabel(occupiedCells);
            if (occupiedCells > MAX_OCCUPIABLE_CELLS) {
                // Annuler la modification
                spinner.setValue(spinner.getPreviousValue());
            }
            else {
                configController.setShipNumber(shipType, (int) spinner.getValue());
            }
            updateOccupiedCellsLabel(getOccupiedCellsCount()); // Mise à jour après validation
        });
        panel.add(spinner);
        container.add(panel);
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
        _occupiedCellsLabel.setForeground(occupiedCells >= MAX_OCCUPIABLE_CELLS ? Color.RED : Color.BLUE.darker());
    }
}
