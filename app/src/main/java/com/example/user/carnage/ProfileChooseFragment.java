package com.example.user.carnage;

import android.animation.ObjectAnimator;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ProfileChooseFragment extends Fragment {
    private ImageButton profile1ImgButton, profile2ImgButton;
    private TextView profile1TextView, profile2TextView, profileChooseTextView;
    private TextView levelTextView, expTextView, strTextView, staTextView, agiTextView, luckTextView, intTextView;
    private Button okButton, backButton;
    private ImageView profileChooseTitleImageView;
    private ConstraintLayout layout;
    private AnimateGame animateGame;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_choose, container, false);

        animateGame = new AnimateGame();

        profileChooseTitleImageView = view.findViewById(R.id.profileMenuTitleImageView);
        profile1ImgButton = view.findViewById(R.id.profile1ImageButton);
        profile2ImgButton = view.findViewById(R.id.profile2ImageButton);
        profile1TextView = view.findViewById(R.id.profile1TextView);
        profile2TextView = view.findViewById(R.id.profile2TextView);
        profileChooseTextView = view.findViewById(R.id.chooseProfileSubtitleTextView);
        layout = view.findViewById(R.id.profileChooseConstraintLayout);

        levelTextView = view.findViewById(R.id.profileChooseTextViewLevel);
        expTextView = view.findViewById(R.id.profileChooseTextViewExp);
        strTextView = view.findViewById(R.id.profileChooseTextViewStr);
        staTextView = view.findViewById(R.id.profileChooseTextViewSta);
        agiTextView = view.findViewById(R.id.profileChooseTextViewAgi);
        luckTextView = view.findViewById(R.id.profileChooseTextViewLuck);
        intTextView = view.findViewById(R.id.profileChooseTextViewInt);
        okButton = view.findViewById(R.id.profileChooseOKButton);
        backButton = view.findViewById(R.id.profileChooseBackButton);

        AssetManager assets = getActivity().getAssets();
        InputStream stream = null;
        try {
            stream = assets.open("carnage_label.png");
            Drawable img = Drawable.createFromStream(stream, "carnage label");
            profileChooseTitleImageView.setImageDrawable(img);

            stream = assets.open(MainActivity.getProfileImage(MainActivity.currentProfile));
            img = Drawable.createFromStream(stream, "profile 1 image");
            profile1ImgButton.setImageDrawable(img);
            stream = assets.open("backgrounds/back1.jpg");
            img = Drawable.createFromStream(stream, "profile 1 back");
            profile1ImgButton.setBackground(img);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "error in profileChooseFrag : "+e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "error closing stream - "+e);
            }
        }

        profile1TextView.setText("Lv."+MainActivity.getInitialStats(MainActivity.currentProfile)[5]);

        profile1ImgButton.setOnClickListener(buttonListener);
        //profile2ImgButton.setOnClickListener(buttonListener);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RPGBattleFragment fragment = new RPGBattleFragment();

                MainActivity.player = new PlayCharacter(MainActivity.RPG_PROFILE_1, getString(R.string.player_1_name));
                MainActivity.enemy = new PlayCharacter(MainActivity.player, getString(R.string.player_2_name));
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment, "MAIN BATTLE FRAGMENT");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final View[] viewsToFade = {profile2ImgButton, profile2TextView, profile1TextView, profileChooseTextView};
            animateGame.animateProfileChoose(view, layout, viewsToFade);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String profile;
                    switch (view.getId()) {
                        case R.id.profile1ImageButton : profile = MainActivity.RPG_PROFILE_1; break;
                        case R.id.profile2ImageButton : profile = MainActivity.RPG_PROFILE_2; break;
                        default: profile = MainActivity.RPG_PROFILE_1;
                    }
                    OnProfileSelectedListener listener = (OnProfileSelectedListener) getActivity();
                    listener.profileSelected(profile);
                }
            }, AnimationTypes.ANIMATION_PROFILE_SELECTED.getDuration()+500);
        }
    };


    public interface OnProfileSelectedListener {
        void profileSelected(String profile);
    }

    private void handleChosenProfile(String profile) {
        int[] stats = MainActivity.getInitialStats(profile);
        levelTextView.setText("Level "+'\n'+Integer.toString(stats[5]));
        expTextView.setText("EXP: "+'\n'+Integer.toString(stats[6]));
        strTextView.setText("STR: "+'\n'+Integer.toString(stats[0]));
        staTextView.setText("STA: "+'\n'+Integer.toString(stats[1]));
        agiTextView.setText("AGI: "+'\n'+Integer.toString(stats[2]));
        luckTextView.setText("LUCK: "+'\n'+Integer.toString(stats[3]));
        intTextView.setText("INT: "+'\n'+Integer.toString(stats[4]));
        TextView[] views = {strTextView, staTextView, agiTextView, luckTextView, intTextView, levelTextView, expTextView};
        for (int i=0; i<views.length; i++) {
            //views[i].setText(Integer.toString(stats[i]));
            views[i].setVisibility(View.VISIBLE);
        }
        okButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        View[] views1 = {strTextView, staTextView, agiTextView, luckTextView, intTextView, levelTextView, expTextView, okButton, backButton};
        animateGame.animateFade(true, 500, views1);
    }
}
