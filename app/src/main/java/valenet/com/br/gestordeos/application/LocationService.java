package valenet.com.br.gestordeos.application;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.services.common.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationDataList;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.realm.OsLocationDataListLocal;

public class LocationService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    //milliseconds
    private static final int LOCATION_INTERVAL = 10000;
    //meters
    private static final float LOCATION_DISTANCE = 0;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        GestorDeOsApplication application;
        private int intervalSendPointsMinutes = 1;

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
                Log.e(TAG, "onLocationChanged: " + location + " current time: " + currentTime.toString() + " codUser: " + codUser + " batteryLevel: " + application.batteryLevel);
                if (OsLocationDataListLocal.getInstance() != null) {
                    OsLocationDataListLocal.getInstance().addOsLocationdataOnListLocal(new OsLocationData(location.getLatitude(), location.getLongitude(), currentTime,
                                                                                        codUser, application.batteryLevel));
                }
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    final OsLocationDataListLocal osLocationDataListLocal = OsLocationDataListLocal.getInstance();
                    if(osLocationDataListLocal != null){
                        List<OsLocationData> osLocationDataList = osLocationDataListLocal.getOsLocationDataList();
                        if(osLocationDataList != null && osLocationDataList.size() > 0){
                            OsLocationData[] osLocationDataArray = new OsLocationData[osLocationDataList.size()];
                            osLocationDataArray = osLocationDataList.toArray(osLocationDataArray);
                            final OsLocationData[] finalOsLocationDataArray = osLocationDataArray;
                            application.API_INTERFACE.sendUserPostions(osLocationDataArray).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                            osLocationDataListLocal.deleteOsLocationDataLists();
                                            List<OsLocationData> osLocationDataList = osLocationDataListLocal.getOsLocationDataList();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                }
                            });
                        }
                    }
                }
            }, intervalSendPointsMinutes * 60 * 1000);
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

    LocationListener[] mLocationListeners = new LocationListener[]{
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

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
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
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}