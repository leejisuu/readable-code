package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;

public interface OutputHandler {

    public void showGameStartComments();

    public void showBoard(GameBoard gameBoard);

    public void showGameWinningComment();

    public void showGameLosingComment();

    public void showCommentForSelectingCell();

    public void showCommentForUserAction();
    public void showExceptionMessage(GameException e);

    public void showSimpleMessage(String message);
}
