package model.player;

import java.awt.*;
import java.util.ArrayList;

import model.*;
import model.weapon.Weapon;

public class ComputerMediumStrategy extends PlayerStrategy {

    public ComputerMediumStrategy (GameConfig config) {
        super(config); // Assuming a default grid size of 10
    }
    // Same as ComputerEasyStrategy for now
    /*
    * Generates the next attack based on medium computer strategy
     */
    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        for (AttackResult r : previousAttackResults) {
            // If the previous attack was a hit, add adjacent coordinates to priority list
            if (r.getStatus() == AttackStatus.HIT) {
                Point p = r.getHitCoordinates();
                addAdjacentCoords(p.x, p.y);
            }
        }

        if (!_priorityCoords.isEmpty()) { // If there are priority coordinates (Adjacent case of a hit), attack them first
            int[] coords = _priorityCoords.poll();
            Weapon selectedWeapon = this._missile; // Default weapon
            return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
        }

        boolean useBomb =
            _bomb != null &&
            _bomb.getUses() > 0 &&
            _random.nextInt(10) == 0; // 10% chance

        Weapon selectedWeapon = useBomb ? _bomb : _missile; 

        // If the last attack was a miss, pick new _random coordinates
        int[] coords = super.getRandomCoordinates(selectedWeapon.getWeaponType());

        return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
    }
}
