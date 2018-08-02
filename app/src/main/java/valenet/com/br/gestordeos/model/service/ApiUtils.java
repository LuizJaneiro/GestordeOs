package valenet.com.br.gestordeos.model.service;

public class ApiUtils {

    public static ApiInterface getService() {
        return ApiValenet.getConnection().create(ApiInterface.class);
    }

    public static ApiInterfaceGoogleDistance getServiceGoogleDistance() {
        return ApiValenet.getConnectionGoogleDistance().create(ApiInterfaceGoogleDistance.class);
    }

    public static ApiInterfaceValenetCall getServiceValenetCall() {
        return ApiValenet.getConnectionApiValenetCall().create(ApiInterfaceValenetCall.class);
    }
}
