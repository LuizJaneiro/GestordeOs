package valenet.com.br.gestordeos.login;

public interface Login {

    public interface LoginView {
        void navigateToForgotPassword();

        void navigateToOsType();

        void hideProgress();

        void showErrorEmptyUser();

        void showErrorEmptyPassword();

        void showProgress();

        void showErrorLogin(String error);
    }

    public interface LoginPresenter {
        void login(String email, String password);
    }

    public interface LoginInteractor {
        void login(String email, String password, onFinishedListener listener);

        interface onFinishedListener {
            void successLogin();

            void errorLogin(String error);
        }
    }
}
