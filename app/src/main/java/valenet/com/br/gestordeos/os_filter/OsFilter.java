package valenet.com.br.gestordeos.os_filter;

import java.util.List;

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

        void loadOsTypesList(List<OsTypeModel> osTypes);
    }

    interface OsFilterPresenter {
        void loadOsTypes();
    }

    interface OsFilterInteractor {
        void loadOsTypes(final onFinishedListenerOsTypes listenerOsTypes);

        interface onFinishedListenerOsTypes {
            void successLoadingOsTypes(List<OsTypeModel> osList);

            void errorServiceOsTypes(String error);

            void errorNetworkOsTypes();
        }
    }
}
