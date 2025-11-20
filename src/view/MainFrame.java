package view;

import javax.swing.*;
import controller.GameController;

public class MainFrame extends JFrame {
    private final GameController _gameController;

    public MainFrame(GameController gameController) {
        _gameController = gameController;
        setTitle("Bataille Navale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
    }

    public void setCurrentScreen(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}