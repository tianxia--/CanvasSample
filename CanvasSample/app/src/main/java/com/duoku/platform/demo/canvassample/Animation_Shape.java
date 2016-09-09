package com.duoku.platform.demo.canvassample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duoku.platform.demo.canvaslibrary.attract.view.Animation_Circle;
import com.duoku.platform.demo.canvaslibrary.attract.view.Animation_Line;

public class Animation_Shape extends AppCompatActivity {
    private Animation_Circle animation_circle;
    private Animation_Line animation_line ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation__shape);
        animation_circle = (Animation_Circle) findViewById(R.id.animation_shape_view);

        animation_line = (Animation_Line)findViewById(R.id.animation_line_view);
        animation_line.init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }


}
