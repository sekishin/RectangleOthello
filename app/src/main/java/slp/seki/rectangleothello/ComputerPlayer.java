package slp.seki.rectangleothello;

import android.graphics.Point;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player implements Runnable{

    private Handler handler = new Handler();
    private PlayerCallback callback;
    private Thread thread;
    private boolean stopped;
    private Random rand;
    private static int WAIT_MSEC = 1;

    public ComputerPlayer(Cell.STATUS color, String name, Board board) {
        super(color, name, board);
        stopped = false;
        rand = new Random();
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public synchronized void startThinking(PlayerCallback callback) {
        this.textView.setText(Cell.statusToDisplay(this.color)+"("+this.name+") is thinking");
        this.callback = callback;
        if (this.board.getAvailableCellList().size() == 0) {
            callback.onEndThinking(null);
            return;
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public synchronized void stopThinking() {
        stopped = true;
    }

    @Override
    public void run() {
        final Point pos = think();
        callback.onEndThinking(pos);
    }

    public Point think() {
        Point pos = null;
        try {
            Thread.sleep(WAIT_MSEC);
        } catch (InterruptedException e) {
            setStopped(true);
        }
        if (isStopped()) return pos;

        ArrayList<Cell> availableCells = this.board.getAvailableCellList();
        if (availableCells.size() == 0) {
            return pos;
        }
        if (isStopped()) return pos;

        int n = rand.nextInt(availableCells.size());
        Cell chosenCell = availableCells.get(n);
        pos = chosenCell.getPoint();
        return pos;
    }

    public void setStopped(boolean flag) {
        this.stopped = flag;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void onProgress(final int percent) {
        setProgress(percent);
    }


}
