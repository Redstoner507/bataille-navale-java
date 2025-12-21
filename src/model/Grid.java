package model;

import model.cell.Cell;
import model.cell.CellState;
import model.cell.IslandCell;
import model.cell.WaterCell;
import model.ship.Ship;
import model.ship.ShipType;
import model.trap.Trap;
import model.weapon.Weapon;
import model.weapon.WeaponType;

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
    private int _tornadoEffect = 0;

    private final ArrayList<GridListener> _listeners = new ArrayList<>();

    public Grid(int size, boolean islandMode) {
        if (size < MIN_SIZE || size > MAX_SIZE)
            throw new IllegalArgumentException("Size must be between " + MIN_SIZE + " and " + MAX_SIZE);
        this._size = size;
        this._islandMode = islandMode;
        this._cells = new Cell[size][size];
        resetGrid();
    }

    public void addListener(GridListener listener) {
        _listeners.add(listener);
    }

    private void notifyCellUpdated(int row, int col) {
        for (GridListener listener : _listeners) {
            listener.onCellUpdated(row, col, _cells[row][col]);
        }
    }

    private void notifyGridReset() {
        for (GridListener listener : _listeners) {
            listener.onGridReset();
        }
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
            // notification automatique
            notifyCellUpdated(p.x, p.y);
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
            case MISS, HIT, ISLAND_MISS, SONAR_RESULT -> handleStandardStates(cellHit, result);

            case SUNK -> handleSunkShip(cellHit);

            case BOMB_FOUND, SONAR_FOUND -> handleItemFound(attack, cellHit, result);

            case BLACKHOLE, TORNADO -> handleTrapActivation(attack, cellHit, result);

            case ALREADY_VISITED -> {
                // Pas de changement
                return;
            }
        }

        // Notification unique
        notifyCellUpdated(hitCoordinates.x, hitCoordinates.y);

        // Ajouter l'effet visuel de l'arme
        if (result.getWeapon() != null) {
            notifyWeaponImpact(hitCoordinates.x, hitCoordinates.y, result.getWeapon().getWeaponType());
        }
    }

    private void handleStandardStates(Cell cell, AttackResult result) {
        if (result.getStatus() == AttackStatus.MISS) {
            cell.setState(CellState.MISS);
        }
        else if (result.getStatus() == AttackStatus.ISLAND_MISS) {
            cell.setState(CellState.ISLAND_HIT);
        }
        else if (result.getStatus() == AttackStatus.HIT && cell.getState() != CellState.SUNK) {
            cell.setState(CellState.HIT);
        }
        else if (result.getStatus() == AttackStatus.SONAR_RESULT) {
            Point hitCoordinates = result.getHitCoordinates();
            notifySonarEffect(hitCoordinates.x, hitCoordinates.y, result.getSonarCount());
        }
    }

    private void handleSunkShip(Cell cell) {
        cell.setState(CellState.SUNK);
        Placeable object = cell.getObject();
        if (object != null) {
            for (Point p : object.getCoordinates()) {
                getCell(p.x, p.y).setState(CellState.SUNK);
                notifyCellUpdated(p.x, p.y);
            }
        }
    }

    private void handleItemFound(Attack attack, Cell cell, AttackResult result) {
        Placeable object = cell.getObject();

        if (object == null || object.getPlaceableType() != PlaceableType.WEAPON) return;

        Weapon weapon = (Weapon) object;
        cell.removeObject();
        attack.getAttackingPlayer().addToInventory(weapon);

        cell.setState(CellState.ISLAND_HIT); // Afficher l'état fouillé
        // Afficher l'état correspondant à l'item trouvé
        CellState state = switch (result.getStatus()) {
            case BOMB_FOUND -> CellState.BOMB_FOUND;
            case SONAR_FOUND -> CellState.SONAR_FOUND;
            default -> CellState.ISLAND_HIT;
        };

        cell.setState(state);
    }

    private void handleTrapActivation(Attack attack, Cell cell, AttackResult result) {
        Placeable object = cell.getObject();

        if (object == null || object.getPlaceableType() != PlaceableType.TRAP) return;

        Trap trap = (Trap) cell.getObject();

        switch (result.getStatus()) {
            case BLACKHOLE -> {
                AttackResult counterSignal = trap.activate(attack, result);
                result.setAttackToRelaunch(counterSignal.getAttackToRelaunch());
            }
            case TORNADO -> {
                trap.activate(attack, result);
                activateTornadoEffect();
            }
        }

        cell.setState(trap.getCellState());
        cell.removeObject();
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
        notifyGridReset();
    }

    public Cell getCell(int row, int col) {
        // Vérification de la validité de la cible (hors limites ou déjà touchée)
        if (!isCoordinateValid(row, col)) {
            throw new IllegalArgumentException("Coordinates must be between " + MIN_SIZE + " and " + MAX_SIZE + "\n" +
                    "Row : " + row + ", Col : " + col);
        }
        return _cells[row][col];
    }

    public boolean isCoordinateValid(int row, int col) {
        return (row >= 0 & row < _size && col >= 0 && col < _size);
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

    public void activateTornadoEffect() {
        _tornadoEffect = TORNADO_DURATION;
    }

    public Point applyOffset(Point realCoordinates) {
        if (_tornadoEffect <= 0) return realCoordinates;

        Random random = new Random();
        int rowOffset = random.nextInt(1, _size);
        int colOffset = random.nextInt(1, _size);

        int realRow = (realCoordinates.x + rowOffset) % _size;
        int realCol = (realCoordinates.y + colOffset) % _size;
        return new Point(realRow, realCol);
    }

    public void decreaseEffects() {
        if (_tornadoEffect > 0) _tornadoEffect--;
    }

    public int getIslandSize() {
        return ISLAND_END - ISLAND_START;
    }

    public boolean hasAliveShip(ShipType type) {
        for (Placeable object : _objects) {
            if (object.getPlaceableType() != PlaceableType.SHIP) continue;
            Ship ship = (Ship) object;
            if (ship.getType() == type && !ship.isSunk()) {
                return true; // Il reste au moins un bateau de ce type vivant.
            }
        }
        return false; // Tous les bateaux de ce type sont coulés.
    }

    private void notifySonarEffect(int row, int col, int count) {
        for (GridListener listener : _listeners) {
            listener.onSonarEffect(row, col, count);
        }
    }

    private void notifyWeaponImpact(int row, int col, WeaponType weapon) {
        for (GridListener listener : _listeners) {
            listener.onWeaponImpact(row, col, weapon);
        }
    }
}