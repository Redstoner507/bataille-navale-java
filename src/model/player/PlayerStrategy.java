package model.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import model.Attack;
import model.AttackResult;
import model.GameConfig;
import model.weapon.Weapon;
import model.weapon.WeaponType;

public abstract class PlayerStrategy {
    protected final List<int[]> _coordsList = new ArrayList<>();
    protected final Queue<int[]> _priorityCoords = new ArrayDeque<>();
    protected Weapon _missile;
    protected Weapon _bomb;
    protected Weapon _sonar;
    protected final Random _random = new Random();
    protected final GameConfig _config;
    protected final int _gridSize;

    public PlayerStrategy(GameConfig config) {
        this._config = config;
        this._gridSize = config.getGridSize();
        for (int row = 0; row < _gridSize; row++) {
            for (int col = 0; col < _gridSize; col++) {
                _coordsList.add(new int[] { row, col });
            }
        }
    }   

    public abstract Attack getAttack(ArrayList<AttackResult> previousAttackResults);

    protected int[] getRandomCoordinates(WeaponType weaponType) {
        if (_coordsList.isEmpty()) {
            throw new IllegalStateException("No more coordinates available");
        }

        int index = _random.nextInt(_coordsList.size());
        int lastIndex = _coordsList.size() - 1;
        int[] result = _coordsList.get(index);
        _coordsList.set(index, _coordsList.get(lastIndex));
        _coordsList.remove(lastIndex);

        if (weaponType == WeaponType.BOMB) {
            int[][] directions = { {-1,0}, {1,0}, {0,-1}, {0,1} };

            for (int[] dir : directions) {
                int newRow = result[0] + dir[0];
                int newCol = result[1] + dir[1];

                if (newRow >= 0 && newRow < _gridSize && newCol >= 0 && newCol < _gridSize) {
                    deleteCoordinates(new int[] { newRow, newCol });
                }
            }
        }
        
        return result;
    }

    protected void deleteCoordinates(int[] coords) {
        _coordsList.removeIf(c -> c[0] == coords[0] && c[1] == coords[1]);
    }

    protected boolean isRemainingCoordinate(int row, int col) {
        return _coordsList.stream().anyMatch(c -> c[0] == row && c[1] == col);
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

    protected void setWeaponList(Inventory inventory) {
        this._missile = inventory.getWeaponByType(model.weapon.WeaponType.MISSILE);
        this._bomb = inventory.getWeaponByType(model.weapon.WeaponType.BOMB);
        this._sonar = inventory.getWeaponByType(model.weapon.WeaponType.SONAR);
    }
}
