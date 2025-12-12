package model.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import model.Attack;
import model.AttackResult;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public abstract class PlayerStrategy {
    protected final List<int[]> coordsList = new ArrayList<>();
    protected final Queue<int[]> _priorityCoords = new ArrayDeque<>();
    protected final Random random = new Random();
    protected final Weapon missile = new WeaponFactory().createMissile();
    protected final Weapon bomb = new WeaponFactory().createBomb();
    protected final Weapon sonar = new WeaponFactory().createSonar();
    protected int _bombUses = 1; // Example usage limit (à modfier l'emplacement (inventaire) de cette variable)
    protected int _sonarUses = 1; // Example usage limit
    protected int _gridSize = 10;

    public PlayerStrategy(int gridSize) {
        this._gridSize = gridSize;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                coordsList.add(new int[] { row, col });
            }
        }
    }   

    public abstract Attack getAttack(ArrayList<AttackResult> previousAttackResults);

    protected int[] getRandomCoordinates() {
        if (coordsList.isEmpty()) {
            throw new IllegalStateException("No more coordinates available");
        }

        int index = random.nextInt(coordsList.size());
        int lastIndex = coordsList.size() - 1;
        int[] result = coordsList.get(index);
        coordsList.set(index, coordsList.get(lastIndex));
        coordsList.remove(lastIndex);
        
        return result;
    }

    protected void deleteCoordinates(int[] coords) {
        coordsList.removeIf(c -> c[0] == coords[0] && c[1] == coords[1]);
    }

    protected boolean isRemainingCoordinate(int row, int col) {
        return coordsList.stream().anyMatch(c -> c[0] == row && c[1] == col);
    }

    /*
    * Adds adjacent coordinates to the priority list after a hit
     */
    protected void addAdjacentCoords(int row, int col) {
        int[][] directions = { {-1,0}, {1,0}, {0,-1}, {0,1} };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow >= 0 && newRow < _gridSize && newCol >= 0 && newCol < _gridSize) {

                // must still be a valid coordinate to shoot
                if (!isRemainingCoordinate(newRow, newCol))
                    continue;

                boolean alreadyIn = _priorityCoords.stream()
                        .anyMatch(c -> c[0] == newRow && c[1] == newCol);

                if (!alreadyIn)
                    _priorityCoords.add(new int[] { newRow, newCol });
            }
        }
    }
}
