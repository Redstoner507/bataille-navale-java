package controller;

import java.util.ArrayList;

import model.ship.Ship;
import model.GameConfig;
import view.ConfigScreen;
import view.MainFrame;
import view.PlacementScreen;
import view.StartScreen;

public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private ConfigController _configController;
    ArrayList <Ship> _ships;
    //Screens
    private ConfigScreen _configScreen;
    private StartScreen _startScreen;
    private PlacementScreen _placementScreen;
    private MainFrame _mainFrame;

    public GameController() {
        _mainFrame = new MainFrame(this);         
        _startScreen = new StartScreen(this);
        _mainFrame.setCurrentScreen(_startScreen);
        _mainFrame.setVisible(true);
        _configController = new ConfigController();
    }

    public void showStartScreen() {
        _mainFrame.setCurrentScreen(_startScreen);
    }

    public void showConfigScreen() {
        if (_configScreen == null) {
            _configScreen = new ConfigScreen(this,_configController);
        }
        _mainFrame.setCurrentScreen(_configScreen);
    }

    public void startPlacementScreen() {
        if (_placementScreen == null) {
            GameConfig config = _configController.getGameConfig();
            _placementScreen = new PlacementScreen(this, config.getGridSize(), _configController.getShipConfig(), CELL_SIZE);
        }
        _mainFrame.setCurrentScreen(_placementScreen);
    }   

    public void dispose() {
        _mainFrame.dispose();
    }
}
