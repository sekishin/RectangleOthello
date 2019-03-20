package slp.seki.rectangleothello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OthelloView othelloView;
    private Button blackButton;
    private Button whiteButton;
    private TextView textView;
    private GradientDrawable drawable;
    private int chosen;

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
        othelloView.getBlackPlayer().setTextView(textView);
        othelloView.getWhitePlayer().setTextView(textView);
        othelloView.setTextView(textView);
    }

    public Player makePlayer(int id, Cell.STATUS color) {
        switch (id) {
            case 0:
                return new HumanPlayer(color, othelloView.getBoard());
            case 1:
                return new ComputerPlayer(color, othelloView.getBoard(), 1);
            default:
                return new HumanPlayer(color, othelloView.getBoard());
        }
    }

    public void setChosen(int id) {
        this.chosen = id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void buttonClicked(View v) {
        final String[] items = {"Human", "Computer Lv.1"};
        if (v.getId() == R.id.blackButton) {
            textView.setText("黒");
            int chosen = PlayerSelectDialog.showDialog(this, "Black Player", items, othelloView.getBlackPlayer().getPlayerId());
            textView.setText(Integer.toString(chosen));
            if (chosen != othelloView.getBlackPlayer().getPlayerId()) othelloView.setBlackPlayer(makePlayer(chosen, Cell.STATUS.Black));
            othelloView.getBlackPlayer().setTextView(textView);
            //othelloView.callPlayer();
        } else if (v.getId() == R.id.whiteButton) {
            textView.setText("白");
            int chosen = PlayerSelectDialog.showDialog(this, "White Player", items, othelloView.getWhitePlayer().getPlayerId());
            if (chosen != othelloView.getBlackPlayer().getPlayerId()) othelloView.setWhitePlayer(makePlayer(chosen, Cell.STATUS.White));
            othelloView.getWhitePlayer().setTextView(textView);
            othelloView.callPlayer();
        }
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

    public static class PlayerSelectDialog extends DialogFragment {
        public static void showDialog(Activity activity, String title, String[] items, int chosen) {
            PlayerSelectDialog dialog = new PlayerSelectDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putStringArray("items", items);
            args.putInt("chosen", chosen);
            dialog.setArguments(args);
            dialog.show(activity.getFragmentManager(), "TAG");
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final List<Integer> checkedItems = new ArrayList<>();
            final String[] items = getArguments().getStringArray("items");
            final String title = getArguments().getString("title");
            int chosen = getArguments().getInt("chosen");
            checkedItems.add(chosen);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            return builder.setSingleChoiceItems(items, chosen, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedItems.clear();
                    checkedItems.add(which);
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!checkedItems.isEmpty()) {
                        Log.d("checkedItem:", "" + checkedItems.get(0));
                    }
                }
            }).setNegativeButton("Cancel", null).show();
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

}
