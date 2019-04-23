package com.example.user.carnage.animation;


import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.util.ArrayList;

public class TestThread extends Thread {
    private ArrayList<AnimatorSet> animatorSets = new ArrayList<>();
    long delay = 0;
    Handler mHandler;

    public TestThread(AnimateGame animateGame, ImageView plView, ImageView enView) {
        mHandler = new Handler(Looper.getMainLooper());
        animatorSets.add(animateGame.getAnimateAttack(plView, enView, true));
        animatorSets.add(animateGame.getAnimateAttack(plView, enView, true));
        animatorSets.add(animateGame.getAnimateAttack(plView, enView, false));
        animatorSets.add(animateGame.getAnimateAttack(plView, enView, true));
    }

    // need to use getTotalDuration(), maybe.
    // getDuration() returns -1 as the dur is not set for the main set, but only to children
    // thus, need to create own method and add dur manually
    @Override
    public void run() {
        for (final AnimatorSet set : animatorSets) {
            delay = set.getDuration();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    set.start();
                }
            });
            System.out.println("thread delay = "+delay);
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(AnimatorSet set) {
        animatorSets.add(set);
    }
}
