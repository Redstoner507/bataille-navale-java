package controller;

import model.GameConfig;
import model.Placeable;
import model.ship.ShipFactory;
import model.ship.ShipType;
import model.trap.TrapFactory;

import java.util.ArrayList;
import java.util.Map;


public class ConfigController {
    private final GameConfig _gameConfig;

    public ConfigController() {
        _gameConfig = new GameConfig();
    }

    public void setGridSize(int gridSize) {
        _gameConfig.setGridSize(gridSize);
    }

    public void setShipNumber(ShipType shipType, int number) {
        _gameConfig.setShipNumber(shipType, number);
    }

    public GameConfig getGameConfig() {
        return _gameConfig;
    }

    public ArrayList<Placeable> getShipConfig() {
        Map<ShipType, Integer> config = _gameConfig.getShipConfig();
        int carriers    = config.get(ShipType.CARRIER);
        int battleShips = config.get(ShipType.BATTLESHIP);
        int cruisers    = config.get(ShipType.CRUISER);
        int submarines  = config.get(ShipType.SUBMARINE);
        int destroyers  = config.get(ShipType.DESTROYER);

        return getShipConfig(carriers, battleShips, cruisers, submarines, destroyers);
    }
    
    public ArrayList<Placeable> getShipConfig(int carriers, int batteShips, int cruisers, int submarines, int destroyer) {
        ArrayList<Placeable> ships = new ArrayList<>();
        ShipFactory factory = new ShipFactory();
        for (int i = 0; i < carriers; i++) {
            ships.add(factory.createCarrier());
        }
        for (int i = 0; i < batteShips; i++) {
            ships.add(factory.createBattleship());
        }
        for (int i = 0; i < cruisers; i++) {
            ships.add(factory.createCruiser());
        }
        for (int i = 0; i < submarines; i++) {
            ships.add(factory.createSubmarine());
        }
        for (int i = 0; i < destroyer; i++) {
            ships.add(factory.createDestroyer());
        }
        return ships;
    }

    public ArrayList<Placeable> getTrapConfig() {
        ArrayList<Placeable> traps = new ArrayList<>();
        TrapFactory factory = new TrapFactory();
        if (!_gameConfig.isIslandModeEnabled()) {
            traps.add(factory.createBlackHole());
            traps.add(factory.createTornado());
        }
        return traps;
    }

    public ArrayList<Placeable> getAllPlaceables() {
        ArrayList<Placeable> placeables = new ArrayList<>();
        placeables.addAll(getShipConfig());
        placeables.addAll(getTrapConfig());
        return placeables;
    }

    public void setIslandModeEnabled(boolean islandsEnabled) {
        this._gameConfig.setIslandModeEnabled(islandsEnabled);
    }

    public void setPlayerStrategy(String playerStrategy) {
        this._gameConfig.setComputerStrategy(playerStrategy);
    }
}
