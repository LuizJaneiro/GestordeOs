package valenet.com.br.gestordeos.application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.realm.OsLocationDataListLocal;

public class LocationService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    public static LocationManager mLocationManager = null;
    //milliseconds
    public static int LOCATION_INTERVAL = 60;
    //meters
    public static float LOCATION_DISTANCE = 20;
    public static LocationListener mLocationListerStatic;
    GestorDeOsApplication application;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
            Date currentTime = Calendar.getInstance().getTime();
            Integer codUser = null;
            if (LoginLocal.getInstance() != null) {
                if (LoginLocal.getInstance().getCurrentUser() != null)
                    codUser = LoginLocal.getInstance().getCurrentUser().getCoduser();
            }

            if (codUser != null) {
                Log.e(TAG, "onLocationChanged: " + location + " current time: " + currentTime.toString() + " codUser: " + codUser + " batteryLevel: " + application.batteryLevel
                        + " imei: " + application.imei);
                if (OsLocationDataListLocal.getInstance() != null) {
                    OsLocationDataListLocal.getInstance().addOsLocationdataOnListLocal(new OsLocationData(location.getLatitude(), location.getLongitude(), currentTime,
                            codUser, application.batteryLevel, application.imei));
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    public LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    public static void setLocationListener() {
        try {
            if (mLocationManager != null)
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL * 1000, LOCATION_DISTANCE,
                        mLocationListerStatic);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onLocation:onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL * 1000, LOCATION_DISTANCE,
                    mLocationListeners[0]);
            mLocationListerStatic = mLocationListeners[0];
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel;
            channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(this, channel.getId())
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Localização rodando em background")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Localização rodando em background")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onLocation:onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}