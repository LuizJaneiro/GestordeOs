package valenet.com.br.gestordeos.model.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ApiValenet {

    private static Retrofit retrofit = null;
    private static Retrofit retrofitGoogleDistance = null;
    private static Retrofit retrofitApiValenetCall = null;

    public static Retrofit getConnection() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ValenetUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getConnectionGoogleDistance() {
        if (retrofitGoogleDistance == null) {
            retrofitGoogleDistance = new Retrofit.Builder()
                    .baseUrl(ValenetUtils.BASE_URL_GOOGLE_DISTANCE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitGoogleDistance;
    }

    public static Retrofit getConnectioApiValenetCall() {
        if (retrofitApiValenetCall == null) {
            retrofitApiValenetCall = new Retrofit.Builder()
                    .baseUrl(ValenetUtils.BASE_URL_API_VALENET_CALL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitApiValenetCall;
    }

}
