package com.example.user.carnage.fragments;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.user.carnage.MainActivity;
import com.example.user.carnage.R;
import com.example.user.carnage.logic.skills.Fireball;
import com.example.user.carnage.logic.skills.Skill;
import com.example.user.carnage.logic.skills.SmallHeal;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SkillsFragment extends Fragment {
    private Button useSkillButton;
    private AppCompatImageButton skillButton1, skillButton2, closeButton;
    private Skill skill;

    private HashMap<Skill, ImageButton> skillButtonMap;

    private int selectedSkillIdx = 0;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills, container, false);

        useSkillButton = view.findViewById(R.id.exitButton);
        useSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
                listener.onButtonSelected(skill);
                //container.setVisibility(View.GONE);
            }
        });

        closeButton = view.findViewById(R.id.closeSkillsButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
                listener.onButtonSelected(null);
            }
        });

        skillButton1 = view.findViewById(R.id.skillButton1);
        skillButton2 = view.findViewById(R.id.skillButton2);
        skillButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill = new SmallHeal(MainActivity.player);
                //Toast.makeText(getContext(), "SmallHeal effect: "+skill.getEffect(), Toast.LENGTH_LONG).show();
            }
        });
        skillButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill = new Fireball(MainActivity.player, MainActivity.enemy);
                //Toast.makeText(getContext(), "Fireball effect: "+skill.getEffect(), Toast.LENGTH_LONG).show();
            }
        });

        skillButtonMap = new HashMap<>();
        Stack<ImageButton> imageButtons = new Stack<>();
        imageButtons.push(skillButton1);
        imageButtons.push(skillButton2);

        for (Skill skill : MainActivity.chosenSkillsSet.keySet()) {
            skillButtonMap.put(skill, imageButtons.pop());
        }

        AsyncFileLoader loader = new AsyncFileLoader();
        loader.execute(MainActivity.chosenSkillsSet);

        return view;
    }

    public interface OnSelectedButtonListener {
        void onButtonSelected(Skill skill);
    }

    private View.OnClickListener skillButtonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.skillButton1 : selectedSkillIdx = 1; break;
                case R.id.skillButton2 : selectedSkillIdx = 2; break;
            }
        }
    };

    private void setImages(Map<Skill, Drawable> map) {
        for (Skill skill : map.keySet()) {
            skillButtonMap.get(skill).setImageDrawable(map.get(skill));
        }
    }

    private class AsyncFileLoader extends AsyncTask<Map<Skill, String>, Void, Map<Skill, Drawable>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Map<Skill, Drawable> doInBackground(Map<Skill, String>... maps) {
            Map<Skill, String> map = maps[0];
            Map<Skill, Drawable> resultMap = new HashMap<>();
            int counter = 0;

            for (Skill skill : map.keySet()) {
                try (InputStream stream = getActivity().getAssets().open(map.get(skill))) {
                    resultMap.put(skill, Drawable.createFromStream(stream, "skill image #"+ ++counter));
                } catch (IOException e) {
                    Log.e("SKILLS FRAGMENT", "error loading skill img: "+e);
                }
            }
            return resultMap;
        }

        @Override
        protected void onPostExecute(Map<Skill, Drawable> skillDrawableMap) {
            setImages(skillDrawableMap);
        }
    }
}
