package model.player;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ArrayList;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public class ComputerEasyStrategy extends PlayerStrategy {
    private final Queue<int[]> _priorityCoords = new ArrayDeque<>();

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

            Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
            return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
        }

        // If the last attack was a miss, pick new random coordinates
        int[] coords = super.getRandomCoordinates();

        Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
        return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
    }

    /*
    * Adds adjacent coordinates to the priority list after a hit
     */
    private void addAdjacentCoords(int row, int col) {
        int[][] directions = { {-1,0}, {1,0}, {0,-1}, {0,1} };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10) {

                // must still be a valid coordinate to shoot
                if (!super.isRemainingCoordinate(newRow, newCol))
                    continue;

                boolean alreadyIn = _priorityCoords.stream()
                        .anyMatch(c -> c[0] == newRow && c[1] == newCol);

                if (!alreadyIn)
                    _priorityCoords.add(new int[] { newRow, newCol });
            }
        }
    }
}

