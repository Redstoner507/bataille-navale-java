package view;

import javax.swing.*;
import controller.GameController;

public class MainFrame extends JFrame {
    //private static final int CELL_SIZE = 50;
    //private final StartScreen _startScreen;
    //private final ConfigScreen _configScreen;
    //private PlacementScreen _placementScreen;
    private JPanel currentScreen;
    private final GameController _gameController;

    public MainFrame(GameController gameController) {
        _gameController = gameController;
        setTitle("Bataille Navale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);



        /* 
        _startScreen = new StartScreen();
        _configScreen = new ConfigScreen(_gameController);

        updateScreen(_startScreen);
        

        // Navigation

        _configScreen.getBackButton().addActionListener(e -> updateScreen(_startScreen));
        _configScreen.getStartPlacementButton().addActionListener(e -> {
            int size = _configScreen.getGridSize();
            _placementScreen = new PlacementScreen(_gameController,size, _configScreen.getShipList(), CELL_SIZE);
            _placementScreen.getBackButton().addActionListener(f -> updateScreen(_configScreen));
            _placementScreen.getBackButton().addActionListener(f -> updateScreen(_configScreen));
            updateScreen(_placementScreen);
        });

        */
    }

    public void setCurrentScreen(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }

    /* 
    private void updateScreen(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
    */
}