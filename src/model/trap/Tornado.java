package model.trap;

import model.Attack;
import model.AttackResult;

public class Tornado extends Trap {

    public Tornado() {
        super(TrapType.TORNADO);
    }

    @Override
    public void activate(Attack attack, AttackResult result) {
    }
}
