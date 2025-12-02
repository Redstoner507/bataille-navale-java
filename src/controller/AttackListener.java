package controller;

import model.weapon.Weapon;

public interface AttackListener {
    void onCellAttacked(int row, int col, Weapon selectedWeapon);
}
