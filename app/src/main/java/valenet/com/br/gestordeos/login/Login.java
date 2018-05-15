package valenet.com.br.gestordeos.login;

public interface Login {

    public interface LoginView {
        void navigateToMain();

        void hideProgress();

        void showErrorEmptyUser();

        void showErrorEmptyPassword();

        void showProgress();

        void showErrorLogin(String error);

        void showLoginView();

        void hideLoginView();

        void saveLoginData(String userName, String password);
    }

    public interface LoginPresenter {
        void login(String email, String password);
    }

    public interface LoginInteractor {
        void login(String email, String password, onFinishedListener listener);

        interface onFinishedListener {
            void successLogin(String userName, String password);

            void errorLogin(String error);
        }
    }
}
