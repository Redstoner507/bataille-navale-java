package controller;

import model.Grid;
import model.Orientation;
import model.ship.Ship;
import view.CellView;
import view.GridView;

import java.awt.*;

public class PlacementController {
    private final Grid _modelGrid;
    private final GridView _viewGrid;

    public PlacementController(Grid modelGrid, GridView viewGrid) {
        this._modelGrid = modelGrid;
        this._viewGrid = viewGrid;
    }

    /**
     * Tries to place a ship in the grid and updates the view if it was successful.
     * @return true if the ship was placed in the grid, false otherwise.
     */
    public boolean placeShip(Ship ship, int row, int col) {
        if (!_modelGrid.placeShip(ship, row, col)) return false;

        for (Point p : ship.getCoordinates()) {
            CellView cell = _viewGrid.getCell(p.x, p.y);
            if (cell != null) {
                cell.setSegmentImage();
            }
        }

        return true;
    }

    /**
     * Changes the ship orientation.
     */
    public void rotateShip(Ship ship) {
        ship.setOrientation(
                ship.getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL
        );
    }

    /*
    public void resetGrid() {
        _modelGrid.resetGrid();
        _viewGrid.resetGridView();
    }
     */
    
}