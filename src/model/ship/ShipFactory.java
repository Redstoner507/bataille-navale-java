package model.ship;

public class ShipFactory {

    public ShipFactory() {}

    public Ship createCarrier() {
        return new Carrier();
    }

    public Ship createBattleship() {
        return new Battleship();
    }

    public Ship createCruiser() {
        return new Cruiser();
    }

    public Ship createSubmarine() {
        return new Submarine();
    }

    public Ship createDestroyer() {
        return new Destroyer();
    }
}
