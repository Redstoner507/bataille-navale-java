package model.weapon;

import model.Attack;
import model.AttackResult;
import model.Grid;
import model.cell.Cell;

import java.util.ArrayList;

public class Bomb extends Weapon{

    public Bomb() {
        super(WeaponType.BOMB);
    }

    @Override
    public ArrayList<AttackResult> applyAttack(Grid grid, Attack attack) {
        int centerRow = attack.getTargetRow();
        int centerCol = attack.getTargetCol();

        ArrayList<AttackResult> impactResults = new ArrayList<>();

        int[][] offsets = {
                {0, 0}, { -1, 0}, {1, 0}, {0, -1}, {0, 1} // Centre + Nord, Sud, Ouest, Est
        };

        for (int[] offset : offsets) {
            int currentRow = centerRow + offset[0];
            int currentCol = centerCol + offset[1];

            if (grid.isCoordinateValid(currentRow, currentCol)) {
                Cell targetCell = grid.getCell(currentRow, currentCol);
                if (!targetCell.isHit()) {
                    AttackResult singleResult = targetCell.hit();
                    singleResult.setHitCoordinates(currentRow, currentCol);
                    impactResults.add(singleResult);
                }
            }
        }
        return impactResults;
    }
}
