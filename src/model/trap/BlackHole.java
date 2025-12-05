package model.trap;

import model.Attack;
import model.AttackResult;
import model.Grid;
import model.cell.Cell;

import java.awt.*;

public class BlackHole extends Trap {

    public BlackHole() {
        super(TrapType.BLACKHOLE);
    }

    @Override
    public void activate(Attack attack, AttackResult result) {
        Grid targetGrid = attack.getAttackingPlayer().getGrid();
        Point hitCoordinates = result.getHitCoordinates();
        Cell targetCell = targetGrid.getCell(hitCoordinates.x, hitCoordinates.y);
        result = targetCell.hit();
        targetGrid.applyAttack(attack, result);
    }
}
