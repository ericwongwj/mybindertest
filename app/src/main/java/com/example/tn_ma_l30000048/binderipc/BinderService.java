package com.example.tn_ma_l30000048.binderipc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.util.Log;

public class BinderService extends Service implements Thread.UncaughtExceptionHandler{
    static final String TAG=BinderService.class.getSimpleName();

    public static final int REPORT_CODE = 0;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
    }

    //并没有被用到
    public interface IReporter {
        int reportInt(String values, int type);
        int reportString(String values, int type);
    }

    public final class Reporter extends Binder implements IReporter {

        @Override
        public int reportInt(String values, int type) {
            return type*100;
        }

        @Override
        public int reportString(String values, int type) {
            return type;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Log.d(TAG,"Reporter onTransact");
            switch (code) {
                case REPORT_CODE:
                    data.enforceInterface("reporterData");
                    String str1 = data.readString();
                    int intValue = data.readInt();
                    String str2=data.readString();
                    int result = reportInt(str1, intValue);

                    Log.i("Reporter", "Parcel data : " + str1 + " "+intValue+" "+str2);
//                    Intent intent=new Intent(BinderService.this,SecondActivity.class);
//                    startActivity(intent);
                    reply.writeInterfaceToken("reporterReply");
                    reply.writeInt(result);
                    reply.writeString("String from binderService");
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    private Reporter mReporter;

    public BinderService() {
        mReporter = new Reporter();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return mReporter;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }
}
