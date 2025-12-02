package model;

import model.cell.Cell;
import model.cell.CellState;
import model.cell.WaterCell;
import model.ship.Ship;
import model.trap.Trap;
import model.weapon.Weapon;
import view.GridView;

import java.awt.*;
import java.util.ArrayList;

public class Grid {
    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 10;

    private final int _size;
    private final Cell[][] _cells;
    private final ArrayList<Ship> _ships = new ArrayList<>();

    public Grid(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE)
            throw new IllegalArgumentException("Size must be between " + MIN_SIZE + " and " + MAX_SIZE);
        this._size = size;
        this._cells = new Cell[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                _cells[r][c] = new WaterCell();
    }

    /**
     * Tests if the boat's placement coordinates are valid.
     * @return true if the coordinates are valid, false otherwise.
     */
    public boolean canPlaceShip(Ship ship, int startRow, int startCol) {
        for (int i = 0; i < ship.getSize(); i++) {
            int r = startRow;
            int c = startCol;
            if (ship.getOrientation() == Orientation.HORIZONTAL) c += i;
            else r += i;

            if (r < 0 || r >= _size || c < 0 || c >= _size) return false;

            if (_cells[r][c].getState() != CellState.WATER) return false;

        }
        return true;
    }

    /**
     * Places the boat in the grid if the coordinates are valid.
     * @return true if the boat was placed, false otherwise.
     */
    public boolean placeShip(Ship ship, int startRow, int startCol) {
        if (!canPlaceShip(ship, startRow, startCol)) return false;

        ship.setStartPosition(startRow, startCol);
        _ships.add(ship);

        for (Point p : ship.getCoordinates()) {
            try {
                _cells[p.x][p.y].setState(CellState.SHIP);
                _cells[p.x][p.y].setObject(ship);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }

        return true;
    }

    /**
     * Applies the attack to the grid.
     * @param attack The Attack context
     * @param result The AttackResult
     */
    public void applyAttack(Attack attack, AttackResult result) {
        int row = attack.getRealRow();
        int col = attack.getRealCol();

        Cell targetCell = _cells[row][col];

        switch (result.getStatus()) {
            case MISS -> targetCell.setState(CellState.MISS);
            case HIT -> targetCell.setState(CellState.HIT);
            case SUNK -> {
                targetCell.setState(CellState.SUNK);
                Ship ship = (Ship) result.getElementHit();
                GridView playerGridView = attack.getTargetPlayer().getGridView();
                for (Point p : ship.getCoordinates()) {
                    playerGridView.updateCellDisplay(p.x, p.y, AttackStatus.SUNK);
                }
            }
            case TRAP_ACTIVATED -> {
                targetCell.setState(CellState.MISS);
                Trap trap = (Trap) result.getElementHit();
                targetCell.removeObject();
                trap.activate(attack.getAttackingPlayer().getGrid(), row, col);
            }
            case ITEM_FOUND -> {
                targetCell.setState(CellState.MISS);
                Placeable object = result.getElementHit();
                targetCell.removeObject();
                attack.getAttackingPlayer().addObjectToInventory(object);
            }
        }
    }

    public int getSize() {
        return this._size;
    }

    public ArrayList<Ship> getShips() {
        return _ships;
    }

    public Cell getCell(int row, int col) {
        // Vérification de la validité de la cible (hors limites ou déjà touchée)
        if (row < 0 || row >= _size || col < 0 || col >= _size) return null;
        return _cells[row][col];
    }

    public void clearGrid() {
        for (int r = 0; r < _size; r++) {
            for (int c = 0; c < _size; c++) {
                _cells[r][c] = new WaterCell();
            }
        }
        _ships.clear();
    }
}