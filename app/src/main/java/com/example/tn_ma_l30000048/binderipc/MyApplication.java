package com.example.tn_ma_l30000048.binderipc;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by tn-ma-l30000048 on 17/9/18.
 */

public class MyApplication extends Application {

    String TAG=getClass().getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate");

        //这玩意catch之后不处理就会卡在这
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                log("Exception caught in handler:"+e.getMessage());
                System.exit(0);
//                startActivity(new In);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        log("attachBaseContext");
    }


    void log(String msg){
        Log.d(TAG,msg);
    }
}
