package model;

import java.util.HashMap;
import java.util.Map;

public class GameConfig {
    private int _gridSize;
    private boolean _islandsEnabled;
    private Map<String, Integer> _shipConfig;

    public GameConfig() {
        this._gridSize = 10; // Default grid size
        this._islandsEnabled = false; // Default islands setting
        this. _shipConfig = new HashMap<>(); // Default ship configuration
        _shipConfig.put("carrier", 1);
        _shipConfig.put("battleShip", 1);
        _shipConfig.put("cruiser", 1);
        _shipConfig.put("submarine", 1);
        _shipConfig.put("destroyer", 1);
    }

    public int getGridSize() {
        return _gridSize;
    }

    public void setGridSize(int gridSize) {
        this._gridSize = gridSize;
    }

    public boolean isIslandsEnabled() {
        return _islandsEnabled;
    }

    public void setIslandsEnabled(boolean islandsEnabled) {
        this._islandsEnabled = islandsEnabled;
    }

    public Map<String, Integer> getShipConfig() {
        return _shipConfig;
    }

    public void setShipNumber(String shipType, int number) {
        this._shipConfig.put(shipType, number);
    }
}
