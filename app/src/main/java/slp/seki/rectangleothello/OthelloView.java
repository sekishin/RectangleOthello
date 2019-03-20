package slp.seki.rectangleothello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 14t242 on 2016/07/22.
 */
public class OthelloView extends View {

    private Board board;
    GradientDrawable drawable;
    private Button black;
    private Button white;
    private int canvasWidth;
    private int canvasHeight;
    private int cellSize = 150;


    //-- コンストラクタ
    public OthelloView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    public Cell.STATUS getTurn() {
        return this.board.getTurn();
    }

    public void relateButton(Button black, Button white) {
        this.black = black;
        this.white = white;
    }

    public void initBoard() {
        board = new Board(canvasWidth, canvasHeight, cellSize);
        invalidate();
    }

    public void setHintVisible(boolean flag) {
        this.board.setHintVisible(flag);
        invalidate();
    }

    //-- 盤面描画
    public void drawBoard(Canvas canvas) {
        if (board == null) {
            this.canvasWidth = canvas.getWidth();
            this.canvasHeight = canvas.getHeight();
            board = new Board(canvasWidth, canvasHeight, cellSize);
        }
        this.black.setText("●：" + Integer.toString(this.board.countCells(Cell.STATUS.Black)));
        this.white.setText("○：" + Integer.toString(this.board.countCells(Cell.STATUS.White)));
        canvas.drawColor(Color.BLACK);
        board.draw(canvas);
    }

    //-- 画面をタッチしたら
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
            switchButtonColor();
        }
        if (board.countPuttableCell()==0) {
            board.switchPlayer();
            switchButtonColor();
        }

    }

    public void showCountsToast(){
        Cell.STATUS winner = board.getWinner();
        String msg = "Black: " + board.countCells(Cell.STATUS.Black) + "\n"
                + "White: " + board.countCells(Cell.STATUS.White) + "\n\n";

        if (board.isFinished()){
            if (winner != Cell.STATUS.Empty){
                msg += "Winner is: " + Cell.statusToDisplay(winner) + "!!";
            } else {
                msg += "Draw game! ";
            }
        } else {
            if (winner != Cell.STATUS.Empty){
                msg += Cell.statusToDisplay(winner) + " is winning...\n\n";
            }
            msg += board.turnToDisplay() + "'s turn.";
        }
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void switchButtonColor() {
        drawable = new GradientDrawable();

        if (getTurn() == Cell.STATUS.Black) {
            drawable = (GradientDrawable)black.getBackground();
            drawable.setStroke(10, resColor(R.color.Red));
            drawable = (GradientDrawable)white.getBackground();
            drawable.setStroke(10, resColor(R.color.Gray));
        } else if (getTurn() == Cell.STATUS.White) {
            drawable = (GradientDrawable)white.getBackground();
            drawable.setStroke(10, resColor(R.color.Red));
            drawable = (GradientDrawable)black.getBackground();
            drawable.setStroke(10, resColor(R.color.Gray));
        }
    }
    int resColor(int res) {
        return getResources().getColor(res);
    }



}
