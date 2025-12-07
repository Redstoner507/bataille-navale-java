package view;

import javax.swing.*;

import controller.GameController;

import java.awt.*;

public class StartScreen extends JPanel {

    public StartScreen(GameController gameController) {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(contentPanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        JLabel title = new JLabel("Bataille Navale", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(50));

        JButton playButton = new JButton("Play");
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        playButton.setPreferredSize(new Dimension(150, 40));
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.addActionListener(e -> gameController.showConfigScreen());

        JButton quitButton = new JButton("Quit");
        quitButton.setAlignmentX(CENTER_ALIGNMENT);
        quitButton.setPreferredSize(new Dimension(150, 40));
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.addActionListener(e -> gameController.quit());

        contentPanel.add(playButton);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(quitButton);
    }
}
