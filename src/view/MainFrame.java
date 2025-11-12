package view;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final StartScreen _startScreen;
    private final ConfigScreen _configScreen;
    private PlacementScreen _placementScreen;

    public MainFrame() {
        setTitle("Bataille Navale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        _startScreen = new StartScreen();
        _configScreen = new ConfigScreen();
        _placementScreen = new PlacementScreen(10);

        updateScreen(_startScreen);
        setVisible(true);

        // Navigation
        _startScreen.getPlayButton().addActionListener(_ -> updateScreen(_configScreen));
        _startScreen.getQuitButton().addActionListener(_ -> dispose());

        _configScreen.getBackButton().addActionListener(_ -> updateScreen(_startScreen));
        _configScreen.getStartPlacementButton().addActionListener(_ -> {
            int size = _configScreen.getGridSize();
            _placementScreen = new PlacementScreen(size);
            _placementScreen.getBackButton().addActionListener(_ -> updateScreen(_configScreen));
            updateScreen(_placementScreen);
        });

        _placementScreen.getBackButton().addActionListener(_ -> updateScreen(_configScreen));
    }

    private void updateScreen(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}