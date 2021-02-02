package com.wawa.baselib.utils.glide.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.wawa.baselib.utils.glide.config.AnimationMode;
import com.wawa.baselib.utils.glide.config.GlobalConfig;
import com.wawa.baselib.utils.glide.config.PriorityMode;
import com.wawa.baselib.utils.glide.config.ScaleMode;
import com.wawa.baselib.utils.glide.config.ShapeMode;
import com.wawa.baselib.utils.glide.config.SingleConfig;
import com.wawa.baselib.utils.glide.utils.DownLoadImageService;
import com.wawa.baselib.utils.glide.utils.ImageUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GlideLoader implements ILoader {

    /**
     * @param context        上下文
     * @param cacheSizeInM   Glide默认磁盘缓存最大容量250MB
     * @param memoryCategory 调整内存缓存的大小 LOW(0.5f) ／ NORMAL(1f) ／ HIGH(1.5f);
     * @param isInternalCD   true 磁盘缓存到应用的内部目录 / false 磁盘缓存到外部存
     */
    @Override
    public void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD) {
        Glide.get(context).setMemoryCategory(memoryCategory); //如果在应用当中想要调整内存缓存的大小，开发者可以通过如下方式：
        GlideBuilder builder = new GlideBuilder();
        if (isInternalCD) {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024));
        } else {
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024));
        }
    }

    @Override
    public void request(final SingleConfig config) {
        RequestManager requestManager = Glide.with(config.getContext());
        RequestBuilder request = getDrawableTypeRequest(config, requestManager);

        if (config.isAsBitmap()) {
            requestManager.asBitmap();
            /*Target target=new Target<Bitmap>(config.getWidth(), config.getHeight()){
                @Override
                public void onStart() {

                }

                @Override
                public void onStop() {

                }

                @Override
                public void onDestroy() {

                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {

                }

                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    config.getBitmapListener().onSuccess(bitmap);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }

                @Override
                public void getSize(@NonNull SizeReadyCallback cb) {

                }

                @Override
                public void removeCallback(@NonNull SizeReadyCallback cb) {

                }

                @Override
                public void setRequest(@Nullable Request request) {

                }

                @Nullable
                @Override
                public Request getRequest() {
                    return null;
                }
            };*/
            SimpleTarget target = new SimpleTarget<Bitmap>(config.getWidth(), config.getHeight()) {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    config.getBitmapListener().onSuccess(bitmap);
                }

            };

            setShapeModeAndBlur(config, request);
            if (config.getDiskCacheStrategy() != null) {
                request.diskCacheStrategy(config.getDiskCacheStrategy());
            }
            request.into(target);

        } else {

            if (request == null) {
                return;
            }
            if(config.isGif()){
                requestManager.asGif();
            }
            if (ImageUtil.shouldSetPlaceHolder(config)) {
                request.placeholder(config.getPlaceHolderResId());
            }

            int scaleMode = config.getScaleMode();

            switch (scaleMode) {
                case ScaleMode.CENTER_CROP:
                    request.centerCrop();
                    break;
                case ScaleMode.FIT_CENTER:
                    request.fitCenter();
                    break;
                default:
                    request.fitCenter();
                    break;
            }

            setShapeModeAndBlur(config, request);

            //设置缩略图
            if (config.getThumbnail() != 0) {
                request.thumbnail(config.getThumbnail());
            }

            //设置图片加载的分辨 sp
            if (config.getoWidth() != 0 && config.getoHeight() != 0) {
                request.override(config.getoWidth(), config.getoHeight());
            }

            //是否跳过磁盘存储
            if (config.getDiskCacheStrategy() != null) {
                request.diskCacheStrategy(config.getDiskCacheStrategy());
            }

            //设置图片加载动画
            setAnimator(config, request);

            //设置图片加载优先级
            setPriority(config, request);

            if (config.getErrorResId() > 0) {
                request.error(config.getErrorResId());
            }

            if (config.getTarget() instanceof ImageView) {
                request.into((ImageView) config.getTarget());
            }
        }

    }

    /**
     * 设置加载优先级
     *
     * @param config
     * @param request
     */
    private void setPriority(SingleConfig config, RequestBuilder request) {
        switch (config.getPriority()) {
            case PriorityMode.PRIORITY_LOW:
                request.priority(Priority.LOW);
                break;
            case PriorityMode.PRIORITY_NORMAL:
                request.priority(Priority.NORMAL);
                break;
            case PriorityMode.PRIORITY_HIGH:
                request.priority(Priority.HIGH);
                break;
            case PriorityMode.PRIORITY_IMMEDIATE:
                request.priority(Priority.IMMEDIATE);
                break;
            default:
                request.priority(Priority.IMMEDIATE);
                break;
        }
    }

    /**
     * 设置加载进入动画
     *
     * @param config
     * @param request
     */
    private void setAnimator(SingleConfig config, RequestBuilder request) {
        /*if (config.getAnimationType() == AnimationMode.ANIMATIONID) {
            request.transition(withCrossFade(config.getAnimationId()));
        } else if (config.getAnimationType() == AnimationMode.ANIMATOR) {
            request.transition(withCrossFade(config.getAnimator()));
        } else if (config.getAnimationType() == AnimationMode.ANIMATION) {
            request.transition(withCrossFade(config.getAnimation()));
        }*/
        request.transition(withCrossFade());
    }

    @Nullable
    private  RequestBuilder getDrawableTypeRequest(SingleConfig config, RequestManager requestManager) {
        RequestBuilder request = null;
        if (!TextUtils.isEmpty(config.getUrl())) {
            request = requestManager.load(ImageUtil.appendUrl(config.getUrl()));
            Log.e("TAG","getUrl : "+config.getUrl());
        } else if (!TextUtils.isEmpty(config.getFilePath())) {
            request = requestManager.load(ImageUtil.appendUrl(config.getFilePath()));
            Log.e("TAG","getFilePath : "+config.getFilePath());
        } else if (!TextUtils.isEmpty(config.getContentProvider())) {
            request = requestManager.load(Uri.parse(config.getContentProvider()));
            Log.e("TAG","getContentProvider : "+config.getContentProvider());
        } else if (config.getResId() > 0) {
            request = requestManager.load(config.getResId());
            Log.e("TAG","getResId : "+config.getResId());
        } else if(config.getFile() != null){
            request = requestManager.load(config.getFile());
            Log.e("TAG","getFile : "+config.getFile());
        } else if(!TextUtils.isEmpty(config.getAssertspath())){
            request = requestManager.load(config.getAssertspath());
            Log.e("TAG","getAssertspath : "+config.getAssertspath());
        } else if(!TextUtils.isEmpty(config.getRawPath())){
            request = requestManager.load(config.getRawPath());
            Log.e("TAG","getRawPath : "+config.getRawPath());
        }
        return request;
    }

    /**
     * 设置图片滤镜和形状
     *
     * @param config
     * @param request
     */
    private void setShapeModeAndBlur(SingleConfig config, RequestBuilder request) {

        int count = 0;

        Transformation[] transformation = new Transformation[statisticsCount(config)];

        if (config.isNeedBlur()) {
            transformation[count] = new BlurTransformation( config.getBlurRadius());
            count++;
        }

        if (config.isNeedBrightness()) {
            transformation[count] = new BrightnessFilterTransformation( config.getBrightnessLeve()); //亮度
            count++;
        }

        if (config.isNeedGrayscale()) {
            transformation[count] = new GrayscaleTransformation(); //黑白效果
            count++;
        }

        if (config.isNeedFilteColor()) {
            transformation[count] = new ColorFilterTransformation(config.getFilteColor());
            count++;
        }

        if (config.isNeedSwirl()) {
            transformation[count] = new SwirlFilterTransformation( 0.5f, 1.0f, new PointF(0.5f, 0.5f)); //漩涡
            count++;
        }

        if (config.isNeedToon()) {
            transformation[count] = new ToonFilterTransformation(); //油画
            count++;
        }

        if (config.isNeedSepia()) {
            transformation[count] = new SepiaFilterTransformation(); //墨画
            count++;
        }

        if (config.isNeedContrast()) {
            transformation[count] = new ContrastFilterTransformation(config.getContrastLevel()); //锐化
            count++;
        }

        if (config.isNeedInvert()) {
            transformation[count] = new InvertFilterTransformation(); //胶片
            count++;
        }

        if (config.isNeedPixelation()) {
            transformation[count] =new PixelationFilterTransformation( config.getPixelationLevel()); //马赛克
            count++;
        }

        if (config.isNeedSketch()) {
            transformation[count] =new SketchFilterTransformation(); //素描
            count++;
        }

        if (config.isNeedVignette()) {
            transformation[count] =new VignetteFilterTransformation( new PointF(0.5f, 0.5f),
                    new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);//晕映
            count++;
        }

        switch (config.getShapeMode()) {
            case ShapeMode.RECT:

                break;
            case ShapeMode.RECT_ROUND:
                transformation[count] = new RoundedCornersTransformation
                        ( config.getRectRoundRadius(), 0, config.getRectRoundRadiusType());
                count++;
                break;
            case ShapeMode.OVAL:
                transformation[count] = new CropCircleTransformation();
                count++;
                break;

            case ShapeMode.SQUARE:
                transformation[count] = new CropSquareTransformation();
                count++;
                break;
        }

        if (transformation.length != 0) {
            request.transform(transformation);

        }

    }

    private int statisticsCount(SingleConfig config) {
        int count = 0;

        if (config.getShapeMode() == ShapeMode.OVAL || config.getShapeMode() == ShapeMode.RECT_ROUND || config.getShapeMode() == ShapeMode.SQUARE) {
            count++;
        }

        if (config.isNeedBlur()) {
            count++;
        }

        if (config.isNeedFilteColor()) {
            count++;
        }

        if (config.isNeedBrightness()) {
            count++;
        }

        if (config.isNeedGrayscale()) {
            count++;
        }

        if (config.isNeedSwirl()) {
            count++;
        }

        if (config.isNeedToon()) {
            count++;
        }

        if (config.isNeedSepia()) {
            count++;
        }

        if (config.isNeedContrast()) {
            count++;
        }

        if (config.isNeedInvert()) {
            count++;
        }

        if (config.isNeedPixelation()) {
            count++;
        }

        if (config.isNeedSketch()) {
            count++;
        }

        if (config.isNeedVignette()) {
            count++;
        }

        return count;
    }

    @Override
    public void pause() {
        Glide.with(GlobalConfig.context).pauseRequestsRecursive();

    }

    @Override
    public void resume() {
        Glide.with(GlobalConfig.context).resumeRequestsRecursive();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(GlobalConfig.context).clearDiskCache();
    }

    @Override
    public void clearMomoryCache(View view) {
//        Glide.get(GlobalConfig.context)
//        Glide.clear(view);
    }

    @Override
    public void clearMomory() {
        Glide.get(GlobalConfig.context).clearMemory();
    }

    @Override
    public boolean isCached(String url) {
        return false;
    }

    @Override
    public void trimMemory(int level) {
        Glide.with(GlobalConfig.context).onTrimMemory(level);
    }

    @Override
    public void clearAllMemoryCaches() {
        Glide.with(GlobalConfig.context).onLowMemory();
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        new Thread(downLoadImageService).start();
    }

}