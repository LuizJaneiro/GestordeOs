package valenet.com.br.gestordeos.model.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import valenet.com.br.gestordeos.model.entity.User;

public interface ApiInterface {
    @GET("OrdemDeServico?")
    Call<User> getUser(@Query("userName") String userName, @Query("password") String password);

}
