package com.kk.clickmaskimageviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.kk.clickmaskimageview.RatioImageView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        findAllChildrenByTag(findViewById(R.id.root), ImageView.class, "imageView", imageViews);
        for (ImageView imageView : imageViews) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, view.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                }
            });
            if (imageView instanceof RatioImageView) {
                RatioImageView ratioImageView = (RatioImageView) imageView;
                ratioImageView.setOnSizeChangedListener(new RatioImageView.OnSizeChangedListener() {
                    @Override
                    public void onSizeChanged(RatioImageView ratioImageView, int w, int h, int oldw, int oldh) {
                        Log.d("RatioImageView", "RatioImageView(" + ratioImageView + ") ratio = " + ratioImageView.getAspectRatio() + " onSizeChanged w = " + w + " h = " + h + " oldw = " + oldw + " oldh = " + oldh);
                    }
                });
            }
        }
    }

    public static <T> void findAllChildrenByTag(View parent, java.lang.Class<T> clazz, Object
            tag, ArrayList<T> views) {
        if (tag != null && clazz != null && parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                if (clazz.isInstance(child) && tag.equals(child.getTag())) {
                    views.add((T) child);
                } else {
                    findAllChildrenByTag(child, clazz, tag, views);
                }
            }
        }
    }
}
