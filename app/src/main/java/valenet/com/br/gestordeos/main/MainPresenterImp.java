package valenet.com.br.gestordeos.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.realm.OsListLocal;

public class MainPresenterImp implements Main.MainPresenter, Main.MainInteractor.onFinishedListenerOsTypes, Main.MainInteractor.onFinishedListenerOsList,
        Main.MainInteractor.onFinishedListenerOsDistance {
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
        if (!isSwipeRefresh) {
            hideViews();
            view.showLoading();
        }
        interactor.loadOsList(latitude, longitude, codUser, isSearchingByCloseOs, group, this);
    }

    @Override
    public void loadMainOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh) {
        if (!isSwipeRefresh) {
            hideViews();
            view.showLoading();
        }
        interactor.loadMainOsList(latitude, longitude, codUser, isSearchingByCloseOs, group, this);
    }

    @Override
    public void loadOsDistance(Double myLatitude, Double myLongitude, Os os) {
        interactor.loadOsDistance(myLatitude, myLongitude, os, this);
    }

    @Override
    public void successLogout() {
        view.navigateToLogin();
    }

    @Override
    public void successLoadingOsTypes(List<OsTypeModel> osList) {
        hideViews();
        if (osList != null) {
            if (osList.size() == 0)
                view.showErrorServerView();
            else {
                view.loadOsTypes(osList);
            }
        }
    }

    @Override
    public void errorServiceOsTypes(String error) {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if(osListLocal == null)
            view.showErrorServerView();
        else {
            ArrayList<OsTypeModel> osTypeModels = (ArrayList) osListLocal.getOsTypeModelList();
            view.showErrorServerView(osTypeModels);
        }
    }

    @Override
    public void errorNetworkOsTypes() {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if(osListLocal == null)
            view.showErrorConnectionView();
        else {
            ArrayList<OsTypeModel> osTypeModels = (ArrayList) osListLocal.getOsTypeModelList();
            view.showErrorConnectionView(osTypeModels);
        }
    }

    @Override
    public void successLoadingOsScheduleList(List<Os> osList) {
        hideViews();
        if (osList != null) {
            if (osList.size() == 0)
                view.showEmptyListView();
            else {
                view.loadScheduleListOs(osList);
            }
        }
    }

    @Override
    public void successLoadingOsNextList(List<Os> osList) {
        hideViews();
        if (osList != null) {
            if (osList.size() == 0)
                view.showEmptyListView();
            else {
                view.loadNextListOs(osList);
            }
        }
    }

    @Override
    public void errorServiceOsList(String error) {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if(osListLocal == null)
            view.showErrorServerView();
        else{
            ArrayList<Os> scheduleOsList = (ArrayList) osListLocal.getScheduleOsList();
            ArrayList<Os> nextOsList = (ArrayList) osListLocal.getNextOsList();
            view.showErrorServerView(scheduleOsList, nextOsList);
        }
    }

    @Override
    public void errorNetworkOsList() {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if(osListLocal == null)
            view.showErrorConnectionView();
        else{
            ArrayList<Os> scheduleOsList = (ArrayList) osListLocal.getScheduleOsList();
            ArrayList<Os> nextOsList = (ArrayList) osListLocal.getNextOsList();
            view.showErrorConnectionView(scheduleOsList, nextOsList);
        }
    }

    @Override
    public void successLoadingOsDistance(Integer distance, Os os) {
        view.setOsDistance(distance.doubleValue(), os);
    }

    @Override
    public void errorMainServiceOsList() {
        hideViews();
        view.showErrorMainService();
    }

    @Override
    public void errorMainNetworkOsList() {
        hideViews();
        view.showErrorMainService();
    }

    @Override
    public void errorServiceOsDistance() {

    }

    @Override
    public void errorNetworkOsDistance() {

    }

    private void hideViews() {
        view.hideContainer();
        view.hideErrorConnectionView();
        view.hideLoading();
        view.hideErrorServerView();
        view.hidePager();
        view.hideEmptyListView();
    }
    // endregion Methods
}
