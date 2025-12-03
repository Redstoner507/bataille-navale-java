package model.player;

import model.*;
import model.ship.Ship;
import model.trap.Trap;
import model.trap.TrapType;
import model.weapon.Weapon;
import view.GridView;

import java.util.ArrayList;

public class Player {
    private final String _name;
    private final Grid _grid;
    private final GridView _gridView;
    private ArrayList<Placeable> _inventory;
    private PlayerStrategy _strategy;

    public Player(int gridSize, int cellSize, String playerName) {
        _grid = new Grid(gridSize);
        _gridView = new GridView(gridSize, cellSize);
        _name = playerName;
    }

    public void setStrategy(PlayerStrategy strategy) {
        this._strategy = strategy;
    }

    public Attack play(AttackResult attackResult) {
        if (_strategy == null) return null;
        return _strategy.getAttack(attackResult);
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
        // La logique de Piège (Trou Noir, Tornade) devrait être vérifiée ici ou dans applyAttack.
        // Pour l'instant, nous déléguons directement à la grille.

        // getGrid() est la grille de défense de ce joueur.
        Weapon weapon = attack.getWeapon();
        ArrayList<AttackResult> results = weapon.applyAttack(_grid, attack);
        for(AttackResult result : results) {
            _grid.applyAttack(attack, result);
        }

        return results;
    }

    public Grid getGrid() {
        return _grid;
    }

    public GridView getGridView() {
        return _gridView;
    }

    public String getName() {
        return _name;
    }

    public void addObjectToInventory(Placeable placeable) {
        _inventory.add(placeable);
    }
}
