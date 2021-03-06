package com.kk.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Set aspectRatio before we got the real drawable(avoid layout jump).
 * you must set width with fill_parent or exact value, so the height = width * aspectRatio
 * Created by hk on 13-11-10.
 */
public class RatioFrameLayout extends FrameLayout {
    public interface OnSizeChangedListener {
        /**
         * Callback method to be invoked when the size be measured.
         */
        public void onSizeChanged(RatioFrameLayout ratioImageView, int w, int h, int oldw, int oldh);
    }

    private float aspectRatio = 0;
    private boolean adjustWidth;
    private OnSizeChangedListener onSizeChangedListener;

    public RatioFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Context context = getContext();
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.RatioImageView);
            if (a != null) {
                aspectRatio = a.getFloat(R.styleable.RatioImageView_aspectRatio, 0);
                adjustWidth = a.getBoolean(R.styleable.RatioImageView_adjustWidth, false);
            }
        }
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio > 0 && this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            requestLayout();
            invalidate();
        }
    }

    public void setAdjustWidth(boolean adjustWidth) {
        if (this.adjustWidth != adjustWidth) {
            this.adjustWidth = adjustWidth;
            requestLayout();
            invalidate();
        }
    }

    public boolean isAdjustWidth() {
        return adjustWidth;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        if (aspectRatio > 0) {//&& (widthSpecMode == MeasureSpec.AT_MOST || widthSpecMode == MeasureSpec.EXACTLY)) {
            int widthSize;
            int heightSize;

            int pleft = getPaddingLeft();
            int pright = getPaddingRight();
            int ptop = getPaddingTop();
            int pbottom = getPaddingBottom();

            int w = 0;
            int h = 0;
            w += pleft + pright;
            h += ptop + pbottom;

            if (adjustWidth) {
                heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
                widthSize = (int) ((heightSize - ptop - pbottom) * aspectRatio) + pleft + pright;
            } else {
                widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
                heightSize = (int) ((widthSize - pleft - pright) * aspectRatio) + ptop + pbottom;
            }

            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View element = getChildAt(i);
                ViewGroup.LayoutParams p = element.getLayoutParams();
                if (p == null) {
                    p = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                }

                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec,
                        ptop + pbottom, p.height);
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec,
                        pleft + pright, p.width);
                element.measure(childWidthSpec, childHeightSpec);
            }

            setMeasuredDimension(widthSize, heightSize);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | 0x01000000;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & 0xff000000);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
        }
    }
}
