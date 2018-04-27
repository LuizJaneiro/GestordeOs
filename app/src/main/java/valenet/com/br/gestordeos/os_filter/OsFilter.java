package valenet.com.br.gestordeos.os_filter;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;

public class OsFilter {
    interface OsFilteriew {
        void hideFilterView();

        void showFilterView();

        void hideErrorConectionView();

        void showErrorConectionView();

        void hideErrorServerView();

        void showErrorServerView();

        void hideEmptyListView();

        void showEmptyListView();

        void hideLoading();

        void showLoading();

        void loadListOs(List<Os> list);
    }

    interface OsFilterPresenter {
        void loadOsList(Double latitude, Double longitude, Integer codUser,
                        Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);
    }

    interface OsFilterInteractor {
        void loadOsList(Double latitude, Double longitude, Integer codUser,
                        Boolean isSearchingByCloseOs, Integer group, final onFinishedListener listener);
        interface onFinishedListener {
            void successLoadingOsList(List<Os> osList);

            void errorService(String error);

            void errorNetwork();
        }
    }
}
