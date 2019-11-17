package com.example.user.carnage.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.user.carnage.R;

import java.util.HashMap;

public class AnimateGame {
    interface Durations {
        long getDuration();
    }
    interface Floats {
        float getFloat();
    }

    enum Attack implements Durations {
        TRANSLATION_DUR_1(500),
        TRANSLATION_DUR_2(100),
        TRANSLATION_DUR_3(300),
        TRANSLATION_DUR_4(500);

        private final long duration;
        Attack(long dur) { duration = dur; }

        @Override
        public long getDuration() {
            return duration;
        }
        public static long getDurationToPoints() {
            return TRANSLATION_DUR_1.duration +TRANSLATION_DUR_2.duration;
        }
        public static long getFullDuration() {
            return TRANSLATION_DUR_1.duration + TRANSLATION_DUR_2.duration +
                    TRANSLATION_DUR_3.duration + TRANSLATION_DUR_4.duration;
        }
    }

    enum Dodge {
        ;
        enum Duration implements Durations {
            JUMP_1(150),
            JUMP_2(200),
            JUMP_BACK(500);

            private final long duration;
            Duration(long dur) { duration = dur; }
            @Override
            public long getDuration() {
                return duration;
            }
        }
        enum Float implements Floats {
            TRANSLATION_X_1(0F),
            TRANSLATION_X_2(40F),
            TRANSLATION_X_3(80F),
            TRANSLATION_Y_1(0F),
            TRANSLATION_Y_2(-30F),
            ROTATION_1(0F),
            ROTATION_2(30F);

            private final float value;
            Float(float f) { value = f; }

            @Override
            public float getFloat() {
                return value;
            }
        }

        public static long getDurationToPoints() {
            return Duration.JUMP_1.duration + Duration.JUMP_2.duration + Duration.JUMP_BACK.duration;
        }
    }

    enum Hit implements Durations {
        DURATION_1(300), DURATION_2(1000);

        private final long duration;
        Hit(long dur) { duration = dur; }

        @Override
        public long getDuration() {
            return duration;
        }
        public static long getDurationToPoints() {
            return DURATION_1.duration + DURATION_2.duration;
        }
    }

    enum Critical {
        ;
        enum Duration implements Durations {
            ROTATE_1(500), ROTATE_2(1000),
            BACK(ROTATE_2.duration / 2);

            private final long duration;
            Duration(long dur) { duration = dur; }

            @Override
            public long getDuration() {
                return duration;
            }
        }
        enum Float implements Floats {
            ROTATION_1(0F), ROTATION_2(360F),
            TRANSLATION_X_1(50F), TRANSLATION_Y_1(0F),
            SCALE_1(1F), SCALE_2(0.6F);

            private final float value;
            Float(float f) { value = f; }

            @Override
            public float getFloat() {
                return value;
            }
        }

        public static long getDurationToPoints() {
            return Duration.ROTATE_1.duration + Duration.ROTATE_2.duration + Duration.BACK.duration;
        }
    }

    enum Block {
        ;
        enum Duration implements Durations {
            SHAKE_DURATION(100);

            private final long duration;
            Duration(long dur) { duration = dur; }

            @Override
            public long getDuration() {
                return duration;
            }
        }
        enum Float implements Floats {
            STATE_1(0f), STATE_2(15f), STATE_3(-15f);

            private final float value;
            Float(float f) { value = f; }

            @Override
            public float getFloat() {
                return value;
            }
        }
        public static long getDurationToPoints() {
            return Duration.SHAKE_DURATION.duration * 3;
        }
    }

    enum BlockBreak {
        ;
        enum Duration implements Durations {
            SHAKE(100),
            ROTATION_1(500), ROTATION_2(1000),
            BACK(ROTATION_2.duration / 2);

            private final long duration;
            Duration(long dur) { duration = dur; }

            @Override
            public long getDuration() {
                return duration;
            }
        }
        enum Float implements Floats {
            STATE_1(0F), STATE_2(15F), STATE_3(-15F),
            ROTATION_1(0F), ROTATION_2(360F),
            TRANSLATION_X_1(50F), TRANSLATION_X_2(0F),
            TRANSLATION_Y_1(0F), TRANSLATION_Y_2(0F),
            SCALE_1(1F), SCALE_2(0.6F);

            private final float value;
            Float(float f) { value = f; }

            @Override
            public float getFloat() {
                return value;
            }
        }
        public static long getDurationToPoints() {
            return Duration.SHAKE.duration*2 + Duration.ROTATION_1.duration
                    + Duration.ROTATION_2.duration + Duration.BACK.duration;
        }
    }

