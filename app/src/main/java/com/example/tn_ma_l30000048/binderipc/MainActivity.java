package com.example.tn_ma_l30000048.binderipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IBinder mReporterBinder;
    String TAG=MainActivity.class.getSimpleName();

    String s="09-18 09:28:10.516 2547-2570/com.example.tn_ma_l30000048.binderipc D/OpenGLRenderer: endAllActiveAnimators on 0xaa729380 (RippleDrawable) with handle 0xb55342a0\n";

    {
        System.out.println("MainActivity init");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bind:
                Intent intent = new Intent(this, BinderService.class);
                bindConnection=new BindConnection();
                bindService(intent, bindConnection, BIND_AUTO_CREATE);//这一步耗时的 0.5s左右 自动create 而不会调用onStartCommand
                break;
            case R.id.btn_transact:
                doTransact();
                break;
            case R.id.btn_unbind:
                unbindService(bindConnection);
                break;
            case R.id.btn_aidl_bind:
                throw new RuntimeException("hahaha");
                //break;
            case R.id.btn_aidl_transact:

                break;
            case R.id.btn_aidl_unbind:

                break;
        }
    }

    BindConnection bindConnection;
    private class BindConnection implements ServiceConnection {
        String TAG="BindConnection";
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected "+name.getShortClassName());
            mReporterBinder = service;//在这里初始化IBinder对象
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected "+name);
            mReporterBinder = null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log("onCreate");
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_transact).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        findViewById(R.id.btn_aidl_bind).setOnClickListener(this);
        findViewById(R.id.btn_aidl_transact).setOnClickListener(this);
        findViewById(R.id.btn_aidl_unbind).setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                try{
                    int a=10/0;
//                }
//                catch (ArithmeticException e){//runtime exception 还是能被抓住的
//                    e.printStackTrace();
//                }
//                int []a={1,2,3,4,5};
//                int b=a[5];

                //throw new RuntimeException("my intent");//android logcat中的进程死了 但是ps 显示进程在futex wait状态 界面也显示 然后会anr
            }
        },3000);
    }

    void doTransact(){
//      Activity 中通过 bindService 拿到 Binder Driver 中的 mRemote 对象（IBinder 的实例），然后「组包」，然后「调用 transact 接口」按序发送数据包
//      Service 中继承 Binder 类，「重载 onTransact 函数」，实现参数的「解包」，发送返回包等，在 onBind 中返回具体的实现类 如上文中的 Reporter
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken("reporterData");//接口不一致就会造成crash SecurityException: Binder invocation to an incorrect interface
        data.writeString("this is a string from mainaty.");
        data.writeInt(100);
        data.writeString("this is another test string.");
        try {
            log("doTransact");
            boolean isTransact=mReporterBinder.transact(BinderService.REPORT_CODE, data, reply, 0);//Perform a generic operation with the object.
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        reply.enforceInterface("reporterReply");
        int result = reply.readInt();
        log("Reply: int "+result+" String "+reply.readString());
        data.recycle();
        reply.recycle();
    }

    void log(String msg){
        Log.d(TAG,msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.btn_unbind).post(new Runnable() {
            @Override
            public void run() {
                System.out.println("hahaha let's thrash");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<1000;i++){
                    String temp=s;
                    for(int j=0;j<50;j++)
                        temp+=s;
                }
            }
        });;//.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy");
        if(bindConnection!=null)
            unbindService(bindConnection);
    }
}
