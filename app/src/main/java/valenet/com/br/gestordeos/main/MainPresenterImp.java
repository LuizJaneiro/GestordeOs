package valenet.com.br.gestordeos.main;

public class MainPresenterImp implements Main.MainPresenter {
    // region Members
    private Main.MainView view;
    private Main.MainInteractor interactor;

    // endregion Members

    // region constructors
    public MainPresenterImp(Main.MainView view) {
        this.view = view;
        interactor = new MainInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    // endregion Methods
}
