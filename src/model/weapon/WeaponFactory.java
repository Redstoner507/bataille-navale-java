package model.weapon;

public class WeaponFactory {

    public Weapon createMissile() {
        return new Missile();
    }

    public Weapon createBomb(int nbUses) {
        return new Bomb(nbUses);
    }

    public Weapon createSonar(int nbUses) {
        return new Sonar(nbUses);
    }
}
