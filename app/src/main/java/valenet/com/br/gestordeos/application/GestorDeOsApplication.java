package valenet.com.br.gestordeos.application;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.service.ApiInterface;
import valenet.com.br.gestordeos.model.service.ApiInterfaceGoogleDistance;
import valenet.com.br.gestordeos.model.service.ApiUtils;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import valenet.com.br.gestordeos.provider_location.ProviderLocation;

public class GestorDeOsApplication extends android.app.Application {

    public static final ApiInterface API_INTERFACE = ApiUtils.getService();
    public static final ApiInterfaceGoogleDistance API_INTERFACE_GOOGLE_DISTANCE = ApiUtils.getServiceGoogleDistance();
    public static Realm realm;
    public static final Locale myLocale = new Locale("pt", "BR");
    //private static FirebaseAnalytics mFirebaseAnalytics;
    ProviderLocation providerLocation;
    // region Members
    private static Context appContext;

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
        super.onTerminate();
    }

    public static Realm getRealmInstance(){
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

}
