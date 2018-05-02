package valenet.com.br.gestordeos.os_filter;

import java.util.HashMap;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class OsFilter {
    interface OsFilterView {
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

        void loadOsTypesList(List<OsTypeModel> osTypes);

        interface selectedFiltersListener {
            List<Os> filterList();
        }
    }

    interface OsFilterPresenter {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser,
                                  Boolean isSearchingByCloseOs, Integer group, boolean loadOsList, boolean isSwipeRefresh);
    }

    interface OsFilterInteractor {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser,
                                  Boolean isSearchingByCloseOs, Integer group, boolean loadOsList, final onFinishedListener listener);

        interface onFinishedListener {
            void successLoading(List<Os> osList, List<OsTypeModel> osTypes, boolean loadOsList);

            void errorService(String error);

            void errorNetwork();
        }
    }
}
