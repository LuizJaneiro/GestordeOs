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
import valenet.com.br.gestordeos.utils.ValenetUtils;

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
                                     Boolean isSearchingByCloseOs, Integer group, final OsList.OsListInteractor.onFinishedListenerOsList listener) {

        application.API_INTERFACE.getOsList(latitude, longitude, codUser, isSearchingByCloseOs,
                group).enqueue(new Callback<List<Os>>() {
            @Override
            public void onResponse(Call<List<Os>> call, Response<List<Os>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Os> osList = new ArrayList<>();
                    osList = response.body();
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
    public void loadOsTypes(final Integer group, final onFinishedListenerOsTypes listener) {
        application.API_INTERFACE.getOsTypeList().enqueue(new Callback<List<OsTypeModel>>() {
            @Override
            public void onResponse(Call<List<OsTypeModel>> call, Response<List<OsTypeModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OsTypeModel> osTypeModelList = new ArrayList<>();
                    osTypeModelList = response.body();

                    List<OsTypeModel> osTypeModelListReturn = new ArrayList<>();
                    if (osTypeModelList != null && osTypeModelList.size() > 0) {
                        for (OsTypeModel model : osTypeModelList) {
                            if (group == ValenetUtils.GROUP_OS_MERCANTIL && model.getTipoMercantil()) {
                                osTypeModelListReturn.add(model);
                            } else if (group == ValenetUtils.GROUP_OS_CORRETIVA && !model.getTipoMercantil()) {
                                osTypeModelList.add(model);
                            }
                        }
                    }

                    listener.successLoadingOsTypes(osTypeModelListReturn);
                } else {
                    listener.errorServiceOsTypes("Ocorreu um problema no carregamento dos tipos de OS!");
                }
            }

            @Override
            public void onFailure(Call<List<OsTypeModel>> call, Throwable t) {
                listener.errorNetworkOsTypes();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }

    // endRegion Methods
}
