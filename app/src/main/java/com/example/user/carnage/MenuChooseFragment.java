package com.example.user.carnage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.zip.Inflater;

public class MenuChooseFragment extends Fragment {
    private final String TAG = "Carnage MCF";

    private RadioGroup playerGroup, enemyGroup;
    private Button startButton;
    private ImageView label;
    private SecureRandom random;
    public Chars playerChar, enemyChar;

    private Button testButton;

    public static PlayCharacter player, enemy; // TODO: replace static from here to MainActivity,  update links

    public MenuChooseFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_choose, container, false);

        random = new SecureRandom();

        label = view.findViewById(R.id.menuTitleImageView);
        AssetManager assets = getActivity().getAssets();
        try(InputStream stream = assets.open("carnage_label.png")) {
            Drawable img = Drawable.createFromStream(stream, "carnage_label");
            label.setImageDrawable(img);
        } catch (IOException exc) {
            Log.e(TAG, "error loading label img: "+exc);
        }

        playerGroup = view.findViewById(R.id.playerRadioGroup);
        enemyGroup = view.findViewById(R.id.enemyRadioGroup);
        startButton = view.findViewById(R.id.startGameButton);

        playerGroup.setOnCheckedChangeListener(playerGroupListener);
        enemyGroup.setOnCheckedChangeListener(enemyGroupListener);
        startButton.setOnClickListener(startButtonListener);

        testButton = view.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LevelUpFragment fragment = new LevelUpFragment();
                //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.replace(R.id.container, fragment).commit();
                ProfileChooseFragment fragment = new ProfileChooseFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment).commit();
            }
        });
        return view;
    }

    private RadioGroup.OnCheckedChangeListener playerGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.playerRadioButton1: playerChar = Chars.BALANCED; break;
                case R.id.playerRadioButton2: playerChar = Chars.BERSERKER; break;
                case R.id.playerRadioButton3: playerChar = Chars.TANK; break;
                case R.id.playerRadioButton4: playerChar = Chars.RANDOM; break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener enemyGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.enemyRadioButton1: enemyChar = Chars.BALANCED; break;
                case R.id.enemyRadioButton2: enemyChar = Chars.BERSERKER; break;
                case R.id.enemyRadioButton3: enemyChar = Chars.TANK; break;
                case R.id.enemyRadioButton4: enemyChar = Chars.RANDOM; break;
            }
        }
    };

    private View.OnClickListener startButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playerGroup.getCheckedRadioButtonId() !=-1 && enemyGroup.getCheckedRadioButtonId() !=-1) {
                RPGBattleFragment fragment = new RPGBattleFragment();

                player = new PlayCharacter(playerChar, getString(R.string.player_1_name));
                enemy = new PlayCharacter(enemyChar, getString(R.string.player_2_name));
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment, "MAIN BATTLE FRAGMENT");
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Toast.makeText(getContext(), R.string.menu_choose_toast_choose_class, Toast.LENGTH_SHORT).show();
            }
        }
    };

    //public static Chars getPlayerChar() { return playerChar; }
   // public static Chars getEnemyChar() { return enemyChar; }

}
