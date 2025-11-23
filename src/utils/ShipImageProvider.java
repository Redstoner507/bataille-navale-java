package utils;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShipImageProvider {
    private static final String BASE_PATH = "src/resources/images/ship.png";
    private static Image _segmentBase;

    /**
     * Returns the base ship image with the proper dimensions.
     */
    public static Image getShipSegment(int cellSize) {
        if (_segmentBase == null) {
            _segmentBase = new ImageIcon(BASE_PATH).getImage();
        }

        BufferedImage scaled = new BufferedImage(cellSize, cellSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(_segmentBase, 0, 0, cellSize, cellSize, null);
        g2d.dispose();

        return scaled;
    }
}