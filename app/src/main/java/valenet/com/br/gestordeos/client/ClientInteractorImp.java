package valenet.com.br.gestordeos.client;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.model.entity.ModelCheck;

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
    public void checkin(final Integer osId, final Integer codUser, final Double latitude, final Double longitude, final onCheckinListener listener) {
        application.API_INTERFACE.putCheckin(osId, codUser, latitude, longitude, null).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    listener.onSuccessCheckin();
                else {
                    Date currentDate = Calendar.getInstance().getTime();
                    ModelCheck modelCheck = new ModelCheck(osId, codUser, latitude, longitude, true, currentDate, null);
                    listener.onErrorCheckin(modelCheck);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Date currentDate = Calendar.getInstance().getTime();
                ModelCheck modelCheck = new ModelCheck(osId, codUser, latitude, longitude, true, currentDate, null);
                listener.onErrorCheckin(modelCheck);
            }
        });
    }

    @Override
    public void checkout(final Integer osId, final Integer codUser, final Double latitude, final Double longitude, final onCheckoutListener listener) {
        application.API_INTERFACE.putCheckout(osId, codUser, latitude, longitude, null).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    listener.onSuccessCheckout();
                else {
                    Date currentDate = Calendar.getInstance().getTime();
                    ModelCheck modelCheck = new ModelCheck(osId, codUser, latitude, longitude, false, null, currentDate);
                    listener.onErrorCheckout(modelCheck);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Date currentDate = Calendar.getInstance().getTime();
                ModelCheck modelCheck = new ModelCheck(osId, codUser, latitude, longitude, false, null, currentDate);
                listener.onErrorCheckout(modelCheck);
            }
        });
    }

    @Override
    public void putScheduleFishEvent(Integer agendaEventoId, Integer coduser, final onFinshedListenerScheduleFish listener) {
        application.API_INTERFACE.putAgendaPesca(agendaEventoId, coduser).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    listener.successPutScheduleFish();
                else
                    listener.errorNetworkPutScheduleFish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.errorNetworkPutScheduleFish();
            }
        });
    }

    // endRegion Methods
}
