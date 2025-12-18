package model.player;

import java.util.ArrayList;

import model.Attack;
import model.AttackResult;
import model.GameConfig;
import model.weapon.Weapon;

public class ComputerVeryEasyStrategy extends PlayerStrategy {
    public ComputerVeryEasyStrategy (GameConfig config) {
        super(config); // Assuming a default grid size of 10
    }
    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        // Generate completely _random coordinates
        int[] coords = super.getRandomCoordinates();
        
        // Randomly choose a weapon (mostly _missile, sometimes bomb)
        Weapon selectedWeapon = this._missile; // Default weapon for very easy strategy
        if (_random.nextInt(100) < 10 && this._bombUses > 0) { // 10% chance to use bomb if available
            selectedWeapon = this._bomb;
            this._bombUses--;
        }
        return new Attack(null, null, selectedWeapon, coords[0], coords[1]); // Player will be set later
    }
}