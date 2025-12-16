package view;

import model.weapon.Weapon;

import javax.swing.*;

public class WaeponView extends PlaceableView {

    public WaeponView(Weapon weapon, LabeledGridView grid, JLayeredPane topLayer, JPanel originalPanel, int cellSize) {
        super(weapon, grid, topLayer, originalPanel, cellSize);
    }
}