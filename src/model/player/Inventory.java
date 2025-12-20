package model.player;

import model.weapon.Weapon;
import model.weapon.WeaponType;

import java.util.ArrayList;

public class Inventory {
    private final ArrayList<Weapon> _weapons;

    public Inventory() {
        this._weapons = new ArrayList<>();
    }

    public void addWeapon(Weapon weapon) {
        this._weapons.add(weapon);
    }

    public ArrayList<Weapon> getWeapons() {
        return _weapons;
    }

    public Weapon getWeaponByType(WeaponType type) {
        for (Weapon weapon : _weapons) {
            if (weapon.getWeaponType() == type) {
                return weapon;
            }
        }
        return null;
    }
}
