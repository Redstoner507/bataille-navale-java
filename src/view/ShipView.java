package view;

import model.Orientation;
import model.Ship;

import javax.swing.*;
import java.awt.*;

public class ShipView extends JPanel {
    private final Ship _ship;

    public ShipView(Ship ship) {
        this._ship = ship;
        setOpaque(false);

        int size = ship.getSize();
        if (ship.getOrientation() == Orientation.HORIZONTAL_LEFT || ship.getOrientation() == Orientation.HORIZONTAL_RIGHT)
            setPreferredSize(new Dimension(size * 32, 32));
        else
            setPreferredSize(new Dimension(32, size * 32));
    }
}
