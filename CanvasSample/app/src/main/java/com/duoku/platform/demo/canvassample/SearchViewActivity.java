package com.duoku.platform.demo.canvassample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.duoku.platform.demo.canvaslibrary.attract.view.SearchView;

/**
 * Created by chenpengfei_d on 2016/9/7.
 */
public class SearchViewActivity  extends AppCompatActivity {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case 0:
                    searchView.setState(SearchView.Seach_State.END);
                    break;
            }
        }
    };
    SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchView = new SearchView(this);
        searchView.setState(SearchView.Seach_State.START);
        setContentView(searchView);
        handler.sendEmptyMessageDelayed(0,10000);
    }
}
