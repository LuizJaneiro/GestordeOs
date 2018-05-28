package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OsNextList extends RealmObject{
    @SerializedName("osListSchedule")
    @Expose
    private RealmList<Os> osListNext;

    public OsNextList(){

    }

    public OsNextList(RealmList<Os> osListNext) {
        this.osListNext = osListNext;
    }

    public RealmList<Os> getOsListNext() {
        return osListNext;
    }

    public void setOsListNext(RealmList<Os> osListNext) {
        this.osListNext = osListNext;
    }
}
