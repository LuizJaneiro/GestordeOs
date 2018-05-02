package valenet.com.br.gestordeos.os_list;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.os_filter.OsFilter;

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
    public void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser,
                                     Boolean isSearchingByCloseOs, Integer group, final OsList.OsListInteractor.onFinishedListener listener) {

        application.API_INTERFACE.getOsList(latitude, longitude, codUser, isSearchingByCloseOs,
                group).enqueue(new Callback<List<Os>>() {
            @Override
            public void onResponse(Call<List<Os>> call, Response<List<Os>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Os> osList = new ArrayList<>();
                    osList = response.body();
                    final List<Os> finalOsList = osList;
                    application.API_INTERFACE.getOsTypeList().enqueue(new Callback<List<OsTypeModel>>() {
                        @Override
                        public void onResponse(Call<List<OsTypeModel>> call, Response<List<OsTypeModel>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<OsTypeModel> osTypeModelList = new ArrayList<>();
                                osTypeModelList = response.body();
                                listener.successLoading(finalOsList, osTypeModelList);
                            } else {
                                listener.errorService("Ocorreu um problema no carregamento dos tipos de OS!");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OsTypeModel>> call, Throwable t) {
                            listener.errorNetwork();
                            Log.d("OsListInteractor", "error loading from API");
                        }
                    });
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
