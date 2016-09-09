package com.example.chenpengfei_d.canvasdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private ImageView mIv_ImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private Canvas canvas;
    private Paint paint;
    private void init() {
        mIv_ImageView = (ImageView)findViewById(R.id.iv_imageView);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);

        paint = new Paint();
        paint.setColor(Color.BLUE);
//        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);

        RectF rectF = new RectF(10,20,200,100);
        canvas.drawRect(rectF, paint);

        RectF rectOval = new RectF(200,20,400,100);
        canvas.drawOval(rectOval, paint);

        canvas.drawCircle(550, 140, 120, paint);

        Path path = new Path();
        path.moveTo(20, 200);//没有moveTod 话就会直接从0,0点开始绘制
        path.lineTo(200, 200);
        path.lineTo(150, 260);
        path.lineTo(100, 220);
        path.lineTo(70, 260);
        path.lineTo(70, 260);
        path.lineTo(20, 200);
        canvas.drawPath(path, paint);

        paint.setTextSize(36);
        paint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("你好，我在画图", 200, 200, paint);

        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("你好，我在画图", 200, 240, paint);

        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText("你好，我在画图", 200, 280, paint);

        Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC);
        paint.setTypeface(typeface);
        canvas.drawText("你好，我在画图", 200, 320, paint);
//        加载第三方字体
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"typeface.TTF");
        paint.setTypeface(typeface1);
        paint.setTextSize(48);
        Path textPath = new Path();
        textPath.moveTo(20,400);
        textPath.lineTo(40, 440);
        textPath.lineTo(80, 480);
        textPath.lineTo(120, 540);
        textPath.lineTo(180, 600);
        textPath.lineTo(190,600);
        textPath.lineTo(330,600);
        canvas.drawTextOnPath("where are you from ?", textPath, 0, 0, paint);

        paint.setColor(Color.RED);
        canvas.drawText("使用第三方字体", 200, 400, paint);
        mIv_ImageView.setImageBitmap(bitmap);
        mIv_ImageView.setOnTouchListener(this);
    }

    private float startX = 0 ;
    private float startY = 0 ;
    private float endX = 0 ;
    private float endY = 0 ;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
             /*
                绘制起点到移动点的直线
                endX = event.getX() ;
                endY = event.getY();
                canvas.drawLine(startX,startY,endX,endY,paint);
                mIv_ImageView.invalidate();*/

                endX = event.getX();
                endY = event.getY();
                canvas.drawLine(startX,startY,endX,endY,paint);
                startX = endX; startY = endY;
                mIv_ImageView.invalidate();
                break;
        }
        return true;
    }
}
