package valenet.com.br.gestordeos.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.User;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;

public interface ApiInterface {
    @GET("OrdemDeServico/Autenticacao?")
    Call<User> getUser(@Query("userName") String userName, @Query("password") String password);

    @GET("OrdemDeServico?")
    Call<List<Os>> getOsList(@Query("lat") Double latitude, @Query("lng") Double longitude, @Query("coduser") Integer codUser,
                           @Query("proximidade") Boolean isSearchingByCloseOs, @Query("grupo") Integer group);

    @GET("TipoAtividade")
    Call<List<OsTypeModel>> getOsTypeList();

    @GET("OrdemDeServico/{codUser}/historico")
    Call<List<Os>> getUserHistory(@Path("codUser") Integer codUser);

    @POST("/localizacaoapp")
    Call<Integer> sendUserPostions(@Body OsLocationData[] posicoes);
}
