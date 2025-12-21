package model;

import model.weapon.Weapon;
import model.player.Player;

import java.awt.Point;

/**
 * Represents the different elements that form the attack, including the weapon used and the targeted coordinates.
 */
public class Attack {
    private Player _attackingPlayer; // Le joueur qui lance l'attaque
    private Player _targetPlayer;    // Le joueur qui reçoit l'attaque
    private final Weapon _weapon;    // L'arme utilisée (Missile, Bombe, Sonar)
    // Coordonnées réelles après modification par des pièges (si applicables)
    private final Point _realCoordinates;

    public Attack(Player attackingPlayer, Player targetPlayer, Weapon weapon, int targetRow, int targetCol) {
        this._attackingPlayer = attackingPlayer;
        this._targetPlayer = targetPlayer;
        this._weapon = weapon;
        this._realCoordinates = new Point(targetRow, targetCol);
    }

    public void setAttackingPlayer(Player attackingPlayer) {
        this._attackingPlayer = attackingPlayer;
    }

    public Player getAttackingPlayer() {
        return _attackingPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this._targetPlayer = targetPlayer;
    }

    public Player getTargetPlayer() {
        return _targetPlayer;
    }

    public Weapon getWeapon() {
        return _weapon;
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
    public void setRealCoordinates(Point coords) {
        this._realCoordinates.x = coords.x;
        this._realCoordinates.y = coords.y;
    }
}