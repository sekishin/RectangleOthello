package slp.seki.rectangleothello;

import android.graphics.Canvas;
import android.graphics.Color;
import slp.seki.rectangleothello.Cell;

/**
 * Created by 14t242 on 2016/07/22.
 */
public class Board {
    private final int cellSize;
    private int horizontalCellCount;
    private int verticalCellCount;
    private Cell[][] cells, tmp;
    private Cell.STATUS player = Cell.STATUS.Black;
    private Cell.STATUS enemy = Cell.STATUS.White;
    private int spaceTop, spaceLeft;
    private int setX, setY;
    private boolean isFinish;

    public Board(int width, int height, int cellSize) {
        this.cellSize = cellSize;
        this.horizontalCellCount = width / cellSize;
        this.verticalCellCount = height / cellSize;
        this.isFinish = false;
        spaceTop = height % cellSize / 2;
        spaceLeft = width % cellSize / 2;
        cells = createBoard();
        tmp = createBoard();
        checkPutPosition();
    }

    private Cell[][] createBoard() {
        int centerY = verticalCellCount/2;
        int centerX = horizontalCellCount/2;
        if (horizontalCellCount%2 == 0) { centerX--; }
        if (verticalCellCount%2 == 0) { centerY--; }

        Cell[][] board = new Cell[verticalCellCount][horizontalCellCount];

        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                Cell.STATUS status = Cell.STATUS.Empty;
                int left = x * cellSize + 1 + spaceLeft;
                int top = y * cellSize + 1 + spaceTop;
                int right = left + cellSize - 2;
                int bottom = top + cellSize - 2;
                if ((y == centerY && x == centerX) || (y == centerY+1 && x == centerX+1)) {
                    status = Cell.STATUS.Black;
                }
                if ((y == centerY+1 && x == centerX) || (y == centerY && x == centerX+1)) {
                    status = Cell.STATUS.White;
                }
                if (horizontalCellCount%2==1) {
                    if ((y == centerY && x == centerX-1)) {
                        status = Cell.STATUS.White;
                    }
                    if (y == centerY+1 && x == centerX-1) {
                        status = Cell.STATUS.Black;
                    }
                }
                if (verticalCellCount%2==1) {
                    if ((y == centerY-1 && x == centerX)) {
                        status = Cell.STATUS.White;
                    }
                    if (y == centerY-1 && x == centerX+1) {
                        status = Cell.STATUS.Black;
                    }
                }
                if (verticalCellCount%2==1 && horizontalCellCount%2==1) {
                    if (y == centerY-1 && x == centerX-1) {
                        status = Cell.STATUS.Black;
                    }
                }

                board[y][x] = new Cell(status, cellSize/2, left, top, right, bottom);
            }
        }
        return board;
    }

    public void draw(Canvas canvas) {
        checkPutPosition();
        int yLength = cells.length;
        for (int y = 0; y < yLength; y++) {
            int xLength = cells[y].length;
            for (int x = 0; x < xLength; x++) {
                cells[y][x].draw(canvas);
            }
        }
    }

    public int countCells(Cell.STATUS status) {
        int count = 0;
        int yLength = cells.length;
        for (int y = 0; y < yLength; y++) {
            int xLength = cells[y].length;
            for (int x = 0; x < xLength; x++) {
                if (cells[y][x].getStatus()==status) count++;
            }
        }
        return count;
    }

    public boolean isFinished() {
        if (countCells(Cell.STATUS.Empty)==0) {
            isFinish = true;
        } else {
            isFinish = false;
        }
        return isFinish;
    }

    public Cell.STATUS getWinner() {
        int whileCount = countCells(Cell.STATUS.White);
        int blackCount = countCells(Cell.STATUS.Black);
        if (whileCount > blackCount) return Cell.STATUS.White;
        if (blackCount > whileCount) return Cell.STATUS.Black;
        return Cell.STATUS.Empty;
    }

    public Cell.STATUS getTurn() {
        return this.player;
    }

    public void copyBoard() {
        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                this.tmp[y][x].setStatus(this.cells[y][x].getStatus());
            }
        }
    }

    public void setHintVisible(boolean flag) {
        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                this.cells[y][x].setHintVisible(flag);
            }
        }
    }

    public String turnToDisplay() {
        return Cell.statusToDisplay(this.player);
    }

    private void checkPutPosition() {
        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                this.cells[y][x].setCanPut(false);
                if (canPut(x, y)) {
                    this.cells[y][x].setCanPut(true);
                }
            }
        }
    }

    public boolean canPut(float x, float y) {
        this.setX = (int) ((x - spaceLeft) / cellSize);
        this.setY = (int) ((y - spaceTop) / cellSize);
        if ( setX < 0 || setY < 0 ) { return false; }
        if ( setX >= horizontalCellCount || setY > verticalCellCount ) { return false; }
        if ( cells[setY][setX].getStatus() != Cell.STATUS.Empty ) { return  false; }
        copyBoard();
        return boardTurn(this.tmp);
    }

    public boolean canPut(int x, int y) {
        this.setX = x;
        this.setY = y;
        if ( setX >= horizontalCellCount || setY >= verticalCellCount ) { return false; }
        if ( setX < 0 || setY < 0 ) { return false; }
        if ( cells[setY][setX].getStatus() != Cell.STATUS.Empty ) { return  false; }
        copyBoard();
        return boardTurn(this.tmp);
    }

    public void put() {
        boardTurn(this.cells);
    }

    public void setTouch(float x, float y) {
        this.setX = (int) ((x - spaceLeft) / cellSize);
        this.setY = (int) ((y - spaceTop) / cellSize);
        if ( setX >= horizontalCellCount || setY >= verticalCellCount ) { return; }
        if ( setX < 0 || setY < 0 ) { return; }
        for (int i = 0; i < verticalCellCount; i++) {
            for (int j = 0; j < horizontalCellCount; j++) {
                this.cells[i][j].setIsTouch(false);
            }
        }
        this.cells[setY][setX].setIsTouch(true);

    }

    private boolean boardTurn(Cell[][] board) {
        int count = 0;
        count += boardTurnDir(board, +1, -1);
        count += boardTurnDir(board, +1, 0);
        count += boardTurnDir(board, +1, +1);
        count += boardTurnDir(board, 0, -1);
        count += boardTurnDir(board, 0, +1);
        count += boardTurnDir(board, -1, -1);
        count += boardTurnDir(board, -1, 0);
        count += boardTurnDir(board, -1, +1);
        if (count == 0) { return  false; }
        return true;
    }

    private int boardTurnDir(Cell[][] board, int dx, int dy) {
        int len = 0;
        int tx = setX;
        int ty = setY;

        //-- はさんでいるか判定
        while ( true ) {
            //- 現在位置を指定方向に更新
            tx += dx;
            ty += dy;
            //- 現在位置が相手の駒なら連長を増分
            if ( valueOfCell(ty, tx, board) == enemy ) { len++; }
            //- 連長が正で自分の駒なら打ち切り
            else if ( len > 0 && valueOfCell(ty, tx, board) == player ) { break; }
            //- どちらでもない(盤外か空マス)なら0を返す
            else { return 0; }
        }
        //-- 駒をひっくり返す
        while ( true ) {
            //- 現在位置から指定方向の逆へ更新
            tx -= dx;
            ty -= dy;
            //- 駒が相手の駒でないなら打ち切り
            if ( valueOfCell(ty, tx, board) != enemy ) { break; }
            //- 自分の駒に更新
            board[ty][tx].setStatus(player);
        }
        //-- 打つ位置に自分の駒を置く
        board[ty][tx].setStatus(player);
        //-- 返却
        return len;
    }

    private Cell.STATUS valueOfCell(int y, int x, Cell[][] board) {
        if ( y >= verticalCellCount || x >= horizontalCellCount ) { return Cell.STATUS.Out; }
        if ( y < 0 || x < 0 ) { return Cell.STATUS.Out; }
        return board[y][x].getStatus();
    }

    public void switchPlayer() {
        if ( player == Cell.STATUS.Black ) {
            player = Cell.STATUS.White;
            enemy = Cell.STATUS.Black;
        } else {
            player = Cell.STATUS.Black;
            enemy = Cell.STATUS.White;
        }
        checkPutPosition();
    }

    public int countPuttableCell() {
        int count=0;
        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                if (cells[y][x].getCanPut()) { count++; }
            }
        }
        return count;
    }

    public int getPlayerColor() {
        if (this.player== Cell.STATUS.Black) return Color.BLACK;
        return Color.WHITE;

    }






}
