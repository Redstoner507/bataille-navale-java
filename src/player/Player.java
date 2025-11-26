package player;

import model.Grid;
import model.Placeable;

import java.util.ArrayList;

public class Player {
    private final Grid _grid;
    private ArrayList<Placeable> _inventory;
    private PlayerStrategy _strategy;

    public Player(int gridSize) {
        _grid = new Grid(gridSize);
    }

    public void setStrategy(PlayerStrategy strategy) {
        this._strategy = strategy;
    }

    public void play() {
        if (_strategy != null) {
            _strategy.playTurn();
        }
    }

    public Grid getGrid() {
        return _grid;
    }
}
