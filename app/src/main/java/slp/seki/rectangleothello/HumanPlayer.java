package slp.seki.rectangleothello;

public class HumanPlayer extends Player {
    public HumanPlayer(Cell.STATUS color, Board board) {
        super(color, "Human", board);
        this.playerId = 0;
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public int getPlayerId() {
        return playerId;
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
