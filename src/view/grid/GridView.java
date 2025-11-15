package view.grid;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Dimension;
public class GridView extends JPanel{
    
    private int _gridSize;

    public GridView(int gridsize) {
        _gridSize = gridsize;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // --- Première ligne : coin vide + lettres A–J ---
        // Coin vide (0,0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(""), gbc);

        // Lettres (A-J)
        for (int col = 0; col < _gridSize; col++) {
            gbc.gridx = col + 1;
            JLabel label = new JLabel(String.valueOf((char)('A' + col)), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            add(label, gbc);
        }

        // --- Lignes numérotées + cases ---
        for (int row = 0; row < _gridSize; row++) {

            // Numéro de ligne (colonne 0)
            gbc.gridx = 0;
            gbc.gridy = row + 1;
            JLabel label = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            add(label, gbc);

            for (int col = 0; col < _gridSize; col++) {
                gbc.gridx = col + 1;
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(40, 40));
                cell.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK));
                add(cell, gbc);
            }
        }
    }
}
