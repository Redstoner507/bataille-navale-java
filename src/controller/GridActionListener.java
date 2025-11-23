package controller;

import view.ShipView;

public interface GridActionListener {
    boolean shipDropped(ShipView shipView, int row, int col);
}
