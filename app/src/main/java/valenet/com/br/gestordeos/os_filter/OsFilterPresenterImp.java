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
    public void loadOsListAndOsTypes(Double latitude, Double longitude, Integer codUser, Integer group, boolean loadNextOsList,
                                     boolean loadScheduleOsList, boolean loadOsTypes) {
        view.hideFilterView();
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideEmptyListView();
        view.showLoading();
        interactor.loadOsListAndOsTypes(latitude, longitude, codUser,
                group, loadNextOsList, loadScheduleOsList, loadOsTypes, this);
    }

    @Override
    public void successLoading(List<Os> nextOsList, List<Os> scheduleOsList, List<OsTypeModel> osTypes, boolean loadNextOsList, boolean loadScheduleOsList, boolean loadOsTypes) {
        view.hideErrorConectionView();
        view.hideErrorServerView();
        view.hideEmptyListView();
        view.hideFilterView();
        view.hideLoading();

        boolean showEmptyList = false;
        if(loadNextOsList){
            if(nextOsList != null){
                if(nextOsList.size() == 0) {
                    showEmptyList = true;
                    view.showEmptyListView();
                } else {
                    view.loadNextOsList(nextOsList);
                }
            }
        }
        if(loadScheduleOsList){
            if(scheduleOsList != null){
                if(scheduleOsList.size() == 0){
                    showEmptyList = true;
                    view.showEmptyListView();
                }else {
                    view.loadScheduleOsList(scheduleOsList);
                }
            }
        }
        if(loadOsTypes){
            if(osTypes != null){
                if(osTypes.size() == 0) {
                    showEmptyList = true;
                    view.showEmptyListView();
                } else
                    view.loadOsTypesList(osTypes);
            }
        }
        if(!showEmptyList)
            view.showFilterView();
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
