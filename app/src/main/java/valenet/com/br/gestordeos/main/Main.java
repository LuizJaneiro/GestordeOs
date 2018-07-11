package valenet.com.br.gestordeos.main;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.AppConfig;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;

public interface Main {
    interface MainView {
        void navigateToLogin();

        void navigateToSearch();

        void showLoading();

        void hideLoading();

        void showErrorServerView();

        void showErrorServerView(List<OrdemDeServico> ordemDeServicoSchedule, List<OrdemDeServico> ordemDeServicoNext);

        void hideErrorServerView();

        void showErrorConnectionView();

        void showErrorConnectionView(List<OrdemDeServico> ordemDeServicoSchedule, List<OrdemDeServico> ordemDeServicoNext);

        void hideErrorConnectionView();

        void showContainer();

        void hideContainer();

        void showPager();

        void hidePager();

        void showEmptyListView();

        void hideEmptyListView();

        void loadOsTypes(List<OsTypeModel> osList);

        void loadScheduleListOs(List<OrdemDeServico> ordemDeServicoList);

        void loadNextListOs(List<OrdemDeServico> ordemDeServicoList);

        void setOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast);

        void showErrorMainService();

        void showErrorServerView(ArrayList<OsTypeModel> osTypeModels);

        void showErrorConnectionView(ArrayList<OsTypeModel> osTypeModels);

        void loadAppConfig(List<AppConfig> appConfigs);
    }

    interface MainPresenter {
        void logout();

        void successLogout();

        void loadOsTypes();

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);

        void loadMainOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);

        void loadOsDistance(Double myLatitude, Double myLongitude, OrdemDeServico ordemDeServico, boolean isLast);

        void getAppConfig();

        void sendUserPoint();
    }

    interface MainInteractor {
        void logout();

        void loadOsTypes(onFinishedListenerOsTypes listenerOsTypes);

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        void loadMainOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        void loadOsDistance(Double myLatitude, Double myLongitude, OrdemDeServico ordemDeServico, boolean isLast, onFinishedListenerOsDistance listener);

        void getAppConfig(onFinishedListenerAppConfig listener);

        void sendUserPoints();

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }

        interface onFinishedListenerOsList{
            void successLoadingOsScheduleList(List<OrdemDeServico> ordemDeServicoList);

            void successLoadingOsNextList(List<OrdemDeServico> ordemDeServicoList);

            void successLoadingMainOsScheduleList(List<OrdemDeServico> ordemDeServicoList);

            void successLoadingMainOsNextList(List<OrdemDeServico> ordemDeServicoList);

            void errorServiceOsList(String error);

            void errorNetworkOsList();

            void errorMainServiceOsList();

            void errorMainNetworkOsList();
        }

        interface  onFinishedListenerOsDistance {
            void successLoadingOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast);

            void errorServiceOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast);

            void errorNetworkOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast);
        }

        interface onFinishedListenerAppConfig {
            void successLoadingAppConfig(List<AppConfig> appConfigs);

            void errorLoadingAppConfig();

            void errorInternetAppConfig();
        }
    }
}
