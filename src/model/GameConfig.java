package model;

import model.ship.ShipType;

import java.util.HashMap;
import java.util.Map;

public class GameConfig {
    private int _gridSize;
    private boolean _islandModeEnabled;
    private final Map<ShipType, Integer> _shipConfig;

    public GameConfig() {
        this._gridSize = 10; // Default grid size
        this._islandModeEnabled = false; // Default islands setting
        this. _shipConfig = new HashMap<>(); // Default ship configuration
        _shipConfig.put(ShipType.CARRIER, 1);
        _shipConfig.put(ShipType.BATTLESHIP, 1);
        _shipConfig.put(ShipType.CRUISER, 1);
        _shipConfig.put(ShipType.SUBMARINE, 1);
        _shipConfig.put(ShipType.DESTROYER, 1);
    }

    public int getGridSize() {
        return _gridSize;
    }

    public void setGridSize(int gridSize) {
        this._gridSize = gridSize;
    }

    public boolean isIslandModeEnabled() {
        return _islandModeEnabled;
    }

    public void setIslandModeEnabled(boolean islandsEnabled) {
        this._islandModeEnabled = islandsEnabled;
    }

    public Map<ShipType, Integer> getShipConfig() {
        return _shipConfig;
    }

    public void setShipNumber(ShipType shipType, int number) {
        this._shipConfig.put(shipType, number);
    }
}
