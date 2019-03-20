package slp.seki.rectangleothello;

public class HumanPlayer extends Player {
    public HumanPlayer(Cell.STATUS color, String name, Board board) {
        super(color, name, board);
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public void startThinking(PlayerCallback callback) {
        this.textView.setText(Cell.statusToDisplay(this.color)+"("+this.name+") is thinking");
        callback.onEndThinking(null);
    }

    @Override
    public void stopThinking() {

    }
}
