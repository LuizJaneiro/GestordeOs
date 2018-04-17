package valenet.com.br.gestordeos.os_list;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;

public interface OsList {

    interface OsListView {
        void navigateToSearch();

        void hideOsListView();

        void showOsListView();

        void hideLoading();

        void showLoading();

        void showListOs(List<Os> osListAdapter);
    }

    interface OsListPresenter {
        void loadOsList(Double latitude, Double longitude, Integer codUser,
                        Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);
    }

    interface OsListInteractor {
        void loadOsList(Double latitude, Double longitude, Integer codUser,
                               Boolean isSearchingByCloseOs, Integer group, final onFinishedListener listener);
        interface onFinishedListener {
            void successLoadingOsList(List<Os> osList);

            void errorService(String error);

            void errorNetwork();
        }
    }
}
