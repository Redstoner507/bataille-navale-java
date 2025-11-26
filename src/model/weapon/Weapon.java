package model.weapon;

import model.Grid;
import model.Orientation;
import model.Placeable;
import model.cell.CellType;

public abstract class Weapon extends Placeable {
    private final WeaponType _weaponType;

    public Weapon(WeaponType type) {
        super(1, Orientation.HORIZONTAL, type.name().toLowerCase(), CellType.ISLAND);
        _weaponType = type;
    }

    public abstract void apply(Grid grid, int row, int col);

    public WeaponType getWeaponType() {
        return _weaponType;
    }
}
