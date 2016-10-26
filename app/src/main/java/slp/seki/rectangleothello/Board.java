package slp.seki.rectangleothello;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by 14t242 on 2016/07/22.
 */
public class Board {
    private final int cellSize;
    private int horizontalCellCount;
    private int verticalCellCount;
    private Cell[][] cells, tmp;
    protected int player = Cell.TYPE_BLACK;
    private int spaceTop, spaceLeft;
    private int setX, setY;

    public Board(int width, int height, int cellSize) {
        this.cellSize = cellSize;
        this.horizontalCellCount = width / cellSize;
        this.verticalCellCount = height / cellSize;
        spaceTop = height % cellSize / 2;
        spaceLeft = width % cellSize / 2;
        cells = createBoard();
        tmp = createBoard();
    }

    private Cell[][] createBoard() {
        if (horizontalCellCount%2 == 0) { horizontalCellCount--; }
        if (verticalCellCount%2 == 0) { verticalCellCount--; }

        Cell[][] board = new Cell[verticalCellCount][horizontalCellCount];

        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                int type = Cell.TYPE_EMPTY;
                int left = x * cellSize + 1 + spaceLeft;
                int top = y * cellSize + 1 + spaceTop;
                int right = left + cellSize - 2;
                int bottom = top + cellSize - 2;
                if ((y == verticalCellCount/2 && x == horizontalCellCount/2) || (y == verticalCellCount/2+1 && x == horizontalCellCount/2+1)) {
                    type = Cell.TYPE_BLACK;
                }
                if ((y == verticalCellCount/2+1 && x == horizontalCellCount/2) || (y == verticalCellCount/2 && x == horizontalCellCount/2+1)) {
                    type = Cell.TYPE_WHITE;
                }
                board[y][x] = new Cell(type, cellSize/2, left, top, right, bottom);
            }
        }
        return board;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(player);
        int yLength = cells.length;
        for (int y = 0; y < yLength; y++) {
            int xLength = cells[y].length;
            for (int x = 0; x < xLength; x++) {
                cells[y][x].draw(canvas);
            }
        }
    }

    public void copyBoard() {
        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                this.tmp[y][x] = this.cells[y][x];
            }
        }
    }

    public boolean canPut(float x, float y) {
        this.setX = (int) ((x - spaceLeft) / cellSize);
        this.setY = (int) ((y - spaceTop) / cellSize);
        if ( setX >= horizontalCellCount || setY > verticalCellCount ) { return false; }
        if ( cells[setY][setX].type != Cell.TYPE_EMPTY ) { return  false; }
        copyBoard();
        return boardTurn(this.tmp);
    }

    public void put() {
        boardTurn(this.cells);
        this.cells[setY][setX].type = player;
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
            if ( valueOfCell(ty, tx, board) == -player ) { len++; }
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
            if ( valueOfCell(ty, tx, board) != -player ) { break; }
            //- 自分の駒に更新
            board[ty][tx].type = player;
        }
        //-- 打つ位置に自分の駒を置く
        board[ty][tx].type = player;
        //-- 返却
        return len;
    }

    private int valueOfCell(int y, int x, Cell[][] board) {
        if ( y >= verticalCellCount || x >= horizontalCellCount ) { return  Cell.TYPE_OUT; }
        if ( y < 0 || x < 0 ) { return Cell.TYPE_OUT; }
        return board[y][x].type;
    }

    public void changePlayerColor() {
        if ( player == Cell.TYPE_BLACK ) { player = Cell.TYPE_WHITE; }
        else { player = Cell.TYPE_BLACK; }
    }


    class Cell {
        private static final int TYPE_EMPTY = 0;
        private static final int TYPE_WHITE = -1;
        private static final int TYPE_BLACK = +1;
        private static final int TYPE_OUT = -2;

        private int type;
        private final int size;
        private final Paint paint;
        final Rect rect;

        private Cell(int type, int size, int left, int top, int right, int bottom) {
            this.type = type;
            this.size = size;
            paint = new Paint();
            rect = new Rect(left,top,right,bottom);
        }

        private void draw(Canvas canvas) {
            paint.setColor(Color.GREEN);
            canvas.drawRect(rect,paint);
            switch (type) {
                case TYPE_BLACK:
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle(rect.left+size,rect.top+size,size,paint);
                    break;
                case TYPE_WHITE:
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(rect.left+size,rect.top+size,size,paint);
                    break;
            }

        }
    }



}
