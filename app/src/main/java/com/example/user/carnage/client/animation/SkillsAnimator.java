package com.example.user.carnage.client.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.carnage.client.fragments.RPGBattleFragment;
import com.example.user.carnage.common.logic.skills.Skill;

public class SkillsAnimator extends AnimateGame {

    enum Fireball implements Durations {
        DURATION_1(800), DURATION_2(200), DURATION_3(70), DURATION_4(300);

        Fireball(long dur) {
            duration = dur;
        }

        private final long duration;

        @Override
        public long getDuration() {
            return duration;
        }

        public static long getDurationToPoints() {
            return DURATION_1.duration + DURATION_2.duration;
        }
    }

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
        setAnimDurationToPoints(skill.getName());

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
                break;
            }
            case HEAL_SMALL: {
                mainSet = doNothing();
                break;
            }
        }
        imageFile = skill.getIcon();
    }

    public void start() {
        fragment.animateSkillsFragmentAppearance(false);

        fragment.skillEffect_img.setVisibility(View.VISIBLE);
        points.setText(Integer.toString(skill.getEffect()));

        mainSet.start();

        mainSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fragment.setAllEnabled(true);
                setDefault();
            }
        });
        new Handler().postDelayed(() -> magicCallBack.animatePointsNow(skill), animDurationToPoints);

    }

    private AnimatorSet doNothing() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0, 0).setDuration(0)
        );
        return set;
    }

    public AnimatorSet animateFireBall() {
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        AnimatorSet state3 = new AnimatorSet();
        AnimatorSet set = new AnimatorSet();

        state1.setDuration(Fireball.DURATION_1.getDuration()).playTogether(
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 0f, 1f),
                animateChangeScale(imageView, 0f, 1f, NULL_DURATION),
                animateRotation(imageView, 0f, 720f, NULL_DURATION)
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
                ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 1f, 0f),
                ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, 1f, 0f)
        );

        set.playSequentially(
                state1,
                animateTranslation(imageView, enemyImg.getX() - imageView.getX(),
                        (enemyImg.getBottom() - enemyImg.getTop()) / (-2) + imageView.getHeight() / 2, Fireball.DURATION_2),
                animateChangeScale(imageView, 1f, 2f, Fireball.DURATION_3),
                ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0).setDuration(Fireball.DURATION_4.getDuration()),
                state2
        );
        return set;
    }

    private void setDefault() {
        View[] views = {fragment.enemy_points, fragment.player_points, fragment.skillEffect_img, fragment.enemy_img, fragment.player_img};
        for (View view : views) {
            ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 1f);
            animateTranslation(view, 0f, 0f, NULL_DURATION);
            animateChangeScale(view, 1f, 1f, NULL_DURATION);
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
                duration = Fireball.getDurationToPoints();
                break;
            case HEAL_SMALL:
                duration = 0;
                break;
            default:
                duration = 0;
        }
        animDurationToPoints = duration;
    }


    public interface MagicCallBack {
        boolean isSkillUsable(Skill skill);

        void magicUsed(Skill skill, AnimatorSet set);

        void animatePointsNow(Skill skill);
    }

    private void registerMagicCallBack(MagicCallBack callBack) {
        magicCallBack = callBack;
    }
}


