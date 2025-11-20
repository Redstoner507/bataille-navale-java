package model.ship;

import model.Orientation;
import model.Placeable;

public abstract class Ship extends Placeable {

    public Ship(int size, String name) {
        super(size, Orientation.HORIZONTAL, name);
    }
}
