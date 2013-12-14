package com.kk.clickmaskimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Implement a simple round corner ImageView with high performance
 * TODO: ScaleType, border, oval
 *
 * @require: the bitmap src equals to the view's size(always use wrap_content) or the same aspect ratio
 * @notice: the default scale type is centerCrop
 * <p/>
 * Created by xj on 13-8-26.
 */
public class RoundCornerImageView extends ImageView {
    private static final float DEFAULT_RADIUS_X = 0f;
    private static final float DEFAULT_RADIUS_Y = 0f;

    private float mRadiusX = DEFAULT_RADIUS_X;
    private float mRadiusY = DEFAULT_RADIUS_Y;
    private boolean mIsRound = false;
    protected Paint mBitmapPaint;
    protected Paint mBackgroundColorPaint;
    BitmapShader mBitmapShader;
    private int mBackgroundColor = 0;

    public RoundCornerImageView(Context context) {
        super(context);
        init(null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
            mBackgroundColor = a.getColor(R.styleable.RoundCornerImageView_background, 0);
            mRadiusX = a.getDimension(R.styleable.RoundCornerImageView_radiusX, DEFAULT_RADIUS_X);
            mRadiusY = a.getDimension(R.styleable.RoundCornerImageView_radiusY, DEFAULT_RADIUS_Y);
            float radius = a.getDimension(R.styleable.RoundCornerImageView_radiusXY, -1);
            if (radius != -1) {
                mRadiusX = mRadiusY = radius;
            }
            mIsRound = a.getBoolean(R.styleable.RoundCornerImageView_isRound, false);
        }
        getBitmapPaint();
        getBackgroundColorPaint().setColor(mBackgroundColor);
    }

    private Paint getBitmapPaint() {
        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint();
            mBitmapPaint.setAntiAlias(true);
        }
        return mBitmapPaint;
    }

    private Paint getBackgroundColorPaint() {
        if (mBackgroundColorPaint == null) {
            mBackgroundColorPaint = new Paint();
            mBackgroundColorPaint.setStyle(Paint.Style.FILL);
        }
        return mBackgroundColorPaint;
    }

    public void setRadiusX(float radiusX) {
        if (mRadiusX > 0) {
            this.mRadiusX = radiusX;
            invalidate();
        }
    }

    public void setRadiusY(float radiusY) {
        if (radiusY > 0) {
            this.mRadiusY = radiusY;
            invalidate();
        }
    }

    public void setRadius(float radius) {
        if (radius > 0) {
            this.mRadiusX = this.mRadiusY = radius;
            invalidate();
        }
    }

    public void setIsRound(boolean isRound) {
        this.mIsRound = isRound;
    }

    public boolean isRound() {
        return mIsRound;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mBackgroundColor = color;
        getBackgroundColorPaint().setColor(mBackgroundColor);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        super.setColorFilter(cf);
        if (cf != null) {
            mBitmapPaint.setColorFilter(cf);
        } else {
            mBitmapPaint.setColorFilter(null);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        Bitmap bitmap = null;
        if (getDrawable() instanceof BitmapDrawable)
            bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        if (bitmap != null) {
            mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            getBitmapPaint().setShader(mBitmapShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (!(getWidth() > 0 && getHeight() > 0))
            return;
        Bitmap bitmap = null;
        if (getDrawable() instanceof BitmapDrawable)
            bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vheight = getHeight() - getPaddingTop() - getPaddingBottom();

        if (bitmap != null) {
            int dwidth = bitmap.getWidth();
            int dheight = bitmap.getHeight();
            if (vwidth > 0 && vheight > 0 && dwidth > 0 && dheight > 0) {
                Matrix mDrawMatrix = new Matrix();

                // centerCrop
                mDrawMatrix.setTranslate((int) ((vwidth - dwidth) * 0.5f + 0.5f),
                        (int) ((vheight - dheight) * 0.5f + 0.5f));

                float scale;
                float dx = 0, dy = 0;

                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * 0.5f;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * 0.5f;
                }

                // centerInside
//
//                float scale;
//                float dx;
//                float dy;
//
//                if (dwidth <= vwidth && dheight <= vheight) {
//                    scale = 1.0f;
//                } else {
//                    scale = Math.min((float) vwidth / (float) dwidth,
//                            (float) vheight / (float) dheight);
//                }
//
//                dx = (int) ((vwidth - dwidth * scale) * 0.5f + 0.5f);
//                dy = (int) ((vheight - dheight * scale) * 0.5f + 0.5f);

//                mDrawMatrix.setScale(scale, scale);
//                mDrawMatrix.postTranslate(dx, dy);

                mDrawMatrix.setScale(scale, scale);
                //mDrawMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
                int saveCount = canvas.getSaveCount();
                canvas.save();
                canvas.translate((int) (dx + 0.5f), (int) (dy + 0.5f));
                canvas.concat(mDrawMatrix);

                // draw bitmap
                //BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                //mBitmapPaint.setShader(shader);
                //RectF rect = new RectF(0, 0, getWidth() - getPaddingRight(), getHeight() - getPaddingRight());

                // rect contains the bounds of the shape
                // radius is the radius in pixels of the rounded corners
                // paint contains the shader that will texture the shape
                RectF rect = new RectF(0, 0, dwidth, dheight);
                if (isRound()) {
                    canvas.drawRoundRect(rect, vwidth / 2, vheight / 2, getBitmapPaint());
                } else {
                    canvas.drawRoundRect(rect, mRadiusX, mRadiusY, getBitmapPaint());
                }
                canvas.restoreToCount(saveCount);

                // draw border
//                Paint paint = new Paint();
//                paint.setColor(Color.RED);
//                float borderThickness = 2f;//f * ScreenUtil.getScreenDensity();
//                paint.setStrokeWidth(borderThickness);
//                paint.setStyle(Paint.Style.STROKE);
//                canvas.drawRoundRect(new RectF(borderThickness / 2, borderThickness / 2, getWidth()
//                        - borderThickness / 2, getHeight() - borderThickness / 2), mRadiusX, mRadiusY,
//                        paint);
            }
        } else {
            // draw background color
            if (mBackgroundColor != 0) {
                if (vwidth > 0 && vheight > 0) {
                    RectF rect = new RectF(getPaddingLeft(), getPaddingTop(), getWidth(), getHeight());
                    if (isRound()) {
                        canvas.drawRoundRect(rect, vwidth / 2, vheight / 2, getBackgroundColorPaint());
                    } else {
                        canvas.drawRoundRect(rect, mRadiusX, mRadiusY, getBackgroundColorPaint());
                    }
                }
            }
        }
    }

}
