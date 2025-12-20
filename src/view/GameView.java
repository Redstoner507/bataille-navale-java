package view;

import controller.ConfigController;
import controller.GameController;
import controller.PlacementController;
import model.Grid;
import model.weapon.WeaponType;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;

public class GameView extends JPanel {
    private final GridView _computerGridView;
    private final HistoryView _historyView;
    private final EnumMap<WeaponType, JRadioButton> weaponButtons = new EnumMap<>(WeaponType.class);


    public GameView(GridView playerGridView, GridView computerGridView, PlacementController placementController, Grid computerGrid, ConfigController configController, GameController gameController) {
        setLayout(new BorderLayout(10, 10));

        // Titre principal
        JLabel titleLabel = new JLabel("Bataille Navale", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // --- 1. Panneau de Contrôle du Joueur (Grille + Armes) ---

        // Panneau pour la grille du joueur et les contrôles (Utilise BorderLayout pour placer les armes en SOUTH)
        JPanel playerControlPanel = new JPanel(new BorderLayout());

        // Grille du joueur
        LabeledGridView playerLabeledGridView = new LabeledGridView("Your ships", playerGridView);
        playerGridView.setLocked(true);
        SquarePanel playerSquarePanel = new SquarePanel(playerLabeledGridView);
        playerControlPanel.add(playerSquarePanel, BorderLayout.CENTER);

        // Panneau des armes (à placer en SOUTH du panneau de contrôle du joueur)
        JPanel weaponSelectionPanel = createWeaponSelectionPanel(gameController, new ButtonGroup());
        playerControlPanel.add(weaponSelectionPanel, BorderLayout.SOUTH);


        // --- 2. Panneau Central contenant les 2 Grilles et l'historique ---

        JPanel gridsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        gridsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Grille de l'ordinateur
        LabeledGridView computerLabeledGridView = new LabeledGridView("Enemy ships", computerGridView, false);
        _computerGridView = computerLabeledGridView.getGridView();
        _computerGridView.setAttackListener(gameController::handleHumanAttack);

        // Logique de placement aléatoire de l'ordinateur
        placementController.randomObjectsPlacement(computerGrid, _computerGridView, configController.getAllPlaceables());
        _computerGridView.clearGrid(); // Cache les bateaux

        // Historique
        _historyView = new HistoryView();

        // Ajouter le Panneau de Contrôle du Joueur à gauche
        gridsPanel.add(playerControlPanel);

        // Ajouter l'historique au centre
        gridsPanel.add(_historyView);

        // Ajouter la Grille de l'Ordinateur à droite
        SquarePanel computerSquarePanel = new SquarePanel(computerLabeledGridView);
        gridsPanel.add(computerSquarePanel);

        add(gridsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> gameController.showPlacementScreen());
        quitButton.setFont(new Font("Arial", Font.BOLD, 22));
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the pannel containing the radio buttons to select the weapon.
     */
    private JPanel createWeaponSelectionPanel(GameController gameController, ButtonGroup buttonGroup) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Weapons"));

        WeaponType[] availableWeapons = {
            WeaponType.MISSILE,
            WeaponType.BOMB,
            WeaponType.SONAR
        };

        for (WeaponType type : availableWeapons) {
            JRadioButton button = new JRadioButton(type.toString());
            button.setActionCommand(type.name());

            button.addActionListener(e ->
                gameController.setSelectedWeapon(WeaponType.valueOf(e.getActionCommand()))
            );

            buttonGroup.add(button);
            panel.add(button);

            // ⭐ Stockage du bouton
            weaponButtons.put(type, button);
        }

        // Sélection par défaut
        weaponButtons.get(WeaponType.MISSILE).setSelected(true);

        return panel;
    }


    public GridView getComputerGridView() {
        return _computerGridView;
    }

    public HistoryView getHistoryView() {
        return _historyView;
    }

    public void setWeaponSelection(WeaponType weaponType) {
        JRadioButton button = weaponButtons.get(weaponType);
        if (button != null) {
            button.setSelected(true);
        }
    }

}
