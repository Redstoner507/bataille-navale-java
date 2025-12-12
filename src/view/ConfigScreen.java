package view;

import javax.swing.*;

import controller.ConfigController;
import controller.GameController;
import model.ship.ShipType;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class ConfigScreen extends JPanel {
    private static final int MAX_OCCUPIABLE_CELLS = 35;

    // Hauteur de référence pour calculer l'échelle
    private static final float BASE_HEIGHT = 800f;

    // Listes pour garder une référence vers les éléments à redimensionner
    private final ArrayList<JLabel> _allLabels = new ArrayList<>();
    private final ArrayList<JComponent> _allControls = new ArrayList<>(); // Boutons, Checkbox, Spinners...
    private final JLabel _titleLabel;

    // Composants
    private final JSlider _gridSizeSlider;
    private final JSpinner _carrierSpinner, _battleshipSpinner, _cruiserSpinner, _submarineSpinner, _destroyerSpinner;
    private final JLabel _occupiedCellsLabel;
    private final JCheckBox _islandCheckBox;
    private final JComboBox<String> _computerLevelBox;
    private final JButton _backButton, _confirmButton;

    public ConfigScreen(GameController gameController, ConfigController configController) {
        setLayout(new BorderLayout());

        // Conteneur pour le centrage
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        add(centerWrapper, BorderLayout.CENTER);

        // Conteneur principal pour tous les éléments de configuration
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        // Ajouter le contentPanel au GridBagLayout
        centerWrapper.add(contentPanel, new GridBagConstraints());

        // Titre
        _titleLabel = new JLabel("Game Configuration", SwingConstants.CENTER);
        _titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(_titleLabel);
        contentPanel.add(Box.createVerticalGlue());

        // Taille de la grille
        JPanel gridPanel = new JPanel();
        JLabel gridLabel = new JLabel("Grid Size :");
        _allLabels.add(gridLabel);
        gridPanel.add(gridLabel);

        // JSlider pour la taille (6 à 10)
        _gridSizeSlider = new JSlider(SwingConstants.HORIZONTAL, 6, 10, 10);
        _gridSizeSlider.setMajorTickSpacing(1);
        _gridSizeSlider.setPaintTicks(true);
        _gridSizeSlider.setPaintLabels(true);

        gridPanel.add(_gridSizeSlider);
        gridPanel.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(gridPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Nombre de bateaux
        // Étiquette des cellules occupées
        _occupiedCellsLabel = new JLabel();
        _occupiedCellsLabel.setAlignmentX(CENTER_ALIGNMENT);
        _allLabels.add(_occupiedCellsLabel);

        // Panneau pour les spinners de bateaux
        JPanel shipConfigPanel = new JPanel();
        shipConfigPanel.setLayout(new BoxLayout(shipConfigPanel, BoxLayout.Y_AXIS));

        // Panneau enveloppe pour centrer le BoxLayout horizontalement
        JPanel shipPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        shipPanelWrapper.add(shipConfigPanel);
        shipPanelWrapper.setAlignmentX(CENTER_ALIGNMENT);

        shipConfigPanel.add(_occupiedCellsLabel); // Ajout du label dans le panneau

        _carrierSpinner = createShipSpinner(configController, ShipType.CARRIER, "Carrier (5 cells) : ", shipConfigPanel);
        _battleshipSpinner = createShipSpinner(configController, ShipType.BATTLESHIP, "Battleship (4 cells) : ", shipConfigPanel);
        _cruiserSpinner = createShipSpinner(configController, ShipType.CRUISER, "Cruiser (3 cells) : ", shipConfigPanel);
        _submarineSpinner = createShipSpinner(configController, ShipType.SUBMARINE, "Submarine (3 cells) : ", shipConfigPanel);
        _destroyerSpinner = createShipSpinner(configController, ShipType.DESTROYER, "Destroyer (2 cells) : ", shipConfigPanel);

        contentPanel.add(shipPanelWrapper);
        contentPanel.add(Box.createVerticalGlue());

        updateOccupiedCellsLabel(getOccupiedCellsCount());

        // Mode île et difficulté de l'ordinateur
        JPanel verticalOptionsPanel = new JPanel();
        verticalOptionsPanel.setLayout(new BoxLayout(verticalOptionsPanel, BoxLayout.Y_AXIS));

        // Mode île
        _islandCheckBox = new JCheckBox("Island mode");
        _islandCheckBox.setAlignmentX(CENTER_ALIGNMENT);
        _allControls.add(_islandCheckBox);

        verticalOptionsPanel.add(_islandCheckBox);
        verticalOptionsPanel.add(Box.createVerticalStrut(5)); // Espacement

        // Difficulté de l'ordinateur
        String[] options = {"Very Easy", "Easy", "Medium", "Hard", "Very Hard"};
        _computerLevelBox = new JComboBox<>(options);
        _allControls.add(_computerLevelBox);

        JPanel computerDifficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel diffLabel = new JLabel("Computer difficulty : ");
        _allLabels.add(diffLabel);
        computerDifficultyPanel.add(diffLabel);
        computerDifficultyPanel.add(_computerLevelBox);
        computerDifficultyPanel.setAlignmentX(CENTER_ALIGNMENT);

        verticalOptionsPanel.add(computerDifficultyPanel);

        // Wrapper pour centrer le bloc d'options
        JPanel optionsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionsWrapper.add(verticalOptionsPanel);
        optionsWrapper.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(optionsWrapper);

        contentPanel.add(Box.createVerticalGlue());

        // Boutons
        _backButton = new JButton("Back");
        _confirmButton = new JButton("Confirm");
        _allControls.add(_backButton);
        _allControls.add(_confirmButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(_backButton);
        buttonPanel.add(_confirmButton);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Listeners
        setupListeners(gameController, configController);

        // Listener de redimensionnement
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateResponsiveLayout();
            }
        });
    }

    /**
     * Méthode appelée à chaque redimensionnement pour ajuster les tailles de police.
     */
    private void updateResponsiveLayout() {
        int height = getHeight();
        if (height == 0) return;

        // Calcul du ratio par rapport à la hauteur de référence
        // On limite le ratio entre 0.6 (très petit) et 2.0 (très grand) pour rester lisible
        float ratio = Math.min(Math.max((float) height / BASE_HEIGHT, 0.6f), 2.5f);

        // Tailles de base (en pixels)
        int baseTitleSize = 40;
        int baseTextSize = 18;
        int baseSubTitleSize = 22;

        // Calcul des nouvelles tailles
        Font titleFont = new Font("Arial", Font.BOLD, (int) (baseTitleSize * ratio));
        Font regularFont = new Font("Arial", Font.PLAIN, (int) (baseTextSize * ratio));
        Font boldFont = new Font("Arial", Font.BOLD, (int) (baseTextSize * ratio));
        Font subtitleFont = new Font("Arial", Font.BOLD, (int) (baseSubTitleSize * ratio));

        // Appliquer au Titre
        _titleLabel.setFont(titleFont);

        // Appliquer aux Labels standards
        for (JLabel lbl : _allLabels) {
            lbl.setFont(regularFont);
        }
        _occupiedCellsLabel.setFont(subtitleFont);

        // Appliquer aux Contrôles (Boutons, Checkbox, ComboBox)
        for (JComponent comp : _allControls) {
            comp.setFont(boldFont);
        }

        // Slider
        _gridSizeSlider.setFont(regularFont);
        // Ajuster la taille préférée du slider en fonction du ratio
        _gridSizeSlider.setPreferredSize(new Dimension((int)(200 * ratio), (int)(50 * ratio)));

        // Mise à jour des Spinners (cas particulier car composés de sous-éléments)
        updateSpinnerFont(_carrierSpinner, regularFont, ratio);
        updateSpinnerFont(_battleshipSpinner, regularFont, ratio);
        updateSpinnerFont(_cruiserSpinner, regularFont, ratio);
        updateSpinnerFont(_submarineSpinner, regularFont, ratio);
        updateSpinnerFont(_destroyerSpinner, regularFont, ratio);

        // Mise à jour des Boutons (taille)
        Dimension btnDim = new Dimension((int)(160 * ratio), (int)(50 * ratio));
        _backButton.setPreferredSize(btnDim);
        _confirmButton.setPreferredSize(btnDim);

        // Forcer le rafraîchissement
        revalidate();
        repaint();
    }

    private void updateSpinnerFont(JSpinner spinner, Font font, float ratio) {
        spinner.setFont(font);
        // Ajuster la zone de texte interne
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(font);
        }
        // Ajuster la taille du spinner
        spinner.setPreferredSize(new Dimension((int)(70 * ratio), (int)(30 * ratio)));
    }

    private JSpinner createShipSpinner(ConfigController configController, ShipType shipType, String labelText, JPanel container) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel label = new JLabel(labelText);
        _allLabels.add(label);
        panel.add(label);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));

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

    private void setupListeners(GameController gameController, ConfigController configController) {
        _gridSizeSlider.addChangeListener(e -> {
            int gridSize = _gridSizeSlider.getValue();
            configController.setGridSize(gridSize);
            if (gridSize < 10 && _islandCheckBox.isSelected()) {
                _islandCheckBox.setSelected(false);
                configController.setIslandModeEnabled(false);
            }
        });

        _islandCheckBox.addActionListener(e -> {
            configController.setIslandModeEnabled(_islandCheckBox.isSelected());
            if (_islandCheckBox.isSelected()) {
                configController.setGridSize(10);
                _gridSizeSlider.setValue(10);
            }
        });

        _computerLevelBox.addActionListener(e -> {
            String selectedLevel = (String) _computerLevelBox.getSelectedItem();
            configController.setPlayerStrategy(selectedLevel);
        });

        _backButton.addActionListener(e -> gameController.showStartScreen());
        _confirmButton.addActionListener(e -> gameController.showPlacementScreen());
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
