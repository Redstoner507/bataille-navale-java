package controller;

import view.ConfigScreen;
import view.MainFrame;
import view.PlacementScreen;
import view.StartScreen;

public class GameController {
    private ConfigScreen _configScreen;
    private StartScreen _startScreen;
    private PlacementScreen _placementScreen;
    private MainFrame _mainFrame;

    public GameController() {
        _mainFrame = new MainFrame(this);         
        _startScreen = new StartScreen(this);
        _mainFrame.setCurrentScreen(_startScreen);
        _mainFrame.setVisible(true);
    }

    public void showStartScreen() {
        _mainFrame.setCurrentScreen(_startScreen);
    }

    public void showConfigScreen() {
        if (_configScreen == null) {
            _configScreen = new ConfigScreen(this);
        }
        _mainFrame.setCurrentScreen(_configScreen);
    }

    public void startPlacementScreen() {
        if (_placementScreen == null) {
            // Example parameters; replace with actual configuration values
            int gridSize = 10;
            //var ships = _configScreen.getConfiguredShips();
            int cellSize = 50; // Example cell size
            ConfigController configController = new ConfigController();
            _placementScreen = new PlacementScreen(this, gridSize, configController.getShipConfig(_configScreen.getShipConfig()), cellSize);
        }
        _mainFrame.setCurrentScreen(_placementScreen);
    }

    public void dispose() {
        _mainFrame.dispose();
    }
}
