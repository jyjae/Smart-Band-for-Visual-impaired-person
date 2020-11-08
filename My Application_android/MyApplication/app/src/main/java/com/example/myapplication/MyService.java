package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapView;

public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi;
    static int result;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent2 = new Intent(MyService.this, MainActivity.class)
                    .setAction(Intent.ACTION_MAIN) .addCategory(Intent.CATEGORY_LAUNCHER)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity
                    (MyService.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

            MainActivity m=new MainActivity();
            result=Integer.parseInt(m.setValue().getStrChecked());

            Notifi = new Notification.Builder(getApplicationContext())
                    .setContentTitle("!위험위험!")
                    .setContentText("즉시 지도를 확인해 주세요!!")
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("위험 알림")
//                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            //소리추가
            Notifi.defaults = Notification.DEFAULT_SOUND;
            //알림 소리를 한번만 내도록
            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            //확인하면 자동으로 알림이 제거 되도록
            Notifi.flags = Notification.FLAG_AUTO_CANCEL;

            if (result == 1) {
                Notifi_M.notify(777, Notifi);
                Toast.makeText(MyService.this, "위험 알림", Toast.LENGTH_LONG).show();
                MainActivity.btnRenew.performClick();

                if(result==0){
                    Notifi_M.cancel(777);
                }
            }
        }
    }
}
