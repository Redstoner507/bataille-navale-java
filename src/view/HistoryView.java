package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class HistoryView extends JPanel {
    private final JTextArea _playerHistoryArea;
    private final JTextArea _computerHistoryArea;

    public HistoryView() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("History"));

        // Panneau pour les deux colonnes
        JPanel columnsPanel = new JPanel(new GridLayout(1, 2));

        // Colonne Joueur Humain
        _playerHistoryArea = createTextArea("Your Attacks");
        columnsPanel.add(new JScrollPane(_playerHistoryArea));

        // Colonne Ordinateur
        _computerHistoryArea = createTextArea("The Enemy's Attacks");
        columnsPanel.add(new JScrollPane(_computerHistoryArea));

        add(columnsPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(300, 400)); // Taille par défaut
    }

    private JTextArea createTextArea(String title) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setText(title + "\n" + "---------------------\n");
        return textArea;
    }

    /**
     * Met à jour les deux colonnes de l'historique.
     * @param playerLog Liste des entrées formatées pour le joueur humain (colonne de gauche).
     * @param computerLog Liste des entrées formatées pour l'ordinateur (colonne de droite).
     */
    public void updateHistory(List<String> playerLog, List<String> computerLog) {
        // Le Contrôleur doit s'assurer que les deux listes ont la même taille
        // ou sont gérées pour être affichées correctement (avec des lignes vides).

        StringBuilder playerText = new StringBuilder("Your Attacks\n---------------------\n");
        for (String entry : playerLog) {
            playerText.append(entry).append("\n");
        }
        _playerHistoryArea.setText(playerText.toString());

        StringBuilder computerText = new StringBuilder("The Enemy's Attacks\n---------------------\n");
        for (String entry : computerLog) {
            computerText.append(entry).append("\n");
        }
        _computerHistoryArea.setText(computerText.toString());

        // Défilement automatique vers le bas pour les deux
        _playerHistoryArea.setCaretPosition(_playerHistoryArea.getDocument().getLength());
        _computerHistoryArea.setCaretPosition(_computerHistoryArea.getDocument().getLength());
    }
}