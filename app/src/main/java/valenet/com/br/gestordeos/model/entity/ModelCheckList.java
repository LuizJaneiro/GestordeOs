package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModelCheckList extends RealmObject {
    @SerializedName("modelCheckList")
    @Expose
    private RealmList<ModelCheck> modelCheckRealmList;

    public ModelCheckList() {

    }

    public RealmList<ModelCheck> getModelCheckRealmList() {
        return modelCheckRealmList;
    }

    public void setModelCheckRealmList(RealmList<ModelCheck> modelCheckRealmList) {
        this.modelCheckRealmList = modelCheckRealmList;
    }

    public void addModelCheck(ModelCheck modelCheck) {
        if(this.modelCheckRealmList != null){
            this.modelCheckRealmList.add(modelCheck);
        } else {
            this.modelCheckRealmList = new RealmList<>();
            this.modelCheckRealmList.add(modelCheck);
        }
    }
}
