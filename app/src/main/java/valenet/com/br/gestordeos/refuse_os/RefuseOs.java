package valenet.com.br.gestordeos.refuse_os;

public interface RefuseOs {
    interface RefuseOsView {

        void hideRefuseOsView();

        void showRefuseOsView();

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
