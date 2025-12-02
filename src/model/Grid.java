package model;

import model.cell.Cell;
import model.cell.CellState;
import model.cell.WaterCell;
import model.ship.Ship;
import model.trap.Trap;

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
     * @param attack The Attack object that contains the element and the target.
     * @return AttackResult The result of the attack.
     */
    public AttackResult applyAttack(Attack attack) {
        // Pour l'attaque de base (Missile), nous utilisons les coordonnées réelles
        // (qui sont les mêmes que les coordonnées ciblées, sauf si la Tornade les a modifiées).
        int row = attack.getRealRow();
        int col = attack.getRealCol();

        // Vérification de la validité de la cible (hors limites ou déjà touchée)
        if (row < 0 || row >= _size || col < 0 || col >= _size) {
            return new AttackResult(AttackStatus.MISS, "Shot beyond the limits !");
        }

        Cell targetCell = _cells[row][col];

        if (targetCell.getState() != CellState.WATER
                && targetCell.getState() != CellState.ISLAND
                && targetCell.getState() != CellState.SHIP) {
            return new AttackResult(AttackStatus.ALREADY_VISITED, "Already hit.");
        }

        // Si c'est un Sonar, la logique serait différente (ne fait pas targetCell.hit())
        if (attack.getWeapon().getName().equals("Sonar")) {
            // Logique du Sonar : compter les bateaux dans la zone 3x3 et retourner SONAR_RESULT
            return new AttackResult(AttackStatus.SONAR_RESULT, "Sonar scan completed.");
        }

        // Marquer la case comme touchée
        targetCell.hit();
        Placeable element = targetCell.getObject(); // Element sur la case (Ship, Trap, null)

        // Traitement de l'élément touché
        if (element == null) {
            // Case vide / eau
            targetCell.setState(CellState.MISS);
            return new AttackResult(AttackStatus.MISS, "You missed !");

        } else if (element.getPlaceableType() == PlaceableType.SHIP) {
            // Bateau touché
            Ship ship = (Ship) element;
            ship.hit();

            if (ship.isSunk()) {
                for (Point p : ship.getCoordinates()) {
                    attack.getTargetPlayer().getGridView().updateCellDisplay(p.x, p.y, AttackStatus.SUNK);
                }
                targetCell.setState(CellState.SUNK);
                return new AttackResult(AttackStatus.SUNK, "Hit ! The " + ship.getName() + " was sunk !", ship);
            } else {
                targetCell.setState(CellState.HIT);
                return new AttackResult(AttackStatus.HIT, "Hit !", ship);
            }

        } else if (element.getPlaceableType() == PlaceableType.TRAP) {
            // On active le piège
            Trap trap = (Trap) element;
            targetCell.removeObject();
            trap.activate(attack.getAttackingPlayer().getGrid(), row, col);
            targetCell.setState(CellState.HIT);
            return new AttackResult(AttackStatus.TRAP_ACTIVATED, "A trap was triggered !", trap);

        } else if (element.getPlaceableType() == PlaceableType.WEAPON) {
            // Récupérer l'arme
            targetCell.removeObject();
            attack.getAttackingPlayer().addObjectToInventory(element);
            targetCell.setState(CellState.ISLAND_HIT);
            return new AttackResult(AttackStatus.ITEM_FOUND, "You found an item : " + element.getName(), element);

        } else {
            targetCell.setState(CellState.MISS);
            return new AttackResult(AttackStatus.MISS, "Shot without effect.");
        }
    }

    public int getSize() {
        return this._size;
    }

    public ArrayList<Ship> getShips() {
        return _ships;
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