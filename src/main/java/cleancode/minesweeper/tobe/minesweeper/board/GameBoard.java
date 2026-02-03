package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.board.cell.*;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/*
 * 게임 도메인 로직 관리
 * - 게임 상태 관리
 * - 게임 행위 관리(셀 오픈, 깃발 꽂기)
 * */

public class GameBoard {
    private final Cell[][] board;
    private final int landMineCount;
    private GameStatus gameStatus;

    // 생성자
    public GameBoard(GameLevel gameLevel) {
        board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
        landMineCount = gameLevel.getLandMineCount();
        initalizeGameStatus();
    }

    // [S] public 메서드

    // 상태 변경
    public void initializeGame() {
        initalizeGameStatus();

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

    public void openAt(CellPosition cellPosition) {
        if (isLandMinCellAt(cellPosition)) { // 지뢰 셀을 선택한 경우
            openOneCellAt(cellPosition);
            changeGameStatusToLose();
            return;
        }

        // 지뢰가 아니면 주변 셀 오픈(재귀)
        // openSurroundedCells(cellPosition);
        openSurroundedCells2(cellPosition);

        // 게임 종료 여부 체크
        checkIfGameIsOver();
    }

    public void flagAt(CellPosition cellPosition) {
        findCell(cellPosition).flag();

        checkIfGameIsOver();
    }

    // 판별
    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        // 입력받은 좌표가 board의 크기보다 크니?
        return cellPosition.isRowIndexMoreThenOrEqual(getRowSize())
                || cellPosition.isColIndexMoreThenOrEqual(getColSize());
    }

    public boolean isInProgess() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }

    // 조회
    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }
    // [E] public 메서드

    // [S] private 메서드
    private void initalizeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition cadidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(cadidatePosition);
            if (count != 0) {
                updateCellAt(cadidatePosition, new NumberCell(count));
            }
        }
    }

    private int countNearbyLandMines(CellPosition cellPosition) {
        long count = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize()).stream()
                .filter(this::isLandMinCellAt)
                .count();

        return (int) count;
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNEDE_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy) // 주변 셀의 index가 0 이상인것만
                .map(cellPosition::calculatePositionBy) // 움직인 좌표 반환
                .filter(position -> position.isRowIndexLessThen(rowSize)) // 움직인 좌표가 보드의 범위 안인것만
                .filter(position -> position.isColIndexLessThen(colSize))
                .toList();
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    private void openSurroundedCells(CellPosition cellPosition) {
        // 셀 사인 비교를 메서드 생성해서 비교
        if (isOpenedCell(cellPosition)) { // 이미 열린 셀이면 리턴
            return;
        }
        if (isLandMinCellAt(cellPosition)) { // 지뢰면 리턴
            return;
        }

        // 해당 셀 오픈
        openOneCellAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) { // 주변에 지뢰가 1개 이상이라면 board에 지뢰 개수를 노출하고 리턴
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
        surroundedPositions.forEach(this::openSurroundedCells);
    }

    private void openSurroundedCells2(CellPosition cellPosition) {
        // Stack<CellPosition> stack = new Stack<>();
        Deque<CellPosition> deque = new ArrayDeque<>();

        deque.push(cellPosition); // 사용자가 열겠다고 선택한 셀

        while (!deque.isEmpty()) {
            openAndPushCellAt(deque);
        }
    }

    private void openAndPushCellAt(Deque<CellPosition> deque) {
        CellPosition currentCellPosition = deque.pop();

        if (isOpenedCell(currentCellPosition)) { // 이미 열린 셀이면 리턴
            return;
        }
        if (isLandMinCellAt(currentCellPosition)) { // 지뢰면 리턴
            return;
        }

        // 해당 셀 오픈
        openOneCellAt(currentCellPosition);

        if (doesCellHaveLandMineCount(currentCellPosition)) { // 주변에 지뢰가 1개 이상이라면 board에 지뢰 개수를 노출하고 리턴
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(currentCellPosition, getRowSize(), getColSize());
        for (CellPosition surroundedPosition : surroundedPositions) {
            deque.push(surroundedPosition);
        }
    }


    private void openOneCellAt(CellPosition cellPosition) {
        findCell(cellPosition).open();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        return findCell(cellPosition).isOpened();
    }

    private boolean isLandMinCellAt(CellPosition cellPosition) {
        return findCell(cellPosition).isLandMine();
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        return findCell(cellPosition).hasLandMineCount();
    }

    private void checkIfGameIsOver() {
        if (isAllCellChecked()) { // open -> true는 보드 판을 다 열었다는 것. 게임 종료
            changeGameStatusToWin();
        }
    }

    private boolean isAllCellChecked() {
        // 모든 셀이 다 체크 됐으면 게임 종료
        // 셀 사인 비교를 메서드 생성해서 비교
        Cells cells = Cells.from(board);
        return cells.isAllChecked();
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }

    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

    // [E] private 메서드
}
