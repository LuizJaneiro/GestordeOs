package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OsNextList extends RealmObject{
    @SerializedName("osListSchedule")
    @Expose
    private RealmList<OrdemDeServico> ordemDeServicoListNext;

    public OsNextList(){

    }

    public OsNextList(RealmList<OrdemDeServico> ordemDeServicoListNext) {
        this.ordemDeServicoListNext = ordemDeServicoListNext;
    }

    public RealmList<OrdemDeServico> getOrdemDeServicoListNext() {
        return ordemDeServicoListNext;
    }

    public void setOrdemDeServicoListNext(RealmList<OrdemDeServico> ordemDeServicoListNext) {
        this.ordemDeServicoListNext = ordemDeServicoListNext;
    }
}
