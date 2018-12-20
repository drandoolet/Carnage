package com.example.user.carnage;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class ProfileChooseFragment extends Fragment {
    private ImageButton profile1ImgButton, profile2ImgButton;
    private TextView profile1TextView, profile2TextView;
    private ImageView profileChooseTitleImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_choose, container, false);

        profileChooseTitleImageView = view.findViewById(R.id.profileMenuTitleImageView);
        profile1ImgButton = view.findViewById(R.id.profile1ImageButton);
        profile2ImgButton = view.findViewById(R.id.profile2ImageButton);
        profile1TextView = view.findViewById(R.id.profile1TextView);
        profile2TextView = view.findViewById(R.id.profile2TextView);

        AssetManager assets = getActivity().getAssets();
        InputStream stream = null;
        try {
            stream = assets.open("carnage_label.png");
            Drawable img = Drawable.createFromStream(stream, "carnage label");
            profileChooseTitleImageView.setImageDrawable(img); // TODO: NullPointer here
            stream = assets.open(MainActivity.getProfileImage(MainActivity.RPG_PROFILE_1));
            img = Drawable.createFromStream(stream, "profile 1 image");
            profile1ImgButton.setImageDrawable(img);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "error in profileChooseFrag : "+e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "error closing stream - "+e);
            }
        }

        profile1TextView.setText("Lv."+MainActivity.getInitialStats()[5]+'\n'+"EXP: "+MainActivity.getInitialStats()[6]);

        return view;
    }
}
