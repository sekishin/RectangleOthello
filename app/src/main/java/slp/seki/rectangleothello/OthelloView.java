package slp.seki.rectangleothello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by 14t242 on 2016/07/22.
 */
public class OthelloView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int DRAW_INTERVAL = 1000/60;
    private Board board;

    //-- コンストラクタ
    public OthelloView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }
    //-- 描画スレッド
    private DrawThread drawThread;
    private class DrawThread extends Thread {
        private final AtomicBoolean isFinished = new AtomicBoolean();

        public void finish() {
            isFinished.set(true);
        }

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();
            while (! isFinished.get()) {
                if (holder.isCreating()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) {
                    continue;
                }
                drawBoard(canvas);
                holder.unlockCanvasAndPost(canvas);
                synchronized (this) {
                    try {
                        wait(DRAW_INTERVAL);
                    } catch (InterruptedException e) {

                    }
                }

            }
        }
    }

    //-- ゲーム開始
    public void startDrawThread() {
        stopDrawThread();
        drawThread = new DrawThread();
        drawThread.start();
    }

    //-- ゲーム終了
    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }
        drawThread.finish();
        drawThread = null;
        return true;
    }

    //-- 描画開始
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //-- 描画終了
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //-- 盤面描画
    void drawBoard(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        int cellSize = 100;
        if (board == null) {
            board = new Board(canvas.getWidth(), canvas.getHeight(), cellSize);
        }
        board.draw(canvas);
    }

}
