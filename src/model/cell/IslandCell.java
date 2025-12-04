package model.cell;

public class IslandCell extends Cell {
    //private boolean _hasWeapon = false;

    public IslandCell() {
        super(CellType.ISLAND);
        setScanned(true); // Pour ne pas pouvoir utiliser le Sonar dessus
    }
}
