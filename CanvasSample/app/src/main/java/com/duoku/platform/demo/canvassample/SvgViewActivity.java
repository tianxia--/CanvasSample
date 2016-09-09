package com.duoku.platform.demo.canvassample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.duoku.platform.demo.canvaslibrary.attract.view.SvgView;

public class SvgViewActivity extends AppCompatActivity {
    private SvgView mSvgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_view);

        mSvgView = (SvgView)findViewById(R.id.svg_view);
        mSvgView.getPathAnimator().duration(5000)
                .start();
    }
}
