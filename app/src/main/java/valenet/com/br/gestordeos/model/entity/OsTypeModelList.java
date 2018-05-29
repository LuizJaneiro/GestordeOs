package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OsTypeModelList extends RealmObject {

    @SerializedName("osTypeModelList")
    @Expose
    private RealmList<OsTypeModel> osTypeModelListRealmList;

    public OsTypeModelList() {
    }

    public OsTypeModelList(RealmList<OsTypeModel> osTypeModelListRealmList) {
        this.osTypeModelListRealmList = osTypeModelListRealmList;
    }

    public RealmList<OsTypeModel> getOsTypeModelListRealmList() {
        return osTypeModelListRealmList;
    }

    public void setOsTypeModelListRealmList(RealmList<OsTypeModel> osTypeModelListRealmList) {
        this.osTypeModelListRealmList = osTypeModelListRealmList;
    }
}
