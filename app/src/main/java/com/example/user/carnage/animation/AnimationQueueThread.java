package com.example.user.carnage.animation;


import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

public class AnimationQueueThread extends Thread {
    private AnimationEndListener listener;
    private ArrayList<Test> testArrayList = new ArrayList<>();
    private Handler mHandler;


    private class Test {
        private AnimatorSet set;
        private long duration;

        Test(AnimatorSet animatorSet, long l) {
            set = animatorSet;
            duration = l;
        }

        public long getDuration() {
            return duration;
        }

        public AnimatorSet getSet() {
            return set;
        }
    }

    public AnimationQueueThread() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    // need to use getTotalDuration(), maybe.
    // getDuration() returns -1 as the dur is not set for the main set, but only to children
    // thus, need to create own method and add dur manually
    @Override
    public void run() {
        for (final Test test : testArrayList) {
            long delay = test.getDuration();

            mHandler.post(() -> test.getSet().start());
            //System.out.println("thread delay = "+delay);
            //Log.i("AnimationQueueThread", "thread delay = "+delay);
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        listener.animationEnded();
    }

    public void add(AnimatorSet set, long duration) {
        testArrayList.add(new Test(set, duration));
    }


    public void registerListener(AnimationEndListener listener) {
        this.listener = listener;
    }

    public interface AnimationEndListener {
        void animationEnded();
    }
}
