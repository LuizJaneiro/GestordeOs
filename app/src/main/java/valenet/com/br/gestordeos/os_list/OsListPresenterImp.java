package valenet.com.br.gestordeos.os_list;

public class OsListPresenterImp implements OsList.OsListPresenter {
    // region Members
    private OsList.OsListView view;
    private OsList.OsListInteractor interactor;

    // endregion Members

    // region constructors
    public OsListPresenterImp(OsList.OsListView view) {
        this.view = view;
        interactor = new OsListInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    // endregion Methods
}
