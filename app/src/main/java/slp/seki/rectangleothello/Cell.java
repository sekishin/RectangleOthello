package slp.seki.rectangleothello;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Cell {

    public enum STATUS {
        Empty,
        White,
        Black,
        Out
    };

    private STATUS status;
    private boolean isTouch;
    private boolean canPut;
    private boolean hintVisible;
    private final int size;
    private final Paint paint;
    private final Rect rect;

    public Cell(STATUS status, int size, int left, int top, int right, int bottom) {
        this.status = status;
        this.size = size;
        this.isTouch = false;
        this.canPut = false;
        this.hintVisible = true;
        paint = new Paint();
        rect = new Rect(left,top,right,bottom);
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setCanPut(boolean flag) {
        this.canPut = flag;
    }

    public void setIsTouch(boolean flag) {
        this.isTouch = flag;
    }

    public boolean getCanPut() {
        return this.canPut;
    }

    public boolean getIsTouch() {
        return this.isTouch;
    }

    public STATUS getStatus() {
        return this.status;
    }

    public void setHintVisible(boolean flag) {
        this.hintVisible = flag;
    }

    public void draw(Canvas canvas) {
        // セル背景描画
        if (this.isTouch) this.paint.setColor(Color.RED);
        else if (this.canPut && this.hintVisible) this.paint.setColor((Color.BLUE));
        else { this.paint.setColor(Color.GREEN); }
        canvas.drawRect(rect,paint);

        // 石描画
        switch (status) {
            case Black:
                this.paint.setColor(Color.BLACK);
                canvas.drawCircle(rect.left+size,rect.top+size,size,paint);
                break;
            case White:
                this.paint.setColor(Color.WHITE);
                canvas.drawCircle(rect.left+size,rect.top+size,size,paint);
                break;
        }

    }

    public static String statusToDisplay(STATUS status) {
        if (status==STATUS.Black) return "黒";
        return "白";
    }
}
