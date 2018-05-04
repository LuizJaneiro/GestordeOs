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

        void loadNextOsList(List<Os> list);

        void loadScheduleOsList(List<Os> list);

        void loadOsTypesList(List<OsTypeModel> osTypes);

        interface selectedFiltersListener {
            List<Os> filterNextOsList();
            List<Os> filterScheduleOsList();
        }
    }

    interface OsFilterPresenter {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser,
                                  Integer group, boolean loadNextOsList,
                                  boolean loadScheduleOsList, boolean loadOsTypes);
    }

    interface OsFilterInteractor {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, Integer group, boolean loadNextOsList,
                                  boolean loadScheduleOsList, boolean loadOsTypes, final onFinishedListener listener);

        interface onFinishedListener {
            void successLoading(List<Os> nextOsList, List<Os> scheduleOsList, List<OsTypeModel> osTypes, boolean loadNextOsList, boolean loadScheduleOsList, boolean loadOsTypes);

            void errorService(String error);

            void errorNetwork();
        }
    }
}
