package valenet.com.br.gestordeos.application;

import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import valenet.com.br.gestordeos.R;

public class GestorDeOsApplication extends android.app.Application {

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
        //Fabric.with(this, new Crashlytics());
        appContext = getApplicationContext();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    // endregion Lifecyle Methods

    // region Methods
    //connect to the service
    // endregion methods


}
