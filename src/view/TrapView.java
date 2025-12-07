package view;

import model.trap.Trap;

import javax.swing.*;

public class TrapView extends PlaceableView {

    public TrapView(Trap trap, LabeledGridView grid, JLayeredPane topLayer, JPanel originalPanel, int cellSize) {
        super(trap, grid, topLayer, originalPanel, cellSize, switch (trap.getTrapType()) {
            case BLACKHOLE -> "images/blackhole.png";
            case TORNADO -> "images/tornado.png";
        });
    }
}