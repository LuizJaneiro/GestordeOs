package valenet.com.br.gestordeos.model.entity.os_location_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import valenet.com.br.gestordeos.model.realm.OsListLocal;

public class OsLocationDataList extends RealmObject {

    @SerializedName("osListLocationData")
    @Expose
    private RealmList<OsLocationData> osListLocationData;

    public OsLocationDataList(){

    }

    public RealmList<OsLocationData> getOsListLocationData() {
        return osListLocationData;
    }

    public void setOsListLocationData(RealmList<OsLocationData> osListLocationData) {
        this.osListLocationData = osListLocationData;
    }

    public void addOsLocationData(OsLocationData osLocationData) {
        if(this.osListLocationData != null){
            this.osListLocationData.add(osLocationData);
        } else {
            this.osListLocationData = new RealmList<>();
            this.osListLocationData.add(osLocationData);
        }
    }
}
