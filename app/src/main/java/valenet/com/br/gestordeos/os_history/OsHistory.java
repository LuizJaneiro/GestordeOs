package valenet.com.br.gestordeos.os_history;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.OrdemDeServico;

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

        void loadOsHistoryList(List<OrdemDeServico> ordemDeServicoHistoryList);
    }

    interface OsHistoryPresenter {
        void loadHistoryUser(Integer coduser, boolean isSwipeRefresh);
    }

    interface OsHistoryInteractor {
        void loadHistoryUser(Integer coduser, onFinishedLoadHistoryUser listener);

        interface onFinishedLoadHistoryUser {
            void onSuccessLoadingHistory(List<OrdemDeServico> ordemDeServicoHistoryList);

            void onErrorServerLoadingHistory();

            void onErrorConnectionLoadingHistory();
        }
    }
}
