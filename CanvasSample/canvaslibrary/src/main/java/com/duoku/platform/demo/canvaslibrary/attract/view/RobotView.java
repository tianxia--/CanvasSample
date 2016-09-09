package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenpengfei_d on 2016/8/25.
 */
public class RobotView  extends View {
    private Context mContext;
    private Paint mPaint;
    private Path mPath;
    private Path mPath_ear_l,mPath_ear_r;

    private Path mPath_body;

    private Path mPath_head_l,mPath_head_r;
    private Path mPath_leg_l,mPath_leg_r;

    private Path mPath_l,mPath_r;
    public RobotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RobotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RobotView(Context context) {
        super(context);
        init(context);
    }

    public  void init(Context context){
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);

        mPath = new Path();
        RectF rectF = new RectF();
        rectF.set(180,180,600,600);
        mPath.addArc(rectF,180,180);
        mPath.close();

        mPath_ear_l = new Path();
        mPath_ear_l.addCircle(300,270,8, Path.Direction.CW);
        mPath_ear_r = new Path();
        mPath_ear_r.addCircle(500,270,8, Path.Direction.CCW);

        mPath_body = new Path();

        RectF rectF_body = new RectF(180,400,600,600);
        mPath_body.addRoundRect(rectF_body,10,10, Path.Direction.CCW);

        mPath_head_l = new Path();
        RectF rectF_head_l = new RectF(140,420,170,540);
        mPath_head_l.addRoundRect(rectF_head_l,30,30,Path.Direction.CCW);

        mPath_head_r = new Path();
        RectF rectF_head_r = new RectF(610,420,640,540);
        mPath_head_r.addRoundRect(rectF_head_r,30,30,Path.Direction.CCW);

        mPath_leg_l = new Path();
        RectF rectF_leg_l = new RectF(250,600, 300,670);
        mPath_leg_l.addRoundRect(rectF_leg_l,10,10, Path.Direction.CCW);

        mPath_leg_r = new Path();
        RectF rectF_leg_r = new RectF(480,600, 530,670);
        mPath_leg_r.addRoundRect(rectF_leg_r, 10,10,Path.Direction.CCW);

        mPath_l = new Path();
        RectF rectF_l = new RectF(280,150,290,200);
        mPath_l.addRect(rectF_l, Path.Direction.CCW);

        mPath_r = new Path();
        RectF rectF_r = new RectF(490,150,500,200);
        mPath_r.addRect(rectF_r, Path.Direction.CCW);

    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
        canvas.drawPath(mPath_ear_l,mPaint);
        canvas.drawPath(mPath_ear_r,mPaint);

        canvas.drawPath(mPath_body,mPaint);

        canvas.drawPath(mPath_head_l,mPaint);
        canvas.drawPath(mPath_head_r,mPaint);

        canvas.drawPath(mPath_leg_l,mPaint);
        canvas.drawPath(mPath_leg_r,mPaint);
        canvas.drawPath(mPath_l,mPaint);
        canvas.drawPath(mPath_r,mPaint);

    }


}
