package com.duoku.platform.demo.canvassample;

import android.app.Activity;
import android.os.Bundle;

import com.duoku.platform.demo.canvaslibrary.attract.view.RobotView;
import com.duoku.platform.demo.canvaslibrary.attract.view.RobotView_two;

/**
 * Created by chenpengfei_d on 2016/8/25.
 */
public class RobotActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RobotView robotView = new RobotView(this);
        RobotView_two robotView_two = new RobotView_two(this);
        setContentView(robotView);
    }
}
