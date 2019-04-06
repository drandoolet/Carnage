package com.example.user.carnage.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AnimateGame {
    protected final  long ANIMATE_ATTACK_DURATION_TRANSLATION_1 = 500;
    protected final  long ANIMATE_ATTACK_DURATION_TRANSLATION_2 = 100;
    protected final  long ANIMATE_ATTACK_DURATION_TRANSLATION_3 = 300;
    protected final  long ANIMATE_ATTACK_DURATION_TRANSLATION_4 = 500;

    protected final long ANIMATE_DODGE_DURATION_JUMP_1 = 150;
    protected final long ANIMATE_DODGE_DURATION_JUMP_2 = 200;
    protected final long ANIMATE_DODGE_DURATION_JUMP_BACK = 500;
    private final  float ANIMATE_DODGE_TRANSLATION_X1 = 0f;
    private final float ANIMATE_DODGE_TRANSLATION_X2 = 40f;
    private final float ANIMATE_DODGE_TRANSLATION_X3 = 80f;
    private final float ANIMATE_DODGE_TRANSLATION_Y1 = 0f;
    private final float ANIMATE_DODGE_TRANSLATION_Y2 = -30f;
    private final float ANIMATE_DODGE_ROTATION_1 = 0.0f;
    private final float ANIMATE_DODGE_ROTATION_2 = 30.0f;

    protected final long ANIMATE_HIT_DURATION_1 = 300;
    protected final long ANIMATE_HIT_DURATION_2 = 1000;

    protected final long ANIMATE_CRIT_DURATION_ROTATE_1 = 500;
    protected final long ANIMATE_CRIT_DURATION_ROTATE_2 = 1000;
    protected final long ANIMATE_CRIT_DURATION_BACK = ANIMATE_CRIT_DURATION_ROTATE_2 / 2;
    private final float ANIMATE_CRIT_ROTATION_1 = 0.0f;
    private final float ANIMATE_CRIT_ROTATION_2 = 360.0f;
    private final float ANIMATE_CRIT_TRANSLATION_X1 = 50f;
    private final float ANIMATE_CRIT_TRANSLATION_Y1 = 0.0f;
    private final float ANIMATE_CRIT_SCALE_1 = 1.0f;
    private final float ANIMATE_CRIT_SCALE_2 = 0.6f;

    protected final long ANIMATE_BLOCK_DURATION_SHAKE = 100;
    private final float ANIMATE_BLOCK_STATE_1 = 0.0f;
    private final float ANIMATE_BLOCK_STATE_2 = 15.0f;
    private final float ANIMATE_BLOCK_STATE_3 = -15.0f;

    protected final long ANIMATE_BLOCK_BREAK_DURATION_SHAKE = 100;
    private final float ANIMATE_BLOCK_BREAK_STATE_1 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_STATE_2 = 15.0f;
    private final float ANIMATE_BLOCK_BREAK_STATE_3 = -15.0f;
    protected final long ANIMATE_BLOCK_BREAK_DURATION_ROTATE_1 = 500;
    protected final long ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2 = 1000;
    protected final long ANIMATE_BLOCK_BREAK_DURATION_BACK = ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2 / 2;
    private final float ANIMATE_BLOCK_BREAK_ROTATION_1 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_ROTATION_2 = 360.0f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_X1 = 50f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_X2 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_Y1 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_TRANSLATION_Y2 = 0.0f;
    private final float ANIMATE_BLOCK_BREAK_SCALE_1 = 1.0f;
    private final float ANIMATE_BLOCK_BREAK_SCALE_2 = 0.6f;

    protected final long ANIMATE_SKILLS_FRAGMENT_DURATION = 400;

    protected final long ANIMATE_PROFILE_CHOOSE_DURATION = 1000;
    protected final long ANIMATE_PROFILE_CHOOSE_DURATION_WAIT = 300;
    protected final long ANIMATE_PROFILE_CHOOSE_DURATION_UP = 600;

    protected long ANIMATE_POINTS_DURATION_1 = 200;
    protected long ANIMATE_POINTS_DURATION_2 = 1000;

    public AnimateGame() {
        AnimationTypes.ANIMATION_BATTLE_ATTACK.setDuration(ANIMATE_ATTACK_DURATION_TRANSLATION_1 + ANIMATE_ATTACK_DURATION_TRANSLATION_2);
        AnimationTypes.ANIMATION_BATTLE_DODGE.setDuration(ANIMATE_DODGE_DURATION_JUMP_1 + ANIMATE_DODGE_DURATION_JUMP_2 + ANIMATE_DODGE_DURATION_JUMP_BACK);
        AnimationTypes.ANIMATION_BATTLE_HIT.setDuration(ANIMATE_HIT_DURATION_1 + ANIMATE_HIT_DURATION_2);
        AnimationTypes.ANIMATION_BATTLE_CRITICAL.setDuration(ANIMATE_CRIT_DURATION_ROTATE_1 + ANIMATE_CRIT_DURATION_ROTATE_2 + ANIMATE_CRIT_DURATION_BACK);
        AnimationTypes.ANIMATION_BATTLE_BLOCK.setDuration(ANIMATE_BLOCK_DURATION_SHAKE *3);
        AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK.setDuration(ANIMATE_BLOCK_BREAK_DURATION_SHAKE *2
                + ANIMATE_BLOCK_BREAK_DURATION_ROTATE_1 + ANIMATE_BLOCK_BREAK_DURATION_ROTATE_2 + ANIMATE_BLOCK_BREAK_DURATION_BACK);
        AnimationTypes.ANIMATION_PROFILE_SELECTED.setDuration(ANIMATE_PROFILE_CHOOSE_DURATION + ANIMATE_PROFILE_CHOOSE_DURATION_WAIT);
        AnimationTypes.ANIMATION_PROFILE_SELECTED.setFullDuration(ANIMATE_PROFILE_CHOOSE_DURATION + ANIMATE_PROFILE_CHOOSE_DURATION_WAIT + ANIMATE_PROFILE_CHOOSE_DURATION_UP);

        long minDuration = ANIMATE_ATTACK_DURATION_TRANSLATION_1 + ANIMATE_ATTACK_DURATION_TRANSLATION_2
                + ANIMATE_ATTACK_DURATION_TRANSLATION_3 + ANIMATE_ATTACK_DURATION_TRANSLATION_4;

        AnimationTypes types[] = {AnimationTypes.ANIMATION_BATTLE_DODGE, AnimationTypes.ANIMATION_BATTLE_HIT,
                AnimationTypes.ANIMATION_BATTLE_CRITICAL, AnimationTypes.ANIMATION_BATTLE_BLOCK, AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK};
        for (AnimationTypes type : types) {
            if (type.getDuration() < minDuration) type.setDuration(minDuration);
        }
    }

    private boolean isAnimating = false;

    public void animateFade(boolean fadeOut, long duration, View...views) {
        AnimatorSet set = new AnimatorSet();
        for (View view : views) {
            set.setDuration(duration).playTogether(
                    ObjectAnimator.ofFloat(view, View.ALPHA, (fadeOut? 0:1), (fadeOut? 1:0)).setDuration(duration)
            );
        }
        set.start();
    }

    public void animateProfileChoose(View view, View layout, View[] viewsToFade, boolean isChosen) {
        AnimatorSet set = new AnimatorSet();
        AnimatorSet state1 = new AnimatorSet();
        float translation_x;
        float translation_y;
        float scale1;
        float scale2;
        if (isChosen) {
            translation_x = layout.getRight()/2 - view.getRight() + view.getWidth()/2;
            translation_y = layout.getBottom()/2 - view.getBottom() + view.getHeight()/2;
            scale1 = 1f;
            scale2 = (layout.getWidth() / view.getWidth()) * 0.7F;
        } else {
            translation_x = 0;
            translation_y = 0;
            scale1 = (layout.getWidth() / view.getWidth()) * 0.7F;
            scale2 = 1f;
        }

        state1.setDuration(ANIMATE_PROFILE_CHOOSE_DURATION).playTogether(
                animateTranslation(view, translation_x, translation_y, 0),
                animateChangeScale(view, scale1, scale2, 0)
        );

        for (View view1 : viewsToFade) {
            state1.setDuration(ANIMATE_PROFILE_CHOOSE_DURATION).playTogether(
                    ObjectAnimator.ofFloat(view1, View.ALPHA, (isChosen? 1:0), (isChosen? 0:1))
            );
        }

        if (isChosen) {
            set.playSequentially(
                    state1,
                    ObjectAnimator.ofFloat(view, View.ALPHA, 1, 1).setDuration(ANIMATE_PROFILE_CHOOSE_DURATION_WAIT),
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getTop()/(-2)).setDuration(ANIMATE_PROFILE_CHOOSE_DURATION_UP)
            );
        }
        set.playTogether(
                state1
        );
        set.start();
    }

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
        AnimatorSet set = getAnimateDamagePointsSet(view, isPlayer);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
    }

    protected AnimatorSet getAnimateDamagePointsSet(final View view, boolean isPlayer) {
        view.setVisibility(View.VISIBLE);
        float ANIMATE_POINTS_TRANSLATION_X = 10f * (isPlayer ? -1 : 1);
        float ANIMATE_POINTS_TRANSLATION_Y = -40f;
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
        return set;
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

    protected AnimatorSet animateChangeScale(View view, float f1, float f2, long dur) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(dur).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, f1, f2),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, f1, f2)
        );
        return set;
    }

    protected AnimatorSet animateRotation(View view, float f1, float f2, long dur) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(dur).playTogether(
                ObjectAnimator.ofFloat(view, View.ROTATION, f1, f2)
        );
        return set;
    }

    protected AnimatorSet animateTranslation(View view, float x, float y, long dur) {
        AnimatorSet set = new AnimatorSet();
            set.setDuration(dur).playTogether(
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_X, x),
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, y)
            );
        return set;
    }

    protected AnimatorSet animateHitOnReceivedDmg(View view, long dur, boolean playerOrEnemy) {
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

    protected AnimatorSet animateHitOnRecoverFromDmg(View view, long dur, boolean playerOrEnemy) {
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

    public enum AnimationTypes {
        ANIMATION_BATTLE_ATTACK(0),
        ANIMATION_BATTLE_HIT(0),
        ANIMATION_BATTLE_BLOCK(0),
        ANIMATION_BATTLE_DODGE(0),
        ANIMATION_BATTLE_CRITICAL(0),
        ANIMATION_BATTLE_BLOCK_BREAK(0),
        ANIMATION_TEST(0),
        ANIMATION_PROFILE_SELECTED(0);

        private long duration;
        private long fullDuration = 0;

        AnimationTypes(long dur) {
            duration = dur;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
        public long getDuration() {
            return duration;
        }

        public void setFullDuration(long fullDuration) {
            this.fullDuration = fullDuration;
        }

        public long getFullDuration() {
            if (fullDuration == 0) return duration;
            return fullDuration;
        }
    }
}

