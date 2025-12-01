package controller;

import view.ShipView;

public interface ShipPlacementListener {
    boolean shipDropped(ShipView shipView, int row, int col);
}
