package controller;

import model.GameConfig;
import model.Grid;
import view.*;


public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private final ConfigController _configController;
    private final PlacementController _placementController;
    //Screens
    private final MainFrame _frame;
    //Grids
    private Grid _playerGrid;
    private Grid _botGrid;
    private LabeledGridView _playerGridView;
    private static final String START = "start";
    private static final String CONFIG = "config";
    private static final String PLACEMENT = "placement";
    private static final String GAME = "game";

    public GameController() {
        _configController = new ConfigController();
        _placementController = new PlacementController();
        _playerGrid = null;
        _botGrid = null;

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
        GameConfig config = _configController.getGameConfig();
        _playerGrid = new Grid(config.getGridSize());
        _botGrid = new Grid(config.getGridSize());
        PlacementScreen placement = new PlacementScreen(this, config.getGridSize(), CELL_SIZE, _placementController, _configController);
        _frame.addScreen(PLACEMENT, placement);
        _frame.showScreen(PLACEMENT);
    }

    public void showGameScreen() {
        GameConfig config = _configController.getGameConfig();
        GameView game = new GameView(_playerGridView, config.getGridSize(), CELL_SIZE, _placementController, _botGrid, _configController);
        _frame.addScreen(GAME, game);
        _frame.showScreen(GAME);
    }

    public Grid getPlayerGrid() {
        return _playerGrid;
    }

    public Grid getBotGrid() {
        return _botGrid;
    }

    public void setPlayerGridView(LabeledGridView playerGridView) {
        this._playerGridView = playerGridView;
    }

    public LabeledGridView getPlayerGridView() {
        return _playerGridView;
    }
    
    public void quit() {
        _frame.dispose();
    }
}
