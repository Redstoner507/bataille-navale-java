package controller;

import model.ship.Ship;
import model.ship.ShipFactory;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;

public class ConfigController {

    public ConfigController() {}

    public ArrayList<Ship> getShipConfig(Dictionary<String, Integer> config) {
        Enumeration<Integer> numbers = config.elements();
        if (!numbers.hasMoreElements()) {
            return null;
        }
        int carriers = numbers.nextElement();   //config.get("carrier");
        int batteShips = numbers.nextElement(); //config.get("batteShip");
        int cruisers = numbers.nextElement();   //config.get("cruiser");
        int submarines = numbers.nextElement(); //config.get("submarine");
        int destroyer = numbers.nextElement();  //config.get("destroyer");
        return getShipConfig(carriers, batteShips, cruisers, submarines, destroyer);
    }

    public ArrayList<Ship> getShipConfig(int carriers, int batteShips, int cruisers, int submarines, int destroyer) {
        ArrayList<Ship> ships = new ArrayList<>();
        ShipFactory factory = new ShipFactory();
        for (int i=0; i<carriers; i++) {
            ships.add(factory.createCarrier());
        }
        for (int i=0; i<batteShips; i++) {
            ships.add(factory.createBattleShip());
        }
        for (int i=0; i<cruisers; i++) {
            ships.add(factory.createCruiser());
        }
        for (int i=0; i<submarines; i++) {
            ships.add(factory.createSubmarine());
        }
        for (int i=0; i<destroyer; i++) {
            ships.add(factory.createDestroyer());
        }
        return ships;
    }
}
