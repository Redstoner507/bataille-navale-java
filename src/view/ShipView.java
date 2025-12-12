package view;

import model.ship.Ship;

import javax.swing.*;

public class ShipView extends PlaceableView {

    public ShipView(Ship ship, LabeledGridView grid, JLayeredPane topLayer, JPanel originalPanel, int cellSize) {
        super(ship, grid, topLayer, originalPanel, cellSize, new int[]{0, 3}); // A mofifier pour chaque type de bateau
    }
}
