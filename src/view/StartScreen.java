package view;

import javax.swing.*;

import controller.GameController;

import java.awt.*;

public class StartScreen extends JPanel {
    private final GameController _gameController;
    private final JButton _playButton;
    private final JButton _quitButton;

    public StartScreen(GameController gameController) {
        _gameController = gameController;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Bataille Navale", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);
        add(Box.createVerticalStrut(50));

        _playButton = new JButton("Play");
        _playButton.setAlignmentX(CENTER_ALIGNMENT);
        _playButton.addActionListener(e -> _gameController.showConfigScreen());
        add(_playButton);
        add(Box.createVerticalStrut(20));

        _quitButton = new JButton("Quit");
        _quitButton.setAlignmentX(CENTER_ALIGNMENT);
        _quitButton.addActionListener(e -> _gameController.dispose());
        add(_quitButton);
    }
}
