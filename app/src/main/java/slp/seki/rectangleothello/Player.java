package slp.seki.rectangleothello;

import android.widget.TextView;

public abstract class Player {
    protected Cell.STATUS color;
    protected String name;
    protected Board board;

    private int progress;
    private Cell currentCell;
    protected TextView textView;

    public Player(Cell.STATUS color, String name, Board board) {
        setColor(color);
        setName(name);
        setBoard(board);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public abstract boolean isHuman();

    public void setColor(Cell.STATUS color) {
        this.color = color;
    }

    public Cell.STATUS getColor() {
        return this.color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void startThinking(PlayerCallback callback);
    public abstract void stopThinking();

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

}
