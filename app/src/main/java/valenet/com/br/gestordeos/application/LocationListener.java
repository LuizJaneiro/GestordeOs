package valenet.com.br.gestordeos.application;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import valenet.com.br.gestordeos.main.MainActivity;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.realm.OsLocationDataListLocal;

import static valenet.com.br.gestordeos.main.MainActivity.myService;

public class LocationListener implements android.location.LocationListener {
    /**
     * Listener que escuta o GPS e atualiza os pontos caso necessário
     */
    private static final String TAG = "BOOMBOOMTESTGPS";
    Location mLastLocation;
    public static Date initTime = null;
    public static Date endTime = null;
    GestorDeOsApplication application;
    private stopService listener;

    public LocationListener(String provider, LocationService service) {
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
        listener = service;
    }

    @Override
    public void onLocationChanged(Location location) {
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(currentTime);
        Calendar calInitTime = Calendar.getInstance();
        Calendar calEndTime = Calendar.getInstance();
        int hourInitime, hourEndTime, minuteInitTime, minuteEndtime, hourCurrentTime, minuteCurrentTime;
        hourCurrentTime = calCurrentTime.get(Calendar.HOUR_OF_DAY);
        minuteCurrentTime = calCurrentTime.get(Calendar.MINUTE);
        if (initTime != null) {
            calInitTime.setTime(initTime);
            hourInitime = calInitTime.get(Calendar.HOUR_OF_DAY);
            minuteInitTime = calInitTime.get(Calendar.MINUTE);
        } else {
            hourInitime = Integer.MAX_VALUE;
            minuteInitTime = Integer.MAX_VALUE;
        }

        if (endTime != null) {
            calEndTime.setTime(endTime);
            hourEndTime = calEndTime.get(Calendar.HOUR_OF_DAY);
            minuteEndtime = calCurrentTime.get(Calendar.MINUTE);
        } else {
            hourEndTime = Integer.MIN_VALUE;
            minuteEndtime = Integer.MIN_VALUE;
        }

        //Verifica se está dentro do tempo delimitado via API
        if ((hourInitime == hourCurrentTime && minuteInitTime <= minuteCurrentTime) || (hourEndTime == hourCurrentTime && minuteEndtime >= minuteCurrentTime)
                || (hourInitime < hourCurrentTime && hourEndTime > hourCurrentTime)) {
            mLastLocation.set(location);
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
        } else {
            listener.stopService();
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

    public interface stopService {
        void stopService();
    }
}
