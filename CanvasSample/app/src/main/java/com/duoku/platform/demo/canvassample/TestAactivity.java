package com.duoku.platform.demo.canvassample;

import android.app.Activity;
import android.os.Bundle;

import com.duoku.platform.demo.canvaslibrary.attract.view.DrawView;

/**
 * Created by chenpengfei_d on 2016/8/25.
 */
public class TestAactivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawView drawView = new DrawView(this);
        setContentView(drawView);
    }
}
