package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.grid.GridView;

public class MainScreen extends JFrame {

    private int _gridSize;
    private JPanel _mainPanel;

    public MainScreen(int gridSize) {
        _gridSize = gridSize;
        setTitle("Warships of Java");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _mainPanel = new GridView(_gridSize);
        
        setContentPane(_mainPanel);
    }
}
