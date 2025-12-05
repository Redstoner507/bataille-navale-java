package model.player;

import java.util.ArrayList;
import java.util.Random;

import model.Attack;
import model.AttackResult;
import model.weapon.Weapon;

public class ComputerVeryEasyStrategy extends PlayerStrategy {
    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        // Generate completely random coordinates
        Random random = new Random();
        int row = random.nextInt(10);
        int col = random.nextInt(10);
        
        // Randomly choose a weapon (mostly missile, sometimes bomb)
        Weapon selectedWeapon = this.missile; // Default weapon for very easy strategy
        if (random.nextInt(100) < 10 && this.bombUses > 0) { // 10% chance to use bomb if available
            selectedWeapon = this.bomb;
            this.bombUses--;
        }
        return new Attack(null, null, selectedWeapon, row, col); // Player will be set later
    }
}