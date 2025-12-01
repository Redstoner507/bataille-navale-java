package model;

import model.cell.CellType;

import java.awt.*;
import java.util.ArrayList;

public abstract class Placeable {
    private final String _name;
    private final int _size;
    private int _startRow = -1;
    private int _startCol = -1;
    private Orientation _orientation;
    private final CellType _cellType;
    private final PlaceableType _placeableType;

    public Placeable(int size, Orientation defaultOrientation, String name, CellType placementCellType, PlaceableType placeableType) {
        _size = size;
        _orientation = defaultOrientation;
        _name = name;
        _cellType = placementCellType;
        _placeableType = placeableType;
    }

    public int getSize() {
        return _size;
    }

    public void setOrientation(Orientation orientation) {
        this._orientation = orientation;
    }

    public PlaceableType getPlaceableType() {
        return _placeableType;
    }

    public Orientation getOrientation() {
        return _orientation;
    }

    public String getName() {
        return _name;
    }

    public void setStartPosition(int row, int col) {
        this._startRow = row;
        this._startCol = col;
    }

    public int getStartRow() {
        return _startRow;
    }

    public int getStartCol() {
        return _startCol;
    }

    public ArrayList<Point> getCoordinates() {
        ArrayList<Point> coordinates = new ArrayList<>();
        for (int i = 0; i < _size; i++) {
            int r = _startRow;
            int c = _startCol;

            switch (_orientation) {
                case HORIZONTAL -> c += i;
                case VERTICAL -> r += i;
            }

            coordinates.add(new Point(r, c));
        }
        return coordinates;
    }
}
