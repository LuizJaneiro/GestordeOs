package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;

public class OsScheduleList extends RealmObject {
    @SerializedName("osListSchedule")
    @Expose
    private List<Os> osListSchedule;

    public OsScheduleList(){

    }

    public OsScheduleList(List<Os> osListSchedule){
        this.osListSchedule = osListSchedule;
    }

    public List<Os> getOsListSchedule() {
        return osListSchedule;
    }

    public void setOsListSchedule(List<Os> osListSchedule) {
        this.osListSchedule = osListSchedule;
    }
}
