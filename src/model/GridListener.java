package model;

import model.cell.Cell;
import model.weapon.WeaponType;

public interface GridListener {
    void onCellUpdated(int row, int col, Cell cell);
    void onGridReset();
    void onSonarEffect(int row, int col, int count);
    void onWeaponImpact(int row, int col, WeaponType weapon);
}
