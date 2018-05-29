package valenet.com.br.gestordeos.os_filter;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class OsFilterPresenterImp implements OsFilter.OsFilterPresenter, OsFilter.OsFilterInteractor.onFinishedListenerOsTypes {
    // region Members
    private OsFilter.OsFilterView view;
    private OsFilter.OsFilterInteractor interactor;

    // endregion Members

    // region constructors
    public OsFilterPresenterImp(OsFilter.OsFilterView view) {
        this.view = view;
        interactor = new OsFilterInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void loadOsTypes() {
        this.hideAllViews();
        view.showLoading();
        interactor.loadOsTypes(this);
    }

    @Override
    public void successLoadingOsTypes(List<OsTypeModel> osList) {
        this.hideAllViews();
        if(osList != null && osList.size() == 0){
            view.showEmptyListView();
        }else {
            view.loadOsTypesList(osList);
        }
    }

    @Override
    public void errorServiceOsTypes(String error) {
        this.hideAllViews();
        view.showErrorServerView();
    }

    @Override
    public void errorNetworkOsTypes() {
        this.hideAllViews();
        view.showErrorConectionView();
    }

    private void hideAllViews(){
        view.hideFilterView();
        view.hideEmptyListView();
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideLoading();
    }


    // endregion Methods
}
