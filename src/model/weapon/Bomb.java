package model.weapon;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.Grid;
import model.cell.Cell;
import model.cell.CellType;
import model.cell.CellState;

import java.util.ArrayList;

public class Bomb extends Weapon{

    public Bomb(int nbUses) {
        super(WeaponType.BOMB, CellState.BOMB, nbUses);
    }

    @Override
    public AttackResult hit() {
        return new AttackResult(AttackStatus.BOMB_FOUND, "You found a " + getName() + " !");
    }

    @Override
    public ArrayList<AttackResult> applyAttack(Grid grid, Attack attack) {
        int centerRow = attack.getRealRow();
        int centerCol = attack.getRealCol();

        ArrayList<AttackResult> impactResults = new ArrayList<>();

        int[][] offsets = {
                {0, 0}, { -1, 0}, {1, 0}, {0, -1}, {0, 1} // Centre + Nord, Sud, Ouest, Est
        };

        // Annuler si une des cases touchées fait partie de l'île
        for (int[] offset : offsets) {
            int currentRow = centerRow + offset[0];
            int currentCol = centerCol + offset[1];
            if (!grid.isCoordinateValid(currentRow, currentCol)) continue;
            Cell tagetCell = grid.getCell(currentRow, currentCol);
            if (tagetCell.getType() == CellType.ISLAND) {
                impactResults.add(new AttackResult(AttackStatus.ALREADY_VISITED, ""));
                return impactResults;
            }
        }

        for (int[] offset : offsets) {
            int currentRow = centerRow + offset[0];
            int currentCol = centerCol + offset[1];

            if (grid.isCoordinateValid(currentRow, currentCol)) {
                Cell targetCell = grid.getCell(currentRow, currentCol);

                // Sauter la case si elle est déja touchée
                if (!targetCell.isHit()) {
                    AttackResult singleResult = targetCell.hit();
                    if (currentRow == centerRow && currentCol == centerCol) {
                        singleResult.setWeapon(attack.getWeapon());
                    }
                    else {
                        singleResult.setWeapon(new WeaponFactory().createMissile());
                    }
                    singleResult.setHitCoordinates(currentRow, currentCol);
                    impactResults.add(singleResult);
                }
                if (currentRow == centerRow && currentCol == centerCol && targetCell.getState() == CellState.BLACKHOLE) {
                    // Ne renvoyer qu'un seul résultat si l'impact central touche le trou noir
                    break;
                }
            }
        }
        return impactResults;
    }
}
