package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;

public class Minesweeper implements GameInitializable, GameRunnable {
    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        gameBoard = new GameBoard(gameLevel);
        this.inputHandler = inputHandler;
        this.outputHandler= outputHandler;
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments();

        while (true) {
            try {
                outputHandler.showBoard(gameBoard);

                if (doseUserWinTheGame()) {
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doseUserLoseTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }

                CellPosition cellPosition = getCellInputFromUser();
                UserAction userActionInput = getUserActionInputFromUser();
                actOnCell(cellPosition, userActionInput);
            } catch(GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
            }
        }

    }

    private void actOnCell(CellPosition cellPosition, UserAction userAction) {
        if (doseUserChooseToPlantFlag(userAction)) { // 깃발 꽂기
            gameBoard.flagAt(cellPosition);
            // 깃발을 꽂은 후에 유저가 모든 셀을 체크한거면 이겼으므로 체크
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userAction)) { // 셀을 열겠다
            if (gameBoard.isLandMinCellAt(cellPosition)) { // 지뢰 셀을 선택한 경우
                gameBoard.openAt(cellPosition);
                changeGameStatusToLose();
                return;
            }

            // 지뢰가 아니면 셀 오픈
            gameBoard.openSurroundedCells(cellPosition);
            // 셀 열고 나서도 모든 셀을 체크한건지 다시 체크
            checkIfGameIsOver();
            return;
        }

        throw new GameException("잘못된 번호를 선택하셨습니다");
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private boolean doseUserChooseToOpenCell(UserAction userAction) {
        return userAction == UserAction.OPEN;
    }

    private boolean doseUserChooseToPlantFlag(UserAction userAction) {
        return userAction == UserAction.FLAG;
    }


    private UserAction getUserActionInputFromUser() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserActionFromUser();
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if(gameBoard.isInvalidCellPosition(cellPosition)) {
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

    private boolean doseUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doseUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) { // open -> true는 보드 판을 다 열었다는 것. 게임 종료
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = 1;
    }
}
