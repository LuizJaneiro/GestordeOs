package valenet.com.br.gestordeos.main;

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

        void hideErrorServerView();

        void showErrorConnectionView();

        void hideErrorConnectionView();

        void showContainer();

        void hideContainer();

        void showPager();

        void hidePager();

        void showEmptyListView();

        void hideEmptyListView();

        void loadOsTypes(List<OsTypeModel> osList);

        void loadListOs(List<Os> osList);
    }

    interface MainPresenter {
        void logout();

        void successLogout();

        void loadOsTypes();

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);
    }

    interface MainInteractor {
        void logout();

        void loadOsTypes(onFinishedListenerOsTypes listenerOsTypes);

        void loadOsList(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, onFinishedListenerOsList listener);

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }

        interface onFinishedListenerOsList{
            void successLoadingOsList(List<Os> osList);

            void errorServiceOsList(String error);

            void errorNetworkOsList();
        }
    }
}
