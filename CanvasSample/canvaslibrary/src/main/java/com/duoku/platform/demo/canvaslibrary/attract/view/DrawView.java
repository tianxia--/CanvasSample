package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.duoku.platform.demo.canvaslibrary.R;

/**
 * Created by chenpengfei_d on 2016/8/26.
 */
public class DrawView extends View {

    private Paint mPaint;
    private Context mContext;
    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.mContext = context;
        mPaint = new Paint();
        /**
         * R
         * g
         * b rgb 设置-1的话是黑色，设置1就是各自的颜色，如果都设置1 是混合颜色
         * a 使用的时候 a 不能小于等于0,否则就是是空白
         */
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 1.3f, 0, 0,
                0, 0, 0, 1.0f, 0,
        });
        //颜色矩阵过滤
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //曝光颜色过滤,下边这个是过滤掉蓝色，绿色，增强红色
        mPaint.setColorFilter(new LightingColorFilter(0xFFFF0000,0X00FF0000));
        //混合颜色过滤
        mPaint.setColorFilter(new PorterDuffColorFilter(0xFFFF0000, PorterDuff.Mode.SRC_ATOP));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(bitmap,matrix,mPaint);
      float[] pts=  new float[]{100,100,150,150,200,200,300,300,400,400,500,500};
        canvas.drawLines(pts,mPaint);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoints(pts,mPaint);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setDither(true);
        RectF rectF_arc = new RectF(0,150,150,300);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setDither(true);
        canvas.drawArc(rectF_arc ,0,140,true,mPaint);
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF_arc ,140,30,true,mPaint);
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        canvas.drawArc(rectF_arc ,170,190,true,mPaint);



    }
}
