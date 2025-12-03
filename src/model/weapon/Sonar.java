package model.weapon;

import model.Attack;
import model.AttackResult;
import model.Grid;

import java.util.ArrayList;

public class Sonar extends Weapon{

    public Sonar() {
        super(WeaponType.SONAR);
    }

    @Override
    public ArrayList<AttackResult> applyAttack(Grid grid, Attack attack) {
        return null;
    }
}
