package com.example.user.carnage;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ProfileChooseFragment extends Fragment {
    private ImageButton profile1ImgButton, profile2ImgButton;
    private TextView profile1TextView, profile2TextView, profileChooseTextView;
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

        AssetManager assets = getActivity().getAssets();
        InputStream stream = null;
        try {
            stream = assets.open("carnage_label.png");
            Drawable img = Drawable.createFromStream(stream, "carnage label");
            profileChooseTitleImageView.setImageDrawable(img);

            stream = assets.open(MainActivity.getProfileImage(MainActivity.RPG_PROFILE_1));
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

        profile1TextView.setText("Lv."+MainActivity.getInitialStats(MainActivity.RPG_PROFILE_1)[5] + '\n'+
                Arrays.toString(MainActivity.getInitialStats(MainActivity.RPG_PROFILE_1)));

        final View[] viewsToFade = {profile2ImgButton, profile2TextView, profile1TextView, profileChooseTextView};
        profile1ImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateGame.animateProfileChoose(profile1ImgButton, layout, viewsToFade);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OnProfileSelectedListener listener = (OnProfileSelectedListener) getActivity();
                        listener.profileSelected(MainActivity.RPG_PROFILE_1);
                    }
                }, AnimationTypes.ANIMATION_PROFILE_SELECTED.getDuration()+500);
            }
        });

        return view;
    }

    public interface OnProfileSelectedListener {
        void profileSelected(String profile);
    }
}
