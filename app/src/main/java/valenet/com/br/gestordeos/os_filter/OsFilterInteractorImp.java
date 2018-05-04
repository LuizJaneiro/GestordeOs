package valenet.com.br.gestordeos.os_filter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.os_list.OsList;

public class OsFilterInteractorImp implements OsFilter.OsFilterInteractor {
    // region Members
    private OsFilter.OsFilterPresenter presenter;
    private GestorDeOsApplication application;
    private List<Os> nextOsList;
    private List<Os> scheduleOsList;
    private List<OsTypeModel> osTypeModelList;
    // endregion Members

    // region constructors
    public OsFilterInteractorImp(OsFilter.OsFilterPresenter presenter) {
        this.presenter = presenter;
        this.nextOsList = null;
        this.scheduleOsList = null;
    }
    // endregion constructors

    // region Methods


    @Override
    public void loadOsListAndOsTypes(final Double latitude, final Double longitude, final Integer codUser, final Integer group, final boolean loadNextOsList, final boolean loadScheduleOsList, final boolean loadOsTypes, final onFinishedListener listener) {
        if(loadNextOsList || loadScheduleOsList || loadOsTypes){
            application.API_INTERFACE.getOsList(latitude, longitude, codUser, true,
                    group).enqueue(new Callback<List<Os>>() {
                @Override
                public void onResponse(Call<List<Os>> call, Response<List<Os>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Os> nextOsList;
                        nextOsList = response.body();
                        setNextOsList(nextOsList);
                        application.API_INTERFACE.getOsList(latitude, longitude, codUser, false,
                                group).enqueue(new Callback<List<Os>>() {
                            @Override
                            public void onResponse(Call<List<Os>> call, Response<List<Os>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    List<Os> scheduleOsList;
                                    scheduleOsList = response.body();
                                    setScheduleOsList(scheduleOsList);
                                    application.API_INTERFACE.getOsTypeList().enqueue(new Callback<List<OsTypeModel>>() {
                                        @Override
                                        public void onResponse(Call<List<OsTypeModel>> call, Response<List<OsTypeModel>> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                List<OsTypeModel> osTypeModelList;
                                                osTypeModelList = response.body();
                                                setOsTypeModelList(osTypeModelList);
                                                listener.successLoading(getNextOsList(), getScheduleOsList(), getOsTypeModelList(), loadNextOsList, loadScheduleOsList, loadOsTypes);
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
    }

    private List<Os> getNextOsList() {
        return nextOsList;
    }

    private void setNextOsList(List<Os> nextOsList) {
        this.nextOsList = nextOsList;
    }

    private List<Os> getScheduleOsList() {
        return scheduleOsList;
    }

    private void setScheduleOsList(List<Os> scheduleOsList) {
        this.scheduleOsList = scheduleOsList;
    }

    private List<OsTypeModel> getOsTypeModelList() {
        return osTypeModelList;
    }

    private void setOsTypeModelList(List<OsTypeModel> osTypeModelList) {
        this.osTypeModelList = osTypeModelList;
    }

    // endRegion Methods
}
