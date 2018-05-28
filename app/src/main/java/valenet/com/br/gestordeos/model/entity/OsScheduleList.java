package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OsScheduleList extends RealmObject {
    @SerializedName("osListSchedule")
    @Expose
    private RealmList<Os> osListSchedule;

    public OsScheduleList(){

    }

    public OsScheduleList(RealmList<Os> osListSchedule) {
        this.osListSchedule = osListSchedule;
    }

    public RealmList<Os> getOsListSchedule() {
        return osListSchedule;
    }

    public void setOsListSchedule(RealmList<Os> osListSchedule) {
        this.osListSchedule = osListSchedule;
    }

}
