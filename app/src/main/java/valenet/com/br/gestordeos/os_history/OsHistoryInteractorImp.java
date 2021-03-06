package valenet.com.br.gestordeos.os_history;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;

public class OsHistoryInteractorImp implements OsHistory.OsHistoryInteractor {
    // region Members
    private OsHistory.OsHistoryPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public OsHistoryInteractorImp(OsHistory.OsHistoryPresenter presenter) {
        this.presenter = presenter;
    }

    // endregion constructors

    // region Methods

    @Override
    public void loadHistoryUser(Integer coduser, final onFinishedLoadHistoryUser listener) {
        application.API_INTERFACE.getUserHistory(coduser, 1).enqueue(new Callback<List<OrdemDeServico>>() {
            @Override
            public void onResponse(Call<List<OrdemDeServico>> call, Response<List<OrdemDeServico>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<OrdemDeServico> historyList = response.body();
                    listener.onSuccessLoadingHistory(historyList);
                } else {
                    listener.onErrorServerLoadingHistory();
                }
            }

            @Override
            public void onFailure(Call<List<OrdemDeServico>> call, Throwable t) {
                listener.onErrorConnectionLoadingHistory();
            }
        });
    }


    // endRegion Methods
}
