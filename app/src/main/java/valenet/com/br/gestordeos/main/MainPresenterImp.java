package valenet.com.br.gestordeos.main;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class MainPresenterImp implements Main.MainPresenter, Main.MainInteractor.onFinishedListenerOsTypes, Main.MainInteractor.onFinishedListenerOsList {
    // region Members
    private Main.MainView view;
    private Main.MainInteractor interactor;

    // endregion Members

    // region constructors
    public MainPresenterImp(Main.MainView view) {
        this.view = view;
        interactor = new MainInteractorImp(this);
    }

    // endregion constructors

    // region Methods

    @Override
    public void logout() {
        hideViews();
        view.showLoading();

        interactor.logout();
    }

    @Override
    public void loadOsTypes() {
        hideViews();
        view.showLoading();
        interactor.loadOsTypes(this);
    }

    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh) {
        if(!isSwipeRefresh) {
            hideViews();
            view.showLoading();
        }
        interactor.loadOsList(latitude, longitude, codUser, isSearchingByCloseOs, group, this);
    }

    @Override
    public void successLogout() {
        view.navigateToLogin();
    }

    @Override
    public void successLoadingOsTypes(List<OsTypeModel> osList) {
        hideViews();
        if(osList != null) {
            if(osList.size() == 0)
                view.showErrorServerView();
            else{
                view.loadOsTypes(osList);
                view.showPager();
            }
        }
    }

    @Override
    public void errorServiceOsTypes(String error) {
        hideViews();
        view.showErrorServerView();
    }

    @Override
    public void errorNetworkOsTypes() {
        hideViews();
        view.showErrorConnectionView();
    }

    @Override
    public void successLoadingOsList(List<Os> osList) {
        hideViews();
        if(osList != null) {
            if(osList.size() == 0)
                view.showEmptyListView();
            else{
                view.loadListOs(osList);
            }
        }
    }

    @Override
    public void errorServiceOsList(String error) {
        hideViews();
        view.showErrorServerView();
    }

    @Override
    public void errorNetworkOsList() {
        hideViews();
        view.showErrorConnectionView();
    }

    private void hideViews(){
        view.hideContainer();
        view.hideErrorConnectionView();
        view.hideLoading();
        view.hideErrorServerView();
        view.hidePager();
        view.hideEmptyListView();
    }
    // endregion Methods
}
