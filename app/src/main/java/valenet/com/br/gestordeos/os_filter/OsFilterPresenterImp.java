package valenet.com.br.gestordeos.os_filter;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;

public class OsFilterPresenterImp implements OsFilter.OsFilterPresenter, OsFilter.OsFilterInteractor.onFinishedListener {
    // region Members
    private OsFilter.OsFilteriew view;
    private OsFilter.OsFilterInteractor interactor;

    // endregion Members

    // region constructors
    public OsFilterPresenterImp(OsFilter.OsFilteriew view) {
        this.view = view;
        interactor = new OsFilterInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh) {
        if(!isSwipeRefresh) {
            view.hideFilterView();
            view.hideErrorConectionView();
            view.hideErrorServerView();
            view.hideEmptyListView();
            view.showLoading();
        }
        interactor.loadOsList(latitude, longitude, codUser, isSearchingByCloseOs, group, this);
    }

    @Override
    public void successLoadingOsList(List<Os> osList) {
        view.hideErrorConectionView();
        view.hideEmptyListView();
        view.hideLoading();
        if(osList != null) {
            if(osList.size() == 0)
                view.showEmptyListView();
            else{
                view.loadListOs(osList);
                view.showFilterView();
            }
        }
    }

    @Override
    public void errorService(String error) {
        view.hideLoading();
        view.hideFilterView();
        view.hideEmptyListView();
        view.hideErrorConectionView();
        view.showErrorServerView();
    }

    @Override
    public void errorNetwork() {
        view.hideLoading();
        view.hideFilterView();
        view.hideEmptyListView();
        view.hideErrorServerView();
        view.showErrorConectionView();
    }


    // endregion Methods
}
