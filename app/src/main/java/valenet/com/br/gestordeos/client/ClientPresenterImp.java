package valenet.com.br.gestordeos.client;

public class ClientPresenterImp implements Client.ClientPresenter, Client.ClientInteractor.onCheckoutListener, Client.ClientInteractor.onCheckinListener {

    // region Members
    private Client.ClientView view;
    private Client.ClientInteractor interactor;

    // endregion Members

    // region constructors
    public ClientPresenterImp(Client.ClientView view) {
        this.view = view;
        interactor = new ClientInteractorImp(this);
    }

    // endregion constructors

    // region Methods


    @Override
    public void checkin(Integer osId, Integer codUser, Double latitude, Double longitude) {
        hideAllViews();
        view.showProgress();
        interactor.checkin(osId, codUser, latitude, longitude, this);
    }

    @Override
    public void checkout(Integer osId, Integer codUser, Double latitude, Double longitude) {
        hideAllViews();
        view.showProgress();
        interactor.checkout(osId, codUser, latitude, longitude, this);
    }

    @Override
    public void onSuccessCheckin() {
        hideAllViews();
        view.showLayoutClient();
        view.showSuccessCheckin();
    }

    @Override
    public void onErrorCheckin() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorCheckin();
    }

    @Override
    public void onErrorInternetCheckin() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorInternetCheckin();
    }

    @Override
    public void onSuccessCheckout() {
        hideAllViews();
        view.showLayoutClient();
        view.showSuccessCheckout();
    }

    @Override
    public void onErrorCheckout() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorCheckout();
    }

    @Override
    public void onErrorInternetCheckout() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorInternetCheckout();
    }

    private void hideAllViews() {
        view.hideLayoutClient();
        view.hideProgress();
    }

    // endregion Methods
}
