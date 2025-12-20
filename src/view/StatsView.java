package view;

import controller.StatsController;

import javax.swing.*;
import java.awt.*;

public class StatsView extends JPanel {
    private final JLabel _shipsUntouched = new JLabel();
    private final JLabel _shipsAttacked = new JLabel();
    private final JLabel _shipsSunk = new JLabel();
    private final JLabel _misses = new JLabel();
    private final JLabel _shipsHitRatio = new JLabel();
    private final JLabel _weaponUseRatio = new JLabel();
    private final JLabel _islandCellsLeft = new JLabel();

    public StatsView() {
        setLayout(new BorderLayout());

        // Conteneur pour le centrage
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        add(centerWrapper, BorderLayout.CENTER);

        // Conteneur principal pour tous les éléments
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        // Ajouter le contentPanel au GridBagLayout
        centerWrapper.add(contentPanel, new GridBagConstraints());

        // Titre
        JLabel titleLabel = new JLabel("Stats", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalGlue());

        // Stats
        _shipsUntouched.setAlignmentX(CENTER_ALIGNMENT);
        _shipsAttacked.setAlignmentX(CENTER_ALIGNMENT);
        _shipsSunk.setAlignmentX(CENTER_ALIGNMENT);
        _misses.setAlignmentX(CENTER_ALIGNMENT);
        _shipsHitRatio.setAlignmentX(CENTER_ALIGNMENT);
        _weaponUseRatio.setAlignmentX(CENTER_ALIGNMENT);
        _islandCellsLeft.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(_shipsUntouched);
        contentPanel.add(_shipsAttacked);
        contentPanel.add(_shipsSunk);
        contentPanel.add(_misses);
        contentPanel.add(_shipsHitRatio);
        contentPanel.add(_weaponUseRatio);
        contentPanel.add(_islandCellsLeft);
    }

    public void update(StatsController statsController) {
        _shipsUntouched.setText("Ships untouched: " + statsController.getShipsUntouched());
        _shipsAttacked.setText("Ships attacked: " + statsController.getShipsAttacked());
        _shipsSunk.setText("Ships sunk: " + statsController.getShipsSunk());
        _misses.setText("Misses: " + statsController.getMisses());
        _shipsHitRatio.setText("Ship cells hit / Total ship cells: " + statsController.getShipCellsHit() + " / " + statsController.getTotalShipCells());
        _weaponUseRatio.setText("Weapons available / Weapons used: " + statsController.getWeaponsAvailable() + " / " + statsController.getWeaponsUsed());
        _islandCellsLeft.setText("Islands cells left to search: " + statsController.getIslandCellsLeft());
    }
}
