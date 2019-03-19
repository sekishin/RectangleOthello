package slp.seki.rectangleothello;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 14t242 on 2016/07/22.
 */
public class OthelloView extends View {

    private static final int DRAW_INTERVAL = 1000/60;
    private Board board;
    private final Paint textpaint = new Paint();
    String turn;
    String action;

    //-- コンストラクタ
    public OthelloView(MainActivity context) {
        super(context);
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(40f);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setHintVisible(boolean flag) {
        this.board.setHintVisible(flag);
        invalidate();
    }

    //-- 盤面描画
    public void drawBoard(Canvas canvas) {
        int cellSize = 150;
        if (board == null) {
            board = new Board(canvas.getWidth(), canvas.getHeight(), cellSize);
        }
        canvas.drawColor(board.getPlayerColor());
        board.draw(canvas);
        turn = board.getTurn();
        canvas.drawText("TURN = " + turn, 10, 250, textpaint);
        canvas.drawText("ACTION = " + action, 10, 300, textpaint);
    }

    //-- 画面をタッチしたら
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        action = ""+event.getAction();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                 setTouchedCell(event.getX(), event.getY());
                 break;
            case MotionEvent.ACTION_UP:
                putStone(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_DOWN:
                setTouchedCell(event.getX(), event.getY());
                putStone(event.getX(), event.getY());
                break;
        }
        invalidate();
        return true;
    }

    //-- タッチしたセルをセット
    public void setTouchedCell(float x, float y) {
        board.setTouch(x, y);
    }
    //-- 駒を置く
    public void putStone(float x, float y) {
        if ( board.canPut(x, y) ) {
            board.put();
            board.switchPlayer();
        }
        if (board.countPuttableCell()==0) {
            board.switchPlayer();
        }

    }



}
