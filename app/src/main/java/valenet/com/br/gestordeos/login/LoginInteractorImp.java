package valenet.com.br.gestordeos.login;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.User;
import valenet.com.br.gestordeos.model.realm.LoginLocal;

public class LoginInteractorImp implements Login.LoginInteractor {
    // region Members
    private Login.LoginPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public LoginInteractorImp(Login.LoginPresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods


    @Override
    public void login(String userName, String password, final onFinishedListener listener) {
        application.API_INTERFACE.getUser(userName, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    User user = response.body();

                    LoginLocal.getInstance().saveUser(user);

                    listener.successLogin();
                } else {
                    listener.errorLogin("Ocorreu um problema ao realizar o Login. Verifique o usuário e senha!");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.errorLogin("Ocorreu um problema ao realizar o Login. Verifique sua conexão com a internet!");
                Log.d("MainActivity", "error loading from API");
            }
        });
    }


    // endRegion Methods
}
