package com.example.user.carnage.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.carnage.R;
import com.example.user.carnage.animation.AnimateGame.AnimationTypes;
import com.example.user.carnage.animation.AnimateGame.AnimationTypes.DefenceAnimation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class ImageViewHolder implements AnimationQueueListener, AnimationQueueThread.AnimationEndListener {
    private final ImageView view, magic;
    private final TextView points;
    private final int main_x, main_y;
    private final boolean leftToRight;

    private final Semaphore attackSemaphore, defenceSemaphore;
    private final Handler mHandler;

    private final ArrayList<Runnable> taskList;

    private AnimationQueueListener enemy;
    private AnimationQueueThread.AnimationEndListener endListener;

    Logger logger = Logger.getAnonymousLogger();

    public ImageViewHolder(ImageView view,
                           boolean leftToRight,
                           AnimationQueueListener enemy,
                           AnimationQueueThread.AnimationEndListener endListener,
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
        this.endListener = endListener;
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
/*        taskList.add(new Thread() {
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
*/
/*        taskList.add(() -> {
            final AnimatorSet set = AnimationTypes.ANIMATION_BATTLE_ATTACK.getSet(leftToRight, view, enemy.getView());
            long duration = AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration();

            try {
                attackSemaphore.acquire();
                defenceSemaphore.acquire();
                //mHandler.post(set::start); // anonymous -> lambda -> method reference (::)
                mHandler.post(AnimationTypes.ANIMATION_BATTLE_ATTACK.getRunnableSet(
                        leftToRight, view, enemy.getView(),
                        () -> {
                            attackSemaphore.release();
                            defenceSemaphore.release();
                        }));
                sleep(duration);
                enemy.animateHit(type, effect, defenceSemaphore);
                //sleep(AnimationTypes.ANIMATION_BATTLE_ATTACK.getFullDuration() - duration);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //attackSemaphore.release();
                //defenceSemaphore.release();
            }
        });
*/
        taskList.add(() -> {
            long duration = AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration();
            Semaphore semaphore = new Semaphore(1);

            try {
                semaphore.acquire();
                mHandler.post(AnimationTypes.ANIMATION_BATTLE_ATTACK.getRunnableSet(
                        leftToRight,
                        view,
                        enemy.getView(),
                        semaphore::release)
                );
                sleep(duration);
                enemy.animateHit(type, effect, defenceSemaphore);

                while (semaphore.tryAcquire()) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean animateSequentially(AnimatorSet set) {
        Semaphore semaphore = new Semaphore(1);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                semaphore.release();
            }
        });
        set.start();
        return true;
    }

    public Runnable animate() {
        return () -> {
            SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue<>();
            Thread producer = new Thread() {
                @Override
                public void run() {
                    for (Runnable runnable : taskList) {
                        try {
                            synchronousQueue.put(runnable);
                            //logger.info("a task has been put to the queue");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            producer.start();

            Executor executor = Executors.newSingleThreadExecutor();

            for (int i=0; i<taskList.size(); i++) {
                try {
                    //logger.info("a task has been taken from the queue");
                    executor.execute(synchronousQueue.take());

                    //attackSemaphore.acquire();
                    //attackSemaphore.release();
                    //logger.info("animation task has Ended executing.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                attackSemaphore.acquire();
                attackSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            taskList.clear();
            endListener.animationEnded();
        };

    }

    public Runnable animate(int i) {
        InTurnsExecutor executor = new InTurnsExecutor();

        for (Runnable runnable : taskList) {
            executor.add(runnable);
        }

        return executor.execute();
    }

    @Override
    public ImageView getView() {
        return view;
    }

    @Override
    public void animateHit(DefenceAnimation type, String effect, Semaphore semaphore) {
        AnimationQueueThread.AnimationEndListener listener = semaphore::release;
        try {
            semaphore.acquire();

            mHandler.post(() -> {
                AnimationTypes.Common.POINTS.getSet(points, !leftToRight, false, effect).start();
                //type.getSet(!leftToRight, view).start();
                type.getRunnableSet(!leftToRight, view, listener).run();
            });
            //logger.info("DEF thread sleeps for: "+type.getDuration());
            //sleep(type.getDuration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //semaphore.release();
        }
    }

    @Override
    public void animationEnded() {

    }

    class InTurnsExecutor {
        private ArrayList<Runnable> list;

        InTurnsExecutor() {
            list = new ArrayList<>();
        }

        void add(Runnable runnable) {
            list.add(runnable);
        }

        Thread execute() {
            return new Thread() {
                @Override
                public void run() {
                    for (Runnable runnable : list) {
                        Thread thread = new Thread(runnable);
                        thread.start();
                        try {
                            logger.info("ITE execute() : join() at "+Calendar.getInstance().getTime());
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    taskList.clear();
                }
            };
        }
    }
}

interface AnimationQueueListener {
    ImageView getView();
    void animateHit(DefenceAnimation type, String effect, Semaphore semaphore);
}
