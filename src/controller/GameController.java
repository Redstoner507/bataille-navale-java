package controller;

import java.util.ArrayList;

import model.ship.Ship;
import model.GameConfig;
import view.ConfigScreen;
import view.MainFrame;
import view.PlacementScreen;
import view.StartScreen;

import java.util.Dictionary;

public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private ConfigController _configController;
    ArrayList <Ship> _ships;
    //Screens
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
        PlacementController placementController = new PlacementController();
        GameConfig config = _configController.getGameConfig();
        PlacementScreen placement = new PlacementScreen(this, config.getGridSize(), _configController.getShipConfig(), CELL_SIZE, placementController);

        _frame.addScreen(MainFrame.PLACEMENT, placement);
        _frame.showScreen(MainFrame.PLACEMENT);
    }

    public void quit() {
        _frame.dispose();
    }
}
