package com.wawa.wawaandroid_ep.view.recycleview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class BottomCallbackRecycler extends RecyclerView {

    private OnBottomCallback mOnBottomCallback;

    public interface OnBottomCallback {
        void onBottom();
    }

    public void setOnBottomCallback(OnBottomCallback onBottomCallback) {
        this.mOnBottomCallback = onBottomCallback;
    }

    public BottomCallbackRecycler(Context context) {
        this(context, null);
    }

    public BottomCallbackRecycler(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomCallbackRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {

        if (isSlideToBottom()) {
            mOnBottomCallback.onBottom();
        }
    }

    /**
    * 其实就是它在起作用。
    */
    public boolean isSlideToBottom() {
        return this != null
                && this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
                >= this.computeVerticalScrollRange();
    }

}