package com.kk.clickmaskimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Override the onClickListener
 * <p/>
 * Created by xj on 13-8-26.
 */
public class ClickMaskImageView extends ImageView {
    private OnClickListener onClickListener;

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
        ClickMaskImageViewTouchListener clickMaskImageViewTouchListener = new ClickMaskImageViewTouchListener(this);
        setOnTouchListener(clickMaskImageViewTouchListener);
        clickMaskImageViewTouchListener.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view);
            }
        });
    }
}
