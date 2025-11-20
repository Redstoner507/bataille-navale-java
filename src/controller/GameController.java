package controller;

import view.ConfigScreen;
import view.MainFrame;
import view.PlacementScreen;
import view.StartScreen;

import java.util.Dictionary;

public class GameController {
    private static final int CELL_SIZE = 50;
    private final MainFrame _frame;

    public GameController() {
        _frame = new MainFrame();

        _frame.addScreen(MainFrame.START, new StartScreen(this));
        _frame.addScreen(MainFrame.CONFIG, new ConfigScreen(this));

        _frame.setVisible(true);
        showStartScreen();
    }

    public void showStartScreen() {
        _frame.showScreen(MainFrame.START);
    }

    public void showConfigScreen() {
        _frame.showScreen(MainFrame.CONFIG);
    }

    public void startPlacementScreen(Dictionary<String, Integer> shipConfig, int gridSize) {
        ConfigController configController = new ConfigController();
        PlacementController placementController = new PlacementController();

        var placement = new PlacementScreen(this, gridSize, configController.getShipConfig(shipConfig), CELL_SIZE, placementController);

        _frame.addScreen(MainFrame.PLACEMENT, placement);
        _frame.showScreen(MainFrame.PLACEMENT);
    }

    public void quit() {
        _frame.dispose();
    }
}
