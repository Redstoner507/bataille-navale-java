package model;

import model.weapon.Weapon;

import java.awt.*;

/**
 * Représents the result of an attack after being applied to the grid.
 * It is used to communicate the status (Hit, Sunk, Miss, etc...) to the Controller/View.
 */
public class AttackResult {

    private final AttackStatus _status; // Le résultat principal du coup
    private final String _message;      // Message à afficher à l'utilisateur
    private Attack _attackToRelaunch;

    // Informations
    private int _rowHit;                // La ligne touchée
    private int _colHit;                // La colonne touchée
    private Integer _sonarCount;        // Résultat du Sonar (nombre d'éléments trouvés dans la zone)
    private Weapon _weapon;             // Arme utilisée

    public AttackResult(AttackStatus status, String message) {
        this._status = status;
        this._message = message;
    }

    public Point getHitCoordinates() {
        return new Point(_rowHit, _colHit);
    }

    public void setHitCoordinates(int row, int col) {
        _rowHit = row;
        _colHit = col;
    }

    public void setSonarCount(int count) {
        this._sonarCount = count;
    }

    public AttackStatus getStatus() {
        return _status;
    }

    public String getMessage() {
        return _message;
    }

    public Integer getSonarCount() {
        return _sonarCount;
    }

    public Attack getAttackToRelaunch() {
        return _attackToRelaunch;
    }

    public void setAttackToRelaunch(Attack attack) {
        this._attackToRelaunch = attack;
    }

    public void setWeapon(Weapon weapon) {
        this._weapon = weapon;
    }

    public Weapon getWeapon() {
        return _weapon;
    }
}