package valenet.com.br.gestordeos.refuse_os;

public interface RefuseOs {
    interface RefuseOsView {
        void navigateToSearch();

        void navigateToFilter();

        void hideOsListView();

        void showOsListView();

        void hideLoading();

        void showLoading();

        void showErrorConectionView();

        void hideErrorConectionView();

        void showErrorServerView();

        void hideErrorServerView();
    }

    interface RefuseOsPresenter {

    }

    interface RefuseOsInteractor {

    }
}
