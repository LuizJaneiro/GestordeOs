package valenet.com.br.gestordeos.model.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ApiValenet {

    private static Retrofit retrofit = null;
    private static Retrofit retrofitGoogleDistance = null;
    private static Retrofit retrofitApiMarcelo = null;

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

    public static Retrofit getConnectionApiMarcelo() {
        if (retrofitApiMarcelo == null) {
            retrofitApiMarcelo = new Retrofit.Builder()
                    .baseUrl("http://172.31.31.119/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitApiMarcelo;
    }
}
