package controller;

import model.GameConfig;
import model.Grid;
import view.*;


public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private final ConfigController _configController;
    //Screens
    private final MainFrame _frame;
    //Grids
    private Grid _PlayerGrid;
    private Grid _BotGrid;
    private static final String START = "start";
    private static final String CONFIG = "config";
    private static final String PLACEMENT = "placement";
    private static final String GAME = "game";

    public GameController() {
        _configController = new ConfigController();
        _PlayerGrid = null;
        _BotGrid = null;

        _frame = new MainFrame();

        _frame.addScreen(START, new StartScreen(this));
        _frame.addScreen(CONFIG, new ConfigScreen(this, _configController));

        _frame.setVisible(true);
        showStartScreen();
    }

    public void showStartScreen() {
        _frame.showScreen(START);
    }

    public void showConfigScreen() {
        _frame.showScreen(CONFIG);
    }

    public void showPlacementScreen() {
        PlacementController placementController = new PlacementController();
        GameConfig config = _configController.getGameConfig();
        _PlayerGrid = new Grid(config.getGridSize());
        _BotGrid = new Grid(config.getGridSize());
        PlacementScreen placement = new PlacementScreen(this, config.getGridSize(), _configController.getShipConfig(), CELL_SIZE, placementController, _configController);
        _frame.addScreen(PLACEMENT, placement);
        _frame.showScreen(PLACEMENT);
    }

    public void showGameScreen() {
        GameConfig config = _configController.getGameConfig();
        GameView game = new GameView(_configController.getPlayerGrid(),config.getGridSize(), CELL_SIZE);
        _frame.addScreen(GAME, game);
        _frame.showScreen(GAME);
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
