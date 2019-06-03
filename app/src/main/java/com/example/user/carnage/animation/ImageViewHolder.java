package com.example.user.carnage.animation;

import android.widget.ImageView;

public class ImageViewHolder {
    private final ImageView view;
    private final int main_x, main_y;

    public ImageViewHolder(ImageView view) {
        this.view = view;
        synchronized (view) {
            main_x = view.getLeft();
            main_y = view.getBottom();
        }
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
}
