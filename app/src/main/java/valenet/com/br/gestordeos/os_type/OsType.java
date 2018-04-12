package valenet.com.br.gestordeos.os_type;

public interface OsType {
    interface OsTypeView {
        void navigateToOsList();

        void navigateToLogin();

        void showLoading();

        void hideOsListView();
    }

    interface OsTypePresenter {
        void logout();

        void successLogout();
    }

    interface OsTypeInteractor {
        void logout();
    }
}
