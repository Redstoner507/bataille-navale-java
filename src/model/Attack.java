package model;

import model.weapon.Weapon;
import model.player.Player;

import java.awt.Point;

/**
 * Represents the different elements that form the attack, including the weapon used and the targeted coordinates.
 */
public class Attack {
    private final Player _attackingPlayer; // Le joueur qui lance l'attaque
    private final Weapon _weapon;          // L'arme utilisée (Missile, Bombe, Sonar)
    // Coordonnées initiales (ce que le joueur a cliqué)
    private final int _targetRow;
    private final int _targetCol;
    // Coordonnées réelles après modification par des pièges (si applicables)
    private Point _realCoordinates;

    public Attack(Player attackingPlayer, Weapon weapon, int targetRow, int targetCol) {
        this._attackingPlayer = attackingPlayer;
        this._weapon = weapon;
        this._targetRow = targetRow;
        this._targetCol = targetCol;
        this._realCoordinates = new Point(targetRow, targetCol);
    }

    public Player getAttackingPlayer() {
        return _attackingPlayer;
    }

    public Weapon getWeapon() {
        return _weapon;
    }

    public int getTargetRow() {
        return _targetRow;
    }

    public int getTargetCol() {
        return _targetCol;
    }

    public Point getRealCoordinates() {
        return _realCoordinates;
    }

    public int getRealRow() {
        return _realCoordinates.x;
    }

    public int getRealCol() {
        return _realCoordinates.y;
    }

    /**
     * Modifies the real coordinates of the attack (used by traps like the Tornado).
     */
    public void setRealCoordinates(int newRow, int newCol) {
        this._realCoordinates = new Point(newRow, newCol);
    }
}