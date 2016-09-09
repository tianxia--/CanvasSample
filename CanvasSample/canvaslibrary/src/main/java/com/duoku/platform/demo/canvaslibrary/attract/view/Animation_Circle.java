package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.duoku.platform.demo.canvaslibrary.R;

/**
 * Created by chenpengfei_d on 2016/9/6.
 */
public class Animation_Circle extends View{
    private Paint mPaint;
    private Path mPath_Circle;
    private Context mContext;
    private float current_value=0;
    private float pos [];
    private float tan [];
    private Bitmap mBitmap;
    private Matrix matrix;
    public Animation_Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Animation_Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Animation_Circle(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        this.mContext = context;

        pos = new float[2];
        tan = new float[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1/2;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        matrix = new Matrix();

      WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
      Display display =  manager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath_Circle = new Path();

        mPath_Circle.addCircle(300,300,200, Path.Direction.CW);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(mPath_Circle,false);

            current_value = (current_value + 0.005f);
            if(current_value >= 1){
                current_value =0;
            }

            pathMeasure.getPosTan(pathMeasure.getLength()*current_value,pos,tan);
           /* matrix.reset();
            float angle = (float) (Math.atan2(tan[1],tan[0])*180.0/Math.PI);
            matrix.postRotate(angle,mBitmap.getWidth()/2,mBitmap.getHeight()/2);
            matrix.postTranslate(pos[0] - mBitmap.getWidth()/2, pos[1] - mBitmap.getHeight()/2);*/
            pathMeasure.getMatrix(pathMeasure.getLength()*current_value,matrix,PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
            matrix.preTranslate(-mBitmap.getWidth()/2,-mBitmap.getHeight()/2);
            canvas.drawBitmap(mBitmap,matrix,mPaint);
            invalidate();
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},5);
        mPaint.setPathEffect(effects);
        canvas.drawPath(mPath_Circle,mPaint);
    }


}
