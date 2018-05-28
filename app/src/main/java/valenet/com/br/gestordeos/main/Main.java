package valenet.com.br.gestordeos.main;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

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

        void setOsDistance(Double osDistance, Os os);

        void showErrorMainService();

        void showErrorServerView(ArrayList<OsTypeModel> osTypeModels);

        void showErrorConnectionView(ArrayList<OsTypeModel> osTypeModels);
    }

    interface MainPresenter {
        void logout();

        void successLogout();

        void loadOsTypes();

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);

        void loadMainOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);

        void loadOsDistance(Double myLatitude, Double myLongitude, Os os);
    }

    interface MainInteractor {
        void logout();

        void loadOsTypes(onFinishedListenerOsTypes listenerOsTypes);

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        void loadMainOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        void loadOsDistance(Double myLatitude, Double myLongitude, Os os, onFinishedListenerOsDistance listener);

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }

        interface onFinishedListenerOsList{
            void successLoadingOsScheduleList(List<Os> osList);

            void successLoadingOsNextList(List<Os> osList);

            void errorServiceOsList(String error);

            void errorNetworkOsList();

            void errorMainServiceOsList();

            void errorMainNetworkOsList();
        }

        interface  onFinishedListenerOsDistance {
            void successLoadingOsDistance(Integer distance, Os os);

            void errorServiceOsDistance();

            void errorNetworkOsDistance();
        }
    }
}
