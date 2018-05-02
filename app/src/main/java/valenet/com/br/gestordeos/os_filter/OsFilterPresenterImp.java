package valenet.com.br.gestordeos.os_filter;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class OsFilterPresenterImp implements OsFilter.OsFilterPresenter, OsFilter.OsFilterInteractor.onFinishedListener {
    // region Members
    private OsFilter.OsFilterView view;
    private OsFilter.OsFilterInteractor interactor;

    // endregion Members

    // region constructors
    public OsFilterPresenterImp(OsFilter.OsFilterView view) {
        this.view = view;
        interactor = new OsFilterInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, Boolean isSearchingByCloseOs, Integer group, boolean loadOsList, boolean isSwipeRefresh) {
        if(!isSwipeRefresh) {
            view.hideFilterView();
            view.hideErrorConectionView();
            view.hideErrorServerView();
            view.hideEmptyListView();
            view.showLoading();
        }
        interactor.loadOsListAndOsTypes(latitude, longitude, codUser, isSearchingByCloseOs, group, loadOsList, this);
    }

    @Override
    public void successLoading(List<Os> osList, List<OsTypeModel> osTypes, boolean loadOsList) {
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideEmptyListView();
        view.hideLoading();
        if((loadOsList && (osList == null || osList.size() == 0)) || (osTypes == null || osTypes.size() == 0))
            view.showEmptyListView();
        else {
            if(loadOsList)
                view.loadListOs(osList);
            view.loadOsTypesList(osTypes);
            view.showFilterView();
        }
    }

    @Override
    public void errorService(String error) {
        view.hideLoading();
        view.hideFilterView();
        view.hideEmptyListView();
        view.hideErrorConectionView();
        view.showErrorServerView();
    }

    @Override
    public void errorNetwork() {
        view.hideLoading();
        view.hideFilterView();
        view.hideEmptyListView();
        view.hideErrorServerView();
        view.showErrorConectionView();
    }




    // endregion Methods
}
