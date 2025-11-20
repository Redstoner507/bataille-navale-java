package view;

import javax.swing.*;

import controller.GameController;

import java.awt.*;

public class StartScreen extends JPanel {

    public StartScreen(GameController gameController) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Bataille Navale", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);
        add(Box.createVerticalStrut(50));

        JButton playButton = new JButton("Play");
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        playButton.addActionListener(e -> gameController.showConfigScreen());

        JButton quitButton = new JButton("Quit");
        quitButton.setAlignmentX(CENTER_ALIGNMENT);
        quitButton.addActionListener(e -> gameController.quit());

        add(playButton);
        add(Box.createVerticalStrut(20));
        add(quitButton);
    }
}
