package model.weapon;

import model.Grid;

public class Bomb extends Weapon{

    public Bomb() {
        super(WeaponType.BOMB);
    }

    @Override
    public void apply(Grid grid, int row, int col) {

    }
}
