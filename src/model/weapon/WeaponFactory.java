package model.weapon;

public class WeaponFactory {

    public Weapon createMissile() {
        return new Missile();
    }

    public Weapon createBomb() {
        return new Bomb();
    }

    public Weapon createSonar() {
        return new Sonar();
    }
}
