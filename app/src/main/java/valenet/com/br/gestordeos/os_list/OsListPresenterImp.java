package valenet.com.br.gestordeos.os_list;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.os_type.OsType;

public class OsListPresenterImp implements OsList.OsListPresenter, OsList.OsListInteractor.onFinishedListenerOsList, OsList.OsListInteractor.onFinishedListenerOsTypes {
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
    public void loadOsTypes(Integer group) {
        view.hideOsListView();
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideEmptyListView();
        view.showLoading();
        interactor.loadOsTypes(group, this);
    }

    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh) {
        if(!isSwipeRefresh) {
            view.hideOsListView();
            view.hideErrorConectionView();
            view.hideErrorServerView();
            view.hideEmptyListView();
            view.showLoading();
        }
        interactor.loadOsListAndOsTypes(latitude, longitude, codUser, isSearchingByCloseOs, group, this);
    }

    @Override
    public void successLoadingOsList(List<Os> osList) {
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideEmptyListView();
        view.hideLoading();
        if(osList != null) {
            if(osList.size() == 0)
                view.showEmptyListView();
            else{
                view.showListOs(osList);
            }
        }
    }

    @Override
    public void errorServiceOsList(String error) {
        view.hideLoading();
        view.hideOsListView();
        view.hideEmptyListView();
        view.hideErrorConectionView();
        view.showErrorServerView();
    }

    @Override
    public void errorNetworkOsList() {
        view.hideLoading();
        view.hideOsListView();
        view.hideEmptyListView();
        view.hideErrorServerView();
        view.showErrorConectionView();
    }

    @Override
    public void successLoadingOsTypes(List<OsTypeModel> osList) {
        view.hideErrorConectionView();
        view.hideEmptyListView();
        view.hideLoading();
        if(osList != null) {
            if(osList.size() == 0)
                view.showErrorServerView();
            else{
                view.showListOsType(osList);
                view.showOsListView();
            }
        }
    }

    @Override
    public void errorServiceOsTypes(String error) {
        view.hideLoading();
        view.hideOsListView();
        view.hideEmptyListView();
        view.hideErrorConectionView();
        view.showErrorServerView();
    }

    @Override
    public void errorNetworkOsTypes() {
        view.hideLoading();
        view.hideOsListView();
        view.hideEmptyListView();
        view.hideErrorServerView();
        view.showErrorConectionView();
    }

    // endregion Methods
}
