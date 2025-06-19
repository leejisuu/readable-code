/*
package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int LAND_MINE_COUNT = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final int BOARD_ROW_SIZE = 8;
    private static final int BOARD_COL_SIZE = 10;
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        // Scanner scanner = new Scanner(System.in); // Scanner 상수화
        initializeGame();

        while (true) {
            try {
                showBoard();

                if (doseUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doseUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch(GameException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 생겼습니다.");
                // e.printStackTrace(); // 안티패턴. 현업에서는 로그를 남겨서 확인한다
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doseUserChooseToPlantFlag(userActionInput)) { // 깃발 꽂기
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) { // 셀을 열겠다
            if (isLandMinCell(selectedRowIndex, selectedColIndex)) { // 지뢰 셀을 선택한 경우
                // 이미 셀에 지뢰 sign이 세팅되어 있으므로 셀에 지뢰 sign 넣어줄 필요 없음
                // BOARD[selectedRowIndex][selectedColIndex] = Cell.ofLandMine();
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        // System.out.println("잘못된 번호를 선택하셨습니다.");
        throw new GameException("잘못된 번호를 선택하셨습니다");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMinCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private static boolean doseUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doseUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doseUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doseUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecekd = isAllCellChecked();
        if (isAllChecekd) { // open -> true는 보드 판을 다 열었다는 것. 게임 종료
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

*/
/*    private static boolean isAllCellOpend() {
        boolean isAllOpend = true;
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
                    isAllOpend = false;
                }
            }
        }
        return isAllOpend;
    }*//*


    private static boolean isAllCellChecked() {
        // 모든 셀이 다 체크 됐으면 게임 종료
        // 셀 사인 비교를 메서드 생성해서 비교
        return Arrays.stream(BOARD) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .allMatch(Cell::isChecked);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if(rowIndex >= BOARD_ROW_SIZE) {
            throw new GameException("잘못된 입력입니다.");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new GameException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                */
/*
                * getter 생성
                * 콘솔에 보드를 그리고 있음
                * Cell에다가 보드를 그려달라고하는게 관심사가 더 분리됨
                * Cell에선 데이터를 꺼내주고 보드를 그리는건 여기서 함
                * *//*

                System.out.print(BOARD[row][col].getSign() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                // 빈 셀을 만들어서 보드에 할당
                BOARD[row][col] = Cell.create();
            }
        }

        // 지뢰 10개 세팅
        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            // 지뢰셀로 설정
            BOARD[row][col].turnOnLandMine();
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMinCell(row, col)) { // 지뢰면
                    // 지뢰면 Cell의 landMineCount를 0으로 세팅하는 코드인데 이미 Cell.create()에서 0으로 세팅하므로 삭제
                    // NEARBY_LAND_MINE_COUNTS[row][col] = 0;
                    continue;
                } // 지뢰가 아닌 칸
                int count = countNearbyLandMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;
        // 주변 8칸 중 지뢰가 몇개인지
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMinCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMinCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMinCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMinCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMinCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMinCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMinCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMinCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) { // 판을 벗어나면 리턴
            return;
        }
        // 셀 사인 비교를 메서드 생성해서 비교
        if (BOARD[row][col].isOpened()) { // 이미 열린 셀이면 리턴
            return;
        }
        if (isLandMinCell(row, col)) { // 지뢰면 리턴
            return;
        }

        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) { // 주변에 지뢰가 1개 이상이라면 board에 지뢰 개수를 노출하고 리턴
            // BOARD를 초기화 할 때 nearbyLandMineCount는 이미 초기화 되어있음. 그러므로 셀만 open하면 된다.
            // BOARD[row][col] = Cell.ofNearbyLandMineCount(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        }

        // 주변에 있는 8개 셀
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
*/
