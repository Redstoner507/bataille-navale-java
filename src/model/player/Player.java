package model.player;

import model.*;
import model.ship.Ship;
import model.trap.Trap;
import model.trap.TrapType;
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
    public AttackResult receiveAttack(Attack attack) {
        // La logique de Piège (Trou Noir, Tornade) devrait être vérifiée ici ou dans applyAttack.
        // Pour l'instant, nous déléguons directement à la grille.

        // getGrid() est la grille de défense de ce joueur.
        AttackResult result = _grid.applyAttack(attack);

        // --- Logique du Trou Noir (C3) ---
        // Si la case touchée est un Trou Noir (TRAP_ACTIVATED),
        // l'attaque doit être relancée sur la grille de l'ATTAQUANT.

        if (result.getStatus() != AttackStatus.TRAP_ACTIVATED) return result;
        Placeable object = result.getElementHit();
        if (object == null || object.getPlaceableType() != PlaceableType.TRAP) return result;
        Trap trap = (Trap) object;
        if (trap.getTrapType() == TrapType.BLACKHOLE) {
            // Le Contrôleur (GameController) devra gérer la relance de l'attaque sur la grille de l'attaquant.
            // Ici, on retourne le résultat du Trou Noir au Contrôleur pour qu'il orchestre la relance.
            result.setMessage("Trap triggered : BlackHole ! The shot is returned to the sender !");
        }

        // --- Mise à jour des statistiques internes (Nombre de bateaux coulés, etc.) ---
        // Si result.getStatus() est SUNK, mettre à jour le compteur du joueur.

        return result;
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
