package model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Attack;
import model.AttackResult;

public abstract class PlayerStrategy {
    protected final List<int[]> coords = new ArrayList<>();
    protected final Random random = new Random();

    public PlayerStrategy() {
        // Initialize all possible coordinates (0-9, 0-9)
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                coords.add(new int[] { row, col });
            }
        }
    }   

    public abstract Attack getAttack(ArrayList<AttackResult> previousAttackResults);

    protected int[] getRandomCoordinates() {
        if (coords.isEmpty()) {
            throw new IllegalStateException("No more coordinates available");
        }

        int index = random.nextInt(coords.size());
        int lastIndex = coords.size() - 1;
        int[] result = coords.get(index);
        coords.set(index, coords.get(lastIndex));
        coords.remove(lastIndex);
        
        return result;
    }
}
