package model.player;

import model.*;
import model.ship.Ship;
import model.weapon.Weapon;
import model.weapon.WeaponType;
import model.weapon.WeaponFactory;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String _name;
    private final Grid _grid;
    private final Inventory _inventory;
    private PlayerStrategy _strategy;

    public Player(int gridSize, String playerName, boolean islandMode) {
        _grid = new Grid(gridSize, islandMode);
        _name = playerName;
        _inventory = new Inventory();
        _inventory.addWeapon(new WeaponFactory().createMissile());
        if (!islandMode) {
            _inventory.addWeapon(new WeaponFactory().createBomb(1));
            _inventory.addWeapon(new WeaponFactory().createSonar(1));
        }
    }

    public void setStrategy(PlayerStrategy strategy) {
        this._strategy = strategy;
        if (this._strategy != null) {
            this._strategy.setWeaponList(_inventory);
        }
    }

    public Attack play(ArrayList<AttackResult> attackResults) {
        if (_strategy == null) return null;
        return _strategy.getAttack(attackResults);
    }

    /**
     * Checks if all the player's ships are sunk.
     * @return true if all the ships are sunk, otherwise false.
     */
    public boolean allShipsSunk() {
        for (Ship ship : _grid.getShips()) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return !_grid.getShips().isEmpty();
    }

    /**
     * Handles an attack received by a player.
     * @param attack The Attack object encapsulating the weapon and the target.
     * @return AttackResult The result of the attack.
     */
    public ArrayList<AttackResult> receiveAttack(Attack attack) {

        Weapon weapon = attack.getWeapon();
        Point realCoords = _grid.applyOffset(attack.getRealCoordinates());
        attack.setRealCoordinates(realCoords);
        ArrayList<AttackResult> results = weapon.applyAttack(_grid, attack);
        if (results.isEmpty()) return results;
        AttackResult baseResult = results.getFirst();
        if (baseResult != null) {
            _grid.decreaseEffects();
        }
        for (AttackResult result : results) {
            _grid.applyAttack(attack, result);
        }

        return results;
    }

    public Grid getGrid() {
        return _grid;
    }

    public String getName() {
        return _name;
    }

    public void addToInventory(Weapon weapon) {
        _inventory.addWeapon(weapon);
    }

    public Weapon getWeaponFromInventory(WeaponType type) {
        return _inventory.getWeaponByType(type);
    }
}
