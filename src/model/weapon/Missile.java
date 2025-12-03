package model.weapon;

import model.Attack;
import model.AttackResult;
import model.Grid;
import model.cell.Cell;

import java.util.ArrayList;

public class Missile extends Weapon{

    public Missile() {
        super(WeaponType.MISSILE);
    }

    @Override
    public ArrayList<AttackResult> applyAttack(Grid grid, Attack attack) {
        int row = attack.getTargetRow();
        int col = attack.getTargetCol();
        Cell cell = grid.getCell(row, col);
        ArrayList<AttackResult> results = new ArrayList<>();
        AttackResult result = cell.hit();
        result.setHitCoordinates(row, col);
        results.add(result);
        return results;
    }
}
