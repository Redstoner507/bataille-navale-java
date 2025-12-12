package controller;

import view.PlaceableView;

public interface ObjectPlacementListener {
    boolean objectDropped(PlaceableView placeableView, int row, int col);
}
