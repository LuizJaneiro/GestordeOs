package valenet.com.br.gestordeos.client;

import valenet.com.br.gestordeos.model.entity.ModelCheck;

public interface Client {
    interface ClientView {
        void showProgress();

        void hideProgress();

        void showLayoutClient();

        void hideLayoutClient();

        void showSuccessCheckin();

        void showErrorCheckin();

        void showErrorInternetCheckin();

        void showSuccessCheckout();

        void showErrorCheckout();

        void showErrorInternetCheckout();

        void showSuccessFishing();

        void showErrorFishing();

        void showErrorInternetFishing();
    }

    interface ClientPresenter {
        void checkin(Integer osId, Integer codUser, Double latitude, Double longitude);

        void checkout(Integer osId, Integer codUser, Double latitude, Double longitude);

        void putScheduleFishEvent(Integer agendaEventoId, Integer coduser);
    }

    interface ClientInteractor {

        void checkin(Integer osId, Integer codUser, Double latitude, Double longitude, onCheckinListener listener);

        void checkout(Integer osId, Integer codUser, Double latitude, Double longitude, onCheckoutListener listener);

        void putScheduleFishEvent(Integer agendaEventoId, Integer coduser, onFinshedListenerScheduleFish listener);

        interface onCheckinListener {
            void onSuccessCheckin();

            void onErrorCheckin(ModelCheck modelCheck);
        }

        interface onCheckoutListener {
            void onSuccessCheckout();

            void onErrorCheckout(ModelCheck modelCheck);
        }

        interface onFinshedListenerScheduleFish {
            void successPutScheduleFish();

            void errorServicePutScheduleFish();

            void errorNetworkPutScheduleFish();
        }
    }
}
