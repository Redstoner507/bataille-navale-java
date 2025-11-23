package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout _cardLayout;
    private final JPanel _container;

    public static final String START = "start";
    public static final String CONFIG = "config";
    public static final String PLACEMENT = "placement";

    public MainFrame() {
        setTitle("Bataille Navale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        _cardLayout = new CardLayout();
        _container = new JPanel(_cardLayout);
        setContentPane(_container);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
    }

    public void addScreen(String name, JPanel screen) {
        _container.add(screen, name);
    }

    public void showScreen(String name) {
        _cardLayout.show(_container, name);
    }
}