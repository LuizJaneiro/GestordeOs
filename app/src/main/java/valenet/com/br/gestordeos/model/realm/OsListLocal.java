package valenet.com.br.gestordeos.model.realm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsNextList;
import valenet.com.br.gestordeos.model.entity.OsScheduleList;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.OsTypeModelList;

import static valenet.com.br.gestordeos.application.GestorDeOsApplication.realm;

public class OsListLocal {
    private static OsListLocal repository;

    public static OsListLocal getInstance() {
        if (repository == null) {
            repository = new OsListLocal();
        }
        return repository;
    }

    public List<OrdemDeServico> getNextOsList() {
        RealmResults<OsNextList> osLists = realm.where(OsNextList.class).findAll();
        OsNextList osNextList = null;
        List<OrdemDeServico> arrayListOfUnmanagedObjects = null;
        if (osLists != null && osLists.size() > 0){
            osNextList = osLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(osNextList.getOrdemDeServicoListNext());
        }


        if (osNextList == null)
            return null;

        return arrayListOfUnmanagedObjects;
    }


    public List<OrdemDeServico> getScheduleOsList() {
        RealmResults<OsScheduleList> osLists = realm.where(OsScheduleList.class).findAll();
        OsScheduleList osScheduleList = null;
        List<OrdemDeServico> arrayListOfUnmanagedObjects = null;
        if (osLists != null && osLists.size() > 0){
            osScheduleList = osLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(osScheduleList.getOrdemDeServicoListSchedule());
        }


        if (osScheduleList == null)
            return null;

        return arrayListOfUnmanagedObjects;
    }

    public List<OsTypeModel> getOsTypeModelList(){
        RealmResults<OsTypeModelList> osTypeModelLists = realm.where(OsTypeModelList.class).findAll();
        OsTypeModelList osTypeModelList = null;
        List<OsTypeModel> arrayListOfUnmanagedObjects = null;
        if (osTypeModelLists != null && osTypeModelLists.size() > 0){
            osTypeModelList = osTypeModelLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(osTypeModelList.getOsTypeModelListRealmList());
        }

        if (osTypeModelList == null)
            return null;

        return arrayListOfUnmanagedObjects;
    }

    public void saveOsScheduleListLocal(final List<OrdemDeServico> ordemDeServicoScheduleList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OsScheduleList osScheduleList1 = new OsScheduleList();
                RealmList<OrdemDeServico> ordemDeServicoList = new RealmList<>();
                ordemDeServicoList.addAll(ordemDeServicoScheduleList);
                osScheduleList1.setOrdemDeServicoListSchedule(ordemDeServicoList);
                realm.insertOrUpdate(osScheduleList1);
            }
        });
    }

    public void saveOsNextListLocal(final List<OrdemDeServico> ordemDeServicoNextList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OsNextList osNextList1 = new OsNextList();
                RealmList<OrdemDeServico> ordemDeServicoList = new RealmList<>();
                ordemDeServicoList.addAll(ordemDeServicoNextList);
                osNextList1.setOrdemDeServicoListNext(ordemDeServicoList);
                realm.insertOrUpdate(osNextList1);
            }
        });
    }

    public void saveOsTypeModelLocal(final List<OsTypeModel> osTypeModelList){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OsTypeModelList osNextList1 = new OsTypeModelList();
                RealmList<OsTypeModel> osList = new RealmList<>();
                osList.addAll(osTypeModelList);
                osNextList1.setOsTypeModelListRealmList(osList);
                realm.insertOrUpdate(osNextList1);
            }
        });
    }

    public void deleteOsLocal(OrdemDeServico ordemDeServico) {
        if (ordemDeServico != null) {
            final RealmResults<OrdemDeServico> results = realm.where(OrdemDeServico.class).equalTo("osid", ordemDeServico.getOsid()).findAll();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
        }
    }

    public void deleteAllOsLocal() {
        final RealmResults<OsNextList> resultsOsNextList = realm.where(OsNextList.class).findAll();
        final RealmResults<OsScheduleList> resultsOsScheduleList = realm.where(OsScheduleList.class).findAll();
        final RealmResults<OsTypeModelList> resultsOsTypeModelList = realm.where(OsTypeModelList.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultsOsNextList.deleteAllFromRealm();
                resultsOsScheduleList.deleteAllFromRealm();
                resultsOsTypeModelList.deleteAllFromRealm();
            }
        });
    }

    public void deleteScheduleOsListLocal(){
        final RealmResults<OsScheduleList> resultsOsScheduleList = realm.where(OsScheduleList.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultsOsScheduleList.deleteAllFromRealm();
            }
        });
    }

    public void deleteNextOsListLocal(){
        final RealmResults<OsNextList> resultsOsNextList = realm.where(OsNextList.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultsOsNextList.deleteAllFromRealm();
            }
        });
    }

    public void deleteOsTypeListLocal(){
        final RealmResults<OsTypeModelList> resultsOsTypeModelList = realm.where(OsTypeModelList.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultsOsTypeModelList.deleteAllFromRealm();
            }
        });
    }
}
