package slp.seki.rectangleothello;

public abstract class Player {
    protected Cell.STATUS color;
    protected String name;
    protected Board board;

    public Player(Cell.STATUS color, String name, Board board) {
        setColor(color);
        setName(name);
        this.board = board;
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
}
