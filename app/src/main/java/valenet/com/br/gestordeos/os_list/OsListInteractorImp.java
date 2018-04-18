package valenet.com.br.gestordeos.os_list;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.Os;

public class OsListInteractorImp implements OsList.OsListInteractor {
    // region Members
    private OsList.OsListPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public OsListInteractorImp(OsList.OsListPresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods

    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser,
                           Boolean isSearchingByCloseOs, Integer group, final onFinishedListener listener) {

        application.API_INTERFACE.getOsList(latitude, longitude, codUser, isSearchingByCloseOs,
                                            group).enqueue(new Callback<List<Os>>() {
            @Override
            public void onResponse(Call<List<Os>> call, Response<List<Os>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Os> osList = new ArrayList<>();
                    osList = response.body();
                    listener.successLoadingOsList(osList);
                } else {
                    listener.errorService("Ocorreu um problema no carregamento da lista de OS!");
                }
            }

            @Override
            public void onFailure(Call<List<Os>> call, Throwable t) {
                listener.errorNetwork();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }

    // endRegion Methods
}
