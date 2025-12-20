package controller;

import model.AttackStatus;
import model.Placeable;
import model.ship.Ship;
import model.weapon.Weapon;
import model.weapon.WeaponType;

import java.util.ArrayList;

public class StatsController {
    private int _shipsUntouched = 0;
    private int _shipsAttacked = 0;
    private int _shipsSunk = 0;
    private int _misses = 0;
    private int _shipCellsHit = 0;
    private int _totalShipCells = 0;
    private int _weaponsAvailable = 0;
    private int _weaponsUsed = 0;
    private int _islandCellsLeft = 0;

    private final ArrayList<Ship> _shipsAttackedList = new ArrayList<>();
    private final ArrayList<Weapon> _weaponsUsedList = new ArrayList<>();

    public StatsController() {}

    public void evaluateAttack(AttackStatus status, Placeable placeableHit, Weapon weaponUsed) {
        if (!_weaponsUsedList.contains(weaponUsed) && weaponUsed.getWeaponType() != WeaponType.MISSILE) {
            _weaponsUsedList.add(weaponUsed);
            _weaponsUsed++;
        }
        switch (status) {
            case MISS -> _misses++;
            case HIT -> {
                _shipCellsHit++;
                Ship ship = (Ship) placeableHit;
                if (!_shipsAttackedList.contains(ship)) {
                    _shipsAttackedList.add(ship);
                    _shipsAttacked++;
                    _shipsUntouched--;
                }
            }
            case SUNK -> {
                _shipCellsHit++;
                _shipsSunk++;
            }
            case ISLAND_MISS -> _islandCellsLeft--;
            case ITEM_FOUND -> {
                _islandCellsLeft--;
                _weaponsAvailable++;
            }
        }
    }

    public int getShipsUntouched() {
        return _shipsUntouched;
    }

    public void setShipsUntouched(int shipsUntouched) {
        _shipsUntouched = shipsUntouched;
    }

    public int getShipsAttacked() {
        return _shipsAttacked;
    }

    public int getShipsSunk() {
        return _shipsSunk;
    }

    public int getMisses() {
        return _misses;
    }

    public int getShipCellsHit() {
        return _shipCellsHit;
    }

    public int getTotalShipCells() {
        return _totalShipCells;
    }

    public void setTotalShipCells(int totalShipCells) {
        _totalShipCells = totalShipCells;
    }

    public int getWeaponsAvailable() {
        return _weaponsAvailable;
    }

    public void setWeaponsAvailable(int weaponsAvailable) {
        _weaponsAvailable = weaponsAvailable;
    }

    public int getWeaponsUsed() {
        return _weaponsUsed;
    }

    public int getIslandCellsLeft() {
        return _islandCellsLeft;
    }

    public void setIslandCellsLeft(int islandCellsLeft) {
        _islandCellsLeft = islandCellsLeft;
    }
}
