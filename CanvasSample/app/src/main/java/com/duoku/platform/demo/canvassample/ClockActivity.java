package com.duoku.platform.demo.canvassample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.duoku.platform.demo.canvaslibrary.attract.view.TimeView;

/**
 * Created by chenpengfei_d on 2016/9/8.
 */
public class ClockActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TimeView timeView = new TimeView(this);
        setContentView(timeView);
    }
}
