package model;

/**
 * Représents the result of an attack after being applied to the grid.
 * It is used to communicate the status (Hit, Sunk, Miss, etc...) to the Controller/View.
 */
public class AttackResult {

    private final AttackStatus _status;  // Le résultat principal du coup
    private String _message;             // Message à afficher à l'utilisateur

    // Informations optionnelles basées sur le statut
    private final Placeable _elementHit; // Bateau ou Piège touché/coulé/activé
    private Placeable _gainedItem;       // Arme/Piège trouvé (si mode Île)
    private Integer _sonarCount;         // Résultat du Sonar (nombre d'éléments trouvés dans la zone)

    public AttackResult(AttackStatus status, String message, Placeable elementHit) {
        this._status = status;
        this._message = message;
        this._elementHit = elementHit;
    }

    public AttackResult(AttackStatus status, String message) {
        this(status, message, null);
    }

    public void setGainedItem(Placeable item) {
        this._gainedItem = item;
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

    public void setMessage(String message) {
        this._message = message;
    }

    public Placeable getElementHit() {
        return _elementHit;
    }

    public Placeable getGainedItem() {
        return _gainedItem;
    }

    public Integer getSonarCount() {
        return _sonarCount;
    }
}