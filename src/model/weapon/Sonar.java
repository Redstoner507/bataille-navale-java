package model.weapon;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.Grid;
import model.cell.Cell;
import model.cell.CellState;
import model.cell.CellType;

import java.util.ArrayList;

public class Sonar extends Weapon{

    public Sonar(int nbUses) {
        super(WeaponType.SONAR, CellState.SONAR, nbUses);
    }

    @Override
    public AttackResult hit() {
        return new AttackResult(AttackStatus.SONAR_FOUND, "You found a " + getName() + " !");
    }

    @Override
    public ArrayList<AttackResult> applyAttack(Grid grid, Attack attack) {
        ArrayList<AttackResult> sonarResults = new ArrayList<>();

        int centerRow = attack.getRealRow();
        int centerCol = attack.getRealCol();

        Cell centerCell = grid.getCell(centerRow, centerCol);
        // Ne pas pouvoir utiliser le sonar sur l'île ou une case déjà scannée
        if (centerCell.getType() == CellType.ISLAND || centerCell.isScanned()) {
            AttackResult result = new AttackResult(AttackStatus.ALREADY_VISITED, "");
            result.setWeapon(attack.getWeapon());
            result.setHitCoordinates(centerRow, centerCol);
            sonarResults.add(result);
            return sonarResults;
        }

        int detectedShips = 0;

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
                if (targetState == CellState.SHIP
                        || targetState == CellState.BLACKHOLE
                        || targetState == CellState.HIT
                        || targetState == CellState.SUNK) {
                    detectedShips++;
                }
            }
        }

        AttackResult result = new AttackResult(AttackStatus.SONAR_RESULT, "You detected " + detectedShips + " !");
        result.setHitCoordinates(centerRow, centerCol);
        result.setSonarCount(detectedShips);
        result.setWeapon(attack.getWeapon());
        sonarResults.add(result);

        centerCell.setScanned(true);

        return sonarResults;
    }
}
