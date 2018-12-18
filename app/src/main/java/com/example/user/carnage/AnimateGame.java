package com.example.user.carnage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AnimateGame {
    private final  long ANIMATE_ATTACK_DURATION_TRANSLATION_1 = 500;
    private final  long ANIMATE_ATTACK_DURATION_TRANSLATION_2 = 100;
    private final  long ANIMATE_ATTACK_DURATION_TRANSLATION_3 = 300;
    private final  long ANIMATE_ATTACK_DURATION_TRANSLATION_4 = 500;

    private final long ANIMATE_DODGE_DURATION_JUMP_1 = 150;
    private final long ANIMATE_DODGE_DURATION_JUMP_2 = 200;
    private final long ANIMATE_DODGE_DURATION_JUMP_BACK = 500;
    private final  float ANIMATE_DODGE_TRANSLATION_X1 = 0f;
    private final float ANIMATE_DODGE_TRANSLATION_X2 = 40f;
    private final float ANIMATE_DODGE_TRANSLATION_X3 = 80f;
    private final float ANIMATE_DODGE_TRANSLATION_Y1 = 0f;
    private final float ANIMATE_DODGE_TRANSLATION_Y2 = -30f;
    private final float ANIMATE_DODGE_ROTATION_1 = 0.0f;
    private final float ANIMATE_DODGE_ROTATION_2 = 30.0f;

    private final long ANIMATE_HIT_DURATION_1 = 300;
    private final long ANIMATE_HIT_DURATION_2 = 1000;

    private final long ANIMATE_CRIT_DURATION_ROTATE_1 = 500;
    private final long ANIMATE_CRIT_DURATION_ROTATE_2 = 1000;
    private final long ANIMATE_CRIT_DURATION_BACK = ANIMATE_CRIT_DURATION_ROTATE_2 / 2;
    private final float ANIMATE_CRIT_ROTATION_1 = 0.0f;
    private final float ANIMATE_CRIT_ROTATION_2 = 360.0f;
    private final float ANIMATE_CRIT_TRANSLATION_X1 = 50f;
    private final float ANIMATE_CRIT_TRANSLATION_Y1 = 0.0f;
    private final float ANIMATE_CRIT_SCALE_1 = 1.0f;
    private final float ANIMATE_CRIT_SCALE_2 = 0.6f;

    private final long ANIMATE_BLOCK_DURATION_SHAKE = 100;
    private final float ANIMATE_BLOCK_STATE_1 = 0.0f;
    private final float ANIMATE_BLOCK_STATE_2 = 15.0f;
    private final float ANIMATE_BLOCK_STATE_3 = -15.0f;

    private final long ANIMATE_BLOCK_BREAK_DURATION_SHAKE = 100;
    private final float ANIMATE_BLOCK_BREAK_STATE_1 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_STATE_2 = 15.0f;
    private final float ANIMATE_BLOCK_BREAK_STATE_3 = -15.0f;
    private final long ANIMATE_BLOCK_BREAK_DURATION_ROTATE_1 = 500;
    private final long ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2 = 1000;
    private final long ANIMATE_BLOCK_BREAK_DURATION_BACK = ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2 / 2;
    private final float ANIMATE_BLOCK_BREAK_ROTATION_1 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_ROTATION_2 = 360.0f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_X1 = 50f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_X2 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_Y1 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_Y2 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_SCALE_1 = 1.0f;
    private final float ANIMATE_BLOCK_BREAK_SCALE_2 = 0.6f;

    private final long ANIMATE_SKILLS_FRAGMENT_DURATION = 400;

    AnimateGame() {
        AnimationTypes.ANIMATION_BATTLE_ATTACK.setDuration(ANIMATE_ATTACK_DURATION_TRANSLATION_1 + ANIMATE_ATTACK_DURATION_TRANSLATION_2);
        AnimationTypes.ANIMATION_BATTLE_DODGE.setDuration(ANIMATE_DODGE_DURATION_JUMP_1 + ANIMATE_DODGE_DURATION_JUMP_2 + ANIMATE_DODGE_DURATION_JUMP_BACK);
        AnimationTypes.ANIMATION_BATTLE_HIT.setDuration(ANIMATE_HIT_DURATION_1 + ANIMATE_HIT_DURATION_2);
        AnimationTypes.ANIMATION_BATTLE_CRITICAL.setDuration(ANIMATE_CRIT_DURATION_ROTATE_1 + ANIMATE_CRIT_DURATION_ROTATE_2 + ANIMATE_CRIT_DURATION_BACK);
        AnimationTypes.ANIMATION_BATTLE_BLOCK.setDuration(ANIMATE_BLOCK_DURATION_SHAKE *3);
        AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK.setDuration(ANIMATE_BLOCK_BREAK_DURATION_SHAKE *2
                + ANIMATE_BLOCK_BREAK_DURATION_ROTATE_1 + ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2 + ANIMATE_BLOCK_BREAK_DURATION_BACK);

        long minDuration = ANIMATE_ATTACK_DURATION_TRANSLATION_1 + ANIMATE_ATTACK_DURATION_TRANSLATION_2
                + ANIMATE_ATTACK_DURATION_TRANSLATION_3 + ANIMATE_ATTACK_DURATION_TRANSLATION_4;

        AnimationTypes types[] = {AnimationTypes.ANIMATION_BATTLE_DODGE, AnimationTypes.ANIMATION_BATTLE_HIT,
                AnimationTypes.ANIMATION_BATTLE_CRITICAL, AnimationTypes.ANIMATION_BATTLE_BLOCK, AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK};
        for (AnimationTypes type : types) {
            if (type.getDuration() < minDuration) type.setDuration(minDuration);
        }
    }

    private boolean isAnimating = false;

    public void animateSkillsFragmentAppearance(final View view, boolean b) {
        AnimatorSet set = new AnimatorSet();
        float f1 = (b ? 0 : 1);
        float f2 = (b ? 1 : 0);
        set.setDuration(ANIMATE_SKILLS_FRAGMENT_DURATION).playTogether(
                animateChangeScale(view, f1, f2, 0),
                ObjectAnimator.ofFloat(view, View.ALPHA, f1, f2)
        );
        if (!b) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    super.onAnimationEnd(animation);
                }
            });
        }
        set.start();
    }

    public void animateDamagePoints(final View view, boolean isPlayer) {
        view.setVisibility(View.VISIBLE);
        float ANIMATE_POINTS_TRANSLATION_X = 10f * (isPlayer ? -1 : 1);
        float ANIMATE_POINTS_TRANSLATION_Y = -40f;
        long ANIMATE_POINTS_DURATION_1 = 200;
        long ANIMATE_POINTS_DURATION_2 = 1000;
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        AnimatorSet state3 = new AnimatorSet();
        AnimatorSet set = new AnimatorSet();
        state1.setDuration(ANIMATE_POINTS_DURATION_1).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0.1f, 1.0f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.1f, 1.0f),
                ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1)
        );
        state2.setDuration(ANIMATE_POINTS_DURATION_2).playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.3f),
                animateTranslation(view, ANIMATE_POINTS_TRANSLATION_X, ANIMATE_POINTS_TRANSLATION_Y, 0),
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.7f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.7f)
        );
        state3.setDuration(0).playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 0f),
                animateTranslation(view, 0f, 0f, 0)
        );
        set.playSequentially(state1, state2, state3);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
    }

    public void animateAttack(View view, View enView, boolean isPlayer) {
        float translation_1_x = 30f * (isPlayer ? -1 : 1);
        float translation_2_x = (isPlayer ?
                (enView.getLeft() + enView.getWidth()*0.3f) - view.getRight()
                : (enView.getRight() - enView.getWidth()*0.3f) - view.getLeft());
        float translation_3_x = (isPlayer ? enView.getLeft() - view.getRight()-30f : enView.getRight() - view.getLeft()+30f);
        float translation_1_y = -Math.abs(translation_1_x / 2);
        float translation_2_y = 0f;
        float translation_3_y = -Math.abs(translation_3_x / 2);
        AnimatorSet set = new AnimatorSet();

        set.playSequentially(
                animateTranslation(view, translation_1_x, translation_1_y, ANIMATE_ATTACK_DURATION_TRANSLATION_1),
                animateTranslation(view, translation_2_x, translation_2_y, ANIMATE_ATTACK_DURATION_TRANSLATION_2),
                animateTranslation(view, translation_1_x, translation_1_y, ANIMATE_ATTACK_DURATION_TRANSLATION_3),
                animateTranslation(view, 0f, 0f, ANIMATE_ATTACK_DURATION_TRANSLATION_4)
        );
        set.start();
    }

    public void animateDodge(View view, boolean playerOrEnemy) {
        float translation_x2 = ANIMATE_DODGE_TRANSLATION_X2 * (playerOrEnemy ? 1 : -1);
        float translation_x3 = ANIMATE_DODGE_TRANSLATION_X3 * (playerOrEnemy ? 1 : -1);
        float rotation_2 = ANIMATE_DODGE_ROTATION_2 * (playerOrEnemy ? -1 : 1);
        setAnimating(true);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet jump = new AnimatorSet();
        AnimatorSet jump1 = new AnimatorSet();
        jump.setDuration(ANIMATE_DODGE_DURATION_JUMP_1).playTogether(
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, ANIMATE_DODGE_TRANSLATION_X1, translation_x2),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, ANIMATE_DODGE_TRANSLATION_Y1, ANIMATE_DODGE_TRANSLATION_Y2),
                ObjectAnimator.ofFloat(view, View.ROTATION, ANIMATE_DODGE_ROTATION_1, rotation_2)
        );
        jump1.setDuration(ANIMATE_DODGE_DURATION_JUMP_2).playTogether(
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x2, translation_x3),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, ANIMATE_DODGE_TRANSLATION_Y2, ANIMATE_DODGE_TRANSLATION_Y1),
                ObjectAnimator.ofFloat(view, View.ROTATION, rotation_2, ANIMATE_DODGE_ROTATION_1)
        );

        set.playSequentially(
                jump, jump1,
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x3, ANIMATE_DODGE_TRANSLATION_X1).setDuration(ANIMATE_DODGE_DURATION_JUMP_BACK)
        );
        set.addListener(animListener);
        set.start();
    }

    public void animateHit(View view, boolean playerOrEnemy) {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                animateHitOnReceivedDmg(view, ANIMATE_HIT_DURATION_1, playerOrEnemy),
                animateHitOnRecoverFromDmg(view, ANIMATE_HIT_DURATION_2, playerOrEnemy)

        );
        set.start();
    }

    public void animateCriticalHit(View view, boolean isPlayer) {
        float rotation_2 = ANIMATE_CRIT_ROTATION_2 * (isPlayer ? 1 : -1);
        float translation_2 = ANIMATE_CRIT_TRANSLATION_X1 * (isPlayer ? 1 : -1);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        state1.setDuration(ANIMATE_CRIT_DURATION_ROTATE_1).playTogether(
                animateTranslation(view, translation_2, ANIMATE_CRIT_TRANSLATION_Y1, 0),
                animateRotation(view, ANIMATE_CRIT_ROTATION_1, rotation_2, 0),
                animateChangeScale(view, ANIMATE_CRIT_SCALE_1, ANIMATE_CRIT_SCALE_2, 0)
        );
        state2.setDuration(ANIMATE_CRIT_DURATION_ROTATE_2).playTogether(
                animateRotation(view, rotation_2, ANIMATE_CRIT_ROTATION_1, 0),
                animateChangeScale(view, ANIMATE_CRIT_SCALE_2, ANIMATE_CRIT_SCALE_1, 0)
        );
        set.playSequentially(
                state1, state2,
                animateTranslation(view, 0f, 0f, ANIMATE_CRIT_DURATION_BACK)
        );
        set.start();
    }

    public void animateBlock(View view, boolean isPlayer) {
        float state_2 = ANIMATE_BLOCK_STATE_2 * (isPlayer ? 1 : -1);
        float state_3 = ANIMATE_BLOCK_STATE_3 * (isPlayer ? 1 : -1);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                animateRotation(view, ANIMATE_BLOCK_STATE_1, state_2, ANIMATE_BLOCK_DURATION_SHAKE),
                animateRotation(view, state_2, state_3, ANIMATE_BLOCK_DURATION_SHAKE),
                animateRotation(view, state_3, ANIMATE_BLOCK_STATE_1, ANIMATE_BLOCK_DURATION_SHAKE)
        );
        set.start();
    }

    public void animateBlockBreak(View view, boolean isPlayer) {
        float state_2 = ANIMATE_BLOCK_BREAK_STATE_2 * (isPlayer ? 1 : -1);
        float state_3 = ANIMATE_BLOCK_BREAK_STATE_3 * (isPlayer ? 1 : -1);
        float rotation_2 = ANIMATE_BLOCK_BREAK_ROTATION_2 * (isPlayer ? 1 : -1);
        float translation_x1 = ANIMATE_BLOCK_BREAK_TRANSLATION_X1 * (isPlayer ? 1 : -1);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        state1.setDuration(ANIMATE_BLOCK_BREAK_DURATION_ROTATE_1).playTogether(
                animateTranslation(view, translation_x1, ANIMATE_BLOCK_BREAK_TRANSLATION_Y1, 0),
                animateRotation(view, ANIMATE_BLOCK_BREAK_ROTATION_1, rotation_2, 0),
                animateChangeScale(view, ANIMATE_BLOCK_BREAK_SCALE_1, ANIMATE_BLOCK_BREAK_SCALE_2, 0)
        );
        state2.setDuration(ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2).playTogether(
                animateRotation(view, rotation_2, ANIMATE_BLOCK_BREAK_ROTATION_1, 0),
                animateChangeScale(view, ANIMATE_BLOCK_BREAK_SCALE_2, ANIMATE_BLOCK_BREAK_SCALE_1, 0)
        );
        set.playSequentially(
                animateRotation(view, ANIMATE_BLOCK_BREAK_STATE_1, state_2, ANIMATE_BLOCK_BREAK_DURATION_SHAKE),
                animateRotation(view, state_2, state_3, ANIMATE_BLOCK_BREAK_DURATION_SHAKE),
                state1, state2,
                animateTranslation(view, ANIMATE_BLOCK_BREAK_TRANSLATION_X2, ANIMATE_BLOCK_BREAK_TRANSLATION_Y2, ANIMATE_BLOCK_BREAK_DURATION_BACK)
        );
        set.start();
    }

    private AnimatorSet animateChangeScale(View view, float f1, float f2, long dur) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(dur).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, f1, f2),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, f1, f2)
        );
        return set;
    }

    private AnimatorSet animateRotation(View view, float f1, float f2, long dur) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(dur).playTogether(
                ObjectAnimator.ofFloat(view, View.ROTATION, f1, f2)
        );
        return set;
    }

    private AnimatorSet animateTranslation(View view, float x, float y, long dur) {
        AnimatorSet set = new AnimatorSet();
            set.setDuration(dur).playTogether(
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_X, x),
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, y)
            );
        return set;
    }

    private AnimatorSet animateHitOnReceivedDmg(View view, long dur, boolean playerOrEnemy) {
        float rotation1 = 0.0f;
        float rotation2 = 45.0f * (playerOrEnemy ? 1 : -1);
        float scale1 = 1.0f;
        float scale2 = 0.6f;
        float translationX = 100f * (playerOrEnemy ? 1 : -1);
        float translationY = Math.abs(translationX/2);
        AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    animateRotation(view, rotation1, rotation2, dur),
                    animateChangeScale(view, scale1, scale2, dur),
                    animateTranslation(view, translationX, translationY, dur)
            );
        return set;
    }

    private AnimatorSet animateHitOnRecoverFromDmg(View view, long dur, boolean playerOrEnemy) {
        float rotation1 = 0.0f;
        float rotation2 = 45.0f * (playerOrEnemy ? 1 : -1);
        float scale1 = 1.0f;
        float scale2 = 0.6f;
        float translationX = 0f;
        float translationY = 0f;
        AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    animateRotation(view, rotation2, rotation1, dur),
                    animateChangeScale(view, scale2, scale1, dur),
                    animateTranslation(view, translationX, translationY, dur)
            );
        return set;
    }

    private AnimatorListenerAdapter animListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            setAnimating(false);
            super.onAnimationEnd(animation);
        }

    };

    public void setAnimating(boolean set) {
        isAnimating = set;
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}

enum AnimationTypes {
    ANIMATION_BATTLE_ATTACK(0),
    ANIMATION_BATTLE_HIT(0),
    ANIMATION_BATTLE_BLOCK(0),
    ANIMATION_BATTLE_DODGE(0),
    ANIMATION_BATTLE_CRITICAL(0),
    ANIMATION_BATTLE_BLOCK_BREAK(0),
    ANIMATION_TEST(0);

    private long duration;

    AnimationTypes(long dur) {
        duration = dur;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    public long getDuration() {
        return duration;
    }
}