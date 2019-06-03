package com.example.user.carnage.animation;

import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.user.carnage.animation.AnimateGame.AnimationTypes;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ImageViewHolder {
    private final ImageView view;
    private final int main_x, main_y;
    private final boolean leftToRight;

    private final Semaphore animationSemaphore;
    private final Handler mHandler;

    private final ArrayList<Runnable> taskList;

    private final ImageViewHolder enemy;

    public ImageViewHolder(ImageView view, boolean leftToRight, ImageViewHolder enemy) {
        this.view = view;
        this.leftToRight = leftToRight;
        synchronized (view) {
            main_x = view.getLeft();
            main_y = view.getBottom();
        }
        animationSemaphore = new Semaphore(1);
        mHandler = new Handler(Looper.getMainLooper());
        taskList = new ArrayList<>();
        this.enemy = enemy;
    }

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

    public void animateAttack(AnimationTypes type, String effect) {
        taskList.add(() -> {
            final AnimatorSet set = type.getSet(leftToRight, view, enemy.view);
            long duration = type.getDuration();
            try {
                animationSemaphore.acquire();
                mHandler.post(set::start); // anonymous -> lambda -> method reference (::)
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                animationSemaphore.release();
            }
        });
    }



    public synchronized void addAnimationToQueue(@NonNull final AnimationTypes type, @Nullable final ImageView secondary) {
        taskList.add(() -> {
            final AnimatorSet set = type.getSet(leftToRight, view, secondary);
            long duration = type.getDuration();
            try {
                animationSemaphore.acquire();
                mHandler.post(set::start); // anonymous -> lambda -> method reference (::)
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                animationSemaphore.release();
            }
        });
    }

    public void animate() { // TODO: possibly - unexpected execution mixture
        for (Runnable runnable : taskList) {
            runnable.run();
        }
    }
}
