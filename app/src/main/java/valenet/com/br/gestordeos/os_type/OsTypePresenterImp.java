package valenet.com.br.gestordeos.os_type;

public class OsTypePresenterImp implements OsType.OsTypePresenter {
    // region Members
    private OsType.OsTypeView view;
    private OsType.OsTypeInteractor interactor;

    // endregion Members

    // region constructors
    public OsTypePresenterImp(OsType.OsTypeView view) {
        this.view = view;
        interactor = new OsTypeInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void logout() {
        view.hideOsListView();
        view.showLoading();

        interactor.logout();
    }

    @Override
    public void successLogout() {
        view.navigateToLogin();
    }
}
