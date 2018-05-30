package valenet.com.br.gestordeos.os_history;

public interface OsHistory {
    interface OsHistoryView {
        void showLayoutHistory();

        void hideLayoutHistory();

        void showLoading();

        void hideLoading();

        void showErrorServerView();

        void hideErrorServerView();

        void showErrorConectionView();

        void hideErrorConectionView();

        void showEmptyListView();

        void hideEmptyListView();
    }

    interface OsHistoryPresenter {

    }

    interface OsHistoryInteractor {

    }
}
