package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
import cleancode.minesweeper.tobe.position.RelativePosition;

import java.util.List;

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
        Cells cells = Cells.from(board);
        return cells.isAllChecked();
    }

    // 입력받은 좌표가 board의 크기보다 크니?
    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        return cellPosition.isRowIndexMoreThenOrEqual(getRowSize())
                || cellPosition.isColIndexMoreThenOrEqual(getColSize());
    }

    public void initializeGame() {
        // board의 크기만큼 CellPosition을 세팅해서 List<CellPosition> 정보 갖고 있음
        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        // landMineCount만큼의 무작위 List<CellPosition> 반환
        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMineCells(landMinePositions);

        // 파라미터로 주어진 CellPositions를 뺸 나머지 CellPositions를 줘
        // 현재 cellPositions 상태 -> 지뢰 셀까지 세팅
        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
        // 나머지 셀은 주변셀의 지뢰 개수 계산해서 세팅
        initializeNumberCells(numberPositionCandidates);
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for(CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }
    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for(CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for(CellPosition cadidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(cadidatePosition);
            if(count != 0) {
                updateCellAt(cadidatePosition, new NumberCell(count));
            }
        }
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
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
