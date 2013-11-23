package com.kk.clickmaskimageview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hk on 13-11-10.
 */
public class ClickMaskRatioImageView extends RatioImageView {
    private static final String TAG = "ClickMaskImageView";
    private static final int MASK_COLOR = 0x77000000;
    private View.OnClickListener onClickListener;
    private boolean isPressed = false;

    public ClickMaskRatioImageView(Context context) {
        super(context);
        init();
    }

    public ClickMaskRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickMaskRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
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
