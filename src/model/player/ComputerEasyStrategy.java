package model.player;

import java.awt.*;
import java.util.ArrayList;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.weapon.Weapon;

public class ComputerEasyStrategy extends PlayerStrategy {
    public ComputerEasyStrategy (int gridSize) {
        super(gridSize); // Assuming a default grid size of 10
    }

    /*
    * Generates the next attack based on easy computer strategy
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
            super.deleteCoordinates(coords);
            Weapon selectedWeapon = this.missile; // Default weapon
            return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
        }

        // If the last attack was a miss, pick new random coordinates
        int[] coords = super.getRandomCoordinates();

        Weapon selectedWeapon = this.missile; // Default weapon
        if (random.nextInt(100) < 10 && this._bombUses > 0) { // 10% chance to use bomb if available
            selectedWeapon = this.bomb;
            this._bombUses--;
        }
        return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
    }
}

