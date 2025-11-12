package view;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {
    private final JButton _playButton;
    private final JButton _quitButton;

    public StartScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Bataille Navale", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);
        add(Box.createVerticalStrut(50));

        _playButton = new JButton("Play");
        _playButton.setAlignmentX(CENTER_ALIGNMENT);
        add(_playButton);
        add(Box.createVerticalStrut(20));

        _quitButton = new JButton("Quit");
        _quitButton.setAlignmentX(CENTER_ALIGNMENT);
        add(_quitButton);
    }

    public JButton getPlayButton() {
        return _playButton;
    }

    public JButton getQuitButton() {
        return _quitButton;
    }
}
