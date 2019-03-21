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
    private PlayerSelectDialog dialog;

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
        dialog = new PlayerSelectDialog();
    }

    public void setPlayer(int id, Cell.STATUS color) {
        switch (color) {
            case Black:
                setBlackPlayer(id);
                othelloView.callPlayer();
                break;
            case White:
                setWhitePlayer(id);
                othelloView.callPlayer();
                break;
            default:
                textView.setText(("error"));
                break;
        }
    }

    public void setBlackPlayer(int id) {
        if (othelloView.getBlackPlayer().getPlayerId()==id) return;
        switch (id) {
            case 0:
                othelloView.setBlackPlayer(new HumanPlayer(Cell.STATUS.Black, othelloView.getBoard()));
                othelloView.getBlackPlayer().setTextView(textView);
                break;
            case 1:
                othelloView.setBlackPlayer(new ComputerPlayer(Cell.STATUS.Black, othelloView.getBoard(),1));
                othelloView.getBlackPlayer().setTextView(textView);
                break;
            default:
                textView.setText(("error"));
                break;
        }
        othelloView.restart();
    }

    public void setWhitePlayer(int id) {
        if (othelloView.getWhitePlayer().getPlayerId()==id) return;
        switch (id) {
            case 0:
                othelloView.setWhitePlayer(new HumanPlayer(Cell.STATUS.White, othelloView.getBoard()));
                othelloView.getWhitePlayer().setTextView(textView);
                break;
            case 1:
                othelloView.setWhitePlayer(new ComputerPlayer(Cell.STATUS.White, othelloView.getBoard(),1));
                othelloView.getWhitePlayer().setTextView(textView);
                break;
            default:
                textView.setText(("error"));
                break;
        }
        othelloView.restart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void buttonClicked(View v) {
        if (dialog.isVisible()) return;
        othelloView.pause();
        final String[] items = {"Human", "Computer Lv.1"};
        Bundle args = new Bundle();
        args.putStringArray("items", items);
        if (v.getId() == R.id.blackButton) {
            textView.setText("黒");
            args.putString("title", "Black Player");
            args.putInt("chosen", othelloView.getBlackPlayer().getPlayerId());
            args.putInt("color", 0);
        } else if (v.getId() == R.id.whiteButton) {
            textView.setText("白");
            args.putString("title", "White Player");
            args.putInt("chosen", othelloView.getWhitePlayer().getPlayerId());
            args.putInt("color", 1);
        }
        dialog.setArguments(args);
        dialog.show(this.getFragmentManager(), "TAG");
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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final List<Integer> checkedItems = new ArrayList<>();
            final String[] items = getArguments().getStringArray("items");
            final String title = getArguments().getString("title");
            int chosen = getArguments().getInt("chosen");
            final Cell.STATUS color = (getArguments().getInt("color")==0) ? Cell.STATUS.Black : Cell.STATUS.White;
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
                        MainActivity main = (MainActivity)getActivity();
                        main.setPlayer(checkedItems.get(0), color);
                        Log.d("checkedItem:", "" + checkedItems.get(0));
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity main = (MainActivity)getActivity();
                    main.othelloView.restart();
                }
            }).show();
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

}
