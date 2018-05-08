package valenet.com.br.gestordeos.map;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public interface Maps {
    interface MapsView {
        void showProgress();

        void hideProgress();

        void showMapsView();

        void hideMapsView();

        void showErrorConnectionView();

        void hideErrorConnectionView();

        void showErrorServerView();

        void hideErrorServerView();

        void loadOsList(ArrayList<Os> osArrayList);

        void loadOsTypesList(ArrayList<OsTypeModel> osTypeModels);

        void addedOsMarkers(ArrayList<Os> osArrayList);
    }

    interface MapsPresenter {
        void loadOsList(Double latitude, Double longitude, Integer codUser, Integer group);
    }

    interface MapsInteractor {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, Integer group, final onFinishedListenerOsList listener);

        interface onFinishedListenerOsList {
            void successLoading(List<Os> osList, List<OsTypeModel> osTypes);

            void errorService(String error);

            void errorNetwork();
        }
    }
}