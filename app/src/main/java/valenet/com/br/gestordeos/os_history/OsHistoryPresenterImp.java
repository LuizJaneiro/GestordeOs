package valenet.com.br.gestordeos.os_history;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.OrdemDeServico;

public class OsHistoryPresenterImp implements OsHistory.OsHistoryPresenter, OsHistory.OsHistoryInteractor.onFinishedLoadHistoryUser {
    // region Members
    private OsHistory.OsHistoryView view;
    private OsHistory.OsHistoryInteractor interactor;

    // endregion Members

    // region constructors
    public OsHistoryPresenterImp(OsHistory.OsHistoryView view) {
        this.view = view;
        interactor = new OsHistoryInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void loadHistoryUser(Integer coduser, boolean isSwipeRefresh) {
        if(!isSwipeRefresh){
            hideViews();
            view.showLoading();
        }

        interactor.loadHistoryUser(coduser, this);
    }

    @Override
    public void onSuccessLoadingHistory(List<OrdemDeServico> ordemDeServicoHistoryList) {
        hideViews();
        if(ordemDeServicoHistoryList != null) {
            if(ordemDeServicoHistoryList.size() == 0)
                view.showEmptyListView();
            else
                view.loadOsHistoryList(ordemDeServicoHistoryList);
        }
    }

    @Override
    public void onErrorServerLoadingHistory() {
        hideViews();
        view.showErrorServerView();
    }

    @Override
    public void onErrorConnectionLoadingHistory() {
        hideViews();
        view.showErrorConectionView();
    }

    private void hideViews() {
        view.hideLayoutHistory();
        view.hideErrorConectionView();
        view.hideLoading();
        view.hideErrorServerView();
        view.hideEmptyListView();
    }
    // endregion Methods
}
