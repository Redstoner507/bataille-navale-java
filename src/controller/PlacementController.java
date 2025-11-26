package controller;

import model.Grid;
import model.Orientation;
import model.cell.CellState;
import model.ship.Ship;
import view.CellView;
import view.GridView;
import view.PlacementScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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
                cell.setCellIcon(CellState.SHIP);;
            }
        }

        return true;
    }

    /**
     * Randomly places all ships in the grid.
     */
    public void randomShipsPlacement(PlacementScreen placementScreen, Grid modelGrid, GridView viewGrid, ArrayList<Ship> ships) {
        clearGrid(modelGrid, viewGrid);
        placementScreen.clearShipViews();

        Random rand = new Random();

        for (Ship ship : ships) {
            boolean placed;
            do {
                // Random orientation
                Orientation orientation =
                    rand.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                ship.setOrientation(orientation);

                // Random position
                int row = rand.nextInt(modelGrid.getSize());
                int col = rand.nextInt(modelGrid.getSize());

                // Use controller logic
                placed = placeShip(modelGrid, viewGrid, ship, row, col);
            } while (!placed);
        }
    }

    /**
     * Changes the ship orientation.
     */
    public void rotateShip(Ship ship) {
        ship.setOrientation(
            ship.getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL
        );
    }

    public void clearGrid(Grid modelGrid, GridView viewGrid) {
        modelGrid.clearGrid();
        viewGrid.clearGrid();
    }
}