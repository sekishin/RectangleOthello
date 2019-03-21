package slp.seki.rectangleothello;

import android.graphics.Point;

public abstract interface PlayerCallback {
    public void onEndThinking(Point pos);
    public void onProgress();
    public void onPointStarted(Point pos);
    public void onPointEnded(Point pos);
}
