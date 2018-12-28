package com.example.user.carnage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.TextView;

import javax.crypto.spec.OAEPParameterSpec;

public class SkillsAnimator extends AnimateGame {
    private AnimatorSet mainSet;
    private ImageView imageView, enemyImg;
    private TextView points;
    public String imageFile;
    private long animDuration, animDurationToPoints;
    private Skill skill;
    RPGBattleFragment fragment;

    private SkillsAnimator() {
        super();
    }

    SkillsAnimator(Skill skill, ImageView imageView, ImageView enemyImageView, TextView points, RPGBattleFragment fragment) {
        this.skill = skill;
        this.imageView = imageView;
        enemyImg = enemyImageView;
        this.fragment = fragment;
        this.points = points;

        switch (skill.name) {
            case FIREBALL: {
                mainSet = animateFireBall();
                imageFile = "skill/Fireball.png";
                animDuration = 3600;
                animDurationToPoints = 1100;
                break;
            } case HEAL_SMALL: {

                break;
            }
        }
    }

    public void start() {
        fragment.animateSkillsFragmentAppearance(false);
        fragment.skillEffect_img.setVisibility(View.VISIBLE);
        //animDuration = mainSet.getDuration();
        points.setText(Integer.toString(skill.getEffect()));
        mainSet.start();
        mainSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                skill.use();
                fragment.setAllEnabled(true);
                setDefault();
            }
        }); /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, animDuration); */
    }

    private AnimatorSet animateFireBall() {
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        AnimatorSet state3 = new AnimatorSet();
        AnimatorSet set = new AnimatorSet();

        state1.setDuration(800).playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 0f, 1f),
                animateChangeScale(imageView, 0f, 1f, 0),
                animateRotation(imageView, 0f, 720f, 0)
        ); /*
        state2.playTogether(
                animateChangeScale(imageView, 1f, 3f, 200),
                animateHitOnReceivedDmg(enemyImg, ANIMATE_HIT_DURATION_1, true),
                getAnimateDamagePointsSet(fragment.enemy_points, true)
        ); */
        state3.playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0).setDuration(300),
                animateHitOnRecoverFromDmg(enemyImg, ANIMATE_HIT_DURATION_2, true)
        );
        state2.setDuration(0).playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0),
                ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 1f, 0f)
        );

        set.playSequentially(
                state1,
                animateTranslation(imageView, enemyImg.getX()-imageView.getX(), 0, 200),
                animateChangeScale(imageView, 1f, 2f, 70),
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0).setDuration(300),
                state2
        );
        return set;
    }

    private void setDefault() {
        View[] views = {fragment.enemy_points, fragment.player_points, fragment.skillEffect_img, fragment.enemy_img, fragment.player_img};
        for (View view : views) {
            ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 1f);
            animateTranslation(view, 0f, 0f, 0);
            animateChangeScale(view, 1f, 1f, 0);
            ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 0f);
        }
        fragment.skillEffect_img.setVisibility(View.INVISIBLE);
        fragment.player_points.setVisibility(View.INVISIBLE);
        fragment.enemy_points.setVisibility(View.INVISIBLE);
    }
}

enum SkillsAnimations {
    HEAL_SMALL,
    FIREBALL
}
