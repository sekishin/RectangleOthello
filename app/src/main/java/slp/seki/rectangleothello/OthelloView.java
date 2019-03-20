package slp.seki.rectangleothello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 14t242 on 2016/07/22.
 */
public class OthelloView extends View implements PlayerCallback {

    private Board board;
    GradientDrawable drawable;
    private Button black;
    private Button white;
    private int canvasWidth;
    private int canvasHeight;
    private int cellSize = 150;
    private boolean paused;
    private Player blackPlayer;
    private Player whitePlayer;


    //-- コンストラクタ
    public OthelloView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paused = false;
        board = new Board();
        setBlackPlayer(new HumanPlayer(Cell.STATUS.Black, "Human", this.board));
        setWhitePlayer(new ComputerPlayer(Cell.STATUS.White, "Computer", this.board));
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

    public void setBlackPlayer(Player player) {
        this.blackPlayer = player;
    }

    public Player getBlackPlayer() {
        return this.blackPlayer;
    }

    public void setWhitePlayer(Player player) {
        this.whitePlayer = player;
    }

    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    public Cell.STATUS getTurn() {
        return this.board.getTurn();
    }

    public void relateButton(Button black, Button white) {
        this.black = black;
        this.white = white;
    }

    public void initBoard() {
        board.init(canvasWidth, canvasHeight, cellSize);
        invalidate();
        callPlayer();
    }

    public void setHintVisible(boolean flag) {
        this.board.setHintVisible(flag);
        invalidate();
    }

    //-- 盤面描画
    public void drawBoard(Canvas canvas) {
        if (!board.isPlayableState()) {
            this.canvasWidth = canvas.getWidth();
            this.canvasHeight = canvas.getHeight();
            initBoard();
        }
        this.black.setText("●：" + Integer.toString(this.board.countCells(Cell.STATUS.Black)));
        this.white.setText("○：" + Integer.toString(this.board.countCells(Cell.STATUS.White)));
        canvas.drawColor(Color.BLACK);
        board.draw(canvas);
    }

    //-- 画面をタッチしたら
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Player p = this.board.getTurn()== Cell.STATUS.Black ? blackPlayer : whitePlayer;
        if (p == null || !p.isHuman()) return true;
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

    public void putStone(int x, int y) {
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
        callPlayer();
    }
    int resColor(int res) {
        return getResources().getColor(res);
    }

    @Override
    public void onEndThinking(Point pos) {
        if (pos == null) return;
        if (this.board.canPut(pos.x, pos.y)) return;
        if (paused) return;
        putStone(pos.x, pos.y);
    }

    @Override
    public void onProgress() {
        invalidate();
    }

    @Override
    public void onPointStarted(Point pos) {
        Cell cell = this.board.getCell(pos);
        invalidate(cell.getRect());
    }

    @Override
    public void onPointEnded(Point pos) {
        Cell cell = this.board.getCell(pos);
        invalidate(cell.getRect());
    }

    public void callPlayer() {
        if (paused) return;
        if (!this.board.isPlayableState()) return;;

        Player p = this.board.getTurn()== Cell.STATUS.Black ? blackPlayer : whitePlayer;
        if (p != null) {
            p.startThinking(this);
        }

    }



}
