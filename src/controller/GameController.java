package controller;

import java.io.ObjectInputFilter.Config;

import view.ConfigScreen;
import view.MainFrame;
import view.StartScreen;

public class GameController {
    private ConfigScreen _configScreen;
    private StartScreen _startScreen;
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

    public void dispose() {
        _mainFrame.dispose();
    }
}
