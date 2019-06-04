package com.example.user.carnage.animation;

import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.carnage.animation.AnimateGame.AnimationTypes;
import com.example.user.carnage.animation.AnimateGame.AnimationTypes.DefenceAnimation;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class ImageViewHolder implements AnimationQueueListener {
    private final ImageView view, magic;
    private final TextView points;
    private final int main_x, main_y;
    private final boolean leftToRight;

    private final Semaphore attackSemaphore, defenceSemaphore;
    private final Handler mHandler;

    private final ArrayList<Thread> taskList;

    private AnimationQueueListener enemy;
    private volatile boolean flag = false;

    Logger logger = Logger.getAnonymousLogger();

    public ImageViewHolder(ImageView view,
                           boolean leftToRight,
                           AnimationQueueListener enemy,
                           TextView points,
                           ImageView magic) {
        this.view = view;
        this.points = points;
        this.magic = magic;
        this.leftToRight = leftToRight;
        synchronized (view) {
            main_x = view.getLeft();
            main_y = view.getBottom();
        }
        attackSemaphore = new Semaphore(1);
        defenceSemaphore = new Semaphore(2);
        mHandler = new Handler(Looper.getMainLooper());
        taskList = new ArrayList<>();
        this.enemy = enemy;
    }

    public void setEnemy(ImageViewHolder holder) { enemy = holder; }

    public int getCurrentX() {
        int result;
        synchronized (view) {
            result = view.getLeft();
        }
        return result;
    }

    public int getCurrentY() {
        int result;
        synchronized (view) {
            result = view.getBottom();
        }
        return result;
    }

    public int getBaseX() { return main_x; }
    public int getBaseY() { return main_y; }

    public void animateAttack(DefenceAnimation type, String effect) {
        taskList.add(new Thread() {
            @Override
            public void run() {
                final AnimatorSet set = AnimationTypes.ANIMATION_BATTLE_ATTACK.getSet(leftToRight, view, enemy.getView());
                long duration = AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration();
                try {
                    attackSemaphore.acquire();
                    defenceSemaphore.acquire();
                    mHandler.post(set::start); // anonymous -> lambda -> method reference (::)
                    sleep(duration);
                    enemy.animateHit(type, effect, defenceSemaphore);
                    sleep(AnimationTypes.ANIMATION_BATTLE_ATTACK.getFullDuration() - duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    attackSemaphore.release();
                    defenceSemaphore.release();
                    Logger.getAnonymousLogger().info("exiting animateAttack thread");
                }
            }
        });
    }

    public void animate() { // TODO: fix unexpected execution mixture (take, start etc)
        for (Thread thread : taskList) {
            thread.start();
        }
        taskList.clear();
    }

    @Override
    public ImageView getView() {
        return view;
    }

    @Override
    public void animateHit(DefenceAnimation type, String effect, Semaphore semaphore) {
        logger.info("entering animateHit()");
        try {
            semaphore.acquire();

            mHandler.post(() -> {
                AnimationTypes.Common.POINTS.getSet(points, !leftToRight, false, effect).start();
                type.getSet(!leftToRight, view).start();
            });

            Thread.sleep(type.getDuration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            logger.info("exiting animateHit()");
        }
    }
}

interface AnimationQueueListener {
    ImageView getView();
    void animateHit(DefenceAnimation type, String effect, Semaphore semaphore);
}
