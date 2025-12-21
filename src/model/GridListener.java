package model;

import model.cell.Cell;

public interface GridListener {
    void onCellUpdated(int row, int col, Cell cell);
    void onGridReset();
}
