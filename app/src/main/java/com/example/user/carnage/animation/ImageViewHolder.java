package com.example.user.carnage.animation;

import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.carnage.animation.AnimateGame.AnimationTypes;
import com.example.user.carnage.animation.AnimateGame.AnimationTypes.DefenceAnimation;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
    static volatile boolean flag = false;

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

    public void setEnemy(ImageViewHolder holder) {
        enemy = holder;
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

    public int getBaseX() {
        return main_x;
    }

    public int getBaseY() {
        return main_y;
    }

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
                    //logger.info("ATK thread sleeps for: "+duration);
                    sleep(duration);
                    enemy.animateHit(type, effect, defenceSemaphore);
                    //logger.info("ATK thread sleeps for:    "+(AnimationTypes.ANIMATION_BATTLE_ATTACK.getFullDuration() - duration));
                    sleep(AnimationTypes.ANIMATION_BATTLE_ATTACK.getFullDuration() - duration);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    attackSemaphore.release();
                    defenceSemaphore.release();
                }
            }
        });
    }

    public Runnable animate() {
        return () -> {
            while (flag) {
                try {
                    logger.info("animation task Waits.");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            logger.info("animation task has Started executing.");
            flag = true;
            BlockingQueue<Thread> queue = new LinkedBlockingQueue<>(1);
            Thread producer = new Thread() {
                @Override
                public void run() {
                    for (Thread thread : taskList) {
                        try {
                            queue.put(thread);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            producer.start();

            for (int i = 0; i < taskList.size(); i++) {
                try {
                    queue.take().start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            taskList.clear();
            flag = false;
            try {
                attackSemaphore.acquire();
                attackSemaphore.release();
                logger.info("animation task has Ended executing.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

    }

    @Override
    public ImageView getView() {
        return view;
    }

    @Override
    public void animateHit(DefenceAnimation type, String effect, Semaphore semaphore) {
        try {
            semaphore.acquire();

            mHandler.post(() -> {
                AnimationTypes.Common.POINTS.getSet(points, !leftToRight, false, effect).start();
                type.getSet(!leftToRight, view).start();
            });
            logger.info("DEF thread sleeps for: " + type.getDuration());
            Thread.sleep(type.getDuration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}

interface AnimationQueueListener {
    ImageView getView();

    void animateHit(DefenceAnimation type, String effect, Semaphore semaphore);
}
