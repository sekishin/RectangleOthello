package slp.seki.rectangleothello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private Handler handler;
    private TextView textView;



    //-- コンストラクタ
    public OthelloView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paused = false;
        handler = new Handler();
        board = new Board();
        setBlackPlayer(new HumanPlayer(Cell.STATUS.Black, this.board));
        setWhitePlayer(new ComputerPlayer(Cell.STATUS.White,this.board, 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
    }

    public Board getBoard() {
        return this.board;
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

    public void setTextView(TextView textView) {
        this.textView = textView;
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
        this.black.setText("Black("+blackPlayer.getName()+")：" + Integer.toString(this.board.countCells(Cell.STATUS.Black)));
        this.white.setText("White("+whitePlayer.getName()+")：" + Integer.toString(this.board.countCells(Cell.STATUS.White)));
        canvas.drawColor(Color.BLACK);
        board.draw(canvas);
    }

    //-- 画面をタッチしたら
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Player p = this.board.getTurn()== Cell.STATUS.Black ? blackPlayer : whitePlayer;
        if (p == null) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                 setTouchedCell(event.getX(), event.getY());
                 break;
            case MotionEvent.ACTION_UP:
                if (p.isHuman()) putStone(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_DOWN:
                setTouchedCell(event.getX(), event.getY());
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
            board.setTouch(x, y);
            board.put();
            board.switchPlayer();
            switchButtonColor();
            callPlayer();
        }
        if (board.countPuttableCell()==0) {
            board.switchPlayer();
            switchButtonColor();
            callPlayer();
        }

    }

    public void putStone(int x, int y) {
        if ( board.canPut(x, y) ) {
            board.setTouch(x, y);
            board.put();
            board.switchPlayer();
            switchButtonColor();
            callPlayer();
        }
        if (board.countPuttableCell()==0) {
            board.switchPlayer();
            switchButtonColor();
            callPlayer();
        }
    }

    public void execPass() {
        board.switchPlayer();
        switchButtonColor();
        callPlayer();
    }

    public void showCountsToast(){
        Cell.STATUS winner = board.getWinner();
        String msg = "Black: " + board.countCells(Cell.STATUS.Black) + "\n"
                + "White: " + board.countCells(Cell.STATUS.White) + "\n\n";

        if (board.isFinish()){
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

    @Override
    public void onEndThinking(Point pos) {
        if (board.getPassed()) execPass();
        if (pos == null) return;
        if (!this.board.canPut(pos.x, pos.y)) return;
        if (paused) return;
        putStone(pos.x, pos.y);
        invalidate();
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
        if (!this.board.isPlayableState()) return;

        if (this.board.isFinish()) {
            textView.setText("Game Set");
            showCountsToast();
            return;
        }

        Player p;
        if (this.board.getTurn()== Cell.STATUS.Black) {
            p = blackPlayer;
            whitePlayer.stopThinking();
        } else if (this.board.getTurn() == Cell.STATUS.White){
            p = whitePlayer;
            blackPlayer.stopThinking();
        } else return;
        if (p != null) {
            if (p.isHuman()) board.setHintVisible(true);
            else {
                board.setHintVisible(false);
                if (((ComputerPlayer) p).isStopped()) ((ComputerPlayer) p).setStopped(false);
            }
            p.startThinking(this);
        }
    }

    public void finish() {
        board.setFinish();
    }

    public void pause() {
        if (blackPlayer!=null) blackPlayer.stopThinking();
        if (whitePlayer!=null) whitePlayer.stopThinking();
    }

    public void restart() {
        invalidate();
        callPlayer();
    }



}
