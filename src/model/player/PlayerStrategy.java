package model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Attack;
import model.AttackResult;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

public abstract class PlayerStrategy {
    protected final List<int[]> coordsList = new ArrayList<>();
    protected final Random random = new Random();
    protected final Weapon missile = new WeaponFactory().createMissile();
    protected final Weapon bomb = new WeaponFactory().createBomb();
    protected final Weapon sonar = new WeaponFactory().createSonar();
    protected int bombUses = 1; // Example usage limit (à modfier l'emplacement (inventaire) de cette variable)
    protected int sonarUses = 1; // Example usage limit

    public PlayerStrategy() {
        // Initialize all possible coordinates (0-9, 0-9)
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
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
}
