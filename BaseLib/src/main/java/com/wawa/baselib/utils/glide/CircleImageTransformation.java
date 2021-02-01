package com.wawa.baselib.utils.glide;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

public class CircleImageTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    private int mBorderColor;
    private int mBorderWidth;
    private float radius = 0.5f;

    public CircleImageTransformation(Context context, int borderColor, int borderWidth) {
        this(context);
        mBorderColor=borderColor;
        mBorderWidth=borderWidth;
    }

    public CircleImageTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public CircleImageTransformation(Context context, float radius) {
        this(Glide.get(context).getBitmapPool());
        this.radius = radius;
    }

    public CircleImageTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> bitmapResource, int outWidth, int outHeight) {
        Bitmap source = bitmapResource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (int)((source.getWidth() - size) * radius);
        int height = (int)((source.getHeight() - size) * radius);

        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader =
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size * radius;
        canvas.drawCircle(r, r, r-mBorderWidth, paint);
        if(mBorderWidth>0){
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(mBorderWidth);
            paint.setColor(mBorderColor);
            canvas.drawCircle(r, r, r-mBorderWidth/2, paint);
        }
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }
}
