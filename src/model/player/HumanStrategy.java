package model.player;

import model.Attack;
import model.AttackResult;
import model.GameConfig;

import java.util.ArrayList;

public class HumanStrategy extends PlayerStrategy {
    public HumanStrategy (GameConfig config) {
        super(config); // Assuming a default grid size of 10
    }

    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        return null;
    }
}
