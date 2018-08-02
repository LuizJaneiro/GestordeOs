package valenet.com.br.gestordeos.client;

import valenet.com.br.gestordeos.model.entity.ModelCheck;
import valenet.com.br.gestordeos.model.realm.ModelCheckListLocal;
import valenet.com.br.gestordeos.model.realm.OsListLocal;

public class ClientPresenterImp implements Client.ClientPresenter, Client.ClientInteractor.onCheckoutListener, Client.ClientInteractor.onCheckinListener,
        Client.ClientInteractor.onFinshedListenerScheduleFish, Client.ClientInteractor.onFinishedListenerCall {

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
    public void callPhone(String nroTecnico, String nroCliente) {
        hideAllViews();
        view.showProgress();
        String nroTecnicoTratado = nroTecnico.replaceAll("[^0-9]", "");
        String nroClienteTratado = nroCliente.replaceAll("[^0-9]", "");
        Long nroTecnicoInteger = Long.parseLong(nroTecnicoTratado);
        Long nroClienteInteger = Long.parseLong(nroClienteTratado);
        interactor.callPhone(nroTecnicoInteger, nroClienteInteger, this);
    }

    @Override
    public void putScheduleFishEvent(Integer agendaEventoId, Integer coduser) {
        hideAllViews();
        view.showProgress();
        interactor.putScheduleFishEvent(agendaEventoId, coduser, this);
    }

    @Override
    public void onSuccessCheckin() {
        hideAllViews();
        view.showLayoutClient();
        view.showSuccessCheckin();
    }

    @Override
    public void onErrorCheckin(ModelCheck modelCheck) {
        ModelCheckListLocal modelCheckListLocal = ModelCheckListLocal.getInstance();
        OsListLocal osListLocal = OsListLocal.getInstance();
        hideAllViews();
        if (modelCheckListLocal != null && osListLocal != null) {
            modelCheckListLocal.insertModelCheck(modelCheck);
            osListLocal.putOsDateCheckinCheckout(modelCheck.getOsId(), "checkin realizado", null);
            view.showLayoutClient();
            view.showSuccessCheckin();
        } else {
            view.showLayoutClient();
            view.showErrorCheckin();
        }
    }

    @Override
    public void onSuccessCheckout() {
        hideAllViews();
        view.showLayoutClient();
        view.showSuccessCheckout();
    }

    @Override
    public void onErrorCheckout(ModelCheck modelCheck) {
        ModelCheckListLocal modelCheckListLocal = ModelCheckListLocal.getInstance();
        OsListLocal osListLocal = OsListLocal.getInstance();
        hideAllViews();
        if (modelCheckListLocal != null) {
            modelCheckListLocal.insertModelCheck(modelCheck);
            osListLocal.putOsDateCheckinCheckout(modelCheck.getOsId(), null, "checkout Realizado");
            view.showLayoutClient();
            view.showSuccessCheckout();
        } else {
            view.showLayoutClient();
            view.showErrorCheckout();
        }
    }

    @Override
    public void successPutScheduleFish() {
        hideAllViews();
        view.showSuccessFishing();
    }

    @Override
    public void errorServicePutScheduleFish() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorFishing();
    }

    @Override
    public void errorNetworkPutScheduleFish() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorInternetFishing();
    }

    @Override
    public void successCall() {
        hideAllViews();
        view.showLayoutClient();
        view.showSuccessCall();
    }

    @Override
    public void errorCall() {
        hideAllViews();
        view.showLayoutClient();
        view.showErrorCall();
    }

    private void hideAllViews() {
        view.hideLayoutClient();
        view.hideProgress();
    }
    // endregion Methods
}
