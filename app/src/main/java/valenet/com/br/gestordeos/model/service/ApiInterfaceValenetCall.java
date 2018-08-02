package valenet.com.br.gestordeos.model.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterfaceValenetCall {
    @GET("ligacao?")
    Call<Boolean> getLigacao(@Query("nro_origem") Long numeroOrigem, @Query("nro_destino") Long numeroDestino);
}
