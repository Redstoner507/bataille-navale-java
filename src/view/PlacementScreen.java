package view;

import controller.ConfigController;
import controller.GameController;
import controller.PlacementController;
import model.Orientation;
import model.Placeable;
import model.ship.Ship;
import model.trap.Trap;
import model.weapon.Weapon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlacementScreen extends JPanel {
    private final JPanel _shipPanel;
    private final JPanel _trapPanel;
    private Orientation _currentOrientation = Orientation.HORIZONTAL;

    public PlacementScreen(GameController gameController, int cellSize, PlacementController placementController, ConfigController configController) {

        int gridSize = gameController.getPlayerGrid().getSize();

        setLayout(new BorderLayout(10, 10));

        // Calcul des tailles basées sur la grille
        int gridPanelWidth = (gridSize+1) * cellSize;
        int gridPanelHeight = (gridSize+1) * cellSize;

        // Définir des largeurs minimales pour les panneaux latéraux
        final int MIN_SHIP_WIDTH = 250;
        final int MIN_TRAP_WIDTH = 100;

        // Panneau boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rotateButton = new JButton("Orientation");
        JButton randomPlacementButton = new JButton("Random placement");
        JButton startGameButton = new JButton("Start game");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> gameController.showConfigScreen());

        buttonPanel.add(rotateButton);
        buttonPanel.add(randomPlacementButton);
        buttonPanel.add(startGameButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // JLayeredPane Principal
        JLayeredPane mainLayeredPane = new JLayeredPane();
        mainLayeredPane.setLayout(new GridBagLayout());
        add(mainLayeredPane, BorderLayout.CENTER);

        // Configuration des contraintes pour l'étirement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5); // Marge
        gbc.gridy = 0; // Tous sur la même ligne
        gbc.weighty = 1.0; // Prendre toute la hauteur

        // Conteneur des Bateaux
        _shipPanel = new JPanel();
        _shipPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        _shipPanel.setBorder(BorderFactory.createTitledBorder("Your ships"));
        _shipPanel.setOpaque(true);
        _shipPanel.setBackground(Color.LIGHT_GRAY);
        Dimension minShipDim = new Dimension(MIN_SHIP_WIDTH, 1);
        _shipPanel.setMinimumSize(minShipDim); // Taille minimale
        _shipPanel.setPreferredSize(minShipDim);

        gbc.gridx = 0;
        gbc.weightx = 0.15; // 15% de l'espace horizontal extensible
        gbc.fill = GridBagConstraints.BOTH; // Permet l'étirement
        gbc.anchor = GridBagConstraints.NORTH; // Aligner les éléments en haut
        mainLayeredPane.add(_shipPanel, gbc, JLayeredPane.DEFAULT_LAYER);

        // Grille joueur
        LabeledGridView playerGrid = new LabeledGridView("Place your objects in the grid", gameController.getPlayerGridView(), false);
        playerGrid.setOpaque(true);
        playerGrid.setMinimumSize(new Dimension(gridPanelWidth, gridPanelHeight)); // Taille minimale

        SquarePanel squarePanel = new SquarePanel(playerGrid);

        gbc.gridx = 1;
        gbc.weightx = 0.7; // 70% de l'espace horizontal extensible
        gbc.fill = GridBagConstraints.BOTH; // Permet l'étirement
        gbc.anchor = GridBagConstraints.CENTER; // Centrer au milieu de sa zone
        mainLayeredPane.add(squarePanel, gbc, JLayeredPane.DEFAULT_LAYER);

        // Conteneur des Pièges
        _trapPanel = new JPanel();
        _trapPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        _trapPanel.setBorder(BorderFactory.createTitledBorder("Your traps"));
        _trapPanel.setOpaque(true);
        _trapPanel.setBackground(Color.LIGHT_GRAY);
        Dimension trapDim = new Dimension(MIN_TRAP_WIDTH, 10);
        _trapPanel.setMinimumSize(trapDim); // Taille minimale
        _trapPanel.setPreferredSize(trapDim);

        gbc.gridx = 2;
        gbc.weightx = 0.15; // // 15% de l'espace horizontal extensible
        gbc.fill = GridBagConstraints.BOTH; // Permet l'étirement
        gbc.anchor = GridBagConstraints.NORTH;
        mainLayeredPane.add(_trapPanel, gbc, JLayeredPane.DEFAULT_LAYER);

        // Récupérer les objets à placer
        ArrayList<Placeable> objects = new ArrayList<>();
        ArrayList<Placeable> ships = configController.getShipConfig();
        ArrayList<Placeable> traps = configController.getTrapConfig();
        ArrayList<Placeable> weapons = configController.getWeaponConfig();
        objects.addAll(ships);
        objects.addAll(traps);
        objects.addAll(weapons);

        // Ajouter les WeaponView dans _trapPanel
        if (weapons != null && !weapons.isEmpty()) {
            for (Placeable weapon : weapons) {
                // ShipView doit être créé avec le JLayeredPane central pour le Drag&Drop
                WeaponView weaponView = new WeaponView((Weapon)weapon, playerGrid, mainLayeredPane, _trapPanel, cellSize);
                weaponView.setAlignmentX(Component.CENTER_ALIGNMENT);
                _trapPanel.add(weaponView);
            }
        }
        // Ajouter les TrapView dans _trapPanel
        if (traps != null && !traps.isEmpty()) {
            for (Placeable trap : traps) {
                // ShipView doit être créé avec le JLayeredPane central pour le Drag&Drop
                TrapView trapView = new TrapView((Trap)trap, playerGrid, mainLayeredPane, _trapPanel, cellSize);
                trapView.setAlignmentX(Component.CENTER_ALIGNMENT);
                _trapPanel.add(trapView);
            }
        }

        // Ajouter les ShipView dans _shipPanel
        if (ships != null && !ships.isEmpty()) {
            for (Placeable ship : ships) {
                // ShipView doit être créé avec le JLayeredPane central pour le Drag&Drop
                ShipView shipView = new ShipView((Ship)ship, playerGrid, mainLayeredPane, _shipPanel, cellSize);
                shipView.setAlignmentX(Component.CENTER_ALIGNMENT);
                _shipPanel.add(shipView);
            }
        }

        // PlacementController
        playerGrid.getGridView().setObjectPlacementListener((placeableView, row, col) ->
            placementController.placeObject(gameController.getPlayerGrid(), playerGrid.getGridView(), placeableView.getPlaceable(), row, col)
        );

        rotateButton.addActionListener(e -> {
            // Change l'orientation globale
            _currentOrientation = (_currentOrientation == Orientation.HORIZONTAL)
                    ? Orientation.VERTICAL: Orientation.HORIZONTAL;

            // Parcours tous les ShipView du panel et applique la nouvelle orientation
            for (Component comp : _shipPanel.getComponents()) {
                if (comp instanceof ShipView shipView) {
                    placementController.rotatePlaceable(shipView.getPlaceable());
                    shipView.setOrientation(_currentOrientation);
                }
            }
        });

        randomPlacementButton.addActionListener(e -> {
            placementController.randomObjectsPlacement(gameController.getPlayerGrid(), playerGrid.getGridView(), objects);
            clearPlaceableViews();
        });
        
        startGameButton.addActionListener(e -> {
            if (_shipPanel.getComponentCount() == 0) {
                gameController.showGameScreen();
            }
        });

        revalidate();
        repaint();
    }

    public void clearPlaceableViews() {
        // Nettoyage du conteneur de bateaux
        _shipPanel.removeAll();
        _shipPanel.revalidate();
        _shipPanel.repaint();

        // Nettoyage du conteneur de pièges
        _trapPanel.removeAll();
        _trapPanel.revalidate();
        _trapPanel.repaint();
    }
}
