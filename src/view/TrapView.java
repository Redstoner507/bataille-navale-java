package view;

import model.trap.Trap;

import javax.swing.*;

public class TrapView extends PlaceableView {

    public TrapView(Trap trap, LabeledGridView grid, JLayeredPane topLayer, JPanel originalPanel, int cellSize) {
        super(trap, grid, topLayer, originalPanel, cellSize, switch (trap.getTrapType()) {
            case BLACKHOLE -> new int[]{0, 2};
            case TORNADO -> new int[]{1, 2};
        });
    }
}