package valenet.com.br.gestordeos.map;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class MapsInteractorImp implements Maps.MapsInteractor {

    // region Members
    private Maps.MapsPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public MapsInteractorImp(Maps.MapsPresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods


    @Override
    public void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, final Integer group, final onFinishedListenerOsList listener) {
        application.API_INTERFACE.getOsList(latitude, longitude, codUser, true,
                group).enqueue(new Callback<List<OrdemDeServico>>() {
            @Override
            public void onResponse(Call<List<OrdemDeServico>> call, Response<List<OrdemDeServico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrdemDeServico> ordemDeServicoList = new ArrayList<>();
                    ordemDeServicoList = response.body();
                    final List<OrdemDeServico> finalOrdemDeServicoList = ordemDeServicoList;
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
                                            osTypeModelListReturn.add(model);
                                        }
                                    }
                                }

                                listener.successLoading(finalOrdemDeServicoList, osTypeModelListReturn);
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
            public void onFailure(Call<List<OrdemDeServico>> call, Throwable t) {
                listener.errorNetwork();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }

    @Override
    public void loadScheduleOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, final Integer group, final onFinishedListenerOsList listener) {
        application.API_INTERFACE.getOsList(latitude, longitude, codUser, false,
                group).enqueue(new Callback<List<OrdemDeServico>>() {
            @Override
            public void onResponse(Call<List<OrdemDeServico>> call, Response<List<OrdemDeServico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrdemDeServico> ordemDeServicoList = new ArrayList<>();
                    ordemDeServicoList = response.body();
                    final List<OrdemDeServico> finalOrdemDeServicoList = ordemDeServicoList;
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
                                            osTypeModelListReturn.add(model);
                                        }
                                    }
                                }

                                listener.successLoadingScheduleOsList(finalOrdemDeServicoList, osTypeModelListReturn);
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
            public void onFailure(Call<List<OrdemDeServico>> call, Throwable t) {
                listener.errorNetwork();
                Log.d("OsListInteractor", "error loading from API");
            }
        });
    }
}
