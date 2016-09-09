package com.duoku.platform.demo.canvassample;

import android.app.Activity;
import android.os.Bundle;

import com.duoku.platform.demo.canvaslibrary.attract.view.BaisaierView;

/**
 * Created by chenpengfei_d on 2016/8/25.
 */
public class BeisaierActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaisaierView view = new BaisaierView(this);
        setContentView(view);
    }
}
