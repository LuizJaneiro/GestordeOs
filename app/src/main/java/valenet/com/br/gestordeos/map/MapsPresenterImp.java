package valenet.com.br.gestordeos.map;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class MapsPresenterImp implements Maps.MapsPresenter, Maps.MapsInteractor.onFinishedListenerOsList {
    // region Members
    private Maps.MapsView view;
    private Maps.MapsInteractor interactor;

    // endregion Members

    // region constructors
    public MapsPresenterImp(Maps.MapsView view) {
        this.view = view;
        interactor = new MapsInteractorImp(this);
    }

    // endregion constructors

    // region Methods

    @Override
    public void loadOsList(Double latitude, Double longitude, Integer codUser, Integer group) {
        view.hideMapsView();
        view.hideErrorConnectionView();
        view.hideErrorServerView();
        view.showProgress();
        interactor.loadOsListAndOsTypes(latitude, longitude, codUser, group, this);
    }

    @Override
    public void loadScheduleOsList(Double latitude, Double longitude, Integer codUser, Integer group) {
        view.hideMapsView();
        view.hideErrorConnectionView();
        view.hideErrorServerView();
        view.showProgress();
        interactor.loadScheduleOsListAndOsTypes(latitude, longitude, codUser, group, this);
    }

    @Override
    public void successLoading(List<OrdemDeServico> ordemDeServicoList, List<OsTypeModel> osTypes) {
        if(ordemDeServicoList != null && osTypes != null) {
            view.loadOsTypesList((ArrayList) osTypes);
            view.loadOsList((ArrayList) ordemDeServicoList);
        }
    }

    @Override
    public void successLoadingScheduleOsList(List<OrdemDeServico> ordemDeServicoList, List<OsTypeModel> osTypes) {
        if(ordemDeServicoList != null && osTypes != null) {
            view.loadOsTypesList((ArrayList) osTypes);
            view.loadScheduleOsList((ArrayList) ordemDeServicoList);
        }
    }

    @Override
    public void errorService(String error) {
        view.hideProgress();
        view.showErrorServerView();
    }

    @Override
    public void errorNetwork() {
        view.hideProgress();
        view.showErrorConnectionView();
    }
}
