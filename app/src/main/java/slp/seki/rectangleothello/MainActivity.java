package slp.seki.rectangleothello;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private OthelloView othelloView;
    private Button blackButton;
    private Button whiteButton;
    private TextView textView;
    private GradientDrawable drawable;

    int resColor(int res) {
        return getResources().getColor(res);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawable = new GradientDrawable();
        setContentView(R.layout.activity_main);
        blackButton = (Button) findViewById(R.id.blackButton);
        blackButton.setBackgroundResource(R.drawable.border);
        drawable = (GradientDrawable)blackButton.getBackground();
        drawable.setStroke(10, resColor(R.color.Red));
        whiteButton = (Button) findViewById(R.id.whiteButton);
        drawable = (GradientDrawable)whiteButton.getBackground();
        drawable.setStroke(10, resColor(R.color.Gray));
        othelloView = (OthelloView) findViewById(R.id.othelloView);
        othelloView.relateButton(blackButton, whiteButton);
        textView = (TextView) findViewById(R.id.textView);
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackClicked(v);
            }
        });
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteClicked(v);
            }
        });
        othelloView.getBlackPlayer().setTextView(textView);
        othelloView.getWhitePlayer().setTextView(textView);
        othelloView.setTextView(textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void blackClicked(View view) {
        textView.setText("黒");
    }

    public void whiteClicked(View view) {
        textView.setText("白");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                item.setChecked(!item.isChecked());
                othelloView.setHintVisible(item.isChecked());
                return true;
            case R.id.item2:
                othelloView.showCountsToast();
                return true;
            case R.id.item3:
                othelloView.initBoard();
                return true;
            case R.id.item4:
                return true;
            case R.id.item5:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
