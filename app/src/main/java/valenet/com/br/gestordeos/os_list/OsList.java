package valenet.com.br.gestordeos.os_list;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.os_type.OsType;

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
        void loadOsTypes(Integer group);
        
        void loadOsList(Double latitude, Double longitude, Integer codUser,
                        Boolean isSearchingByCloseOs, Integer group, boolean isSwipeRefresh);
    }

    interface OsListInteractor {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser,
                                  Boolean isSearchingByCloseOs, Integer group, final onFinishedListenerOsList listener);

        void loadOsTypes(Integer group, final onFinishedListenerOsTypes listener);


        interface onFinishedListenerOsList {
            void successLoadingOsList(List<Os> osList);

            void errorServiceOsList(String error);

            void errorNetworkOsList();
        }

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }
    }
}
