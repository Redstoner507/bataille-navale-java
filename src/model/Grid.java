package model;

import model.cell.Cell;
import model.cell.CellState;
import model.cell.IslandCell;
import model.cell.WaterCell;
import model.ship.Ship;
import model.trap.Trap;
import view.GridView;

import java.awt.*;
import java.util.ArrayList;

public class Grid {
    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 10;

    private static final int ISLAND_START = 3;
    private static final int ISLAND_END = 7;

    private final int _size;
    private final Cell[][] _cells;
    private final ArrayList<Placeable> _objects = new ArrayList<>();
    private final boolean _islandMode;

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
            if (cell.getType() != object.getCellType()) return false;
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

        switch (result.getStatus()) {
            case MISS -> cellHit.setState(CellState.MISS);
            case HIT -> {
                if (cellHit.getState() != CellState.SUNK) cellHit.setState(CellState.HIT);
            }
            case SUNK -> {
                cellHit.setState(CellState.SUNK);
                Ship ship = (Ship) cellHit.getObject();
                GridView playerGridView = attack.getTargetPlayer().getGridView();
                for (Point p : ship.getCoordinates()) {
                    playerGridView.updateCellDisplay(result, p.x, p.y);
                }
            }
            case TRAP_ACTIVATED -> {
                cellHit.setState(CellState.MISS);
                Trap trap = (Trap) cellHit.getObject();
                cellHit.removeObject();
                trap.activate(attack, result);
            }
            case ITEM_FOUND -> {
                cellHit.setState(CellState.ISLAND_HIT);
                Placeable object = cellHit.getObject();
                cellHit.removeObject();
                attack.getAttackingPlayer().addObjectToInventory(object);
            }
            case ISLAND_MISS -> cellHit.setState(CellState.ISLAND_HIT);
            case SONAR_RESULT -> cellHit.setHit(false);
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
        if (!isCoordinateValid(row, col)) return null;
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
}