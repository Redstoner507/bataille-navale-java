package view;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class MainFrame extends JFrame {
    private final CardLayout _cardLayout;
    private final JPanel _container;
    private final Dictionary<String, Component> _dictionary = new Hashtable<>();

    public MainFrame() {
        setTitle("Bataille Navale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _cardLayout = new CardLayout();
        _container = new JPanel(_cardLayout);
        setContentPane(_container);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 720);
        setLocationRelativeTo(null);
    }

    public void addScreen(String name, JPanel screen) {
        if (_dictionary.get(name) != null) removeScreen(name);
        _container.add(screen, name);
        _dictionary.put(name, screen);
    }

    public void removeScreen(String name) {
        Component screen = _dictionary.remove(name);
        _container.remove(screen);
    }

    public void showScreen(String name) {
        _cardLayout.show(_container, name);
    }
}