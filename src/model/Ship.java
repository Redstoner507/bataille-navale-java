package model;

import java.awt.*;
import java.util.ArrayList;

public class Ship {
    private final ShipType _type;
    private Orientation _orientation = Orientation.HORIZONTAL;
    private int _startRow = -1;
    private int _startCol = -1;

    public Ship(ShipType type) {
        this._type = type;
    }

    public int getSize() {
        return _type.getSize();
    }

    public void setOrientation(Orientation orientation) {
        this._orientation = orientation;
    }

    public Orientation getOrientation() {
        return _orientation;
    }

    public void setStart(int row, int col) {
        this._startRow = row;
        this._startCol = col;
    }

    public int getStartRow() {
        return _startRow;
    }

    public int getStartCol() {
        return _startCol;
    }

    public ShipType getType() {
        return _type;
    }

    public ArrayList<Point> getCoordinates(int startRow, int startCol) {
        ArrayList<Point> coordinates = new ArrayList<>();
        for (int i = 0; i < _type.getSize(); i++) {
            int r = startRow;
            int c = startCol;

            switch (_orientation) {
                case HORIZONTAL -> c += i;
                case VERTICAL -> r += i;
            }

            coordinates.add(new Point(r, c));
        }
        return coordinates;
    }

    public ArrayList<Point> getCoordinates() {
        return getCoordinates(_startRow, _startCol);
    }
}
