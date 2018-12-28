package com.example.user.carnage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SkillsFragment extends Fragment {
    private Button exitButton;
    private AppCompatImageButton skillButton1, skillButton2;
    private Skill skill;

    private int selectedSkillIdx = 0;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills, container, false);

        exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
                listener.onButtonSelected(skill);
                //container.setVisibility(View.GONE);
            }
        });

        skillButton1 = view.findViewById(R.id.skillButton1);
        skillButton2 = view.findViewById(R.id.skillButton2);
        skillButton1.setOnClickListener(skillButtonsListener);
        skillButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill = new FireBall(MainActivity.player, MainActivity.enemy);
            }
        });

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


}
