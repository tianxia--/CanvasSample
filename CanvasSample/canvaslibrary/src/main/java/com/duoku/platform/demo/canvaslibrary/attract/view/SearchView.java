package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by chenpengfei_d on 2016/9/7.
 */
public class SearchView extends View {
    private Paint mPaint;
    private Context mContext;
    private Path mPath_circle;
    private Path mPath_search;
    private PathMeasure mMeasure;
    private ValueAnimator mValueAnimator_search;
    private long  defaultduration=3000;
    private float curretnAnimationValue;
    private Seach_State mState = Seach_State.SEARCHING;
    public SearchView(Context context) {
        super(context);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.mContext = context;
        initPaint();
        initPath();
        initAnimation();

    }
    public void initPaint(){
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔头效果
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void initPath(){
        mPath_search = new Path();
        mPath_circle = new Path();

        mMeasure = new PathMeasure();

        // 注意,不要到360度,否则内部会自动优化,测量不能取到需要的数值
        RectF oval1 = new RectF(-50, -50, 50, 50);          // 放大镜圆环
        mPath_search.addArc(oval1, 45, 359.9f);

        RectF oval2 = new RectF(-100, -100, 100, 100);      // 外部圆环
        mPath_circle.addArc(oval2, 45, -359.9f);

        float[] pos = new float[2];

        mMeasure.setPath(mPath_circle, false);               // 放大镜把手的位置
        mMeasure.getPosTan(0, pos, null);

        mPath_search.lineTo(pos[0], pos[1]);                 // 放大镜把手

        Log.i("TAG", "pos=" + pos[0] + ":" + pos[1]);

    }

    public void initAnimation(){
        mValueAnimator_search = ValueAnimator.ofFloat(0f,1.0f).setDuration(defaultduration);

        mValueAnimator_search.addUpdateListener(updateListener);

        mValueAnimator_search.addListener(animationListener);
    }
    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            curretnAnimationValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
                if(mState ==Seach_State.START){
                    setState(Seach_State.SEARCHING);
                }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPath(canvas);
    }
    private int mWidth,mHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

    }

    private void drawPath(Canvas c) {
        c.translate(mWidth / 2, mHeight / 2);
        switch (mState){

            case NONE:
                c.drawPath(mPath_search,mPaint);
                break;

            case START:
                mMeasure.setPath(mPath_search,true);
                Path path = new Path();
                mMeasure.getSegment(mMeasure.getLength() * curretnAnimationValue,mMeasure.getLength(),path, true);
                c.drawPath(path,mPaint);
                break;

            case SEARCHING:
                mMeasure.setPath(mPath_circle,true);
                Path path_search = new Path();
                mMeasure.getSegment(mMeasure.getLength()*curretnAnimationValue -30,mMeasure.getLength()*curretnAnimationValue,path_search,true);
                c.drawPath(path_search,mPaint);
                break;

            case END:
                mMeasure.setPath(mPath_search,true);
                Path path_view = new Path();

                mMeasure.getSegment(0,mMeasure.getLength()*curretnAnimationValue,path_view,true);
                c.drawPath(path_view,mPaint);
                break;
        }

    }


    public void setState(Seach_State state){
        this.mState = state;
        startSearch();
    }

    public void startSearch(){
        switch (mState){
            case START:
                mValueAnimator_search.setRepeatCount(0);
                break;

            case SEARCHING:
                mValueAnimator_search.setRepeatCount(ValueAnimator.INFINITE);
                mValueAnimator_search.setRepeatMode(ValueAnimator.REVERSE);
                break;

            case END:
                mValueAnimator_search.setRepeatCount(0);
                break;
        }
        mValueAnimator_search.start();
    }
   public   enum  Seach_State{
       START,END,NONE,SEARCHING
    }
}
