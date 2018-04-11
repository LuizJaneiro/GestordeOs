package valenet.com.br.gestordeos.login;

import android.app.Application;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.User;
import valenet.com.br.gestordeos.model.service.ApiUtils;

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
    public void login(String userName, String password, onFinishedListener listener) {
        application.API_INTERFACE.getUser(userName, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){

                } else {

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }


    // endRegion Methods
}
