package model.player;

import java.util.ArrayList;

import model.Attack;
import model.AttackResult;
import model.weapon.Weapon;

public class ComputerVeryEasyStrategy extends PlayerStrategy {
    public ComputerVeryEasyStrategy (int gridSize) {
        super(gridSize); // Assuming a default grid size of 10
    }
    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        // Generate completely random coordinates
        int[] coords = super.getRandomCoordinates();
        
        // Randomly choose a weapon (mostly missile, sometimes bomb)
        Weapon selectedWeapon = this.missile; // Default weapon for very easy strategy
        if (random.nextInt(100) < 10 && this._bombUses > 0) { // 10% chance to use bomb if available
            selectedWeapon = this.bomb;
            this._bombUses--;
        }
        return new Attack(null, null, selectedWeapon, coords[0], coords[1]); // Player will be set later
    }
}