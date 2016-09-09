package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import com.duoku.platform.demo.canvaslibrary.R;
import com.duoku.platform.demo.canvaslibrary.attract.view.Utils.SvgUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * SvgView 是一个绘制svg的paths路径的动画控件
 */
public class SvgView extends View implements SvgUtils.AnimationStepListener {

    public static final String LOG_TAG = "SvgView";
    /**
     * 默认画笔.
     */
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * U工具类用来解析svg文件
     */
    private final SvgUtils svgUtils = new SvgUtils(paint);
    /**
     * 整个svg文件的path集合
     */
    private List<SvgUtils.SvgPath> paths = new ArrayList<>();

    private final Object mSvgLock = new Object();

    private Thread mLoader;
    /**
     * svg文件的资源id
     */
    private int svgResourceId;
    /**
     * 使用第三方动画框架的动画构造者
     */
    private AnimatorBuilder animatorBuilder;

    private AnimatorSetBuilder animatorSetBuilder;
    /**
     * 绘制的进度
     */
    private float progress = 0f;

    /**
     * 是否使用svg文件中的自然颜色
     */
    private boolean naturalColors;
    /**
     * 是否view充满了svg本来的颜色在绘制的过程中
     */
    private boolean fillAfter;

    private boolean fill;

    private int fillColor;

    private int width;

    private int height;

    private Bitmap mTempBitmap;

    private Canvas mTempCanvas;

    public SvgView(Context context) {
        this(context, null);
    }

