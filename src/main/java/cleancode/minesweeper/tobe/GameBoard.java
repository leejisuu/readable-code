package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.RelativePosition;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GameBoard {
    private final Cell[][] board;
    private final int landMineCount;

    public GameBoard(GameLevel gameLevel) {
        board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
        landMineCount = gameLevel.getLandMineCount();
    }

    public Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    public String getSign(CellPosition cellPosition) {
        return findCell(cellPosition).getSign();
    }

    public void flagAt(CellPosition cellPosition) {
        findCell(cellPosition).flag();
    }

    public void openAt(CellPosition cellPosition) {
        findCell(cellPosition).open();
    }

    public boolean isLandMinCellAt(CellPosition cellPosition) {
        return findCell(cellPosition).isLandMine();
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        return findCell(cellPosition).hasLandMineCount();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        return findCell(cellPosition).isOpened();
    }

    public boolean isAllCellChecked() {
        // 모든 셀이 다 체크 됐으면 게임 종료
        // 셀 사인 비교를 메서드 생성해서 비교
        return Arrays.stream(board) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .allMatch(Cell::isChecked);
    }

    // 입력받은 좌표가 board의 크기보다 크니?
    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        return cellPosition.isRowIndexMoreThenOrEqual(getRowSize())
                || cellPosition.isColIndexMoreThenOrEqual(getColSize());
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
                CellPosition cellPosition = CellPosition.of(row, col);

                if (isLandMinCellAt(cellPosition)) { // 지뢰면
                    continue;
                }
                int count = countNearbyLandMines(cellPosition);
                if(count == 0) {
                    continue;
                }
                board[row][col] = new NumberCell(count);
            }
        }
    }

    public int countNearbyLandMines(CellPosition cellPosition) {
        long count = calculateSurroundedPositions(cellPosition).stream()
                .filter(this::isLandMinCellAt)
                .count();

        return (int) count;
    }

    public void openSurroundedCells(CellPosition cellPosition) {
        // 셀 사인 비교를 메서드 생성해서 비교
        if (isOpenedCell(cellPosition)) { // 이미 열린 셀이면 리턴
            return;
        }
        if (isLandMinCellAt(cellPosition)) { // 지뢰면 리턴
            return;
        }

        // 해당 셀 오픈
        openAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) { // 주변에 지뢰가 1개 이상이라면 board에 지뢰 개수를 노출하고 리턴
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition);
        surroundedPositions.forEach(this::openSurroundedCells);
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition) {
        return RelativePosition.SURROUNEDE_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy) // 주변 셀의 index가 0 이상인것만
                .map(cellPosition::calculatePositionBy) // 움직인 좌표 반환
                .filter(position -> position.isRowIndexLessThen(getRowSize())) // 움직인 좌표가 보드의 범위 안인것만
                .filter(position -> position.isColIndexLessThen(getColSize()))
                .toList();
    }
}
