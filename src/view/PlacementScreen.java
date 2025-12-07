package view;

import controller.ConfigController;
import controller.GameController;
import controller.PlacementController;
import model.Orientation;
import model.Placeable;
import model.ship.Ship;
import model.trap.Trap;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlacementScreen extends JPanel {
    private static final int SHIP_PANEL_WIDTH = 300;
    private static final int TRAP_PANEL_WIDTH = 150;
    private final JPanel _shipPanel;
    private final JPanel _trapPanel;
    private Orientation _currentOrientation = Orientation.HORIZONTAL;

    public PlacementScreen(GameController gameController, int gridSize, int cellSize, PlacementController placementController, ConfigController configController) {
        
        setLayout(new BorderLayout(10, 10));

        int gridWidth = (gridSize+1) * cellSize;
        int gridHeight = (gridSize+1) * cellSize;

        int totalContentWidth = SHIP_PANEL_WIDTH + 10 + gridWidth + 10 + TRAP_PANEL_WIDTH;

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

        // Conteneur pour le Centrage Horizontal
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        add(centerWrapper, BorderLayout.CENTER);

        JLayeredPane mainLayeredPane = new JLayeredPane();
        mainLayeredPane.setPreferredSize(new Dimension(totalContentWidth, gridHeight));
        mainLayeredPane.setLayout(null);

        centerWrapper.add(mainLayeredPane, new GridBagConstraints());

        // Conteneur des Bateaux
        _shipPanel = new JPanel();
        _shipPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        _shipPanel.setBorder(BorderFactory.createTitledBorder("Your ships"));
        _shipPanel.setPreferredSize(new Dimension(SHIP_PANEL_WIDTH, gridHeight));
        _shipPanel.setOpaque(true);
        _shipPanel.setBackground(Color.LIGHT_GRAY);
        _shipPanel.setBounds(0, 0, SHIP_PANEL_WIDTH, gridHeight);
        mainLayeredPane.add(_shipPanel, JLayeredPane.DEFAULT_LAYER);

        // Grille joueur
        LabeledGridView playerGrid = new LabeledGridView("Place your objects in the grid", gameController.getPlayerGridView(), false);
        playerGrid.setOpaque(true);
        // Utiliser la taille réelle de la grille pour les Bounds (départ en 0, 0 du LayeredPane)
        playerGrid.setBounds(SHIP_PANEL_WIDTH + 10, 0, gridWidth, gridHeight);
        mainLayeredPane.add(playerGrid, JLayeredPane.DEFAULT_LAYER);

        // Conteneur des Pièges
        _trapPanel = new JPanel();
        _trapPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        _trapPanel.setBorder(BorderFactory.createTitledBorder("Your traps"));
        _trapPanel.setPreferredSize(new Dimension(TRAP_PANEL_WIDTH, gridHeight));
        _trapPanel.setOpaque(true);
        _trapPanel.setBackground(Color.LIGHT_GRAY);
        _trapPanel.setBounds(SHIP_PANEL_WIDTH + gridWidth + 20, 0, TRAP_PANEL_WIDTH, gridHeight);
        mainLayeredPane.add(_trapPanel, JLayeredPane.DEFAULT_LAYER);

        // Récupérer les objets à placer
        ArrayList<Placeable> objects = new ArrayList<>();
        ArrayList<Placeable> ships = configController.getShipConfig();
        ArrayList<Placeable> traps = configController.getTrapConfig();
        objects.addAll(ships);
        objects.addAll(traps);

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
