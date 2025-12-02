package model.weapon;

import model.Attack;
import model.AttackResult;
import model.Grid;

public class Sonar extends Weapon{

    public Sonar() {
        super(WeaponType.SONAR);
    }

    @Override
    public AttackResult applyAttack(Grid grid, Attack attack) {
        return null;
    }
}
