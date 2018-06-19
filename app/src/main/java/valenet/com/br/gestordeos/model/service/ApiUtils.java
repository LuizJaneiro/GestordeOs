package valenet.com.br.gestordeos.model.service;

import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ApiUtils {

    public static ApiInterface getService() {
        return ApiValenet.getConnection().create(ApiInterface.class);
    }

    public static ApiInterfaceGoogleDistance getServiceGoogleDistance(){
        return ApiValenet.getConnectionGoogleDistance().create(ApiInterfaceGoogleDistance.class);
    }
}
