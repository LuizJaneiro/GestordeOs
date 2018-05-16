package valenet.com.br.gestordeos.main;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class MainPresenterImp implements Main.MainPresenter, Main.MainInteractor.onFinishedListenerOsTypes {
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

    private void hideViews(){
        view.hideContainer();
        view.hideErrorConnectionView();
        view.hideLoading();
        view.hideErrorServerView();
        view.hidePager();
    }
    // endregion Methods
}
