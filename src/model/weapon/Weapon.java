package model.weapon;

import model.*;
import model.cell.CellState;
import model.cell.CellType;

import java.util.ArrayList;

public abstract class Weapon extends Placeable {
    private final WeaponType _weaponType;
    private int _nbUses;

    public Weapon(WeaponType type, CellState state, int nbUses) {
        super(1, Orientation.HORIZONTAL, type.name(), CellType.ISLAND, PlaceableType.WEAPON, state);
        _weaponType = type;
        _nbUses = nbUses;
    }

    public abstract ArrayList<AttackResult> applyAttack(Grid grid, Attack attack);

    public WeaponType getWeaponType() {
        return _weaponType;
    }

    @Override
    public abstract AttackResult hit();

    public int getUses() {
        return _nbUses;
    }

    public void use() {
        if (_nbUses > 0) {
            _nbUses--;
        }
    }
}
