package valenet.com.br.gestordeos.model.realm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.OsTypeModelList;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationData;
import valenet.com.br.gestordeos.model.entity.os_location_data.OsLocationDataList;

import static valenet.com.br.gestordeos.application.GestorDeOsApplication.realm;

public class OsLocationDataListLocal {
    private static OsLocationDataListLocal repository;

    public static OsLocationDataListLocal getInstance() {
        if (repository == null) {
            repository = new OsLocationDataListLocal();
        }
        return repository;
    }

    public List<OsLocationData> getOsLocationDataList() {
        RealmResults<OsLocationDataList> osLists = realm.where(OsLocationDataList.class).findAll();
        OsLocationDataList osLocationDataList = null;
        List<OsLocationData> arrayListOfUnmanagedObjects = null;
        if (osLists != null && osLists.size() > 0){
            osLocationDataList = osLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(osLocationDataList.getOsListLocationData());
        }


        if (osLocationDataList == null)
            return null;

        return arrayListOfUnmanagedObjects;
    }

    public void saveOsLocationDataListLocal(final List<OsLocationData> osLocationDataList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OsLocationDataList osLocationDataList1 = new OsLocationDataList();
                RealmList<OsLocationData> osList = new RealmList<>();
                osList.addAll(osLocationDataList);
                osLocationDataList1.setOsListLocationData(osList);
                realm.insertOrUpdate(osLocationDataList1);
            }
        });
    }

    public void addOsLocationdataOnListLocal(final OsLocationData osLocationData){
        RealmResults<OsLocationDataList> osLists = realm.where(OsLocationDataList.class).findAll();
        OsLocationDataList osLocationDataList = null;
        List<OsLocationData> arrayListOfUnmanagedObjects = null;
        if (osLists != null && osLists.size() > 0){
            osLocationDataList = osLists.first();
            arrayListOfUnmanagedObjects = realm.copyFromRealm(osLocationDataList.getOsListLocationData());
        }


        if (osLocationDataList != null){
            final OsLocationDataList finalOsLocationDataList = osLocationDataList;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    finalOsLocationDataList.addOsLocationData(osLocationData);
                    realm.insertOrUpdate(finalOsLocationDataList);
                }
            });
        } else {
            osLocationDataList = new OsLocationDataList();
            osLocationDataList.addOsLocationData(osLocationData);
            final OsLocationDataList finalOsLocationDataList1 = osLocationDataList;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(finalOsLocationDataList1);
                }
            });
        }
    }

    public void deleteOsLocationDataLists() {
        final RealmResults<OsLocationDataList> resultsOsLocationDataList = realm.where(OsLocationDataList.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultsOsLocationDataList.deleteAllFromRealm();
            }
        });
    }
}
