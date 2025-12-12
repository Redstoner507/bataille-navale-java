package utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public class ImageUtils {

    private static final BufferedImage SPRITE_SHEET;

    static { // bloc static = exécuté une seule fois
        try {
            SPRITE_SHEET = ImageIO.read(Objects.requireNonNull(
                    ImageUtils.class.getResource("/images/Sprites_Bataille_Navale.png")
            ));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le sprite sheet", e);
        }
    }

    public static BufferedImage getSprite(int col, int row) {
        int size = 50;
        return SPRITE_SHEET.getSubimage(col * size, row * size, size, size);
    }

    public static ImageIcon getSpriteIcon(int col, int row) {
        return new ImageIcon(getSprite(col, row));
    }

    public static BufferedImage rotate(BufferedImage img, double angleDegrees) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        // Pour garder le pixel art net
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Rotation autour du centre
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(angleDegrees), w / 2.0, h / 2.0);
        g2d.setTransform(at);

        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
}

