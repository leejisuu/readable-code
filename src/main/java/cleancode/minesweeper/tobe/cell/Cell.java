package cleancode.minesweeper.tobe.cell;

public abstract class Cell {

    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged;
    protected boolean isOpend;

    // 특정
    public abstract boolean isLandMine();

    // 특정
    public abstract boolean hasLandMineCount();

    // 특정
    public abstract String getSign();

    // 공통
    public void flag() {
        this.isFlagged = true;
    }

    // 공통
    public void open() {
        isOpend = true;
    }

    // 공통
    public boolean isChecked() {
        return isFlagged || isOpend;
    }

    // 공통
    public boolean isOpened() {
        return isOpend;
    }
}
