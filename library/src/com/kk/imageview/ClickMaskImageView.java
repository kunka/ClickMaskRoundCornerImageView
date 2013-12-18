package com.kk.imageview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Override the onClickListener
 * <p/>
 * Created by xj on 13-8-26.
 */
public class ClickMaskImageView extends ImageView {
    private static final String TAG = "ClickMaskImageView";
    private static final int MASK_COLOR = 0x77000000;
    private OnClickListener onClickListener;
    private boolean isPressed = false;

    public ClickMaskImageView(Context context) {
        super(context);
        init();
    }

    public ClickMaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickMaskImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void init() {
        CustomOnClickListener customOnClickListener = new CustomOnClickListener(this);
        setOnTouchListener(customOnClickListener);
        customOnClickListener.setOnPressListener(new CustomOnClickListener.OnPressListener() {
            @Override
            public void onPress(View v, boolean press) {
                setPressedEffect(press);
            }
        });
        customOnClickListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.onClick(v);
            }
        });
    }

    private void setPressedEffect(boolean pressed) {
        if (isPressed != pressed) {
            Log.d(TAG, "setPressedEffect " + pressed);
            isPressed = pressed;
            if (isPressed)
                setColorFilter(new PorterDuffColorFilter(MASK_COLOR, PorterDuff.Mode.SRC_ATOP));
            else
                setColorFilter(null);
        }
    }

}
