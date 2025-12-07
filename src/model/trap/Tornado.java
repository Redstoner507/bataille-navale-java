package model.trap;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.cell.CellState;

public class Tornado extends Trap {

    public Tornado() {
        super(TrapType.TORNADO, CellState.TORNADO, AttackStatus.TORNADO);
    }

    @Override
    public AttackResult activate(Attack attack, AttackResult result) {
        return null;
    }
}
