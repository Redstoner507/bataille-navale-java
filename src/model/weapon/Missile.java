package model.weapon;

import model.Attack;
import model.AttackResult;
import model.Grid;
import model.cell.Cell;

public class Missile extends Weapon{

    public Missile() {
        super(WeaponType.MISSILE);
    }

    @Override
    public AttackResult applyAttack(Grid grid, Attack attack) {
        int row = attack.getTargetRow();
        int col = attack.getTargetCol();
        Cell cell = grid.getCell(row, col);
        return cell.hit();
    }
}
