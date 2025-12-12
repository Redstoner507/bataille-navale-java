package model.player;

import model.Attack;
import model.AttackResult;

import java.util.ArrayList;

public class ComputerHardStrategy extends PlayerStrategy {
    public ComputerHardStrategy (int gridSize) {
        super(gridSize); // Assuming a default grid size of 10
    }
    @Override
    public Attack getAttack(ArrayList<AttackResult> previousAttackResults) {
        return null;
    }
}
