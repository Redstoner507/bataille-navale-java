package model.player;

import java.awt.*;
import java.util.ArrayList;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public class ComputerEasyStrategy extends PlayerStrategy {
    private final ArrayList<int[]> _priorityCoords = new ArrayList<>();

    /*
    * Generates the next attack based on easy computer strategy
     */
    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        for (AttackResult previousResult : previousAttackResults) {
            // If the previous attack was a hit, add adjacent coordinates to priority list
            if (previousResult.getStatus() == AttackStatus.HIT) {
                Point hitCoordinates = previousResult.getHitCoordinates();
                addAdjacentCoords(hitCoordinates.x, hitCoordinates.y);
            }
        }

        if (!_priorityCoords.isEmpty()) { // If there are priority coordinates (Adjacent case of a hit), attack them first
            int[] coords = _priorityCoords.removeFirst();

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

            // On reste dans la grille
            if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10) {

                // Optionnel : éviter d'ajouter une coordonnée déjà présente
                boolean alreadyIn = _priorityCoords.stream()
                        .anyMatch(c -> c[0] == newRow && c[1] == newCol);

                if (!alreadyIn) {
                    _priorityCoords.add(new int[] { newRow, newCol });
                }
            }
        }
    }
}

