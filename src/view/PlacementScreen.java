package view;

import controller.ConfigController;
import controller.GameController;
import controller.PlacementController;
import model.Orientation;
import model.ship.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlacementScreen extends JPanel {
    private static final int SHIP_PANEL_WIDTH = 300;
    private static final int LABEL_HEIGHT = 20;
    private final JPanel _shipPanel;
    private Orientation _currentOrientation = Orientation.HORIZONTAL;

    public PlacementScreen(GameController gameController, int gridSize, int cellSize, PlacementController placementController, ConfigController configController) {
        
        setLayout(new BorderLayout(10, 10));

        int width = SHIP_PANEL_WIDTH + (gridSize+1) * cellSize;
        int height = (gridSize+1) * cellSize + LABEL_HEIGHT;

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

        // LayeredPane pour superposer la grille et les ShipView
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        layeredPane.setLayout(null); // layout absolu pour setBounds
        add(layeredPane, BorderLayout.CENTER);

        // Grille joueur
        LabeledGridView playerGrid = new LabeledGridView("Place your ships in the grid", gridSize, cellSize, false);
        playerGrid.setOpaque(true);
        playerGrid.setBounds(SHIP_PANEL_WIDTH, 0, width - SHIP_PANEL_WIDTH, height);
        layeredPane.add(playerGrid, JLayeredPane.DEFAULT_LAYER);

        // Panel gauche : bateaux à drag
        _shipPanel = new JPanel();
        _shipPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        _shipPanel.setBorder(BorderFactory.createTitledBorder("Your ships"));
        _shipPanel.setOpaque(true);
        _shipPanel.setBackground(Color.LIGHT_GRAY);
        _shipPanel.setBounds(0, 0, SHIP_PANEL_WIDTH, height);
        layeredPane.add(_shipPanel, JLayeredPane.DEFAULT_LAYER);

        // PlacementController
        playerGrid.getGridView().setGridActionListener((shipView, row, col) ->
            placementController.placeShip(gameController.getPlayerGrid(), playerGrid.getGridView(), shipView.getShip(), row, col)
        );

        // Ajouter les ShipView
        ArrayList<Ship> ships = configController.getShipConfig();
        if (ships != null && !ships.isEmpty()) {
            for (Ship ship : ships) {
                ShipView shipView = new ShipView(ship, playerGrid, layeredPane, _shipPanel, cellSize);
                shipView.setAlignmentX(Component.CENTER_ALIGNMENT);
                _shipPanel.add(shipView);
            }
        }

        rotateButton.addActionListener(e -> {
            // Change l'orientation globale
            _currentOrientation = (_currentOrientation == Orientation.HORIZONTAL)
                    ? Orientation.VERTICAL: Orientation.HORIZONTAL;

            // Parcours tous les ShipView du panel et applique la nouvelle orientation
            for (Component comp : _shipPanel.getComponents()) {
                if (comp instanceof ShipView shipView) {
                    placementController.rotateShip(shipView.getShip());
                    shipView.setOrientation(_currentOrientation);
                }
            }
        });

        randomPlacementButton.addActionListener(e -> {
            placementController.randomShipsPlacement(gameController.getPlayerGrid(), playerGrid.getGridView(), ships);
            clearShipViews();
        });
        
        startGameButton.addActionListener(e -> {
            if (_shipPanel.getComponentCount() == 0) {
                gameController.setPlayerGridView(playerGrid);
                gameController.showGameScreen();
            }
        });
    }

    public void clearShipViews() {
        _shipPanel.removeAll();
        _shipPanel.revalidate();
        _shipPanel.repaint();
    }
}