    enum ProfileChoose implements Durations {
        DURATION(1000), WAIT(300), UP(600);

        private final long duration;
        ProfileChoose(long dur) { duration = dur; }

        @Override
        public long getDuration() {
            return duration;
        }

        public static long getDurationToFadeOut() {
            return DURATION.duration + WAIT.duration;
        }
        public static long getTotalDuration() {
            return DURATION.duration + WAIT.duration + UP.duration;
        }
    }

    enum Points implements Durations {
        DURATION_1(200), DURATION_2(1000);

        private final long duration;
        Points(long dur) { duration = dur; }

        @Override
        public long getDuration() {
            return duration;
        }

        public static long getTotalDuration() {
            return DURATION_1.duration + DURATION_2.duration;
        }
    }

    protected static final Durations NULL_DURATION = new Durations() {
        @Override
        public long getDuration() {
            return 0;
        }
    };

    protected final long ANIMATE_SKILLS_FRAGMENT_DURATION = 400;

    public AnimateGame() {
        AnimationTypes.ANIMATION_BATTLE_ATTACK.setDuration(Attack.getDurationToPoints());
        AnimationTypes.ANIMATION_BATTLE_ATTACK.setFullDuration(Attack.getFullDuration());
        AnimationTypes.ANIMATION_BATTLE_DODGE.setDuration(Dodge.getDurationToPoints());
        AnimationTypes.ANIMATION_BATTLE_HIT.setDuration(Hit.getDurationToPoints());
        AnimationTypes.ANIMATION_BATTLE_CRITICAL.setDuration(Critical.getDurationToPoints());
        AnimationTypes.ANIMATION_BATTLE_BLOCK.setDuration(Block.getDurationToPoints());
        AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK.setDuration(BlockBreak.getDurationToPoints());
        AnimationTypes.ANIMATION_PROFILE_SELECTED.setDuration(ProfileChoose.getDurationToFadeOut());
        AnimationTypes.ANIMATION_PROFILE_SELECTED.setFullDuration(ProfileChoose.getTotalDuration());

        long minDuration = Attack.TRANSLATION_DUR_1.duration + Attack.TRANSLATION_DUR_2.duration
                + Attack.TRANSLATION_DUR_3.duration + Attack.TRANSLATION_DUR_4.duration;

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

        state1.setDuration(ProfileChoose.DURATION.getDuration()).playTogether(
                animateTranslation(view, translation_x, translation_y, NULL_DURATION),
                animateChangeScale(view, scale1, scale2, NULL_DURATION)
        );

        for (View view1 : viewsToFade) {
            state1.setDuration(ProfileChoose.DURATION.getDuration()).playTogether(
                    ObjectAnimator.ofFloat(view1, View.ALPHA, (isChosen? 1:0), (isChosen? 0:1))
            );
        }

        if (isChosen) {
            set.playSequentially(
                    state1,
                    ObjectAnimator.ofFloat(view, View.ALPHA, 1, 1)
                            .setDuration(ProfileChoose.WAIT.getDuration()),
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getTop()/(-2))
                            .setDuration(ProfileChoose.UP.getDuration())
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
                animateChangeScale(view, f1, f2, NULL_DURATION),
                ObjectAnimator.ofFloat(view, View.ALPHA, f1, f2)
        );
        if (!b) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                }
            });
        }
        set.start();
    }

    public void animateDamagePoints(final View view, boolean isPlayer, boolean isHeal) {
        TextView textView = (TextView) view;
        textView.setTextColor(isHeal ? view.getContext().getColor(R.color.colorHealPoints) :
                view.getContext().getColor(R.color.colorDamagePoints));
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

    private static AnimatorSet getAnimateDamagePointsSet(final View view, boolean isPlayer) {
        view.setVisibility(View.VISIBLE);
        float ANIMATE_POINTS_TRANSLATION_X = 10f * (isPlayer ? -1 : 1);
        float ANIMATE_POINTS_TRANSLATION_Y = -40f;
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        AnimatorSet state3 = new AnimatorSet();
        AnimatorSet set = new AnimatorSet();
        state1.setDuration(Points.DURATION_1.getDuration()).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0.1f, 1.0f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.1f, 1.0f),
                ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1)
        );
        state2.setDuration(Points.DURATION_2.getDuration()).playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.3f),
                animateTranslation(view, ANIMATE_POINTS_TRANSLATION_X, ANIMATE_POINTS_TRANSLATION_Y, NULL_DURATION),
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.7f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.7f)
        );
        state3.setDuration(0).playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 0f),
                animateTranslation(view, 0f, 0f, NULL_DURATION)
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
                animateTranslation(view, translation_1_x, translation_1_y, Attack.TRANSLATION_DUR_1),
                animateTranslation(view, translation_2_x, translation_2_y, Attack.TRANSLATION_DUR_2),
                animateTranslation(view, translation_1_x, translation_1_y, Attack.TRANSLATION_DUR_3),
                animateTranslation(view, 0f, 0f, Attack.TRANSLATION_DUR_4)
        );
        set.start();
    }

    public AnimatorSet getAnimateAttack(View view, View enView, boolean isPlayer) {
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
                animateTranslation(view, translation_1_x, translation_1_y, Attack.TRANSLATION_DUR_1),
                animateTranslation(view, translation_2_x, translation_2_y, Attack.TRANSLATION_DUR_2),
                animateTranslation(view, translation_1_x, translation_1_y, Attack.TRANSLATION_DUR_3),
                animateTranslation(view, 0f, 0f, Attack.TRANSLATION_DUR_4)
        );
        return set;
    }

    public void animateDodge(View view, boolean playerOrEnemy) {
        float translation_x2 = Dodge.Float.TRANSLATION_X_2.getFloat() * (playerOrEnemy ? 1 : -1);
        float translation_x3 = Dodge.Float.TRANSLATION_X_3.getFloat() * (playerOrEnemy ? 1 : -1);
        float rotation_2 = Dodge.Float.ROTATION_2.getFloat() * (playerOrEnemy ? -1 : 1);
        setAnimating(true);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet jump = new AnimatorSet();
        AnimatorSet jump1 = new AnimatorSet();
        jump.setDuration(Dodge.Duration.JUMP_1.getDuration()).playTogether(
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X,
                        Dodge.Float.TRANSLATION_X_1.getFloat(), translation_x2),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,
                        Dodge.Float.TRANSLATION_Y_1.getFloat(), Dodge.Float.TRANSLATION_Y_2.getFloat()),
                ObjectAnimator.ofFloat(view, View.ROTATION, Dodge.Float.ROTATION_1.getFloat(), rotation_2)
        );
        jump1.setDuration(Dodge.Duration.JUMP_2.getDuration()).playTogether(
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x2, translation_x3),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, Dodge.Float.TRANSLATION_Y_2.getFloat(),
                        Dodge.Float.TRANSLATION_Y_1.getFloat()),
                ObjectAnimator.ofFloat(view, View.ROTATION, rotation_2, Dodge.Float.ROTATION_1.getFloat())
        );

        set.playSequentially(
                jump, jump1,
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x3,
                        Dodge.Float.TRANSLATION_X_1.getFloat()).setDuration(Dodge.Duration.JUMP_BACK.getDuration())
        );
        set.addListener(animListener);
        set.start();
    }

    public void animateHit(View view, boolean playerOrEnemy) {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                animateHitOnReceivedDmg(view, Hit.DURATION_1, playerOrEnemy),
                animateHitOnRecoverFromDmg(view, Hit.DURATION_2, playerOrEnemy)

        );
        set.start();
    }

    public void animateCriticalHit(View view, boolean isPlayer) {
        float rotation_2 = Critical.Float.ROTATION_2.getFloat() * (isPlayer ? 1 : -1);
        float translation_2 = Critical.Float.TRANSLATION_X_1.getFloat() * (isPlayer ? 1 : -1);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        state1.setDuration(Critical.Duration.ROTATE_1.getDuration()).playTogether(
                animateTranslation(view, translation_2,
                        Critical.Float.TRANSLATION_Y_1.getFloat(), NULL_DURATION),
                animateRotation(view, Critical.Float.ROTATION_1.getFloat(),
                        rotation_2, NULL_DURATION),
                animateChangeScale(view, Critical.Float.SCALE_1.getFloat(),
                        Critical.Float.SCALE_2.getFloat(), NULL_DURATION)
        );
        state2.setDuration(Critical.Duration.ROTATE_2.getDuration()).playTogether(
                animateRotation(view, rotation_2, Critical.Float.ROTATION_1.getFloat(), NULL_DURATION),
                animateChangeScale(view, Critical.Float.SCALE_2.getFloat(),
                        Critical.Float.SCALE_1.getFloat(), NULL_DURATION)
        );
        set.playSequentially(
                state1, state2,
                animateTranslation(view, 0f, 0f, Critical.Duration.BACK)
        );
        set.start();
    }

    public void animateBlock(View view, boolean isPlayer) {
        float state_2 = Block.Float.STATE_2.getFloat() * (isPlayer ? 1 : -1);
        float state_3 = Block.Float.STATE_3.getFloat() * (isPlayer ? 1 : -1);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                animateRotation(view, Block.Float.STATE_1.getFloat(), state_2, Block.Duration.SHAKE_DURATION),
                animateRotation(view, state_2, state_3, Block.Duration.SHAKE_DURATION),
                animateRotation(view, state_3, Block.Float.STATE_1.getFloat(), Block.Duration.SHAKE_DURATION)
        );
        set.start();
    }

    public void animateBlockBreak(View view, boolean isPlayer) {
        float state_2 = BlockBreak.Float.STATE_2.getFloat() * (isPlayer ? 1 : -1);
        float state_3 = BlockBreak.Float.STATE_3.getFloat() * (isPlayer ? 1 : -1);
        float rotation_2 = BlockBreak.Float.ROTATION_2.getFloat() * (isPlayer ? 1 : -1);
        float translation_x1 = BlockBreak.Float.TRANSLATION_X_1.getFloat() * (isPlayer ? 1 : -1);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        state1.setDuration(BlockBreak.Duration.ROTATION_1.getDuration()).playTogether(
                animateTranslation(view, translation_x1,
                        BlockBreak.Float.TRANSLATION_Y_1.getFloat(), NULL_DURATION),
                animateRotation(view, BlockBreak.Float.ROTATION_1.getFloat(),
                        rotation_2, NULL_DURATION),
                animateChangeScale(view, BlockBreak.Float.SCALE_1.getFloat(),
                        BlockBreak.Float.SCALE_2.getFloat(), NULL_DURATION)
        );
        state2.setDuration(BlockBreak.Duration.ROTATION_2.getDuration()).playTogether(
                animateRotation(view, rotation_2, BlockBreak.Float.ROTATION_1.getFloat(),
                        NULL_DURATION),
                animateChangeScale(view, BlockBreak.Float.SCALE_2.getFloat(),
                        BlockBreak.Float.SCALE_1.getFloat(), NULL_DURATION)
        );
        set.playSequentially(
                animateRotation(view, BlockBreak.Float.STATE_1.getFloat(), state_2, BlockBreak.Duration.SHAKE),
                animateRotation(view, state_2, state_3, BlockBreak.Duration.SHAKE),
                state1, state2,
                animateTranslation(view, BlockBreak.Float.TRANSLATION_X_2.getFloat(),
                        BlockBreak.Float.TRANSLATION_Y_2.getFloat(), BlockBreak.Duration.BACK)
        );
        set.start();
    }

    protected static AnimatorSet animateChangeScale(View view, float f1, float f2, Durations dur) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(dur.getDuration()).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, f1, f2),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, f1, f2)
        );
        return set;
    }

    protected static AnimatorSet animateRotation(View view, float f1, float f2, Durations dur) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(dur.getDuration()).playTogether(
                ObjectAnimator.ofFloat(view, View.ROTATION, f1, f2)
        );
        return set;
    }

    protected static AnimatorSet animateTranslation(View view, float x, float y, Durations dur) {
        AnimatorSet set = new AnimatorSet();
            set.setDuration(dur.getDuration()).playTogether(
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_X, x),
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, y)
            );
        return set;
    }

    protected static AnimatorSet animateHitOnReceivedDmg(View view, Durations dur, boolean playerOrEnemy) {
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

    protected static AnimatorSet animateHitOnRecoverFromDmg(View view, Durations dur, boolean playerOrEnemy) {
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

    public AnimationSetsWithDurations getAnimatorSet(AnimationTypes type, View view, @Nullable View enView, boolean playerOrEnemy) {
        AnimatorSet set;

        switch (type) {
            case ANIMATION_BATTLE_ATTACK: {
                set = getAnimateAttack(view, enView, playerOrEnemy);
                break;
            }
            case ANIMATION_BATTLE_HIT:
            default: {
                set = new AnimatorSet();
                set.playSequentially(
                        animateHitOnReceivedDmg(view, Hit.DURATION_1, playerOrEnemy),
                        animateHitOnRecoverFromDmg(view, Hit.DURATION_2, playerOrEnemy)

                );
                break;
            }
        }
        AnimationSetsWithDurations durSet = new AnimationSetsWithDurations(set, type.getDuration());
        return durSet;
    }

    class AnimationSetsWithDurations {
        private final AnimatorSet set;
        private final long duration;

        AnimationSetsWithDurations(AnimatorSet set, long duration) {
            this.set = set;
            this.duration = duration;
        }

        public AnimatorSet getSet() {
            return set;
        }

        public long getDuration() {
            return duration;
        }
    }

    public enum AnimationTypes {
        ANIMATION_BATTLE_ATTACK(0) {
            @Override
            public AnimatorSet getSet(boolean isPlayer, View...views) {
                View view = views[0];
                View enView = views[1];

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
                        animateTranslation(view, translation_1_x, translation_1_y, Attack.TRANSLATION_DUR_1),
                        animateTranslation(view, translation_2_x, translation_2_y, Attack.TRANSLATION_DUR_2),
                        animateTranslation(view, translation_1_x, translation_1_y, Attack.TRANSLATION_DUR_3),
                        animateTranslation(view, 0f, 0f, Attack.TRANSLATION_DUR_4)
                );
                return set;
            }
        },
        ANIMATION_BATTLE_HIT(0) {
            @Override
            public AnimatorSet getSet(boolean isPlayer, View...views) {
                View view = views[0];
                AnimatorSet set = new AnimatorSet();
                set.playSequentially(
                        animateHitOnReceivedDmg(view, Hit.DURATION_1, isPlayer),
                        animateHitOnRecoverFromDmg(view, Hit.DURATION_2, isPlayer)

                );
                return set;
            }
        },
        ANIMATION_BATTLE_BLOCK(0) {
            @Override
            public AnimatorSet getSet(boolean isPlayer, View...views) {
                View view = views[0];
                float state_2 = Block.Float.STATE_2.getFloat() * (isPlayer ? 1 : -1);
                float state_3 = Block.Float.STATE_3.getFloat() * (isPlayer ? 1 : -1);
                AnimatorSet set = new AnimatorSet();
                set.playSequentially(
                        animateRotation(view, Block.Float.STATE_1.getFloat(), state_2, Block.Duration.SHAKE_DURATION),
                        animateRotation(view, state_2, state_3, Block.Duration.SHAKE_DURATION),
                        animateRotation(view, state_3, Block.Float.STATE_1.getFloat(), Block.Duration.SHAKE_DURATION)
                );
                return set;
            }
        },
        ANIMATION_BATTLE_DODGE(0) {
            @Override
            public AnimatorSet getSet(boolean isPlayer, View...views) {
                View view = views[0];
                float translation_x2 = Dodge.Float.TRANSLATION_X_2.getFloat() * (isPlayer ? 1 : -1);
                float translation_x3 = Dodge.Float.TRANSLATION_X_3.getFloat() * (isPlayer ? 1 : -1);
                float rotation_2 = Dodge.Float.ROTATION_2.getFloat() * (isPlayer ? -1 : 1);

                AnimatorSet set = new AnimatorSet();
                AnimatorSet jump = new AnimatorSet();
                AnimatorSet jump1 = new AnimatorSet();
                jump.setDuration(Dodge.Duration.JUMP_1.getDuration()).playTogether(
                        ObjectAnimator.ofFloat(view, View.TRANSLATION_X,
                                Dodge.Float.TRANSLATION_X_1.getFloat(), translation_x2),
                        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,
                                Dodge.Float.TRANSLATION_Y_1.getFloat(), Dodge.Float.TRANSLATION_Y_2.getFloat()),
                        ObjectAnimator.ofFloat(view, View.ROTATION, Dodge.Float.ROTATION_1.getFloat(), rotation_2)
                );
                jump1.setDuration(Dodge.Duration.JUMP_2.getDuration()).playTogether(
                        ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x2, translation_x3),
                        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, Dodge.Float.TRANSLATION_Y_2.getFloat(),
                                Dodge.Float.TRANSLATION_Y_1.getFloat()),
                        ObjectAnimator.ofFloat(view, View.ROTATION, rotation_2, Dodge.Float.ROTATION_1.getFloat())
                );

                set.playSequentially(
                        jump, jump1,
                        ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x3,
                                Dodge.Float.TRANSLATION_X_1.getFloat()).setDuration(Dodge.Duration.JUMP_BACK.getDuration())
                );
                return set;
            }
        },
        ANIMATION_BATTLE_CRITICAL(0) {
            @Override
            public AnimatorSet getSet(boolean isPlayer, View...views) {
                View view = views[0];
                float rotation_2 = Critical.Float.ROTATION_2.getFloat() * (isPlayer ? 1 : -1);
                float translation_2 = Critical.Float.TRANSLATION_X_1.getFloat() * (isPlayer ? 1 : -1);
                AnimatorSet set = new AnimatorSet();
                AnimatorSet state1 = new AnimatorSet();
                AnimatorSet state2 = new AnimatorSet();
                state1.setDuration(Critical.Duration.ROTATE_1.getDuration()).playTogether(
                        animateTranslation(view, translation_2,
                                Critical.Float.TRANSLATION_Y_1.getFloat(), NULL_DURATION),
                        animateRotation(view, Critical.Float.ROTATION_1.getFloat(),
                                rotation_2, NULL_DURATION),
                        animateChangeScale(view, Critical.Float.SCALE_1.getFloat(),
                                Critical.Float.SCALE_2.getFloat(), NULL_DURATION)
                );
                state2.setDuration(Critical.Duration.ROTATE_2.getDuration()).playTogether(
                        animateRotation(view, rotation_2, Critical.Float.ROTATION_1.getFloat(), NULL_DURATION),
                        animateChangeScale(view, Critical.Float.SCALE_2.getFloat(),
                                Critical.Float.SCALE_1.getFloat(), NULL_DURATION)
                );
                set.playSequentially(
                        state1, state2,
                        animateTranslation(view, 0f, 0f, Critical.Duration.BACK)
                );
                return set;
            }
        },
        ANIMATION_BATTLE_BLOCK_BREAK(0) {
            @Override
            public AnimatorSet getSet(boolean isPlayer, View...views) {
                View view = views[0];
                float state_2 = BlockBreak.Float.STATE_2.getFloat() * (isPlayer ? 1 : -1);
                float state_3 = BlockBreak.Float.STATE_3.getFloat() * (isPlayer ? 1 : -1);
                float rotation_2 = BlockBreak.Float.ROTATION_2.getFloat() * (isPlayer ? 1 : -1);
                float translation_x1 = BlockBreak.Float.TRANSLATION_X_1.getFloat() * (isPlayer ? 1 : -1);
                AnimatorSet set = new AnimatorSet();
                AnimatorSet state1 = new AnimatorSet();
                AnimatorSet state2 = new AnimatorSet();
                state1.setDuration(BlockBreak.Duration.ROTATION_1.getDuration()).playTogether(
                        animateTranslation(view, translation_x1,
                                BlockBreak.Float.TRANSLATION_Y_1.getFloat(), NULL_DURATION),
                        animateRotation(view, BlockBreak.Float.ROTATION_1.getFloat(),
                                rotation_2, NULL_DURATION),
                        animateChangeScale(view, BlockBreak.Float.SCALE_1.getFloat(),
                                BlockBreak.Float.SCALE_2.getFloat(), NULL_DURATION)
                );
                state2.setDuration(BlockBreak.Duration.ROTATION_2.getDuration()).playTogether(
                        animateRotation(view, rotation_2, BlockBreak.Float.ROTATION_1.getFloat(),
                                NULL_DURATION),
                        animateChangeScale(view, BlockBreak.Float.SCALE_2.getFloat(),
                                BlockBreak.Float.SCALE_1.getFloat(), NULL_DURATION)
                );
                set.playSequentially(
                        animateRotation(view, BlockBreak.Float.STATE_1.getFloat(), state_2, BlockBreak.Duration.SHAKE),
                        animateRotation(view, state_2, state_3, BlockBreak.Duration.SHAKE),
                        state1, state2,
                        animateTranslation(view, BlockBreak.Float.TRANSLATION_X_2.getFloat(),
                                BlockBreak.Float.TRANSLATION_Y_2.getFloat(), BlockBreak.Duration.BACK)
                );
                return set;
            }
        },
        ANIMATION_PROFILE_SELECTED(0) { // TODO: very weak! Consider rebuilding
            @Override
            public AnimatorSet getSet(boolean isChosen, View...views) {
                View view = views[0];
                View layout = views[1];
                View[] viewsToFade = {views[2], views[3], views[4], views[5]};
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

                state1.setDuration(ProfileChoose.DURATION.getDuration()).playTogether(
                        animateTranslation(view, translation_x, translation_y, NULL_DURATION),
                        animateChangeScale(view, scale1, scale2, NULL_DURATION)
                );

                for (View view1 : viewsToFade) {
                    state1.setDuration(ProfileChoose.DURATION.getDuration()).playTogether(
                            ObjectAnimator.ofFloat(view1, View.ALPHA, (isChosen? 1:0), (isChosen? 0:1))
                    );
                }

                if (isChosen) {
                    set.playSequentially(
                            state1,
                            ObjectAnimator.ofFloat(view, View.ALPHA, 1, 1)
                                    .setDuration(ProfileChoose.WAIT.getDuration()),
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getTop()/(-2))
                                    .setDuration(ProfileChoose.UP.getDuration())
                    );
                }
                set.playTogether(
                        state1
                );
                return set;
            }
        };

        public interface DefenceAnimation extends Durations {
            AnimatorSet getSet(boolean isPlayer, View view);
            long getDuration();
            long getFullDuration();
        }

        public enum Defence implements DefenceAnimation {
            ANIMATION_BATTLE_HIT {
                @Override
                public AnimatorSet getSet(boolean isPlayer, View view) {
                    AnimatorSet set = new AnimatorSet();
                    set.playSequentially(
                            animateHitOnReceivedDmg(view, Hit.DURATION_1, isPlayer),
                            animateHitOnRecoverFromDmg(view, Hit.DURATION_2, isPlayer)

                    );
                    return set;
                }

                @Override
                public long getDuration() {
                    return Hit.getDurationToPoints();
                }

                @Override
                public long getFullDuration() {
                    return getDuration();
                }
            },
            ANIMATION_BATTLE_BLOCK {
                @Override
                public AnimatorSet getSet(boolean isPlayer, View view) {
                    float state_2 = Block.Float.STATE_2.getFloat() * (isPlayer ? 1 : -1);
                    float state_3 = Block.Float.STATE_3.getFloat() * (isPlayer ? 1 : -1);
                    AnimatorSet set = new AnimatorSet();
                    set.playSequentially(
                            animateRotation(view, Block.Float.STATE_1.getFloat(), state_2, Block.Duration.SHAKE_DURATION),
                            animateRotation(view, state_2, state_3, Block.Duration.SHAKE_DURATION),
                            animateRotation(view, state_3, Block.Float.STATE_1.getFloat(), Block.Duration.SHAKE_DURATION)
                    );
                    return set;
                }

                @Override
                public long getDuration() {
                    return Block.getDurationToPoints();
                }

                @Override
                public long getFullDuration() {
                    return getDuration();
                }
            },
            ANIMATION_BATTLE_DODGE {
                @Override
                public AnimatorSet getSet(boolean isPlayer, View view) {
                    float translation_x2 = Dodge.Float.TRANSLATION_X_2.getFloat() * (isPlayer ? 1 : -1);
                    float translation_x3 = Dodge.Float.TRANSLATION_X_3.getFloat() * (isPlayer ? 1 : -1);
                    float rotation_2 = Dodge.Float.ROTATION_2.getFloat() * (isPlayer ? -1 : 1);

                    AnimatorSet set = new AnimatorSet();
                    AnimatorSet jump = new AnimatorSet();
                    AnimatorSet jump1 = new AnimatorSet();
                    jump.setDuration(Dodge.Duration.JUMP_1.getDuration()).playTogether(
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_X,
                                    Dodge.Float.TRANSLATION_X_1.getFloat(), translation_x2),
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,
                                    Dodge.Float.TRANSLATION_Y_1.getFloat(), Dodge.Float.TRANSLATION_Y_2.getFloat()),
                            ObjectAnimator.ofFloat(view, View.ROTATION, Dodge.Float.ROTATION_1.getFloat(), rotation_2)
                    );
                    jump1.setDuration(Dodge.Duration.JUMP_2.getDuration()).playTogether(
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x2, translation_x3),
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, Dodge.Float.TRANSLATION_Y_2.getFloat(),
                                    Dodge.Float.TRANSLATION_Y_1.getFloat()),
                            ObjectAnimator.ofFloat(view, View.ROTATION, rotation_2, Dodge.Float.ROTATION_1.getFloat())
                    );

                    set.playSequentially(
                            jump, jump1,
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translation_x3,
                                    Dodge.Float.TRANSLATION_X_1.getFloat()).setDuration(Dodge.Duration.JUMP_BACK.getDuration())
                    );
                    return set;
                }

                @Override
                public long getDuration() {
                    return Dodge.getDurationToPoints();
                }

                @Override
                public long getFullDuration() {
                    return getDuration();
                }
            },
            ANIMATION_BATTLE_CRITICAL {
                @Override
                public AnimatorSet getSet(boolean isPlayer, View view) {
                    float rotation_2 = Critical.Float.ROTATION_2.getFloat() * (isPlayer ? 1 : -1);
                    float translation_2 = Critical.Float.TRANSLATION_X_1.getFloat() * (isPlayer ? 1 : -1);
                    AnimatorSet set = new AnimatorSet();
                    AnimatorSet state1 = new AnimatorSet();
                    AnimatorSet state2 = new AnimatorSet();
                    state1.setDuration(Critical.Duration.ROTATE_1.getDuration()).playTogether(
                            animateTranslation(view, translation_2,
                                    Critical.Float.TRANSLATION_Y_1.getFloat(), NULL_DURATION),
                            animateRotation(view, Critical.Float.ROTATION_1.getFloat(),
                                    rotation_2, NULL_DURATION),
                            animateChangeScale(view, Critical.Float.SCALE_1.getFloat(),
                                    Critical.Float.SCALE_2.getFloat(), NULL_DURATION)
                    );
                    state2.setDuration(Critical.Duration.ROTATE_2.getDuration()).playTogether(
                            animateRotation(view, rotation_2, Critical.Float.ROTATION_1.getFloat(), NULL_DURATION),
                            animateChangeScale(view, Critical.Float.SCALE_2.getFloat(),
                                    Critical.Float.SCALE_1.getFloat(), NULL_DURATION)
                    );
                    set.playSequentially(
                            state1, state2,
                            animateTranslation(view, 0f, 0f, Critical.Duration.BACK)
                    );
                    return set;
                }

                @Override
                public long getDuration() {
                    return Critical.getDurationToPoints();
                }

                @Override
                public long getFullDuration() {
                    return getDuration();
                }
            },
            ANIMATION_BATTLE_BLOCK_BREAK {
                @Override
                public AnimatorSet getSet(boolean isPlayer, View view) {
                    float state_2 = BlockBreak.Float.STATE_2.getFloat() * (isPlayer ? 1 : -1);
                    float state_3 = BlockBreak.Float.STATE_3.getFloat() * (isPlayer ? 1 : -1);
                    float rotation_2 = BlockBreak.Float.ROTATION_2.getFloat() * (isPlayer ? 1 : -1);
                    float translation_x1 = BlockBreak.Float.TRANSLATION_X_1.getFloat() * (isPlayer ? 1 : -1);
                    AnimatorSet set = new AnimatorSet();
                    AnimatorSet state1 = new AnimatorSet();
                    AnimatorSet state2 = new AnimatorSet();
                    state1.setDuration(BlockBreak.Duration.ROTATION_1.getDuration()).playTogether(
                            animateTranslation(view, translation_x1,
                                    BlockBreak.Float.TRANSLATION_Y_1.getFloat(), NULL_DURATION),
                            animateRotation(view, BlockBreak.Float.ROTATION_1.getFloat(),
                                    rotation_2, NULL_DURATION),
                            animateChangeScale(view, BlockBreak.Float.SCALE_1.getFloat(),
                                    BlockBreak.Float.SCALE_2.getFloat(), NULL_DURATION)
                    );
                    state2.setDuration(BlockBreak.Duration.ROTATION_2.getDuration()).playTogether(
                            animateRotation(view, rotation_2, BlockBreak.Float.ROTATION_1.getFloat(),
                                    NULL_DURATION),
                            animateChangeScale(view, BlockBreak.Float.SCALE_2.getFloat(),
                                    BlockBreak.Float.SCALE_1.getFloat(), NULL_DURATION)
                    );
                    set.playSequentially(
                            animateRotation(view, BlockBreak.Float.STATE_1.getFloat(), state_2, BlockBreak.Duration.SHAKE),
                            animateRotation(view, state_2, state_3, BlockBreak.Duration.SHAKE),
                            state1, state2,
                            animateTranslation(view, BlockBreak.Float.TRANSLATION_X_2.getFloat(),
                                    BlockBreak.Float.TRANSLATION_Y_2.getFloat(), BlockBreak.Duration.BACK)
                    );
                    return set;
                }

                @Override
                public long getDuration() {
                    return BlockBreak.getDurationToPoints();
                }

                @Override
                public long getFullDuration() {
                    return getDuration();
                }
            }
        }

        private long duration;
        private long fullDuration = 0;
        abstract public AnimatorSet getSet(boolean isPlayer, View...views);

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

        public enum Skills {

        }
        public enum Common {
            POINTS {
                public AnimatorSet getSet(final View view, final boolean isPlayer,
                                          final boolean isHeal, final String text) {
                    final TextView textView = (TextView) view;

                    AnimatorSet set = getAnimateDamagePointsSet(view, isPlayer);
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //view.setVisibility(View.INVISIBLE);
                            super.onAnimationEnd(animation);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            textView.setTextColor(isHeal ? view.getContext().getColor(R.color.colorHealPoints) :
                                    view.getContext().getColor(R.color.colorDamagePoints));
                            ((TextView) view).setText(text);
                            super.onAnimationStart(animation);
                        }
                    });

                    return set;
                }
            };

            public abstract AnimatorSet getSet(View view, boolean isPlayer, boolean isHeal, String text);
        }
    }
}

