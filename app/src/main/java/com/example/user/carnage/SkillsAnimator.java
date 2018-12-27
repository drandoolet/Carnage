package com.example.user.carnage;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;

public class SkillsAnimator extends AnimateGame {
    AnimatorSet mainSet;
    ImageView imageView, enemyImg;

    SkillsAnimator() {
        super();
    }

    SkillsAnimator(SkillsAnimations animation, ImageView imageView, ImageView enemyImageView) {
        this.imageView = imageView;
        enemyImg = enemyImageView;

        switch (animation) {
            case FIREBALL: mainSet = animateFireBall();
        }
    }

    private AnimatorSet animateFireBall() {
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet set = new AnimatorSet();
        state1.setDuration(800).playTogether(
                animateChangeScale(imageView, 0f, 1f, 0),
                animateRotation(imageView, 0f, 720f, 0)
        );
        set.playSequentially(
                state1,
                animateTranslation(imageView, enemyImg.getX()-imageView.getX(), 0, 500),
                animateChangeScale(imageView, 1f, 3f, 200)
        );
        return set;
    }
}

enum SkillsAnimations {
    HEAL_SMALL,
    FIREBALL
}
