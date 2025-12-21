package view;

import model.weapon.Weapon;

import javax.swing.*;

public class WeaponView extends PlaceableView {

    public WeaponView(Weapon weapon, LabeledGridView grid, JLayeredPane topLayer, JPanel originalPanel, int cellSize) {
        super(weapon, grid, topLayer, originalPanel, cellSize);
    }
}