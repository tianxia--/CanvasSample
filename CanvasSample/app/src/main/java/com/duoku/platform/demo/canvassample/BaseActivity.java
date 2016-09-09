package com.duoku.platform.demo.canvassample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtn_Ttract,mBtn_baisaier,mBtn_beisaier;
    private Button mBtn_robot,mBtn_test;
    private Button mBtn_animation_shape;
    private Button mBtn_search_view;
    private Button mBtn_Clock_view;
    private Button mBtn_Svg_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mBtn_baisaier = (Button)findViewById(R.id.btn_baisaier);
        mBtn_Ttract = (Button)findViewById(R.id.btn_attract);
        mBtn_beisaier = (Button)findViewById(R.id.btn_beisaier);
        mBtn_robot = (Button)findViewById(R.id.btn_robot);
        mBtn_test = (Button)findViewById(R.id.btn_test);
        mBtn_animation_shape = (Button)findViewById(R.id.btn_animation_shape);
        mBtn_search_view = (Button)findViewById(R.id.btn_search_view);
        mBtn_Clock_view = (Button)findViewById(R.id.btn_Clock_view);
        mBtn_Svg_view = (Button)findViewById(R.id.btn_svg_view);

        mBtn_Ttract.setOnClickListener(this);
        mBtn_baisaier.setOnClickListener(this);
        mBtn_beisaier.setOnClickListener(this);
        mBtn_robot.setOnClickListener(this);
        mBtn_test.setOnClickListener(this);
        mBtn_animation_shape.setOnClickListener(this);
        mBtn_search_view.setOnClickListener(this);
        mBtn_Clock_view.setOnClickListener(this);
        mBtn_Svg_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()){

            case R.id.btn_attract:
                intent.setClass(BaseActivity.this,MainActivity.class);
                break;

            case R.id.btn_baisaier:
                intent.setClass(BaseActivity.this,BaisaierActivity.class);
                break;
            case R.id.btn_beisaier:
                intent.setClass(BaseActivity.this,BeisaierActivity.class);
                break;
            case R.id.btn_robot:
                intent.setClass(BaseActivity.this,RobotActivity.class);
                break;
            case R.id.btn_test:
                intent.setClass(BaseActivity.this,TestAactivity.class);
                break;
            case R.id.btn_animation_shape:
                intent.setClass(BaseActivity.this,Animation_Shape.class);
                break;
            case R.id.btn_search_view:
                intent.setClass(BaseActivity.this,SearchViewActivity.class);
                break;
            case R.id.btn_Clock_view:
                intent.setClass(BaseActivity.this,ClockActivity.class);
                break;
            case R.id.btn_svg_view:
                intent.setClass(BaseActivity.this,SvgViewActivity.class);
                break;
        }
        startActivity(intent);
    }
}
