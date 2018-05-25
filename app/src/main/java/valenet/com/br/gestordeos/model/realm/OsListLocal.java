package valenet.com.br.gestordeos.model.realm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsNextList;
import valenet.com.br.gestordeos.model.entity.OsScheduleList;

import static valenet.com.br.gestordeos.application.GestorDeOsApplication.realm;

public class OsListLocal {
    private static OsListLocal repository;

    public static OsListLocal getInstance() {
        if (repository == null) {
            repository = new OsListLocal();
        }
        return repository;
    }

    public List<Os> getNextOsList() {
        RealmResults<OsNextList> osLists = realm.where(OsNextList.class).findAll();
        OsNextList osNextList = null;
        if (osLists != null && osLists.size() > 0)
            osNextList = osLists.first();

        if (osNextList == null)
            return null;

        return osNextList.getOsListNext();
    }


    public List<Os> getScheduleOsList() {
        RealmResults<OsScheduleList> osLists = realm.where(OsScheduleList.class).findAll();
        OsScheduleList osScheduleList = null;
        if (osLists != null && osLists.size() > 0)
            osScheduleList = osLists.first();

        if (osScheduleList == null)
            return null;

        return osScheduleList.getOsListSchedule();
    }

    public void saveOsScheduleListLocal(final List<Os> osScheduleList) {
        final OsScheduleList osScheduleList1 = new OsScheduleList(osScheduleList);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(osScheduleList1);
            }
        });
    }

    public void saveOsNextListLocal(final List<Os> osNextList) {
        final OsNextList osNextList1 = new OsNextList(osNextList);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(osNextList);
            }
        });
    }

    public void deleteOsLocal(Os os) {
        if (os != null) {
            final RealmResults<Os> results = realm.where(Os.class).equalTo("osid", os.getOsid()).findAll();
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
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultsOsNextList.deleteAllFromRealm();
                resultsOsScheduleList.deleteAllFromRealm();
            }
        });
    }
}
