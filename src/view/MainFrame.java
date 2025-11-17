package view;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static final int CELL_SIZE = 50;
    private final StartScreen _startScreen;
    private final ConfigScreen _configScreen;
    private PlacementScreen _placementScreen;

    public MainFrame() {
        setTitle("Bataille Navale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        _startScreen = new StartScreen();
        _configScreen = new ConfigScreen();

        updateScreen(_startScreen);
        setVisible(true);

        // Navigation
        _startScreen.getPlayButton().addActionListener(e -> updateScreen(_configScreen));
        _startScreen.getQuitButton().addActionListener(e -> dispose());

        _configScreen.getBackButton().addActionListener(e -> updateScreen(_startScreen));
        _configScreen.getStartPlacementButton().addActionListener(e -> {
            int size = _configScreen.getGridSize();
            _placementScreen = new PlacementScreen(size, _configScreen.getShipList(), CELL_SIZE);
            _placementScreen.getBackButton().addActionListener(f -> updateScreen(_configScreen));
            _placementScreen.getBackButton().addActionListener(f -> updateScreen(_configScreen));
            updateScreen(_placementScreen);
        });
    }

    private void updateScreen(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}