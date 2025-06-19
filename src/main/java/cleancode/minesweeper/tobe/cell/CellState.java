package cleancode.minesweeper.tobe.cell;

public class CellState {

    private boolean isFlagged;
    private boolean isOpend;

    private CellState(boolean isFlagged, boolean isOpend) {
        this.isFlagged = isFlagged;
        this.isOpend = isOpend;
    }

    public static CellState initialize() {
        return new CellState(false, false);
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        isOpend = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpend;
    }

    public boolean isOpened() {
        return isOpend;
    }

    public boolean isFlagged() {
        return isFlagged;
    }
}
