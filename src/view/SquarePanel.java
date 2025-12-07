package view;

import javax.swing.*;
import java.awt.*;

/**
 * Un panneau qui force son composant enfant unique à rester carré
 * et centré, quelle que soit la taille de ce panneau.
 */
public class SquarePanel extends JPanel {

    public SquarePanel(Component component) {
        setLayout(null); // On gère le layout manuellement
        add(component);
        setOpaque(false);
    }

    @Override
    public void doLayout() {
        if (getComponentCount() > 0) {
            Component child = getComponent(0);
            Dimension size = getSize();

            // On prend la plus petite dimension pour faire un carré
            int side = Math.min(size.width, size.height);

            // Calcul pour centrer le carré dans le rectangle disponible
            int x = (size.width - side) / 2;
            int y = (size.height - side) / 2;

            child.setBounds(x, y, side, side);
        }
    }

    // Important pour que le GridBagLayout sache qu'on préfère être carré
    @Override
    public Dimension getPreferredSize() {
        if (getComponentCount() > 0) {
            return getComponent(0).getPreferredSize();
        }
        return super.getPreferredSize();
    }
}