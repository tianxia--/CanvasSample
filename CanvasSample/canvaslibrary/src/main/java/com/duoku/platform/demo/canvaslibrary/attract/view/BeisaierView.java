package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.duoku.platform.demo.canvaslibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenpengfei_d on 2016/8/24.
 */
public class BeisaierView  extends View {
    private Context mContext;
    private Paint mPaint;
    private Point start_Point;
    private Point end_Point;
    private Point control_Point;
    private Path mPath;
    private Path mPath_circle;
    private Canvas mCanvas;
    private List<Path> paths = new ArrayList<Path>();
    float startAngle;
    private boolean isCreate = false;
    public BeisaierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BeisaierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BeisaierView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
       start_Point = new Point(0,0);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        //绘制字符串通过path

        this.mCanvas = canvas;
        if(paths != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
            mCanvas.drawBitmap(bitmap,0,0,mPaint);

           for (int i = 0; i <paths.size();i++){
                mCanvas.drawPath(mPath, mPaint);
            }
            if(mPath_circle != null)
            mCanvas.drawPath(mPath_circle, mPaint);
        }

        Path path = new Path();
        String s = "点击屏幕绘制开始";
        mPaint.getTextPath(s,0,s.length(),100,100,path);
        mPaint.setTextSize(30);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setLetterSpacing(1.0f);
        path.close();
        mCanvas.drawPath(path,mPaint);
        isCreate = true;


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                end_Point = new Point((int)x,(int)y);
                control_Point = new Point((int)x/3,(int)y/2);
                mPath = new Path();
                paths.add(mPath);
                mPath.moveTo(start_Point.x,start_Point.y);
                ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator() {
                    //fraction 是这个动画of的进度。0.00000000 - 1.0；如果是1 - 100 那么他就是100分子一
                    @Override
                    public Object evaluate(float fraction, Object startValue, Object endValue) {

                        return (float)fraction * 100;
                    }
                }, 0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        float fraction = (float) animation.getAnimatedValue();
                        Log.e("tag", fraction+"");
                        if(fraction>=0f && fraction <=25f){
                            Log.e("tag","0---25");
                            float x;
                                x = (float)end_Point.x/25 *fraction;
                            //float y = end_Point.y/10 *  (float)animation.getAnimatedValue();

                           mPath.lineTo(x,0);
                        }else  if(fraction>25f && fraction<=50f){
                            Log.e("tag","25---50");
                            float y;
                            if(fraction == 50)
                                y = (float)end_Point.y;
                            else
                                y= (float)end_Point.y/25 * (fraction-25);

                            mPath.lineTo((float)end_Point.x,y);

                        }else if(fraction > 50 && fraction <=75){
                            Log.e("tag","50---75");
                            float x;
                            if(fraction == 75)
                                x = 0f;
                            else
                                x   = (float)end_Point.x - (float)end_Point.x/25 * (fraction - 50);

                            mPath.lineTo(x,(float)end_Point.y);

                        }else if(fraction > 75 && fraction <=100){
                            Log.e("tag","75---100");
                            float y;
                            if(fraction == 100)
                                y = 0f;
                            else
                                y= (float)end_Point.y - (float)end_Point.y/25 * (fraction - 75);

                            mPath.lineTo(0,y);
                        }

                        invalidate();

                    /*   if((float)animation.getAnimatedValue() == 100){
                           mPath.reset();
                       }*/
                    }

                });

                animator.setDuration(10000);
                animator.start();
                mPath_circle = new Path();

                final RectF rect = new RectF(0, 0, 500, 500);
                ValueAnimator objectAnimator = ValueAnimator.ofObject(new TypeEvaluator() {
                    @Override
                    public Object evaluate(float fraction, Object startValue, Object endValue) {
                        return fraction;
                    }
                },0f,360f);


                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                       float angle = 360 * (float)animation.getAnimatedValue();
                        mPath_circle.addArc(rect,startAngle,angle);
                        startAngle = angle;
                        invalidate();
                    }
                });

                objectAnimator.setDuration(10000);
            objectAnimator.start();

            ObjectAnimator objectAnimator_ = ObjectAnimator.ofObject(mPath_circle,"trotation", null,90,270);
            objectAnimator_.setDuration(5000);
            objectAnimator_.start();
            break;

        }

        return true;
    }

}
