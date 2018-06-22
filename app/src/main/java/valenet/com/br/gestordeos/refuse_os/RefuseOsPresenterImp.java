package valenet.com.br.gestordeos.refuse_os;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.ReasonRefuseOs;

public class RefuseOsPresenterImp implements RefuseOs.RefuseOsPresenter, RefuseOs.RefuseOsInteractor.onFinishedGetReasonsToRefuseOs,
                                            RefuseOs.RefuseOsInteractor.onFinishedPutRefuseOs{
    // region Members
    private RefuseOs.RefuseOsView view;
    private RefuseOs.RefuseOsInteractor interactor;

    // endregion Members

    // region constructors
    public RefuseOsPresenterImp(RefuseOs.RefuseOsView view) {
        this.view = view;
        interactor = new RefuseOsInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void getReasonsToRefuseOs() {
        hideViews();
        view.showLoading();
        interactor.getReasonsToRefuseOs(this);
    }

    @Override
    public void putRefuseOs(Integer agendamentoId, Integer motcanId, String motcanTx) {
        hideViews();
        view.showLoading();
        interactor.putRefuseOs(agendamentoId, motcanId, motcanTx, this);
    }

    @Override
    public void onSuccessGetReasonsToRefuseOs(List<ReasonRefuseOs> reasonRefuseOsList) {
        hideViews();
        if(reasonRefuseOsList != null){
            view.loadReasonsRefuseOs(reasonRefuseOsList);
        }
    }

    @Override
    public void onErrorConectionGetReasonsToRefuseOs() {
        hideViews();
        view.showErrorConectionView();
    }

    @Override
    public void onErrorServerGetReasonsToRefuseOs() {
        hideViews();
        view.showErrorServerView();
    }

    @Override
    public void onSuccessPutRefuseOs() {
        view.showSuccessRefuseOs();
    }

    @Override
    public void onErrorPutRefuseOs() {
        hideViews();
        view.showRefuseOsView();
        view.showErrorRefuseOs();
    }


    void hideViews(){
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideLoading();
        view.hideRefuseOsView();
    }

    // endregion Methods
}
