package model.weapon;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.Grid;
import model.cell.Cell;
import model.cell.CellState;

import java.util.ArrayList;

public class Sonar extends Weapon{

    public Sonar() {
        super(WeaponType.SONAR);
    }

    @Override
    public ArrayList<AttackResult> applyAttack(Grid grid, Attack attack) {
        int centerRow = attack.getTargetRow();
        int centerCol = attack.getTargetCol();

        int detectedShips = 0;

        ArrayList<AttackResult> sonarResults = new ArrayList<>();

        int[][] offsets = {
                {-1, -1}, {0, -1}, {1, -1},
                {-1,  0}, {0,  0}, {1,  0},
                {-1,  1}, {0,  1}, {1,  1},
        };

        for (int[] offset : offsets) {
            int currentRow = centerRow + offset[0];
            int currentCol = centerCol + offset[1];

            if (grid.isCoordinateValid(currentRow, currentCol)) {
                Cell targetCell = grid.getCell(currentRow, currentCol);
                CellState targetState = targetCell.getState();
                if (targetState == CellState.SHIP || targetState == CellState.HIT || targetState == CellState.SUNK) {
                    detectedShips++;
                }
            }
        }

        AttackResult result = new AttackResult(AttackStatus.SONAR_RESULT, "You detected " + detectedShips + " !");
        result.setHitCoordinates(centerRow, centerCol);
        result.setSonarCount(detectedShips);
        sonarResults.add(result);

        return sonarResults;
    }
}
