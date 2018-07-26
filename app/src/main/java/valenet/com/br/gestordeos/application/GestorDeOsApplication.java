package valenet.com.br.gestordeos.application;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.ModelCheck;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.realm.ModelCheckListLocal;
import valenet.com.br.gestordeos.model.realm.OsLocationDataListLocal;
import valenet.com.br.gestordeos.model.service.ApiInterface;
import valenet.com.br.gestordeos.model.service.ApiInterfaceGoogleDistance;
import valenet.com.br.gestordeos.model.service.ApiUtils;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import valenet.com.br.gestordeos.provider_location.ProviderLocation;

public class GestorDeOsApplication extends android.app.Application {

    private static final String TAG = "BOOMBOOMTESTGPS";
    public static final ApiInterface API_INTERFACE = ApiUtils.getService();
    public static final ApiInterfaceGoogleDistance API_INTERFACE_GOOGLE_DISTANCE = ApiUtils.getServiceGoogleDistance();
    public static Realm realm;
    public static final Locale myLocale = new Locale("pt", "BR");
    //private static FirebaseAnalytics mFirebaseAnalytics;
    ProviderLocation providerLocation;
    // region Members
    private static Context appContext;

    public static int batteryLevel = 0;
    public static String imei = "";
    private static Handler handler = new Handler();
    private static Runnable getResponceAfterInterval = new Runnable() {

        public void run() {
            final OsLocationDataListLocal osLocationDataListLocal = OsLocationDataListLocal.getInstance();
            if (osLocationDataListLocal != null) {
                List<OsLocationData> osLocationDataList = osLocationDataListLocal.getOsLocationDataList();
                if (osLocationDataList != null && osLocationDataList.size() > 0) {
                    OsLocationData[] osLocationDataArray = new OsLocationData[osLocationDataList.size()];
                    osLocationDataArray = osLocationDataList.toArray(osLocationDataArray);
                    final OsLocationData[] finalOsLocationDataArray = osLocationDataArray;
                    API_INTERFACE.sendUserPostions(osLocationDataArray).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e(TAG, "onLocationSendPoints: " + osLocationDataListLocal);
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

            final ModelCheckListLocal modelCheckListLocal = ModelCheckListLocal.getInstance();
            if (modelCheckListLocal != null) {
                List<ModelCheck> modelCheckList = modelCheckListLocal.getModelCheckList();
                if (modelCheckList != null && modelCheckList.size() > 0) {
                    for (final ModelCheck modelCheck : modelCheckList) {
                        Log.e(TAG, "onLocationSendModelCheckList size:" + modelCheckList.size());
                        if (modelCheck.isCheckin()) {
                            API_INTERFACE.putCheckin(modelCheck.getOsId(), modelCheck.getCodUser(), modelCheck.getLatitude(), modelCheck.getLongitude(),
                                    modelCheck.getCheckinDate()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    modelCheckListLocal.deleteModelCheckListLocal(modelCheck);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        } else {
                            API_INTERFACE.putCheckout(modelCheck.getOsId(), modelCheck.getCodUser(), modelCheck.getLatitude(), modelCheck.getLongitude(),
                                    modelCheck.getCheckoutDate()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    modelCheckListLocal.deleteModelCheckListLocal(modelCheck);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }
                    }
                }
            }

            handler.postDelayed(this, intervalSendPointsSeconds * 1000);

        }
    };

    public static HashMap<Integer, OsDistanceAndPoints> osDistanceHashMap = new HashMap<>();

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    };

    public static int intervalSendPointsSeconds = 60;

//private String appId = "DIWBEIDNHNWA64DD295FWD293QB3A5NDODOP5WI";
//producao
//private String server = "http://audiobookapi.kumon.com.br/use/";
// homologacao
//private String server = "http://audiobookapitst.kumon.com.br/use/";
//private String server = "http://192.168.1.24:1775/use/";

// endregion Members

// region Acessors

    public static Context getAppContext() {
        return appContext;
    }

// endRegion Acessors

// region Lifecycle Methods

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appContext = getApplicationContext();


        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();

        JodaTimeAndroid.init(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        get_hash_key();
        registerActivityLifecycleCallbacks(new AppLifeTracker());

        //startService(new Intent(this, LocationService.class));
        handler.removeCallbacks(getResponceAfterInterval);
        handler.post(getResponceAfterInterval);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void get_hash_key() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("valenet.com.br.gestordeos", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void onTerminate() {
        Log.e("no such an algorithm", "applicationOnTerminate");
        super.onTerminate();
        if (handler != null)
            handler.removeCallbacks(getResponceAfterInterval);
    }

    public static Realm getRealmInstance() {
        return realm;
    }

// endregion Lifecyle Methods

    // region Methods
//connect to the service
// endregion methods
    public class AppLifeTracker implements ActivityLifecycleCallbacks {

        private int numStarted = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (numStarted == 0) {
                Log.e("app", "App in forregorund ");
                if (LoginLocal.getInstance().getCurrentUser() != null)
                    providerLocation = new ProviderLocation();
//                        providerLocation.setListenerChanges(GestorDeOsApplication.this);
            }
            numStarted++;
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            numStarted--;
            if (numStarted == 0) {
                Log.e("app", "App in background ");
                if (providerLocation != null) {
                    providerLocation.destroyService();
                    providerLocation = null;
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

    }

    public static void setIntervalSendPoints(final Integer intervalSendPointsSecond) {
        intervalSendPointsSeconds = intervalSendPointsSecond;
        getResponceAfterInterval = new Runnable() {

            public void run() {
                final OsLocationDataListLocal osLocationDataListLocal = OsLocationDataListLocal.getInstance();
                if (osLocationDataListLocal != null) {
                    List<OsLocationData> osLocationDataList = osLocationDataListLocal.getOsLocationDataList();
                    if (osLocationDataList != null && osLocationDataList.size() > 0) {
                        OsLocationData[] osLocationDataArray = new OsLocationData[osLocationDataList.size()];
                        osLocationDataArray = osLocationDataList.toArray(osLocationDataArray);
                        API_INTERFACE.sendUserPostions(osLocationDataArray).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.e(TAG, "onLocationSendPoints: " + osLocationDataListLocal);
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

                final ModelCheckListLocal modelCheckListLocal = ModelCheckListLocal.getInstance();
                if (modelCheckListLocal != null) {
                    List<ModelCheck> modelCheckList = modelCheckListLocal.getModelCheckList();
                    if (modelCheckList != null && modelCheckList.size() > 0) {
                        Log.e(TAG, "onLocationSendModelCheckList size:" + modelCheckList.size());
                        for (final ModelCheck modelCheck : modelCheckList) {
                            if (modelCheck.isCheckin()) {
                                API_INTERFACE.putCheckin(modelCheck.getOsId(), modelCheck.getCodUser(), modelCheck.getLatitude(), modelCheck.getLongitude(),
                                        modelCheck.getCheckinDate()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        modelCheckListLocal.deleteModelCheckListLocal(modelCheck);
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            } else {
                                API_INTERFACE.putCheckout(modelCheck.getOsId(), modelCheck.getCodUser(), modelCheck.getLatitude(), modelCheck.getLongitude(),
                                        modelCheck.getCheckoutDate()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        modelCheckListLocal.deleteModelCheckListLocal(modelCheck);
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }
                }

                handler.postDelayed(this, intervalSendPointsSecond * 1000);
            }
        };
        handler.removeCallbacks(getResponceAfterInterval);
        handler.post(getResponceAfterInterval);
    }

    public static void instantiateNewHandler() {
        handler.removeCallbacks(getResponceAfterInterval);
        handler.post(getResponceAfterInterval);
    }

}
