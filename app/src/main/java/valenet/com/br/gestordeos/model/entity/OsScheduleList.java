package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OsScheduleList extends RealmObject {
    @SerializedName("ordemDeServicoListSchedule")
    @Expose
    private RealmList<OrdemDeServico> ordemDeServicoListSchedule;

    public OsScheduleList(){

    }

    public OsScheduleList(RealmList<OrdemDeServico> ordemDeServicoListSchedule) {
        this.ordemDeServicoListSchedule = ordemDeServicoListSchedule;
    }

    public RealmList<OrdemDeServico> getOrdemDeServicoListSchedule() {
        return ordemDeServicoListSchedule;
    }

    public void setOrdemDeServicoListSchedule(RealmList<OrdemDeServico> ordemDeServicoListSchedule) {
        this.ordemDeServicoListSchedule = ordemDeServicoListSchedule;
    }

}
