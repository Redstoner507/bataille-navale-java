package controller;

import model.Grid;
import model.Orientation;
import model.Placeable;

import java.util.ArrayList;
import java.util.Random;

public class PlacementController {
    public PlacementController() {}

    /**
     * Tries to place an object in the grid.
     * @return true if the object was placed in the grid, false otherwise.
     */
    public boolean placeObject(Grid modelGrid, Placeable object, int row, int col) {
        return modelGrid.placeObject(object, row, col);
    }

    /**
     * Randomly places all ships in the grid.
     */
    public void randomObjectsPlacement(Grid modelGrid, ArrayList<Placeable> objects) {
        modelGrid.resetGrid();
        Random rand = new Random();

        for (Placeable placeable : objects) {
            boolean placed;
            int attempts = 0;
            do {
                // Random orientation
                Orientation orientation = rand.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                placeable.setOrientation(orientation);

                // Random position
                int row = rand.nextInt(modelGrid.getSize());
                int col = rand.nextInt(modelGrid.getSize());

                // Use controller logic
                placed = placeObject(modelGrid, placeable, row, col);
                attempts++;
                // Sécurité boucle infinie
                if (attempts > 1000) break;
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
}