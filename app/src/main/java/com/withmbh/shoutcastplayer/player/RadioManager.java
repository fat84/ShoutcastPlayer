package com.withmbh.shoutcastplayer.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.withmbh.shoutcastplayer.MyApp;
import com.withmbh.shoutcastplayer.lib.MyLog;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private final String TAG = getClass().getSimpleName();

    private static RadioManager instance = null;

    private static RadioService service;

    private Context context;

    private boolean serviceBound;

    private RadioManager(Context context) {
        this.context = context;
        serviceBound = false;
    }

    public static RadioManager with(Context context) {

        if (instance == null)
            instance = new RadioManager(context);

        return instance;
    }

    public static RadioService getService(){
        return service;
    }

    public void playOrPause(String streamUrl){
            service.playOrPause(streamUrl);
    }

    public boolean isPlaying() {

        return service.isPlaying();
    }

    public void bind() {
        MyLog.d(TAG, "RadioManager  bind" );

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if(service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {

        context.unbindService(serviceConnection);

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
            MyLog.d(TAG, "onServiceConnected" );

            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;

            // 앱 실행시 바로 플레이어를 구동할수 있도록 서비스 연결시 커스텀 이벤트를 발생시킴.
            EventBus.getDefault().post("ServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            serviceBound = false;
        }
    };

}
