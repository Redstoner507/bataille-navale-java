package model.trap;

import model.*;
import model.cell.CellType;

public abstract class Trap extends Placeable {
    private final TrapType _trapType;

    public Trap(TrapType type) {
        super(1, Orientation.HORIZONTAL, type.name(), CellType.WATER, PlaceableType.TRAP);
        _trapType = type;
    }

    public abstract void activate(Attack attack, AttackResult result);

    public TrapType getTrapType() {
        return _trapType;
    }

    @Override
    public AttackResult hit() {
        return new AttackResult(AttackStatus.TRAP_ACTIVATED, "You triggered a " + getName() + " !");
    }
}
