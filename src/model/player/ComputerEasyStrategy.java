package model.player;

import java.util.ArrayList;
import java.util.Random;
import model.Attack;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public class ComputerEasyStrategy implements PlayerStrategy {
    private Random rand = new Random();
    private ArrayList<int[]> liste = new ArrayList<>();


    @Override
    public Attack getAttack() {
        int row = rand.nextInt(10);
        int col = rand.nextInt(10);
        Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
        return new Attack(null, selectedWeapon, row, col); // Player will be set later
    }
}
