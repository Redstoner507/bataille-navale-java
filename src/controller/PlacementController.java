package controller;

import model.Grid;
import model.Orientation;
import model.Placeable;
import view.CellView;
import view.GridView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlacementController {
    public PlacementController() {}

    /**
     * Tries to place an object in the grid and updates the view if it was successful.
     * @return true if the object was placed in the grid, false otherwise.
     */
    public boolean placeObject(Grid modelGrid, GridView viewGrid, Placeable object, int row, int col) {
        if (!modelGrid.placeObject(object, row, col)) return false;

        for (Point p : object.getCoordinates()) {
            CellView cell = viewGrid.getCell(p.x, p.y);
            if (cell != null) {
                cell.setCellIcon(object.getCellState());
            }
        }

        return true;
    }

    /**
     * Randomly places all ships in the grid.
     */
    public void randomObjectsPlacement(Grid modelGrid, GridView viewGrid, ArrayList<Placeable> objects) {
        clearGrid(modelGrid, viewGrid);

        Random rand = new Random();

        for (Placeable placeable : objects) {
            boolean placed;
            do {
                // Random orientation
                Orientation orientation =
                    rand.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                placeable.setOrientation(orientation);

                // Random position
                int row = rand.nextInt(modelGrid.getSize());
                int col = rand.nextInt(modelGrid.getSize());

                // Use controller logic
                placed = placeObject(modelGrid, viewGrid, placeable, row, col);
            } while (!placed);
        }
    }

    /**
     * Changes the ship orientation.
     */
    public void rotatePlaceable(Placeable placeable) {
        placeable.setOrientation(
            placeable.getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL
        );
    }

    public void clearGrid(Grid modelGrid, GridView viewGrid) {
        modelGrid.resetGrid();
        viewGrid.clearGrid();
    }
}