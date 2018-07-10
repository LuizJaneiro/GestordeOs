package valenet.com.br.gestordeos.client;

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
    }

    interface ClientPresenter {
        void checkin(Integer osId, Integer codUser, Double latitude, Double longitude);

        void checkout(Integer osId, Integer codUser, Double latitude, Double longitude);
    }

    interface ClientInteractor {

        void checkin(Integer osId, Integer codUser, Double latitude, Double longitude, onCheckinListener listener);

        void checkout(Integer osId, Integer codUser, Double latitude, Double longitude, onCheckoutListener listener);

        interface onCheckinListener {
            void onSuccessCheckin();

            void onErrorCheckin();

            void onErrorInternetCheckin();
        }

        interface onCheckoutListener {
            void onSuccessCheckout();

            void onErrorCheckout();

            void onErrorInternetCheckout();
        }
    }
}
