package valenet.com.br.gestordeos.provider_location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationRequest;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import valenet.com.br.gestordeos.model.realm.LoginLocal;

public class ProviderLocation extends Service {

    ReactiveLocationProvider locationProvider;
    private Context activity;
    Subscription subscription;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setListenerChanges(final Context activity) {
        this.activity = activity;
        locationProvider = new ReactiveLocationProvider(activity);

        RxPermissions rxPermissions = RxPermissions.getInstance(activity);

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .map(new Func1<Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean) {
                        if (aBoolean) {
                            sendDataToServer();
                            return true;
                        } else
                            return false;
                    }
                })
                .subscribe();
        //sendDataToServer();
    }

    @SuppressWarnings({"MissingPermission"})
    private void sendDataToServer() {
        final LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);



        subscription = locationProvider.getUpdatedLocation(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        if (LoginLocal.getInstance().getCurrentUser() != null) {
                            HashMap<String,Object> params = new HashMap<>();
                            params.put("latitude",location.getLatitude());
                            params.put("longitude",location.getLongitude());
                            //Chama a funcao de registrar a localizacao do usuario
/*                            ParseCloud.callFunctionInBackground("setProviderLocation", params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object object, ParseException e) {

                                }
                            });*/
                        }
                    }
                });


    }

    public void destroyService() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        onDestroy();
    }
}
