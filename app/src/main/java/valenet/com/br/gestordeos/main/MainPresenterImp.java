package valenet.com.br.gestordeos.main;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.AppConfig;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.model.realm.OsListLocal;

public class MainPresenterImp implements Main.MainPresenter, Main.MainInteractor.onFinishedListenerOsTypes, Main.MainInteractor.onFinishedListenerOsList,
        Main.MainInteractor.onFinishedListenerOsDistance, Main.MainInteractor.onFinishedListenerAppConfig {
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
    public void loadOsDistance(Double myLatitude, Double myLongitude, OrdemDeServico ordemDeServico, boolean isLast) {
        view.showLoading();
        interactor.loadOsDistance(myLatitude, myLongitude, ordemDeServico, isLast, this);
    }

    @Override
    public void getAppConfig() {
        hideViews();
        view.showLoading();
        interactor.getAppConfig(this);
    }

    @Override
    public void successLoadingAppConfig(List<AppConfig> appConfigs) {
        if(appConfigs != null) {
            if(appConfigs.size() == 0) {
                hideViews();
                view.showErrorServerView();
            } else {
                view.loadAppConfig(appConfigs);
            }
        }
    }

    @Override
    public void errorLoadingAppConfig() {
        hideViews();
        view.showErrorServerView();
    }

    @Override
    public void errorInternetAppConfig() {
        hideViews();
        view.showErrorServerView();
    }

    @Override
    public void successLogout() {
        view.navigateToLogin();
    }

    @Override
    public void successLoadingOsTypes(List<OsTypeModel> osList) {
        if (osList != null) {
            if (osList.size() == 0) {
                hideViews();
                view.showErrorServerView();
            } else {
                view.loadOsTypes(osList);
            }
        }
    }

    @Override
    public void errorServiceOsTypes(String error) {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if (osListLocal == null)
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
        if (osListLocal == null)
            view.showErrorConnectionView();
        else {
            ArrayList<OsTypeModel> osTypeModels = (ArrayList) osListLocal.getOsTypeModelList();
            view.showErrorConnectionView(osTypeModels);
        }
    }

    @Override
    public void successLoadingOsScheduleList(List<OrdemDeServico> ordemDeServicoList) {
        hideViews();
        if (ordemDeServicoList != null) {
            if (ordemDeServicoList.size() == 0)
                view.showEmptyListView();
            else {
                view.loadScheduleListOs(ordemDeServicoList);
            }
        }
    }

    @Override
    public void successLoadingOsNextList(List<OrdemDeServico> ordemDeServicoList) {
        hideViews();
        if (ordemDeServicoList != null) {
            if (ordemDeServicoList.size() == 0)
                view.showEmptyListView();
            else {
                view.loadNextListOs(ordemDeServicoList);
            }
        }
    }

    @Override
    public void successLoadingMainOsScheduleList(List<OrdemDeServico> ordemDeServicoList) {
        if (ordemDeServicoList != null) {
            view.loadScheduleListOs(ordemDeServicoList);
        }
    }

    @Override
    public void successLoadingMainOsNextList(List<OrdemDeServico> ordemDeServicoList) {
        if (ordemDeServicoList != null)
            view.loadNextListOs(ordemDeServicoList);
    }

    @Override
    public void errorServiceOsList(String error) {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if (osListLocal == null)
            view.showErrorServerView();
        else {
            ArrayList<OrdemDeServico> scheduleOrdemDeServicoList = (ArrayList) osListLocal.getScheduleOsList();
            ArrayList<OrdemDeServico> nextOrdemDeServicoList = (ArrayList) osListLocal.getNextOsList();
            view.showErrorServerView(scheduleOrdemDeServicoList, nextOrdemDeServicoList);
        }
    }

    @Override
    public void errorNetworkOsList() {
        hideViews();
        OsListLocal osListLocal = OsListLocal.getInstance();
        if (osListLocal == null)
            view.showErrorConnectionView();
        else {
            ArrayList<OrdemDeServico> scheduleOrdemDeServicoList = (ArrayList) osListLocal.getScheduleOsList();
            ArrayList<OrdemDeServico> nextOrdemDeServicoList = (ArrayList) osListLocal.getNextOsList();
            view.showErrorConnectionView(scheduleOrdemDeServicoList, nextOrdemDeServicoList);
        }
    }

    @Override
    public void successLoadingOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast) {
        if (isLast)
            hideViews();
        view.setOsDistance(osDistanceAndPoints, ordemDeServico, isLast);
    }

    @Override
    public void errorServiceOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast) {
        if (isLast)
            hideViews();
        view.setOsDistance(osDistanceAndPoints, ordemDeServico, isLast);
    }

    @Override
    public void errorNetworkOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast) {
        if (isLast)
            hideViews();
        view.setOsDistance(osDistanceAndPoints, ordemDeServico, isLast);
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
    public void sendUserPoint() {
        interactor.sendUserPoints();
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
