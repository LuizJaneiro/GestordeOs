package valenet.com.br.gestordeos.model.realm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import valenet.com.br.gestordeos.model.entity.ModelCheck;
import valenet.com.br.gestordeos.model.entity.ModelCheckList;

import static valenet.com.br.gestordeos.application.GestorDeOsApplication.realm;

public class ModelCheckListLocal {
    private static ModelCheckListLocal repository;

    public static ModelCheckListLocal getInstance() {
        if (repository == null) {
            repository = new ModelCheckListLocal();
        }
        return repository;
    }

    public List<ModelCheck> getModelCheckList() {
        RealmResults<ModelCheckList> checkLists = realm.where(ModelCheckList.class).findAll();
        ModelCheckList modelCheckList = null;
        List<ModelCheck> arrayListOfUnmanagedObjects = null;
        if (checkLists != null && checkLists.size() > 0) {
            modelCheckList = checkLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(modelCheckList.getModelCheckRealmList());
        }


        if (modelCheckList == null)
            return null;

        return arrayListOfUnmanagedObjects;
    }

    public void insertModelCheck(final ModelCheck modelCheck) {
        RealmResults<ModelCheckList> modelCheckLists = realm.where(ModelCheckList.class).findAll();
        ModelCheckList modelCheckList = null;
        List<ModelCheck> arrayListOfUnmanagedObjects = null;
        if (modelCheckLists != null && modelCheckLists.size() > 0) {
            modelCheckList = modelCheckLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(modelCheckList.getModelCheckRealmList());
        }


        if (modelCheckList != null) {
            final ModelCheckList finalModelCheckList = modelCheckList;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    finalModelCheckList.addModelCheck(modelCheck);
                    realm.insertOrUpdate(finalModelCheckList);
                }
            });
        } else {
            modelCheckList = new ModelCheckList();
            modelCheckList.addModelCheck(modelCheck);
            final ModelCheckList finalModelCheckList1 = modelCheckList;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(finalModelCheckList1);
                }
            });
        }
    }

    public void saveModelCheckListLocal(final List<ModelCheck> ordemDeServicoScheduleList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ModelCheckList osScheduleList1 = new ModelCheckList();
                RealmList<ModelCheck> ordemDeServicoList = new RealmList<>();
                ordemDeServicoList.addAll(ordemDeServicoScheduleList);
                osScheduleList1.setModelCheckRealmList(ordemDeServicoList);
                realm.insertOrUpdate(osScheduleList1);
            }
        });
    }

    public void deleteModelCheckListLocal(ModelCheck modelCheck) {
        if (modelCheck != null) {
            final RealmResults<ModelCheck> results = realm.where(ModelCheck.class)
                    .equalTo("osId", modelCheck.getOsId())
                    .equalTo("isCheckin", modelCheck.isCheckin())
                    .findAll();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
        }
    }

    public void deleteAllModelCheckinListLocal() {
        final RealmResults<ModelCheckList> modelCheckListRealmResults = realm.where(ModelCheckList.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                modelCheckListRealmResults.deleteAllFromRealm();
            }
        });
    }
}
