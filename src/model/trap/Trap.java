package model.trap;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.Placeable;
import model.Orientation;
import model.PlaceableType;
import model.cell.CellState;
import model.cell.CellType;

public abstract class Trap extends Placeable {
    private final TrapType _trapType;
    private final AttackStatus _attackStatus;

    public Trap(TrapType type, CellState state, AttackStatus attackStatus) {
        super(1, Orientation.HORIZONTAL, type.name(), CellType.WATER, PlaceableType.TRAP, state);
        _trapType = type;
        _attackStatus = attackStatus;
    }

    public abstract AttackResult activate(Attack attack, AttackResult result);

    public TrapType getTrapType() {
        return _trapType;
    }

    @Override
    public AttackResult hit() {
        return new AttackResult(_attackStatus, "You triggered a " + getName() + " !");
    }
}
