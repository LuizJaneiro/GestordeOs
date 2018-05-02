package valenet.com.br.gestordeos.os_list;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public interface OsList {

    interface OsListView {
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

        void showEmptyListView();

        void hideEmptyListView();

        void showListOs(List<Os> osListAdapter);

        void showListOsType(List<OsTypeModel> osTypes);
    }

    interface OsListPresenter {
        void loadOsList(Double latitude, Double longitude, Integer codUser,
                        Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);
    }

    interface OsListInteractor {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser,
                                  Boolean isSearchingByCloseOs, Integer group, final onFinishedListener listener);


        interface onFinishedListener {
            void successLoading(List<Os> osList, List<OsTypeModel> osTypes);

            void errorService(String error);

            void errorNetwork();
        }
    }
}
