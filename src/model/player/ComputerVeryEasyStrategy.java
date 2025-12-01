package model.player;

import java.util.Random;

import model.Attack;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public class ComputerVeryEasyStrategy implements PlayerStrategy {
    @Override
    public Attack getAttack() {
        Random rand = new Random();
        int row = rand.nextInt(10);
        int col = rand.nextInt(10);
        Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
        return new Attack(null, selectedWeapon, row, col); // Player will be set later
    }
}