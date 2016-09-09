package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by chenpengfei_d on 2016/9/8.
 */
public class TimeView extends View {
    private Paint mPaint,mPaint_time;
    private Paint mPaint_h,mPaint_m,mPaint_s;
    private Path mPath_Circle;
    private Path mPath_Circle_h;
    private Path mPath_Circle_m;
    private Path mPath_h,mPath_m,mPath_s;
    private Path mPath_duration;

    private PathMeasure mMeasure;
    private PathMeasure mMeasure_h;
    private PathMeasure mMeasure_m;
    private Handler mHandler = new Handler();
    private Runnable clockRunnable;
    private boolean isRunning;
    public TimeView(Context context) {
        super(context);
        init();
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    int  t = 3;
    public void init(){
        //初始化画笔
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.RED);
        mPaint_time = new Paint();
        mPaint_time.setDither(true);
        mPaint_time.setAntiAlias(true);
        mPaint_time.setStyle(Paint.Style.STROKE);
        mPaint_time.setStrokeWidth(2);
        mPaint_time.setTextSize(15);
        mPaint_time.setStrokeCap(Paint.Cap.ROUND);
        mPaint_time.setStrokeJoin(Paint.Join.ROUND);
        mPaint_time.setColor(Color.RED);

        mPaint_h = new Paint();
        mPaint_h.setDither(true);
        mPaint_h.setAntiAlias(true);
        mPaint_h.setStyle(Paint.Style.STROKE);
        mPaint_h.setStrokeWidth(6);
        mPaint_h.setTextSize(15);
        mPaint_h.setStrokeCap(Paint.Cap.ROUND);
        mPaint_h.setStrokeJoin(Paint.Join.ROUND);
        mPaint_h.setColor(Color.RED);

        mPaint_m = new Paint();
        mPaint_m.setDither(true);
        mPaint_m.setAntiAlias(true);
        mPaint_m.setStyle(Paint.Style.STROKE);
        mPaint_m.setStrokeWidth(4);
        mPaint_m.setTextSize(15);
        mPaint_m.setStrokeCap(Paint.Cap.ROUND);
        mPaint_m.setStrokeJoin(Paint.Join.ROUND);
        mPaint_m.setColor(Color.RED);

        mPaint_s = new Paint();
        mPaint_s.setDither(true);
        mPaint_s.setAntiAlias(true);
        mPaint_s.setStyle(Paint.Style.STROKE);
        mPaint_s.setStrokeWidth(2);
        mPaint_s.setTextSize(15);
        mPaint_s.setStrokeCap(Paint.Cap.ROUND);
        mPaint_s.setStrokeJoin(Paint.Join.ROUND);
        mPaint_s.setColor(Color.RED);
        //初始化刻度
        mPath_Circle  = new Path();
        mPath_Circle.addCircle(0,0,250, Path.Direction.CCW);
        mPath_Circle_h  = new Path();
        mPath_Circle_h.addCircle(0,0,220, Path.Direction.CCW);
        mPath_Circle_m  = new Path();
        mPath_Circle_m.addCircle(0,0,235, Path.Direction.CCW);
        //初始化PathMeasure测量path坐标，
        mMeasure = new PathMeasure();
        mMeasure.setPath(mPath_Circle,true);
        mMeasure_h = new PathMeasure();
        mMeasure_h.setPath(mPath_Circle_h,true);
        mMeasure_m = new PathMeasure();
        mMeasure_m.setPath(mPath_Circle_m,true);
        //获取刻度path
        mPath_duration = new Path();
        for (int i = 60; i>0 ;i --){
            Path path = new Path();
            float pos [] = new float[2];
            float tan [] = new float[2];
            float pos2 [] = new float[2];
            float tan2 [] = new float[2];
            float pos3 [] = new float[2];
            float tan3 [] = new float[2];
            mMeasure.getPosTan(mMeasure.getLength()*i/60,pos,tan);
            mMeasure_h.getPosTan(mMeasure_h.getLength()*i/60,pos2,tan2);
            mMeasure_m.getPosTan(mMeasure_m.getLength()*i/60,pos3,tan3);

            float x = pos[0];
            float y = pos[1];
            float x2 = pos2[0];
            float y2 = pos2[1];
            float x3 = pos3[0];
            float y3 = pos3[1];
            path.moveTo(x , y);

            if(i% 5 ==0){
                path.lineTo(x2,y2);
                if(t>12){
                    t = t-12;
                }
                String time = t++ +"";
                Path path_time = new Path();
                mMeasure_h.getPosTan(mMeasure_h.getLength()*(i-1)/60,pos2,tan2);
                mPaint.getTextPath(time,0,time.length(),(x2- (x2/15)),y2-(y2/15),path_time);
                path.close();
                path.addPath(path_time);
            }else{
                path.lineTo(x3,y3);
            }


            mPath_duration.addPath(path);
            clockRunnable = new Runnable() {//里面做的事情就是每隔一秒，刷新一次界面
                @Override
                public void run() {
                    //线程中刷新界面
                    postInvalidate();
                    mHandler.postDelayed(this, 1000);
                }
            };
        }

        mPath_h = new Path();
        mPath_h.rLineTo(50,30);

        mPath_m = new Path();
        mPath_m.rLineTo(80,80);

        mPath_s = new Path();
        mPath_s.rLineTo(130,50);
    }
    private int mWidth,mHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isRunning){
            isRunning = true;
            mHandler.postDelayed(clockRunnable,1000);
        }else{
            canvas.translate(mWidth/2,mHeight/2);

            canvas.drawPath(mPath_Circle,mPaint);
            canvas.save();
            canvas.drawPath(mPath_duration,mPaint_time);

            canvas.drawPoint(0,0,mPaint_time);

            drawClockPoint(canvas);
        }





    }
    private Calendar cal;
    private int hour;
    private int min;
    private int second;
    private float hourAngle,minAngle,secAngle;
    /**
     * 绘制三个指针
     * @param canvas
     */
    private void drawClockPoint(Canvas canvas) {
        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);//Calendar.HOUR获取的是12小时制，Calendar.HOUR_OF_DAY获取的是24小时制
        min = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        //计算时分秒指针各自需要偏移的角度
        hourAngle = (float)hour / 12 * 360 + (float)min / 60 * (360 / 12);//360/12是指每个数字之间的角度
        minAngle = (float)min / 60 * 360;
        secAngle = (float)second / 60 * 360;
        //下面将时、分、秒指针按照各自的偏移角度进行旋转，每次旋转前要先保存canvas的原始状态
        canvas.save();
        canvas.rotate(hourAngle,0, 0);
        canvas.drawLine(0, 0, mWidth/6, getHeight() / 6 - 65, mPaint_h);//时针长度设置为65

        canvas.restore();
        canvas.save();
        canvas.rotate(minAngle,0, 0);
        canvas.drawLine(0, 0, mWidth/6, getHeight() / 6 - 90 , mPaint_m);//分针长度设置为90

        canvas.restore();
        canvas.save();
        canvas.rotate(secAngle,0, 0);
        canvas.drawLine(0, 0, mWidth/6, getHeight() / 6 - 110 , mPaint_s);//秒针长度设置为110

        canvas.restore();
    }
}
