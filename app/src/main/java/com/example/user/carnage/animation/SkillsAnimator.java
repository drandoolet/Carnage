package com.example.user.carnage.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.carnage.fragments.RPGBattleFragment;
import com.example.user.carnage.logic.skills.Skill;

public class SkillsAnimator extends AnimateGame {
    private final long DURATION_FIREBALL_1 = 800;
    private final long DURATION_FIREBALL_2 = 200;
    private final long DURATION_FIREBALL_3 = 70;
    private final long DURATION_FIREBALL_4 = 300;

    private AnimatorSet mainSet;
    private ImageView imageView, enemyImg;
    private TextView points;
    public String imageFile;
    private long animDuration, animDurationToPoints;
    private Skill skill;
    RPGBattleFragment fragment;

    private MagicCallBack magicCallBack;

    private SkillsAnimator() {
        super();
    }

    public SkillsAnimator(Skill skill, RPGBattleFragment fragment) {
        this.skill = skill;
        this.imageView = fragment.skillEffect_img;
        if (skill.isEffectOnPlayer()) {
            points = fragment.player_points;
        } else {
            points = fragment.enemy_points;
            enemyImg = fragment.enemy_img;
        }
        this.fragment = fragment;
        registerMagicCallBack(fragment);

        switch (skill.getName()) {
            case FIREBALL: {
                mainSet = animateFireBall();
                imageFile = skill.getIcon();
                break;
            } case HEAL_SMALL: {

                break;
            }
        }
        setAnimDurationToPoints(skill.getName());
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
                //skill.use();  // possibly better to use getEffect() in the fragment

                fragment.setAllEnabled(true);
                setDefault();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                magicCallBack.magicUsed(skill, animDurationToPoints);
            }
        }, animDurationToPoints);
    }

    private AnimatorSet animateFireBall() {
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        AnimatorSet state3 = new AnimatorSet();
        AnimatorSet set = new AnimatorSet();

        state1.setDuration(DURATION_FIREBALL_1).playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 0f, 1f),
                animateChangeScale(imageView, 0f, 1f, 0),
                animateRotation(imageView, 0f, 720f, 0)
        ); /*
        state2.playTogether(
                animateChangeScale(imageView, 1f, 3f, 200),
                animateHitOnReceivedDmg(enemyImg, ANIMATE_HIT_DURATION_1, true),
                getAnimateDamagePointsSet(fragment.enemy_points, true)
        );
        state3.playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0).setDuration(300),
                animateHitOnRecoverFromDmg(enemyImg, ANIMATE_HIT_DURATION_2, true)
        ); */
        state2.setDuration(0).playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0),
                ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 1f, 0f)
        );

        set.playSequentially(
                state1,
                animateTranslation(imageView, enemyImg.getX()-imageView.getX(),
                        (enemyImg.getBottom() - enemyImg.getTop())/(-2), DURATION_FIREBALL_2),
                animateChangeScale(imageView, 1f, 2f, DURATION_FIREBALL_3),
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0).setDuration(DURATION_FIREBALL_4),
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

    private void setAnimDurationToPoints(Skill.SkillTypes type) {
        long duration;
        switch (type) {
            case FIREBALL:
                duration = DURATION_FIREBALL_1 + DURATION_FIREBALL_2; break;
            case HEAL_SMALL: duration = 0; break;
            default: duration = 0;
        }
        animDurationToPoints = duration;
    }



    public interface MagicCallBack {
        void magicUsed(Skill skill, long animDurationToPoints);
    }

    public void registerMagicCallBack(MagicCallBack callBack) {
        magicCallBack = callBack;
    }
}


