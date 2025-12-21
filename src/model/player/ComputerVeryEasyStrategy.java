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
        // Randomly choose a weapon (mostly _missile, sometimes bomb)
       
        boolean useBomb =
            _bomb != null &&
            _bomb.getUses() > 0 &&
            _random.nextInt(10) == 0; // 10% chance

        Weapon selectedWeapon = useBomb ? _bomb : _missile;      
        
        // Generate completely _random coordinates
        int[] coords = super.getRandomCoordinates(selectedWeapon.getWeaponType());
        
        return new Attack(null, null, selectedWeapon, coords[0], coords[1]); // Player will be set later
    }
}