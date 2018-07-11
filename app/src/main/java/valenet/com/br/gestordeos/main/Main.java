package valenet.com.br.gestordeos.main;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.AppConfig;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;

public interface Main {
    interface MainView {
        void navigateToLogin();

        void navigateToSearch();

        void showLoading();

        void hideLoading();

        void showErrorServerView();

        void showErrorServerView(List<Os> osSchedule, List<Os> osNext);

        void hideErrorServerView();

        void showErrorConnectionView();

        void showErrorConnectionView(List<Os> osSchedule, List<Os> osNext);

        void hideErrorConnectionView();

        void showContainer();

        void hideContainer();

        void showPager();

        void hidePager();

        void showEmptyListView();

        void hideEmptyListView();

        void loadOsTypes(List<OsTypeModel> osList);

        void loadScheduleListOs(List<Os> osList);

        void loadNextListOs(List<Os> osList);

        void setOsDistance(OsDistanceAndPoints osDistanceAndPoints, Os os, boolean isLast);

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

        void loadOsDistance(Double myLatitude, Double myLongitude, Os os, boolean isLast);

        void getAppConfig();

        void sendUserPoint();
    }

    interface MainInteractor {
        void logout();

        void loadOsTypes(onFinishedListenerOsTypes listenerOsTypes);

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        void loadMainOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        void loadOsDistance(Double myLatitude, Double myLongitude, Os os, boolean isLast, onFinishedListenerOsDistance listener);

        void getAppConfig(onFinishedListenerAppConfig listener);

        void sendUserPoints();

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }

        interface onFinishedListenerOsList{
            void successLoadingOsScheduleList(List<Os> osList);

            void successLoadingOsNextList(List<Os> osList);

            void successLoadingMainOsScheduleList(List<Os> osList);

            void successLoadingMainOsNextList(List<Os> osList);

            void errorServiceOsList(String error);

            void errorNetworkOsList();

            void errorMainServiceOsList();

            void errorMainNetworkOsList();
        }

        interface  onFinishedListenerOsDistance {
            void successLoadingOsDistance(OsDistanceAndPoints osDistanceAndPoints, Os os, boolean isLast);

            void errorServiceOsDistance(OsDistanceAndPoints osDistanceAndPoints, Os os, boolean isLast);

            void errorNetworkOsDistance(OsDistanceAndPoints osDistanceAndPoints, Os os, boolean isLast);
        }

        interface onFinishedListenerAppConfig {
            void successLoadingAppConfig(List<AppConfig> appConfigs);

            void errorLoadingAppConfig();

            void errorInternetAppConfig();
        }
    }
}
