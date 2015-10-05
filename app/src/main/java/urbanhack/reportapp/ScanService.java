package urbanhack.reportapp;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class ScanService extends Service {
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager notificationManager;
    private Region region;
    private BeaconManager beaconManager;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);



    private void stopScan() {

        if(beaconManager!=null) {
            try {
                beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
            } catch (RemoteException e) {
                Log.d("ScanService", "Error while stopping ranging", e);
            }
            beaconManager.disconnect();
        }
    }



    private void scan() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        beaconManager = new BeaconManager(this);
        region = new Region("lv", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 10829, 58183);//green color assembly
//        region = new Region("lv", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 25744, 39369);//blue color d-7
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                Log.e("ScanService:", "entered " + beacons.get(0).getMinor());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Beacon beacon : beacons) {
                            Log.e("ScanService:", "beacon minor: " + beacon.getMinor() + " beacon major: " + beacon.getMajor() + " beacon proximity uuid: " + beacon.getProximityUUID() + " beacon RSSI" + beacon.getRssi());
                            postNotification("New Notification!");
//                            postNotificationForConnection();
                        }

                    }
                }).start();
            }

            @Override
            public void onExitedRegion(Region region) {
                //do nothing for now
                Log.e("ScanService:", "exitted " + region.getIdentifier());
//                postNotification("Exitted!");
            }
        });

        if (beaconManager.isBluetoothEnabled()) {

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    try {
                        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(2));
                        beaconManager.startMonitoring(region);
                    } catch (RemoteException e) {
                        Log.e("ScanService", "Cannot start ranging", e);
                    }
                }

            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScan();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Integer code = 0;
        try {
            code = intent.getExtras().getInt("taskcode");
        }catch (Exception e){
            Log.e("error"," "+e);
            code = 1;
        }
        if(code == 1){
            //bluetooth is on
            Log.e("ScanService"," Starting Self");
            scan();
        }else{
            //bluetooth is off, kill self
            Log.e("ScanService"," Killing Self");
            this.stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void postNotification(String msg) {
        Intent i = new Intent(this,SplashActivity.class);
        sendNotification(i,"Found nearby Reporter","Reporter Found!");

    }

    private void postNotificationForConnection() {
        Intent i = new Intent(this,KioskConnectActivity.class);
        sendNotification(i,"Post report anonymously!","Kiosk Found!");
    }

    private void sendNotification(Intent intent, String message,String title) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random(100l).nextInt(), notificationBuilder.build());
    }
}