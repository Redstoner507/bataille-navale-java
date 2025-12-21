package view;

import javax.swing.*;

import model.cell.CellState;
import model.cell.CellType;
import model.weapon.WeaponType;
import utils.ImageUtils;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;

public class CellView extends JLabel {  
    private static final Image IMG_SHIP =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/ship.png"))).getImage();
    private static final Image IMG_HIT =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/hit.png"))).getImage();
    private static final Image IMG_MISS =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/miss.png"))).getImage();
    private static final Image IMG_SUNK =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/sunk.png"))).getImage();
    private static final Image IMG_WATER =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/water.png"))).getImage();
    private static final Image IMG_ISLAND =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/island.png"))).getImage();
    private static final Image IMG_ISLAND_HIT =
            new ImageIcon(Objects.requireNonNull(CellView.class.getResource("/images/island_hit.png"))).getImage();
    private static final Image IMG_BLACKHOLE =
            new ImageIcon(ImageUtils.getTrapImage("blackhole")).getImage();
    private static final Image IMG_TORNADO =
            new ImageIcon(ImageUtils.getTrapImage("tornado")).getImage();
    private static final Image IMG_BOMB =
            new ImageIcon(ImageUtils.getWeaponImage("bomb")).getImage();
    private static final Image IMG_SONAR =
            new ImageIcon(ImageUtils.getWeaponImage("sonar")).getImage();

    private Image _baseImage;
    private final ArrayList<Image> _overlays = new ArrayList<>();

    private CellState _cellState;
    private final CellType _cellType;
    private int _sonarCountToDisplay = -1;
    private String _StringShip;
    private int _shipSegment;
    private int _shipOrientation;

    public CellView(int cellSize, CellType cellType) {
        _cellType = cellType;
        setCellIcon(CellState.EMPTY);
        setPreferredSize(new Dimension(cellSize, cellSize));
        _StringShip = "";
        _shipSegment = -1;
    }

    public void setShipInfo(String shipType, int segmentIndex, int orientation) {
        _StringShip = shipType;
        _shipSegment = segmentIndex;
        _shipOrientation = orientation;
    }

    /**
     * Sets the icon of the cell according to its state.
     */
    public void setCellIcon(CellState state) {

        _overlays.clear();
        switch (state) {

            case EMPTY -> {
                if (_cellType == CellType.ISLAND) _baseImage = IMG_ISLAND;
                else _baseImage = IMG_WATER;
            }

            case SHIP -> _overlays.add(ImageUtils.getShipSegement(_StringShip, _shipSegment, _shipOrientation));

            case HIT -> {
                _overlays.add(IMG_SHIP);
                _overlays.add(IMG_HIT);
            }

            case MISS -> _overlays.add(IMG_MISS);

            case SUNK -> {
                _overlays.add(ImageUtils.getShipSegement(_StringShip, _shipSegment, _shipOrientation));
                _overlays.add(IMG_SUNK);
            }

            case ISLAND_HIT -> _overlays.add(IMG_ISLAND_HIT);

            case BLACKHOLE -> _overlays.add(IMG_BLACKHOLE);

            case TORNADO -> _overlays.add(IMG_TORNADO);

            case BOMB -> _overlays.add(IMG_BOMB);

            case SONAR -> _overlays.add(IMG_SONAR);
        }

        _cellState = state;
        repaint();
    }

    public void displayWeaponUsed(WeaponType weaponType) {
        switch (weaponType) {
            case BOMB -> _overlays.add(IMG_BOMB);
            case SONAR -> _overlays.add(IMG_SONAR);
        }
    }


    public void displaySonarScan(int sonarCount) {
        _sonarCountToDisplay = sonarCount;
        revalidate();
        repaint();
    }

    /** 
     * Paints the cell and scales the image to fill the label.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Activer la transparence PNG
        g2d.setComposite(AlphaComposite.SrcOver);

        // 1) Fond
        if (_baseImage != null)
            g2d.drawImage(_baseImage, 0, 0, getWidth(), getHeight(), this);

        // 2) Superposition
        for (Image overlay : _overlays) {
            g2d.drawImage(overlay, 0, 0, getWidth(), getHeight(), this);
        }

        // 3) Sonar
        if (_sonarCountToDisplay >= 0) {
            String text = String.valueOf(_sonarCountToDisplay);
            g2d.setFont(new Font("Arial", Font.BOLD, getHeight() / 2));
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, x, y);
        }

        // Bordure
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }


    /**
     * Overrides addMouseListener to only detect clicks when clickable.
     */
    @Override
    public void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
    }

    public CellState getCellState() {
        return _cellState;
    }
}
