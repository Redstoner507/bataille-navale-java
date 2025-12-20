package model.player;

import model.weapon.Weapon;
import model.weapon.WeaponType;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Weapon> weapons;

    public Inventory() {
        this.weapons = new ArrayList<>();
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public Weapon getWeaponByType(WeaponType type) {
        for (Weapon weapon : weapons) {
            if (weapon.getWeaponType() == type) {
                return weapon;
            }
        }
        return null;
    }
}
