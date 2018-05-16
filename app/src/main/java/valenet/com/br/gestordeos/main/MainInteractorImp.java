package valenet.com.br.gestordeos.main;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class MainInteractorImp implements Main.MainInteractor {
    // region Members
    private Main.MainPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public MainInteractorImp(Main.MainPresenter presenter) {
        this.presenter = presenter;
    }

    // endregion constructors

    // region Methods

    @Override
    public void logout() {
        LoginLocal.getInstance().deleteAllUsers();
        presenter.successLogout();
    }

    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, final onFinishedListenerOsList listener) {
        application.API_INTERFACE.getOsList(latitude, longitude, codUser, isSearchingByCloseOs,
                1).enqueue(new Callback<List<Os>>() {
            @Override
            public void onResponse(Call<List<Os>> call, Response<List<Os>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Os> osList = response.body();
                    final List<Os> finalOsList = osList;
                    listener.successLoadingOsList(finalOsList);
                } else {
                    listener.errorServiceOsList("Ocorreu um problema no carregamento da lista de OS!");
                }
            }

            @Override
            public void onFailure(Call<List<Os>> call, Throwable t) {
                listener.errorNetworkOsList();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }

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
