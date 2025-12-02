package model.weapon;

import model.Attack;
import model.AttackResult;
import model.Grid;

public class Bomb extends Weapon{

    public Bomb() {
        super(WeaponType.BOMB);
    }

    @Override
    public AttackResult applyAttack(Grid grid, Attack attack) {
        return null;
    }
}
