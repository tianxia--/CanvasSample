package com.duoku.platform.demo.canvaslibrary.attract.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class BaisaierView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder sfh;
    private Paint paint;
    private Thread th;
    private boolean flag;
    private Canvas canvas;
    public static int screenW, screenH;
    // -----------以上是SurfaceView游戏框架
    // 贝赛尔曲线成员变量(起始点，控制（操作点），终止点，3点坐标)
    private int startX, startY, controlX, controlY, endX, endY;
    // Path
    private Path path;

    public BaisaierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        setFocusable(true);
        // -----------以上是SurfaceView游戏框架
        //贝赛尔曲线相关初始化
        path = new Path();
        paintQ = new Paint();
        paintQ.setAntiAlias(true);
        paintQ.setStyle(Paint.Style.STROKE);
        paintQ.setStrokeWidth(5);
        paintQ.setColor(Color.WHITE);
        random = new Random();
    }

    // 为了不影响主画笔，这里绘制贝赛尔曲线单独用一个新画笔
    private Paint paintQ;
    // 随机库（让贝赛尔曲线更明显）
    private Random random;
    /**
     * SurfaceView初始化函数
     */
    public BaisaierView(Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        setFocusable(true);
        // -----------以上是SurfaceView游戏框架
        //贝赛尔曲线相关初始化
        path = new Path();
        paintQ = new Paint();
        paintQ.setAntiAlias(true);
        paintQ.setStyle(Paint.Style.STROKE);
        paintQ.setStrokeWidth(5);
        paintQ.setColor(Color.WHITE);
        random = new Random();
    }


    /**
     * SurfaceView视图创建，响应此函数
     */
    public void surfaceCreated(SurfaceHolder holder) {
        screenW = this.getWidth();
        screenH = this.getHeight();
        flag = true;
        // 实例线程
        th = new Thread(this);
        // 启动线程
        th.start();
        // -----------以上是SurfaceView游戏框架
    }
    /**
     * 游戏绘图
     */
    public void myDraw() {
        try {
            canvas = sfh.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                // -----------以上是SurfaceView游戏框架
                drawQpath(canvas);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }
    /**
     * 绘制贝赛尔曲线
     *
     * @param canvas 主画布
     */
    public void drawQpath(Canvas canvas) {
        path.reset();// 重置path
        // 贝赛尔曲线的起始点
        path.moveTo(startX, startY);
        // 设置贝赛尔曲线的操作点以及终止点
        path.quadTo(controlX, controlY, endX, endY);
        // 绘制贝赛尔曲线（Path）
        canvas.drawPath(path, paintQ);
    }
    /**
     * 触屏事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        endX = (int) event.getX();
        endY = (int) event.getY();
        return true;
    }
    /**
     * 游戏逻辑
     */
    private void logic() {
        if (endX != 0 && endY != 0) {
            // 设置操作点为线段x/y的一半
            controlX = (endX - startX) / 2 + 200;
            controlY = (endY - startY) / 2 + 200;
//            controlX = random.nextInt((endX - startX) / 2);
//            controlY = random.nextInt((endY - startY) / 2);
        }
    }
    /**
     * 按键事件监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
    public void run() {
        while (flag) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * SurfaceView视图状态发生改变，响应此函数
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }
    /**
     * SurfaceView视图消亡时，响应此函数
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }
}