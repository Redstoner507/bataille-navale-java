package model;

import controller.StatsController;
import model.cell.Cell;
import model.cell.CellState;
import model.cell.IslandCell;
import model.cell.WaterCell;
import model.ship.Ship;
import model.trap.Trap;
import model.weapon.Weapon;
import view.GridView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Grid {
    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 10;

    private static final int ISLAND_START = 3;
    private static final int ISLAND_END = 7;

    private static final int TORNADO_DURATION = 3;

    private final int _size;
    private final Cell[][] _cells;
    private final ArrayList<Placeable> _objects = new ArrayList<>();
    private final boolean _islandMode;
    private int _rowOffset = 0;
    private int _colOffset = 0;
    private int _tornadoEffect = 0;
    private final StatsController _statsController = new StatsController();

    public Grid(int size, boolean islandMode) {
        if (size < MIN_SIZE || size > MAX_SIZE)
            throw new IllegalArgumentException("Size must be between " + MIN_SIZE + " and " + MAX_SIZE);
        this._size = size;
        this._islandMode = islandMode;
        this._cells = new Cell[size][size];
        resetGrid();
    }

    /**
     * Tests if the object's placement coordinates are valid.
     * @return true if the coordinates are valid, false otherwise.
     */
    public boolean canPlaceObject(Placeable object, int startRow, int startCol) {
        for (int i = 0; i < object.getSize(); i++) {
            int r = startRow;
            int c = startCol;
            if (object.getOrientation() == Orientation.HORIZONTAL) c += i;
            else r += i;

            if (!isCoordinateValid(r, c)) return false;
            Cell cell = _cells[r][c];
            if (cell.getType() != object.getPlacementCellType()) return false;
            if (cell.getState() != CellState.EMPTY) return false;
        }
        return true;
    }

    /**
     * Places the object in the grid if the coordinates are valid.
     * @return true if the object was placed, false otherwise.
     */
    public boolean placeObject(Placeable object, int startRow, int startCol) {
        if (!canPlaceObject(object, startRow, startCol)) return false;

        object.setStartPosition(startRow, startCol);
        _objects.add(object);

        for (Point p : object.getCoordinates()) {
            _cells[p.x][p.y].setState(object.getCellState());
            _cells[p.x][p.y].setObject(object);
        }

        return true;
    }


    /**
     * Applies the attack to the grid.
     * @param attack The Attack context
     * @param result The AttackResult
     */
    public void applyAttack(Attack attack, AttackResult result) {
        Point hitCoordinates = result.getHitCoordinates();
        Cell cellHit = getCell(hitCoordinates.x, hitCoordinates.y);

        GridView playerGridView = attack.getTargetPlayer().getGridView();

        _statsController.evaluateAttack(result.getStatus(), cellHit.getObject(), result.getWeapon());

        switch (result.getStatus()) {
            case MISS -> cellHit.setState(CellState.MISS);
            case HIT -> {
                if (cellHit.getState() != CellState.SUNK) cellHit.setState(CellState.HIT);
            }
            case SUNK -> {
                cellHit.setState(CellState.SUNK);
                Ship ship = (Ship) cellHit.getObject();
                for (Point p : ship.getCoordinates()) {
                    playerGridView.updateCellDisplay(result, p.x, p.y);
                }
            }
            case BOMB_FOUND, SONAR_FOUND -> {
                cellHit.setState(CellState.ISLAND_HIT);
                Weapon weapon = (Weapon) cellHit.getObject();
                cellHit.removeObject();
                attack.getAttackingPlayer().addToInventory(weapon);
            }
            case ISLAND_MISS -> cellHit.setState(CellState.ISLAND_HIT);
            case SONAR_RESULT -> cellHit.setHit(false);
            case BLACKHOLE -> {
                cellHit.setState(CellState.BLACKHOLE);
                Trap trap = (Trap) cellHit.getObject();
                cellHit.removeObject();
                AttackResult counterSignal = trap.activate(attack, result);
                result.setAttackToRelaunch(counterSignal.getAttackToRelaunch());
                playerGridView.updateCellDisplay(result, hitCoordinates.x, hitCoordinates.y);
            }
            case TORNADO -> {
                cellHit.setState(CellState.TORNADO);
                Trap trap = (Trap) cellHit.getObject();
                cellHit.removeObject();
                trap.activate(attack, result);
                playerGridView.updateCellDisplay(result, hitCoordinates.x, hitCoordinates.y);
                activateTornadoEffect();
            }
            case ALREADY_VISITED -> {
                // No state change
            }
        }
    }

    /**
     * Checks if a coordinate is on the island.
     */
    public boolean isCoordinateInIsland(int row, int col) {
        return row >= ISLAND_START && row < ISLAND_END &&
                col >= ISLAND_START && col < ISLAND_END;
    }

    public int getSize() {
        return this._size;
    }

    public ArrayList<Ship> getShips() {
        ArrayList<Ship> ships = new ArrayList<>();
        for (Placeable object : _objects) {
            if (object.getCellState() == CellState.SHIP) {
                ships.add((Ship) object);
            }
        }
        return ships;
    }

    public Cell getCell(int row, int col) {
        // Vérification de la validité de la cible (hors limites ou déjà touchée)
        if (!isCoordinateValid(row, col)) {
            throw new IllegalArgumentException("Coordinates must be between " + MIN_SIZE + " and " + MAX_SIZE + "\n" +
            "Row : " + row + ", Col : " + col);
        }
        return _cells[row][col];
    }

    public void resetGrid() {
        for (int r = 0; r < _size; r++) {
            for (int c = 0; c < _size; c++) {
                if (_islandMode && isCoordinateInIsland(r, c)) {
                    _cells[r][c] = new IslandCell();
                } else {
                    _cells[r][c] = new WaterCell();
                }
            }
        }
        _objects.clear();
    }

    public boolean isCoordinateValid(int row, int col) {
        return (row >= 0 & row < _size && col >= 0 && col < _size);
    }

    public void activateTornadoEffect() {
        _tornadoEffect = TORNADO_DURATION;
    }

    public Point applyOffset(Point realCoordinates) {
        if (_tornadoEffect <= 0) return realCoordinates;

        Random random = new Random();
        _rowOffset = random.nextInt(1, _size);
        _colOffset = random.nextInt(1, _size);

        int realRow = (realCoordinates.x + _rowOffset) % _size;
        int realCol = (realCoordinates.y + _colOffset) % _size;
        return new Point(realRow, realCol);
    }

    public void decreaseEffects(AttackResult baseResult) {
        if (baseResult.getStatus() == AttackStatus.ALREADY_VISITED) return;
        _tornadoEffect--;
        if (_tornadoEffect <= 0) {
            _rowOffset = 0;
            _colOffset = 0;
        }
    }

    public StatsController getStatsController() {
        return _statsController;
    }

    public int getIslandSize() {
        return ISLAND_END - ISLAND_START;
    }
}