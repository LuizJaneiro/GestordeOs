package valenet.com.br.gestordeos.os_type;

import valenet.com.br.gestordeos.model.realm.LoginLocal;

public class OsTypeInteractorImp implements OsType.OsTypeInteractor {
    // region Members
    private OsType.OsTypePresenter presenter;
    // endregion Members

    // region constructors
    public OsTypeInteractorImp(OsType.OsTypePresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods

    @Override
    public void logout() {
        LoginLocal.getInstance().deleteAllUsers();
        presenter.successLogout();
    }


    // endRegion Methods
}
