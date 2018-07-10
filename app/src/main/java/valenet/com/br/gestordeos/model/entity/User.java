package valenet.com.br.gestordeos.model.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class User extends RealmObject{

    @SerializedName("Coduser")
    @Expose
    private Integer coduser;
    @SerializedName("Login")
    @Expose
    private String login;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Nome")
    @Expose
    private String nome;


    public Integer getCoduser() {
        return coduser;
    }

    public void setCoduser(Integer coduser) {
        this.coduser = coduser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
