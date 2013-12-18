package com.kk.imageview;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Implement click mask effect when the imageView is clicked and our onClickListener
 * <p/>
 * Created by xj on 13-8-27.
 */
public class CustomOnClickListener implements View.OnTouchListener {
    private static final String TAG = "ClickMaskImageView";

    /**
     * Interface definition for a callback to be invoked when a view is clicked.
     */
    public interface OnPressListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onPress(View v, boolean press);
    }

    private boolean isOnClick;
    private Runnable mClickedCallback;
    private View.OnClickListener onClickListener;
    private OnPressListener onPressListener;
    private boolean isPressed = false;
    private View mView;

    public CustomOnClickListener(View view) {
        if (view == null) throw new NullPointerException();
        this.mView = view;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnPressListener(OnPressListener onPressListener) {
        this.onPressListener = onPressListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_OUTSIDE:
                android.util.Log.d(TAG, "ACTION_OUTSIDE");
            case MotionEvent.ACTION_CANCEL:
                android.util.Log.d(TAG, "ACTION_CANCEL");
                removeTapCallback();
                setPressedEffect(false);
                break;
            case MotionEvent.ACTION_UP:
                android.util.Log.d(TAG, "ACTION_UP");
                if (isOnClick) {
                    mView.post(new Runnable() {
                        @Override
                        public void run() {
                            handleOnClick();
                        }
                    });
                    removeTapCallback();
                    setPressedEffect(true);
                    mView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setPressedEffect(false);
                        }
                    }, ViewConfiguration.getPressedStateDuration());
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                // check is swiping
                android.util.Log.d(TAG, "ACTION_MOVE");

                // check whether cancel on click
                if (isOnClick && !isPointInsideView(motionEvent.getRawX(), motionEvent.getRawY(), view)) {
                    android.util.Log.d(TAG, "cancel onCLick");
                    removeTapCallback();
                    setPressedEffect(false);
                    return false;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                android.util.Log.d(TAG, "ACTION_DOWN");
                isOnClick = true;
                mClickedCallback = new Runnable() {
                    @Override
                    public void run() {
                        if (isOnClick) {
                            setPressedEffect(true);
                        }
                    }
                };
                mView.postDelayed(mClickedCallback, ViewConfiguration.getTapTimeout());
                return true;
            }
        }

//        return true;
        return mView.onTouchEvent(motionEvent);
    }

    /**
     * Determines if given points are inside view
     *
     * @param x    - x coordinate of point
     * @param y    - y coordinate of point
     * @param view - view object to compare
     * @return true if the points are within view bounds, false otherwise
     */
    private boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if ((x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()))) {
            return true;
        } else {
            return false;
        }
    }

    private void handleOnClick() {
        android.util.Log.d(TAG, "handleOnClick ");
        if (onClickListener != null)
            onClickListener.onClick(mView);
    }

    private void setPressedEffect(boolean pressed) {
        if (isPressed != pressed) {
            android.util.Log.d(TAG, "setPressedEffect " + pressed);
            isPressed = pressed;
            if (onPressListener != null)
                onPressListener.onPress(mView, isPressed);
        }
    }

    private void removeTapCallback() {
        if (mClickedCallback != null) {
            android.util.Log.d(TAG, "removeCallbacks");
            mView.removeCallbacks(mClickedCallback);
            mClickedCallback = null;
        }
        isOnClick = false;
    }
}
