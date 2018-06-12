package valenet.com.br.gestordeos.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;

public interface ApiMarceloInterface {
    @POST("/localizacaoapp")
    Call<OsLocationData[]> enviarPosicoes(@Body OsLocationData[] posicoes);
}
