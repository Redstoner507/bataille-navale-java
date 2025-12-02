package model.player;

import java.util.ArrayList;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public class ComputerEasyStrategy extends PlayerStrategy {
    private ArrayList<int[]> priorityCoords = new ArrayList<>();
    private int[] lastAttackCoords = null;
    
    /*
    * Generates the next attack based on easy computer strategy
     */
    @Override
    public Attack getAttack(AttackResult previousAttackResult) {
        if (previousAttackResult.getStatus() == AttackStatus.HIT) { // If the last attack was a hit, try the same coordinates again
            addAdjacentCoords(lastAttackCoords[0], lastAttackCoords[1]);
        }
        
        if (priorityCoords.size() > 0) { // If there are priority coordinates (Adjacent case of a hit), attack them first
            int[] coords = priorityCoords.remove(0);
            lastAttackCoords = coords;
            Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
            return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
        }

        // If the last attack was a miss, pick new random coordinates
        int[] coords = super.getRandomCoordinates();
        lastAttackCoords = coords;
        Weapon selectedWeapon = new WeaponFactory().createMissile(); // Assuming a default constructor for Weapon
        return new Attack(null, null, selectedWeapon, coords[0], coords[1]);
    }

    /*
    * Adds adjacent coordinates to the priority list after a hit
     */
    private void addAdjacentCoords(int row, int col) { 
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Up, Down, Left, Right
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10) {
                priorityCoords.add(new int[] { newRow, newCol });
            }
        }
    }
}

