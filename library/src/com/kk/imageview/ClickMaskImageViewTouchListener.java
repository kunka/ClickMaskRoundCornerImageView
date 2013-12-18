package com.kk.imageview;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

/**
 * Add click mask effect when the imageView is clicked and implement our own onClickListener
 * <p/>
 * Created by xj on 13-8-27.
 */
@Deprecated
public class ClickMaskImageViewTouchListener implements View.OnTouchListener {
    private static final String TAG = "ClickMaskImageView";
    //overlay is black with transparency of 0x77 (119)
    private static final int MASK_COLOR = 0x77000000;
    private int mTouchSlop;
    private float mDownX;
    private float mDownY;
    private boolean isOnClick = false;
    private boolean isPressed = false;
    private Runnable mClickedCallback;
    private ImageView mImageView;
    private View.OnClickListener onClickListener;

    public ClickMaskImageViewTouchListener(ImageView imageView) {
        if (imageView == null) throw new NullPointerException();
        this.mImageView = imageView;
        ViewConfiguration vc = ViewConfiguration.get(imageView.getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "ACTION_OUTSIDE");
                removeTapCallback();
                setPressedEffect(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_CANCEL");
                removeTapCallback();
                setPressedEffect(false);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                if (isOnClick) {
                    mImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            handleOnClick();
                        }
                    });
                    removeTapCallback();
                    setPressedEffect(true);
                    mImageView.postDelayed(new Runnable() {
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
                Log.d(TAG, "ACTION_MOVE");
//                float deltaX = motionEvent.getRawX() - mDownX;
//                float deltaY = motionEvent.getRawY() - mDownY;

                // check whether cancel on click
                if (isOnClick && !isPointInsideView(motionEvent.getRawX(), motionEvent.getRawY(), view)) {
                    Log.d(TAG, "cancel onCLick");
                    removeTapCallback();
                    setPressedEffect(false);
                    return false;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "ACTION_DOWN");
                mDownX = motionEvent.getRawX();
                mDownY = motionEvent.getRawY();
                isOnClick = true;
                mClickedCallback = new Runnable() {
                    @Override
                    public void run() {
                        if (isOnClick) {
                            setPressedEffect(true);
                        }
                    }
                };
                mImageView.postDelayed(mClickedCallback, ViewConfiguration.getTapTimeout());
                return true;
            }
        }

        return mImageView.onTouchEvent(motionEvent);
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
        Log.d(TAG, "handleOnClick ");
        if (onClickListener != null)
            onClickListener.onClick(mImageView);
    }

    private void setPressedEffect(boolean pressed) {
        if (isPressed != pressed) {
            Log.d(TAG, "setPressedEffect " + pressed);
            isPressed = pressed;
            if (isPressed)
                mImageView.setColorFilter(new PorterDuffColorFilter(MASK_COLOR, PorterDuff.Mode.SRC_ATOP));
            else
                mImageView.setColorFilter(null);
        }
    }

    private void removeTapCallback() {
        if (mClickedCallback != null) {
            Log.d(TAG, "removeCallbacks");
            mImageView.removeCallbacks(mClickedCallback);
            mClickedCallback = null;
        }
        isOnClick = false;
    }

}
