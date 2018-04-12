package valenet.com.br.gestordeos.model.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ApiValenet {

    private static Retrofit retrofit = null;

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
}
