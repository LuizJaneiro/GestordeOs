package valenet.com.br.gestordeos.login;

public class LoginPresenterImp implements Login.LoginPresenter, Login.LoginInteractor.onFinishedListener {
    // region Members
    private Login.LoginView view;
    private Login.LoginInteractor interactor;

    // endregion Members

    // region constructors
    public LoginPresenterImp(Login.LoginView view) {
        this.view = view;
        interactor = new LoginInteractorImp(this);
    }

    // endregion constructors

    // region Methods

    @Override
    public void login(String userName, String password) {
        boolean emailIsEmpty = userName.isEmpty() ||
                userName == null;

        boolean passwordIsEmpty = password.isEmpty() ||
                password == null;

        if (emailIsEmpty) {
            view.showErrorEmptyUser();
            return;
        }
        if (passwordIsEmpty) {
            view.showErrorEmptyPassword();
            return;
        }

        view.hideLoginView();
        view.showProgress();

        interactor.login(userName, password, this);
    }

    @Override
    public void successLogin() {
        view.navigateToOsType();
    }

    @Override
    public void errorLogin(String error) {
        view.hideProgress();
        view.showLoginView();
        view.showErrorLogin(error);
    }

    // endregion Methods
}
