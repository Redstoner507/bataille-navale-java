package model.weapon;

import model.*;
import model.cell.CellType;

public abstract class Weapon extends Placeable {
    private final WeaponType _weaponType;

    public Weapon(WeaponType type) {
        super(1, Orientation.HORIZONTAL, type.name().toLowerCase(), CellType.ISLAND, PlaceableType.WEAPON);
        _weaponType = type;
    }

    public abstract AttackResult applyAttack(Grid grid, Attack attack);

    public WeaponType getWeaponType() {
        return _weaponType;
    }

    @Override
    public AttackResult hit() {
        return new AttackResult(AttackStatus.ITEM_FOUND, "You found a " + getName() + " !");
    }
}
