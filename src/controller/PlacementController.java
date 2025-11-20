package controller;

import model.Grid;
import model.Orientation;
import model.ship.Ship;
import view.CellView;
import view.GridView;

import java.awt.*;

public class PlacementController {
    public PlacementController() {}

    /**
     * Tries to place a ship in the grid and updates the view if it was successful.
     * @return true if the ship was placed in the grid, false otherwise.
     */
    public boolean placeShip(Grid modelGrid, GridView viewGrid, Ship ship, int row, int col) {
        if (!modelGrid.placeShip(ship, row, col)) return false;

        for (Point p : ship.getCoordinates()) {
            CellView cell = viewGrid.getCell(p.x, p.y);
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