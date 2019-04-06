package com.example.user.carnage.fragments;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.carnage.MainActivity;
import com.example.user.carnage.R;

import java.io.IOException;
import java.io.InputStream;


public class LevelUpFragment extends Fragment {
    private TextView titleTextView, statsLeftTextView, strTextView, staTextView, agiTextView,
                        luckTextView, intTextView;
    private TextView strAmountTextView, staAmountTextView, agiAmountTextView, luckAmountTextView, intAmountTextView;
    private ImageView playerImageView;
    private Button button_minus_str, button_minus_sta, button_minus_agi, button_minus_luck, button_minus_int;
    private Button button_plus_str, button_plus_sta, button_plus_agi, button_plus_luck, button_plus_int;
    private Button okButton;
    private int statsLeft, level;

    private int initStr, initSta, initAgi, initLuck, initIntell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allocate_stat_points, container, false);

        titleTextView = view.findViewById(R.id.levelUpTitleTextView);
        statsLeftTextView = view.findViewById(R.id.statsLeftTextView);
        strTextView = view.findViewById(R.id.statTextViewStrength);
        staTextView = view.findViewById(R.id.statTextViewStamina);
        agiTextView = view.findViewById(R.id.statTextViewAgility);
        luckTextView = view.findViewById(R.id.statTextViewLuck);
        intTextView = view.findViewById(R.id.statTextViewIntelligence);
        strAmountTextView = view.findViewById(R.id.statAmountTextView1);
        staAmountTextView = view.findViewById(R.id.statAmountTextView2);
        agiAmountTextView = view.findViewById(R.id.statAmountTextView3);
        luckAmountTextView = view.findViewById(R.id.statAmountTextView4);
        intAmountTextView = view.findViewById(R.id.statAmountTextView5);

        playerImageView = view.findViewById(R.id.levelUpImageView);

        button_minus_str = view.findViewById(R.id.minusButton1);
        button_minus_sta = view.findViewById(R.id.minusButton2);
        button_minus_agi = view.findViewById(R.id.minusButton3);
        button_minus_luck = view.findViewById(R.id.minusButton4);
        button_minus_int = view.findViewById(R.id.minusButton5);

        button_plus_str = view.findViewById(R.id.plusButton1);
        button_plus_sta = view.findViewById(R.id.plusButton2);
        button_plus_agi = view.findViewById(R.id.plusButton3);
        button_plus_luck = view.findViewById(R.id.plusButton4);
        button_plus_int = view.findViewById(R.id.plusButton5);

        okButton = view.findViewById(R.id.okButton);

        handleInitialStats(MainActivity.getInitialStats(MainActivity.currentProfile));

        TextView amounts[] = {strAmountTextView, staAmountTextView, agiAmountTextView, luckAmountTextView, intAmountTextView};
        int initials[] = {initStr, initSta, initAgi, initLuck, initIntell};
        for (int i=0; i<amounts.length; i++) {
            System.out.println("trying: amounts "+i);
            amounts[i].setText(Integer.toString(initials[i]));
        }
        statsLeftTextView.setText(Integer.toString(statsLeft));


        Button minusButtons[] = {button_minus_str, button_minus_sta, button_minus_agi, button_minus_luck, button_minus_int};
        for (Button x : minusButtons) x.setOnClickListener(minusListener);
        Button plusButtons[] = {button_plus_str, button_plus_sta, button_plus_agi, button_plus_luck, button_plus_int};
        for (Button x : plusButtons) x.setOnClickListener(plusListener);
        okButton.setOnClickListener(okListener);

        AssetManager assets = getActivity().getAssets();
        try (InputStream stream = assets.open("player_img/alina_lupit.png")) {
            MainActivity.player_image = Drawable.createFromStream(stream, "alina_lupit.png");
            playerImageView.setImageDrawable(MainActivity.player_image);
        } catch (IOException exc) {
            Log.e(MainActivity.TAG, "error loading player 1 img: "+exc);
        }

        titleTextView.setText("Level "+MainActivity.player.getLevel());

        return view;
    }

    private View.OnClickListener minusListener = new View.OnClickListener() { // TODO prohibit to set amount less than was
        @Override
        public void onClick(View view) {
            TextView textView;
            int initial = 0;

            switch (view.getId()) {
                case R.id.minusButton1:
                    textView = strAmountTextView;
                    initial = initStr;
                    break;
                case R.id.minusButton2:
                    textView = staAmountTextView;
                    initial = initSta;
                    break;
                case R.id.minusButton3:
                    textView = agiAmountTextView;
                    initial = initAgi;
                    break;
                case R.id.minusButton4:
                    textView = luckAmountTextView;
                    initial = initLuck;
                    break;
                case R.id.minusButton5:
                    textView = intAmountTextView;
                    initial = initIntell;
                    break;
                default: textView = statsLeftTextView;
            }
            if (Integer.parseInt(textView.getText().toString()) != initial) {
                int amount = Integer.parseInt(textView.getText().toString());
                textView.setText(new Integer(amount-1).toString());

                textView = statsLeftTextView;
                amount = Integer.parseInt(textView.getText().toString());
                textView.setText(new Integer(amount+1).toString());
                statsLeft++;
            }
        }
    };

    private View.OnClickListener plusListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView textView;
            if (statsLeft != 0) {
                switch (view.getId()) {
                    case R.id.plusButton1: textView = strAmountTextView; break;
                    case R.id.plusButton2: textView = staAmountTextView; break;
                    case R.id.plusButton3: textView = agiAmountTextView; break;
                    case R.id.plusButton4: textView = luckAmountTextView; break;
                    case R.id.plusButton5: textView = intAmountTextView; break;
                    default: textView = statsLeftTextView;
                }

                int amount = Integer.parseInt(textView.getText().toString());
                textView.setText(new Integer(amount+1).toString());

                TextView textView2 = statsLeftTextView;
                amount = Integer.parseInt(textView2.getText().toString());
                textView2.setText(new Integer(amount-1).toString());
                statsLeft--;
            }


        }
    };

    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int[] newStats = new int[8];
            TextView[] views = {strAmountTextView, staAmountTextView, agiAmountTextView, luckAmountTextView,
                    intAmountTextView};
            for (int i=0; i<5; i++) {
                newStats[i] = Integer.parseInt(views[i].getText().toString());
            }
            newStats[5] = MainActivity.player.getLevel();
            newStats[6] = MainActivity.player.getCurrentExp();
            newStats[7] = statsLeft;

            MainActivity.updatePlayerStatsSharedPreferences(newStats, MainActivity.currentProfile);
            MainActivity.newGame(getFragmentManager());
        }
    };

    private void handleInitialStats(int[] stats) {
        initStr = stats[0];
        initSta = stats[1];
        initAgi = stats[2];
        initLuck = stats[3];
        initIntell = stats[4];
        level = stats[5];
        statsLeft = stats[7];
    }
}
