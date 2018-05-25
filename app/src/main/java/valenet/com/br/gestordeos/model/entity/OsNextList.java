package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;

public class OsNextList extends RealmObject{
    @SerializedName("osListSchedule")
    @Expose
    private List<Os> osListNext;

    public OsNextList(){

    }

    public OsNextList(List<Os> osListNext) {
        this.osListNext = osListNext;
    }

    public List<Os> getOsListNext() {
        return osListNext;
    }

    public void setOsListNext(List<Os> osListNext) {
        this.osListNext = osListNext;
    }
}