    public SvgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SvgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint.setStyle(Paint.Style.STROKE);
        getFromAttributes(context, attrs);
    }

    private void getFromAttributes(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SvgView);
        try {
            if (a != null) {
                paint.setColor(a.getColor(R.styleable.SvgView_pathColor, 0xff00ff00));
                paint.setStrokeWidth(a.getDimensionPixelSize(R.styleable.SvgView_pathWidth, 8));
                svgResourceId = a.getResourceId(R.styleable.SvgView_svg, 0);
                naturalColors = a.getBoolean(R.styleable.SvgView_naturalColors, false);
                fill = a.getBoolean(R.styleable.SvgView_fill,false);
                fillColor = a.getColor(R.styleable.SvgView_fillColor,Color.argb(0,0,0,0));
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
            invalidate();
        }
    }

    public void setPaths(final List<Path> paths) {
        for (Path path : paths) {
            this.paths.add(new SvgUtils.SvgPath(path, paint));
        }
        synchronized (mSvgLock) {
            updatePathsPhaseLocked();
        }
    }

    public void setPath(final Path path) {
        paths.add(new SvgUtils.SvgPath(path, paint));
        synchronized (mSvgLock) {
            updatePathsPhaseLocked();
        }
    }

    public void setPercentage(float percentage) {
        if (percentage < 0.0f || percentage > 1.0f) {
            throw new IllegalArgumentException("setPercentage not between 0.0f and 1.0f");
        }
        progress = percentage;
        synchronized (mSvgLock) {
            updatePathsPhaseLocked();
        }
        invalidate();
    }

    private void updatePathsPhaseLocked() {
        final int count = paths.size();
        for (int i = 0; i < count; i++) {
            SvgUtils.SvgPath svgPath = paths.get(i);
            svgPath.path.reset();
            svgPath.measure.getSegment(0.0f, svgPath.length * progress, svgPath.path, true);
            // Required only for Android 4.4 and earlier
            svgPath.path.rLineTo(0.0f, 0.0f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mTempBitmap==null || (mTempBitmap.getWidth()!=canvas.getWidth()||mTempBitmap.getHeight()!=canvas.getHeight()) )
        {
            mTempBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            mTempCanvas = new Canvas(mTempBitmap);
        }

        mTempBitmap.eraseColor(0);
        synchronized (mSvgLock) {
            mTempCanvas.save();
            mTempCanvas.translate(getPaddingLeft(), getPaddingTop());
            fill(mTempCanvas);
            final int count = paths.size();
            for (int i = 0; i < count; i++) {
                final SvgUtils.SvgPath svgPath = paths.get(i);
                final Path path = svgPath.path;
                final Paint paint1 = naturalColors ? svgPath.paint : paint;
                mTempCanvas.drawPath(path, paint1);
            }

            fillAfter(mTempCanvas);

            mTempCanvas.restore();

            applySolidColor(mTempBitmap);

            canvas.drawBitmap(mTempBitmap,0,0,null);
        }
    }

    private void fillAfter(final Canvas canvas) {
        if (svgResourceId != 0 && fillAfter && Math.abs(progress - 1f) < 0.00000001) {
            svgUtils.drawSvgAfter(canvas, width, height);
        }
    }

    private void fill(final Canvas canvas) {
        if (svgResourceId != 0 && fill) {
            svgUtils.drawSvgAfter(canvas, width, height);
        }
    }

    private void applySolidColor(final Bitmap bitmap) {
        if(fill && fillColor!=Color.argb(0,0,0,0) )
            if (bitmap != null) {
                for(int x=0;x<bitmap.getWidth();x++)
                {
                    for(int y=0;y<bitmap.getHeight();y++)
                    {
                        int argb = bitmap.getPixel(x,y);
                        int alpha = Color.alpha(argb);
                        if(alpha!=0)
                        {
                            int red = Color.red(fillColor);
                            int green = Color.green(fillColor);
                            int blue =  Color.blue(fillColor);
                            argb = Color.argb(alpha,red,green,blue);
                            bitmap.setPixel(x,y,argb);
                        }
                    }
                }
            }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mLoader != null) {
            try {
                mLoader.join();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Unexpected error", e);
            }
        }
        if (svgResourceId != 0) {
            mLoader = new Thread(new Runnable() {
                @Override
                public void run() {

                    svgUtils.load(getContext(), svgResourceId);

                    synchronized (mSvgLock) {
                        width = w - getPaddingLeft() - getPaddingRight();
                        height = h - getPaddingTop() - getPaddingBottom();
                        paths = svgUtils.getPathsForViewport(width, height);
                        updatePathsPhaseLocked();
                    }
                }
            }, "SVG Loader");
            mLoader.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (svgResourceId != 0) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(widthSize, heightSize);
            return;
        }

        int desiredWidth = 0;
        int desiredHeight = 0;
        final float strokeWidth = paint.getStrokeWidth() / 2;
        for (SvgUtils.SvgPath path : paths) {
            desiredWidth += path.bounds.left + path.bounds.width() + strokeWidth;
            desiredHeight += path.bounds.top + path.bounds.height() + strokeWidth;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);

        int measuredWidth, measuredHeight;

        if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = desiredWidth;
        } else {
            measuredWidth = widthSize;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = desiredHeight;
        } else {
            measuredHeight = heightSize;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public void setFillAfter(final boolean fillAfter) {
        this.fillAfter = fillAfter;
    }

    public void setFill(final boolean fill) {
        this.fill = fill;
    }

    public void setFillColor(final int color){
        this.fillColor=color;
    }

    public void useNaturalColors() {
        naturalColors = true;
    }

    public AnimatorBuilder getPathAnimator() {
        if (animatorBuilder == null) {
            animatorBuilder = new AnimatorBuilder(this);
        }
        return animatorBuilder;
    }

    public AnimatorSetBuilder getSequentialPathAnimator() {
        if (animatorSetBuilder == null) {
            animatorSetBuilder = new AnimatorSetBuilder(this);
        }
        return animatorSetBuilder;
    }

    public int getPathColor() {
        return paint.getColor();
    }

    public void setPathColor(final int color) {
        paint.setColor(color);
    }

    public float getPathWidth() {
        return paint.getStrokeWidth();
    }

    public void setPathWidth(final float width) {
        paint.setStrokeWidth(width);
    }

    public int getSvgResource() {
        return svgResourceId;
    }


    public void setSvgResource(int svgResource) {
        svgResourceId = svgResource;
    }

    public static class AnimatorBuilder {

        private int duration = 350;

        private Interpolator interpolator;

        private int delay = 0;

        private final ObjectAnimator anim;

        private ListenerStart listenerStart;

        private ListenerEnd animationEnd;

        private SvgViewAnimatorListener SvgViewAnimatorListener;


        public AnimatorBuilder(final SvgView SvgView) {
            anim = ObjectAnimator.ofFloat(SvgView, "percentage", 0.0f, 1.0f);
        }

        public AnimatorBuilder duration(final int duration) {
            this.duration = duration;
            return this;
        }

        public AnimatorBuilder interpolator(final Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public AnimatorBuilder delay(final int delay) {
            this.delay = delay;
            return this;
        }

        public AnimatorBuilder listenerStart(final ListenerStart listenerStart) {
            this.listenerStart = listenerStart;
            if (SvgViewAnimatorListener == null) {
                SvgViewAnimatorListener = new SvgViewAnimatorListener();
                anim.addListener(SvgViewAnimatorListener);
            }
            return this;
        }

        public AnimatorBuilder listenerEnd(final ListenerEnd animationEnd) {
            this.animationEnd = animationEnd;
            if (SvgViewAnimatorListener == null) {
                SvgViewAnimatorListener = new SvgViewAnimatorListener();
                anim.addListener(SvgViewAnimatorListener);
            }
            return this;
        }

        /**
         * 开始动画.
         */
        public void start() {
            anim.setDuration(duration);
            anim.setInterpolator(interpolator);
            anim.setStartDelay(delay);
            anim.start();
        }

        private class SvgViewAnimatorListener implements Animator.AnimatorListener {

            @Override
            public void onAnimationStart(Animator animation) {
                if (listenerStart != null)
                    listenerStart.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationEnd != null)
                    animationEnd.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }

        public interface ListenerStart {

            void onAnimationStart();
        }


        public interface ListenerEnd {

            void onAnimationEnd();
        }
    }

    @Override
    public void onAnimationStep() {
        invalidate();
    }

    /**
     * 动画构建的类
     */
    public static class AnimatorSetBuilder {

        private int duration = 1000;

        private Interpolator interpolator;

        private int delay = 0;
        private final List<Animator> animators = new ArrayList<>();
        /**
         * 动画开始监听
         */
        private AnimatorBuilder.ListenerStart listenerStart;
        /**
         * 动画结束监听
         */
        private AnimatorBuilder.ListenerEnd animationEnd;
        /**
         * 动画监听器.
         */
        private AnimatorSetBuilder.SvgViewAnimatorListener SvgViewAnimatorListener;
        /**
         * 动画path的顺序         */
        private AnimatorSet animatorSet = new AnimatorSet();
        /**
         * 动画路径的列表
         */
        private List<SvgUtils.SvgPath> paths;


        public AnimatorSetBuilder(final SvgView SvgView) {
            paths = SvgView.paths;
            for (SvgUtils.SvgPath path : paths) {
                path.setAnimationStepListener(SvgView);
                ObjectAnimator animation = ObjectAnimator.ofFloat(path, "length", 0.0f, path.getLength());
                animators.add(animation);
            }
            animatorSet.playSequentially(animators);
        }

        /**
         *设置动画时长
         */
        public AnimatorSetBuilder duration(final int duration) {
            this.duration = duration / paths.size();
            return this;
        }

        /**
         * 设置插值器.
         */
        public AnimatorSetBuilder interpolator(final Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        /**
         *设置动画延迟
         */
        public AnimatorSetBuilder delay(final int delay) {
            this.delay = delay;
            return this;
        }

        /**
         *设置动画开始监听
         */
        public AnimatorSetBuilder listenerStart(final AnimatorBuilder.ListenerStart listenerStart) {
            this.listenerStart = listenerStart;
            if (SvgViewAnimatorListener == null) {
                SvgViewAnimatorListener = new SvgViewAnimatorListener();
                animatorSet.addListener(SvgViewAnimatorListener);
            }
            return this;
        }

        /**
         * 设置动画结束的监听器
         */
        public AnimatorSetBuilder listenerEnd(final AnimatorBuilder.ListenerEnd animationEnd) {
            this.animationEnd = animationEnd;
            if (SvgViewAnimatorListener == null) {
                SvgViewAnimatorListener = new SvgViewAnimatorListener();
                animatorSet.addListener(SvgViewAnimatorListener);
            }
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            resetAllPaths();
            animatorSet.cancel();
            animatorSet.setDuration(duration);
            animatorSet.setInterpolator(interpolator);
            animatorSet.setStartDelay(delay);
            animatorSet.start();
        }

        /**
         *重置所有path
         */
        private void resetAllPaths() {
            for (SvgUtils.SvgPath path : paths) {
                path.setLength(0);
            }
        }

        /**
         * svg动画回调监听接口
         */
        private class SvgViewAnimatorListener implements Animator.AnimatorListener {

            @Override
            public void onAnimationStart(Animator animation) {
                if (listenerStart != null)
                    listenerStart.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationEnd != null)
                    animationEnd.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }
    }
}
