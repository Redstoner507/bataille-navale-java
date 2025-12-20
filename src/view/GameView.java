package view;

import controller.ConfigController;
import controller.GameController;
import controller.PlacementController;
import model.Grid;
import model.weapon.WeaponType;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final GridView _computerGridView;
    private final HistoryView _historyView;
    private final StatsView _statsView;

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

        // Panneau de l'ordinateur
        LabeledGridView computerLabeledGridView = new LabeledGridView("Enemy ships", computerGridView, false);
        _computerGridView = computerLabeledGridView.getGridView();
        _computerGridView.setAttackListener(gameController::handleHumanAttack);

        SquarePanel computerSquarePanel = new SquarePanel(computerLabeledGridView);

        // Logique de placement aléatoire de l'ordinateur
        placementController.randomObjectsPlacement(computerGrid, _computerGridView, configController.getAllPlaceables());
        _computerGridView.clearGrid(); // Cache les bateaux

        // --- 2. Panneau Central contenant les 2 Grilles et l'historique ---

        JPanel gridsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        gridsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Panneau central
        JPanel centerControlPanel = new JPanel(new BorderLayout());

        // Historique
        _historyView = new HistoryView();
        centerControlPanel.add(_historyView, BorderLayout.CENTER);

        // Statistiques
        _statsView = new StatsView();
        centerControlPanel.add(_statsView, BorderLayout.SOUTH);

        // Ajouter le Panneau de Contrôle du Joueur à gauche
        gridsPanel.add(playerControlPanel);

        // Ajouter l'historique au centre
        gridsPanel.add(centerControlPanel);

        // Ajouter le Panneau de l'Ordinateur à droite
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

        // Supposons que vous avez une enum WeaponType : MISSILE, BOMB, SONAR
        WeaponType[] availableWeapons = {WeaponType.MISSILE, WeaponType.BOMB, WeaponType.SONAR};

        for (WeaponType type : availableWeapons) {
            JRadioButton button = new JRadioButton(type.toString());
            button.setActionCommand(type.toString()); // Utiliser le nom comme commande

            // Attacher l'écouteur du Contrôleur pour gérer la sélection
            // Le Contrôleur doit stocker l'arme sélectionnée.
            button.addActionListener(e -> gameController.setSelectedWeapon(WeaponType.valueOf(e.getActionCommand())));

            buttonGroup.add(button);
            panel.add(button);
        }

        // Sélectionner le Missile par défaut
        if (buttonGroup.getButtonCount() > 0) {
            buttonGroup.setSelected(buttonGroup.getElements().nextElement().getModel(), true);
        }

        return panel;
    }

    public GridView getComputerGridView() {
        return _computerGridView;
    }

    public HistoryView getHistoryView() {
        return _historyView;
    }

    public StatsView getStatsView() {return _statsView;}
}
