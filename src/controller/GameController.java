package controller;

import model.GameConfig;
import model.Grid;
import view.ConfigScreen;
import view.MainFrame;
import view.PlacementScreen;
import view.StartScreen;


public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private final ConfigController _configController;
    //Screens
    private final MainFrame _frame;
    //Grids
    private Grid _PlayerGrid;
    private Grid _BotGrid;

    public GameController() {
        _configController = new ConfigController();
        _PlayerGrid = null;
        _BotGrid = null;

        _frame = new MainFrame();

        _frame.addScreen(MainFrame.START, new StartScreen(this));
        _frame.addScreen(MainFrame.CONFIG, new ConfigScreen(this, _configController));

        _frame.setVisible(true);
        showStartScreen();
    }

    public void showStartScreen() {
        _frame.showScreen(MainFrame.START);
    }

    public void showConfigScreen() {
        _frame.showScreen(MainFrame.CONFIG);
    }

    public void startPlacementScreen() {
        PlacementController placementController = new PlacementController();
        GameConfig config = _configController.getGameConfig();
        PlacementScreen placement = new PlacementScreen(this, config.getGridSize(), _configController.getShipConfig(), CELL_SIZE, placementController);
        _PlayerGrid = new Grid(config.getGridSize());
        _BotGrid = new Grid(config.getGridSize());
        _frame.addScreen(MainFrame.PLACEMENT, placement);
        _frame.showScreen(MainFrame.PLACEMENT);
    }

    public Grid getPlayerGrid() {
        return _PlayerGrid;
    }

    public Grid getBotGrid() {
        return _BotGrid;
    }
    
    public void quit() {
        _frame.dispose();
    }
}
