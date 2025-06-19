package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {
    private final Cell[][] board;
    private final int landMineCount;

    public GameBoard(GameLevel gameLevel) {
        board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
        landMineCount = gameLevel.getLandMineCount();
    }

    public Cell findCell(int rowIndex, int colIndex) {
        return board[rowIndex][colIndex];
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    public String getSign(int rowIndex, int colIndex) {
        return findCell(rowIndex, colIndex).getSign();
    }

    public void flag(int rowIndex, int colIndex) {
        findCell(rowIndex, colIndex).flag();
    }

    public void open(int rowIndex, int colIndex) {
        findCell(rowIndex, colIndex).open();
    }

    public boolean isLandMinCell(int selectedRowIndex, int selectedColIndex) {
        return findCell(selectedRowIndex, selectedColIndex).isLandMine();
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }

    public boolean isAllCellChecked() {
        // 모든 셀이 다 체크 됐으면 게임 종료
        // 셀 사인 비교를 메서드 생성해서 비교
        return Arrays.stream(board) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .allMatch(Cell::isChecked);
    }

    public void initializeGame() {
        int rowSize = getRowSize();
        int colSize = getColSize();

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                // 빈 셀을 만들어서 보드에 할당
                board[row][col] = new EmptyCell();
            }
        }

        // 지뢰 10개 세팅
        for (int i = 0; i < landMineCount; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            board[landMineRow][landMineCol] = new LandMineCell();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (isLandMinCell(row, col)) { // 지뢰면
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                if(count == 0) {
                    continue;
                }
                board[row][col] = new NumberCell(count);
            }
        }
    }

    public int countNearbyLandMines(int row, int col) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        int count = 0;
        // 주변 8칸 중 지뢰가 몇개인지
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMinCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMinCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < colSize && isLandMinCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMinCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && isLandMinCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && isLandMinCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMinCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && isLandMinCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    public void openSurroundedCells(int row, int col) {
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) { // 판을 벗어나면 리턴
            return;
        }
        // 셀 사인 비교를 메서드 생성해서 비교
        if (isOpenedCell(row, col)) { // 이미 열린 셀이면 리턴
            return;
        }
        if (isLandMinCell(row, col)) { // 지뢰면 리턴
            return;
        }

        // 해당 셀 오픈
        open(row, col);

        if (doesCellHaveLandMineCount(row, col)) { // 주변에 지뢰가 1개 이상이라면 board에 지뢰 개수를 노출하고 리턴
            return;
        }

        // 주변에 있는 8개 셀
        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

}
