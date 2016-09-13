package slp.seki.rectangleothello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private OthelloView othelloView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        othelloView = new OthelloView(this);
        setContentView(othelloView);
    }
}
