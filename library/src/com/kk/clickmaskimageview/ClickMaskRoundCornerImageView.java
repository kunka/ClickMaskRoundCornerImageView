package com.kk.clickmaskimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Override the onClickListener
 * <p/>
 * Created by xj on 13-8-26.
 */
public class ClickMaskRoundCornerImageView extends RoundCornerImageView {
    private View.OnClickListener onClickListener;

    public ClickMaskRoundCornerImageView(Context context) {
        super(context);
        init();
    }

    public ClickMaskRoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickMaskRoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void init() {
        ClickMaskImageViewTouchListener clickMaskImageViewTouchListener = new ClickMaskImageViewTouchListener(this);
        setOnTouchListener(clickMaskImageViewTouchListener);
        clickMaskImageViewTouchListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view);
            }
        });
    }
}
