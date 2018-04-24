package valenet.com.br.gestordeos.os_list;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;

public class OsListPresenterImp implements OsList.OsListPresenter, OsList.OsListInteractor.onFinishedListener {
    // region Members
    private OsList.OsListView view;
    private OsList.OsListInteractor interactor;

    // endregion Members

    // region constructors
    public OsListPresenterImp(OsList.OsListView view) {
        this.view = view;
        interactor = new OsListInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh) {
        if(!isSwipeRefresh) {
            view.hideOsListView();
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
                view.showListOs(osList);
                view.showOsListView();
            }
        }
    }

    @Override
    public void errorService(String error) {
        view.hideLoading();
        view.hideOsListView();
        view.hideEmptyListView();
        view.hideErrorConectionView();
        view.showErrorServerView();
    }

    @Override
    public void errorNetwork() {
        view.hideLoading();
        view.hideOsListView();
        view.hideEmptyListView();
        view.hideErrorServerView();
        view.showErrorConectionView();
    }


    // endregion Methods
}
