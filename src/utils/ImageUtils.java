package utils;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public class ImageUtils {

    public static BufferedImage getShipSegment(String type, int segment, int orientation) {
        if (type == null || type.isEmpty()) {
            System.err.println("Avertissement : Tentative de chargement d'un segment de bateau sans nom.");
            // Retourne une image transparente vide de 50x50 par défaut pour éviter le crash
            return new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        }

        int size = 50;
        String location = "/images/ships/" + type.toLowerCase() + ".png";
        BufferedImage shipSeg;
        try {
            var resource = ImageUtils.class.getResource(location);
            if (resource == null) {
                throw new IOException("Fichier introuvable : " + location);
            }
            shipSeg = ImageIO.read(resource);
        } catch (IOException e) {
            // En phase de debug, on peut imprimer l'erreur pour voir quel nom de fichier est fautif
            System.err.println("Erreur de chargement : " + e.getMessage());
            throw new RuntimeException("Impossible de charger le sprite sheet du bateau: " + type, e);
        }
        BufferedImage segmentImg = shipSeg.getSubimage(0, segment * size, size, size);
        if (orientation == 0) { // 0 = horizontal
            return segmentImg;
        } else { // 1 = vertical
            return rotate(segmentImg, 270);
        }
    }

    public static BufferedImage getTrapImage(String trapType) {
        String location = "/images/traps/" + trapType + ".png";
        BufferedImage trapSheet;
        try {
            trapSheet = ImageIO.read(Objects.requireNonNull(
                    ImageUtils.class.getResource(location)
            ));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le sprite sheet des pièges.", e);
        }
        return trapSheet;
    }

    public static BufferedImage getWeaponImage(String weaponType) {
        String location = "/images/weapon/" + weaponType + ".png";
        BufferedImage weaponSheet;
        try {
            weaponSheet = ImageIO.read(Objects.requireNonNull(
                    ImageUtils.class.getResource(location)
            ));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le sprite sheet des armes.", e);
        }
        return weaponSheet;
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

