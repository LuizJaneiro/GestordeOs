package valenet.com.br.gestordeos.main;

import java.util.List;

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

        void loadOsTypes(List<OsTypeModel> osList);
    }

    interface MainPresenter {
        void logout();

        void successLogout();

        void loadOsTypes();
    }

    interface MainInteractor {
        void logout();

        void loadOsTypes(onFinishedListenerOsTypes listenerOsTypes);

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }
    }
}
