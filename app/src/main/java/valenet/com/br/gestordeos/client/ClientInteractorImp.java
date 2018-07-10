package valenet.com.br.gestordeos.client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;

public class ClientInteractorImp implements Client.ClientInteractor {
    // region Members
    private Client.ClientPresenter presenter;
    private GestorDeOsApplication application;
    // endregion Members

    // region constructors
    public ClientInteractorImp(Client.ClientPresenter presenter) {
        this.presenter = presenter;
    }
    // endregion constructors

    // region Methods


    @Override
    public void checkin(Integer osId, final onCheckinListener listener) {
        application.API_INTERFACE.putCheckin(osId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    listener.onSuccessCheckin();
                else
                    listener.onErrorCheckin();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onErrorInternetCheckin();
            }
        });
    }

    @Override
    public void checkout(Integer osId, final onCheckoutListener listener) {
        application.API_INTERFACE.putCheckout(osId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    listener.onSuccessCheckout();
                else
                    listener.onErrorCheckout();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onErrorInternetCheckout();
            }
        });
    }

    // endRegion Methods
}
