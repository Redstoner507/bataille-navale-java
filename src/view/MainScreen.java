package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.grid.GridView;

public class MainScreen extends JFrame {

    private JPanel _mainPanel;

    public MainScreen() {
        setTitle("Warships of Java");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _mainPanel = new GridView(10);
        
        setContentPane(_mainPanel);
    }
    
}
