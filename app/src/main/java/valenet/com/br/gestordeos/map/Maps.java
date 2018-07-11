package valenet.com.br.gestordeos.map;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
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

        void loadOsList(ArrayList<OrdemDeServico> ordemDeServicoArrayList);

        void loadScheduleOsList(ArrayList<OrdemDeServico> ordemDeServicoArrayList);

        void loadOsTypesList(ArrayList<OsTypeModel> osTypeModels);

        void addedOsMarkers(ArrayList<OrdemDeServico> ordemDeServicoArrayList);

        void navigateToFilter();

        void navigateToSearch();
    }

    interface MapsPresenter {
        void loadOsList(Double latitude, Double longitude, Integer codUser, Integer group);

        void loadScheduleOsList(Double latitude, Double longitude, Integer codUser, Integer group);
    }

    interface MapsInteractor {
        void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, Integer group, final onFinishedListenerOsList listener);

        void loadScheduleOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, Integer group, final onFinishedListenerOsList listener);

        interface onFinishedListenerOsList {
            void successLoading(List<OrdemDeServico> ordemDeServicoList, List<OsTypeModel> osTypes);

            void successLoadingScheduleOsList(List<OrdemDeServico> ordemDeServicoList, List<OsTypeModel> osTypes);

            void errorService(String error);

            void errorNetwork();
        }
    }
}
