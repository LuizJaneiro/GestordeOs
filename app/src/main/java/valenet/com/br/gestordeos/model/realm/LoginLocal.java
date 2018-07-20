package valenet.com.br.gestordeos.model.realm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import valenet.com.br.gestordeos.model.entity.User;

import static valenet.com.br.gestordeos.application.GestorDeOsApplication.realm;

public class LoginLocal {
    private static LoginLocal repository;

    public static LoginLocal getInstance() {
        if (repository == null) {
            repository = new LoginLocal();
        }
        return repository;
    }

    public void saveUser(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(user);
            }
        });
    }

    public User getCurrentUser() {
        RealmResults<User> users = realm.where(User.class).findAll();
        User user = null;
        if (users != null && users.size() > 0)
            user = users.first();
        return user;
    }

    public void deleteUser(User currentUser) {
        if (currentUser != null) {
            final RealmResults<User> results = realm.where(User.class).equalTo("coduser", currentUser.getCoduser()).findAll();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
        }
    }

    public void deleteAllUsers() {
        final RealmResults<User> results = realm.where(User.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }
}
