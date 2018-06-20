package valenet.com.br.gestordeos.refuse_os;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.ReasonRefuseOs;

public class RefuseOsInteractorImp implements RefuseOs.RefuseOsInteractor {
    // region Members
    private RefuseOs.RefuseOsPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public RefuseOsInteractorImp(RefuseOs.RefuseOsPresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods

    @Override
    public void getReasonsToRefuseOs(final onFinishedGetReasonsToRefuseOs listener) {
        application.API_INTERFACE.getReasonsToRefuseOs().enqueue(new Callback<List<ReasonRefuseOs>>() {
            @Override
            public void onResponse(Call<List<ReasonRefuseOs>> call, Response<List<ReasonRefuseOs>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReasonRefuseOs> reasonRefuseOsList = response.body();
                    listener.onSuccessGetReasonsToRefuseOs(reasonRefuseOsList);
                } else {
                    listener.onErrorServerGetReasonsToRefuseOs();
                }
            }

            @Override
            public void onFailure(Call<List<ReasonRefuseOs>> call, Throwable t) {
                listener.onErrorConectionGetReasonsToRefuseOs();
            }
        });
    }

    @Override
    public void putRefuseOs(Integer agendamentoId, Integer motcanId, String motcanTx, final onFinishedPutRefuseOs listener) {
        application.API_INTERFACE.putRefuseOs(agendamentoId, motcanId, motcanTx).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    listener.onSuccessPutRefuseOs();
                else
                    listener.onErrorPutRefuseOs();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onErrorPutRefuseOs();
            }
        });
    }

    // endRegion Methods
}
