package valenet.com.br.gestordeos.os_filter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class OsFilterInteractorImp implements OsFilter.OsFilterInteractor {
    // region Members
    private OsFilter.OsFilterPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public OsFilterInteractorImp(OsFilter.OsFilterPresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods

    @Override
    public void loadOsTypes(final onFinishedListenerOsTypes listenerOsTypes) {
        application.API_INTERFACE.getOsTypeList().enqueue(new Callback<List<OsTypeModel>>() {
            @Override
            public void onResponse(Call<List<OsTypeModel>> call, Response<List<OsTypeModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OsTypeModel> osTypeModelList = response.body();
                    listenerOsTypes.successLoadingOsTypes(osTypeModelList);
                } else {
                    listenerOsTypes.errorServiceOsTypes("Ocorreu um problema no carregamento dos tipos de OS!");
                }
            }

            @Override
            public void onFailure(Call<List<OsTypeModel>> call, Throwable t) {
                listenerOsTypes.errorNetworkOsTypes();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }
    // endRegion Methods
}
