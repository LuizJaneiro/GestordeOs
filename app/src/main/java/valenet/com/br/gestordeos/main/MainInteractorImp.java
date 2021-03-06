package valenet.com.br.gestordeos.main;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.AppConfig;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.Example;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.realm.ModelCheckListLocal;
import valenet.com.br.gestordeos.model.realm.OsListLocal;
import valenet.com.br.gestordeos.model.realm.OsLocationDataListLocal;

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
        OsListLocal.getInstance().deleteAllOsLocal();
        presenter.successLogout();
    }

    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, final Boolean isSearchingByCloseOs, Integer group, final onFinishedListenerOsList listener) {
        application.API_INTERFACE.getOsList(latitude, longitude, codUser, isSearchingByCloseOs,
                group).enqueue(new Callback<List<OrdemDeServico>>() {
            @Override
            public void onResponse(Call<List<OrdemDeServico>> call, Response<List<OrdemDeServico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrdemDeServico> ordemDeServicoList = response.body();
                    final List<OrdemDeServico> finalOrdemDeServicoList = ordemDeServicoList;
                    if (!isSearchingByCloseOs) {
                        OsListLocal osListLocal = OsListLocal.getInstance();
                        if (osListLocal != null) {
                            osListLocal.deleteScheduleOsListLocal();
                            osListLocal.saveOsScheduleListLocal(finalOrdemDeServicoList);
                        }
                        listener.successLoadingOsScheduleList(finalOrdemDeServicoList);
                    } else {
                        OsListLocal osListLocal = OsListLocal.getInstance();
                        if (osListLocal != null) {
                            osListLocal.deleteNextOsListLocal();
                            osListLocal.saveOsNextListLocal(finalOrdemDeServicoList);
                        }
                        listener.successLoadingOsNextList(finalOrdemDeServicoList);
                    }
                } else {
                    listener.errorServiceOsList("Ocorreu um problema no carregamento da lista de OS!");
                }
            }

            @Override
            public void onFailure(Call<List<OrdemDeServico>> call, Throwable t) {
                listener.errorNetworkOsList();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }

    @Override
    public void loadMainOsList(Double latitude, Double longitude, Integer codUser, final Boolean isSearchingByCloseOs, Integer group, final onFinishedListenerOsList listener) {
        application.API_INTERFACE.getOsList(latitude, longitude, codUser, isSearchingByCloseOs,
                group).enqueue(new Callback<List<OrdemDeServico>>() {
            @Override
            public void onResponse(Call<List<OrdemDeServico>> call, Response<List<OrdemDeServico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrdemDeServico> ordemDeServicoList = response.body();
                    final List<OrdemDeServico> finalOrdemDeServicoList = ordemDeServicoList;
                    if (!isSearchingByCloseOs) {
                        OsListLocal osListLocal = OsListLocal.getInstance();
                        if (osListLocal != null) {
                            osListLocal.deleteScheduleOsListLocal();
                            osListLocal.saveOsScheduleListLocal(finalOrdemDeServicoList);
                        }
                        listener.successLoadingMainOsScheduleList(finalOrdemDeServicoList);
                    } else {
                        OsListLocal osListLocal = OsListLocal.getInstance();
                        if (osListLocal != null) {
                            osListLocal.deleteNextOsListLocal();
                            osListLocal.saveOsNextListLocal(finalOrdemDeServicoList);
                        }
                        listener.successLoadingMainOsNextList(finalOrdemDeServicoList);
                    }
                } else {
                    listener.errorMainNetworkOsList();
                }
            }

            @Override
            public void onFailure(Call<List<OrdemDeServico>> call, Throwable t) {
                listener.errorMainNetworkOsList();
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
                    OsListLocal osListLocal = OsListLocal.getInstance();
                    if (osListLocal != null) {
                        osListLocal.deleteOsTypeListLocal();
                        osListLocal.saveOsTypeModelLocal(osTypeModelList);
                    }
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

    @Override
    public void loadOsDistance(Double myLatitude, Double myLongitude, final OrdemDeServico ordemDeServico, final boolean isFalse, final onFinishedListenerOsDistance listener) {
        if(myLatitude == null || myLongitude == null){
            listener.errorServiceOsDistance(null, ordemDeServico, isFalse);
            return;
        }
        application.API_INTERFACE_GOOGLE_DISTANCE.getDistanceDuration("metric", myLatitude + "," + myLongitude,
                ordemDeServico.getLatitude() + "," + ordemDeServico.getLongitude(), "driving").enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful()) {
                    if (response.body().getRoutes() != null && response.body().getRoutes().size() > 0) {
                        Integer distance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getValue();
                        String points = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        OsDistanceAndPoints osDistanceAndPoints = new OsDistanceAndPoints(distance, points);
                        listener.successLoadingOsDistance(osDistanceAndPoints, ordemDeServico, isFalse);
                    } else {
                        listener.errorServiceOsDistance(null, ordemDeServico, isFalse);
                    }
                } else {
                    listener.errorServiceOsDistance(null, ordemDeServico, isFalse);
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                listener.errorNetworkOsDistance(null, ordemDeServico, isFalse);
            }
        });
    }

    @Override
    public void sendUserPoints() {
        final OsLocationDataListLocal osLocationDataListLocal = OsLocationDataListLocal.getInstance();
        if(osLocationDataListLocal != null){
            List<OsLocationData> osLocationDataList = osLocationDataListLocal.getOsLocationDataList();
            if(osLocationDataList != null && osLocationDataList.size() > 0){
                OsLocationData[] osLocationDataArray = new OsLocationData[osLocationDataList.size()];
                osLocationDataArray = osLocationDataList.toArray(osLocationDataArray);
                final OsLocationData[] finalOsLocationDataArray = osLocationDataArray;
                application.API_INTERFACE.sendUserPostions(osLocationDataArray).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Integer qtdSendPoints = response.body();
                            if(qtdSendPoints == finalOsLocationDataArray.length){
                                osLocationDataListLocal.deleteOsLocationDataLists();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                    }
                });
            }
        }
    }

    @Override
    public void getAppConfig(final onFinishedListenerAppConfig listener) {
        application.API_INTERFACE.getAppConfigs().enqueue(new Callback<List<AppConfig>>() {
            @Override
            public void onResponse(Call<List<AppConfig>> call, Response<List<AppConfig>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<AppConfig> appConfigs = response.body();
                    if(appConfigs != null){
                        listener.successLoadingAppConfig(appConfigs);
                    }
                } else {
                    listener.errorLoadingAppConfig();
                }
            }

            @Override
            public void onFailure(Call<List<AppConfig>> call, Throwable t) {
                listener.errorInternetAppConfig();
            }
        });
    }

    // endRegion Methods
}
