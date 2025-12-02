package model.player;

import java.util.Random;

import model.Attack;
import model.AttackResult;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public class ComputerVeryEasyStrategy extends PlayerStrategy {
    @Override
    public Attack getAttack(AttackResult previousAttackResult) {
        Random random = new Random();
        int row = random.nextInt(10);
        int col = random.nextInt(10);

        Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
        return new Attack(null, null, selectedWeapon, row, col); // Player will be set later
    }
}