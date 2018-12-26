package com.example.user.carnage;

import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class WaiterThread extends HandlerThread {
    Thread thread;
    private View views[];
    private View flag, test;

    public WaiterThread(View[] views, View flag, View test) {
        super("test thread");
        this.views = views;
        this.flag = flag;
        //this.test = test;
        thread = new Thread(this, "WAITER");
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("STARTING THREAD WAITER");
        setViewsEnabled(false);
        //test.setEnabled(false);
        synchronized (this) {
            while (flag.getVisibility() != View.GONE) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Log.e(MainActivity.TAG, "error in thread waiter - "+e.getStackTrace());
                }
            }
            setViewsEnabled(true);
        }
    }

    private void setViewsEnabled(boolean set) {
        for (View view : views) view.setEnabled(set);
    }

    boolean calc(int[] phys, int[] rus, int[] math) {
        int physSum = 0, rusSum = 0, mathSum = 0;

        for (int i=0; i<phys.length; i++) physSum += phys[i];
        for (int i=0; i<rus.length; i++) rusSum += rus[i];
        for (int i=0; i<math.length; i++) mathSum += math[i];

        return (physSum >= 8 && rusSum >= 8 && mathSum >= 5);
    }


}
