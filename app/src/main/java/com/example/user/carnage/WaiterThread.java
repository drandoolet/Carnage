package com.example.user.carnage;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class WaiterThread extends Thread {
    Thread thread;
    private View views[];
    private View flag, test;

    public WaiterThread(View[] views, View flag, View test) {
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
}
