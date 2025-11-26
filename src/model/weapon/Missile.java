package model.weapon;

import model.Grid;

public class Missile extends Weapon{

    public Missile() {
        super(WeaponType.MISSILE);
    }

    @Override
    public void apply(Grid grid, int row, int col) {

    }
}
