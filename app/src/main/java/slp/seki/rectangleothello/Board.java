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
    private Cell[][] board;

    public Board(int width, int height, int cellSize) {
        this.cellSize = cellSize;
        this.horizontalCellCount = width / cellSize;
        this.verticalCellCount = height / cellSize;
        board = createBoard();
    }

    private Cell[][] createBoard() {
        if (horizontalCellCount%2 == 0) { horizontalCellCount--; }
        if (verticalCellCount%2 == 0) { verticalCellCount--; }

        Cell[][] board = new Cell[verticalCellCount][horizontalCellCount];

        for (int y = 0; y < verticalCellCount; y++) {
            for (int x = 0; x < horizontalCellCount; x++) {
                int type = Cell.TYPE_EMPTY;
                int left = x * cellSize + 1;
                int top = y * cellSize + 1;
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

    void draw(Canvas canvas) {
        int yLength = board.length;
        for (int y = 0; y < yLength; y++) {
            int xLength = board[y].length;
            for (int x = 0; x < xLength; x++) {
                board[y][x].draw(canvas);
            }
        }
    }


    static class Cell {
        private static final int TYPE_EMPTY = 0;
        private static final int TYPE_WHITE = -1;
        private static final int TYPE_BLACK = +1;

        private final int type;
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
