package valenet.com.br.gestordeos.model.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import valenet.com.br.gestordeos.model.entity.google_distance.Example;

public interface ApiInterfaceGoogleDistance {

    @GET("api/directions/json?key=AIzaSyB5IDebWknzOpcX7FwPKgqZIh1DjHnTNNI")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}
