package view;

import controller.GameController;
import controller.PlacementController;
import model.Orientation;
import model.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlacementScreen extends JPanel {
    private static final int SHIP_PANEL_WIDTH = 300;
    private static final int LABEL_HEIGHT = 20;
    private final JButton _backButton;
    private final JPanel _shipPanel;
    private Orientation _currentOrientation = Orientation.HORIZONTAL;
    private final GameController _gameController;

    public PlacementScreen(GameController gameController, int gridSize, ArrayList<Ship> ships, int cellSize) {
        _gameController = gameController;
        
        setLayout(new BorderLayout(10, 10));

        int width = SHIP_PANEL_WIDTH + (gridSize+1) * cellSize;
        int height = (gridSize+1) * cellSize + LABEL_HEIGHT;

        // Panneau boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rotateButton = new JButton("Orientation");
        JButton fixedPlacementButton = new JButton("Fixed placement");
        JButton randomPlacementButton = new JButton("Random placement");
        JButton startGameButton = new JButton("Start game");
        _backButton = new JButton("Cancel");

        buttonPanel.add(rotateButton);
        buttonPanel.add(fixedPlacementButton);
        buttonPanel.add(randomPlacementButton);
        buttonPanel.add(startGameButton);
        buttonPanel.add(_backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // LayeredPane pour superposer la grille et les ShipView
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        layeredPane.setLayout(null); // layout absolu pour setBounds
        add(layeredPane, BorderLayout.CENTER);

        // Grille joueur
        LabeledGridView playerGrid = new LabeledGridView("Place your ships in the grid", gridSize, cellSize, false);
        playerGrid.setOpaque(true);
        playerGrid.setBackground(Color.CYAN);
        playerGrid.setBounds(SHIP_PANEL_WIDTH, 0, width - SHIP_PANEL_WIDTH, height);
        layeredPane.add(playerGrid, JLayeredPane.DEFAULT_LAYER);

        // Panel gauche : bateaux à drag
        _shipPanel = new JPanel();
        _shipPanel.setLayout(new BoxLayout(_shipPanel, BoxLayout.Y_AXIS));
        _shipPanel.setBorder(BorderFactory.createTitledBorder("Your ships"));
        _shipPanel.setOpaque(true);
        _shipPanel.setBackground(Color.LIGHT_GRAY);
        _shipPanel.setBounds(0, 0, SHIP_PANEL_WIDTH, height);
        layeredPane.add(_shipPanel, JLayeredPane.DEFAULT_LAYER);

        // PlacementController
        PlacementController controller = new PlacementController(playerGrid.getGridView().getGrid(), playerGrid.getGridView());
        playerGrid.getGridView().setGridActionListener((shipView, row, col) ->
                controller.placeShip(shipView.getShip(), row, col)
        );

        // Ajouter les ShipView
        for (Ship ship : ships) {
            ShipView shipView = new ShipView(ship, playerGrid, layeredPane, _shipPanel, cellSize);
            shipView.setAlignmentX(Component.CENTER_ALIGNMENT);
            _shipPanel.add(shipView);
            _shipPanel.add(Box.createRigidArea(new Dimension(0, 10))); // espacement vertical
        }

        rotateButton.addActionListener(e -> {
            // Change l'orientation globale
            _currentOrientation = (_currentOrientation == Orientation.HORIZONTAL)
                    ? Orientation.VERTICAL: Orientation.HORIZONTAL;

            // Parcours tous les ShipView du panel et applique la nouvelle orientation
            for (Component comp : _shipPanel.getComponents()) {
                if (comp instanceof ShipView shipView) {
                    controller.rotateShip(shipView.getShip());
                    shipView.setOrientation(_currentOrientation);
                }
            }
        });
    }

    public JButton getBackButton() {
        return _backButton;
    }
}
